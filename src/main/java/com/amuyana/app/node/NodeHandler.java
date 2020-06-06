package com.amuyana.app.node;

import com.amuyana.app.controllers.FXMLSource;
import com.amuyana.app.controllers.LogicSystemController;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.content.*;
import com.amuyana.app.node.tod.expression.Expression;
import com.amuyana.app.node.menu.TopMenuBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class NodeHandler extends BorderPane implements NodeInterface {
    private Stage stage;
    private TopMenuBar topMenuBar;
    private TabPane contentTabPane;
    private static DataInterface dataInterface;
    private LogicSystem logicSystem;
    ConnectionTab connectionTab;
    private Log log;
    public static String VERSION ="Amuyaña 3.2dev";

    public NodeHandler(DataInterface dataInterface, Stage stage) {
        NodeHandler.dataInterface = dataInterface;
        this.stage = stage;
        Expression.initializeLists(); //todo move
        this.topMenuBar = new TopMenuBar(this);
        this.connectionTab = new ConnectionTab(this);
        this.contentTabPane = new ContentTabPane();

        setTop(this.topMenuBar);
        setCenter(this.contentTabPane);

        this.log = new Log();
        setBottom(log.getContainer());
    }

    public Log getLog() {
        return log;
    }

    public static DataInterface getDataInterface() {
        return dataInterface;
    }

    @Override
    public TopMenuBar getTopMenuBar() {
        return topMenuBar;
    }

    public void setLogicSystem(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
    }

    @Override
    public javafx.scene.Node getRootNode() {
        return this;
    }

    @Override
    public void closeTabsExceptConnection() {
        ObservableList<Tab> tabsToClose = FXCollections.observableArrayList();

        for (Tab tab : contentTabPane.getTabs()) {
            if (!tab.getClass().equals(ConnectionTab.class)) {
                tabsToClose.add(tab);
            }
        }
        contentTabPane.getTabs().removeAll(tabsToClose);
    }

    @Override
    public void resetMenus() {
        getTopMenuBar().resetMenus();
    }

    @Override
    public LogicSystem getLogicSystem() {
        return this.logicSystem;
    }

    @Override
    public void exitAmuyana() {
        stage.close();
    }

    @Override
    public void openConnectionTab() {
        for (Tab tab : contentTabPane.getTabs()) {
            if (tab.getClass().equals(ConnectionTab.class)) {
                contentTabPane.getSelectionModel().select(tab);
                return;
            }
        }
        contentTabPane.getTabs().add(connectionTab);
        contentTabPane.getSelectionModel().select(connectionTab);
    }

    @Override
    public void openLogicSystemTab() {
        dataInterface.connect();
        // opening a new ContentTab...
        LogicSystemContentTab logicSystemContentTab = new LogicSystemContentTab(this);
        contentTabPane.getTabs().add(logicSystemContentTab);
        contentTabPane.getSelectionModel().select(logicSystemContentTab);
        dataInterface.disconnect();
    }

    @Override
    public void addToLogicSystemMenu(LogicSystem logicSystem) {
        topMenuBar.addMenu(logicSystem);
    }

    @Override
    public void load(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
        stage.setTitle(VERSION+" - " + logicSystem.getLabel());
        topMenuBar.logicSystemIsLoaded(true);
        topMenuBar.updateTodMenu();
        log("The Logic System has been loaded");
        closeTabsTod();
    }

    @Override
    public void edit(LogicSystem logicSystem) {
        // Check if a tab is already open for edit
        for (Tab tab : contentTabPane.getTabs()) {
            if (tab.getClass().equals(LogicSystemContentTab.class)) {
                LogicSystemContentTab logicSystemContentTab = (LogicSystemContentTab)tab;
                if (!logicSystemContentTab.getController().isLogicSystemNew()) {
                    if (logicSystemContentTab.getController().getLogicSystem().equals(logicSystem)) {
                        contentTabPane.getSelectionModel().select(tab);
                        return;
                    }
                }
            }
        }
        // If there's no tab open for editing open a new one
        LogicSystemContentTab logicSystemContentTab = new LogicSystemContentTab(this, dataInterface, logicSystem);
        contentTabPane.getTabs().add(logicSystemContentTab);
        contentTabPane.getSelectionModel().select(logicSystemContentTab);
    }

    @Override
    public void delete(LogicSystem logicSystem) {
        Alert alert = Message.confirmDeletionLogicSystem();

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                // First delete dependencies: tod
                // FCCs are not deleted, they are kept...
                for (Tod tod : dataInterface.getTods(logicSystem)) {
                    deleteConfirmed(tod);
                }

                dataInterface.delete(logicSystem);

                LogicSystemContentTab tabToRemove = null;
                for (Tab tab : contentTabPane.getTabs()) {
                    if (tab.getClass().equals(LogicSystemContentTab.class)) {
                        LogicSystemContentTab logicSystemContentTab = (LogicSystemContentTab) tab;
                        LogicSystemController logicSystemController = logicSystemContentTab.getController();
                        if (!logicSystemController.isLogicSystemNew()) {
                            if (logicSystemController.getLogicSystem().equals(logicSystem)) {
                                tabToRemove = (LogicSystemContentTab) tab;
                            }
                        }
                    }
                }
                contentTabPane.getTabs().remove(tabToRemove);
                topMenuBar.removeLogicSystemMenu(logicSystem);

                if (this.logicSystem != null) {
                    if (this.logicSystem.equals(logicSystem)) {
                        setLogicSystem(null);
                        stage.setTitle(VERSION);
                        // Menus
                        topMenuBar.logicSystemIsLoaded(false);
                        topMenuBar.updateTodMenu();
                    }
                }

                closeTabsTod();
            } else if (result.get() == ButtonType.CANCEL) {
            }
        }
    }

    private void closeTabsTod() {
        // Close all tabs of TOD that belong to this system
        ObservableList<Tab> tabsToClose=FXCollections.observableArrayList();
        for (Tab tab : contentTabPane.getTabs()) {
            if (tab.getClass().equals(TodTab.class)) {
                tabsToClose.add(tab);
            }
        }
        contentTabPane.getTabs().removeAll(tabsToClose);
    }

    @Override
    public void newTodTab() {
        dataInterface.getDataConnection().connect();
        TodTab todTab = new TodTab(this);
        contentTabPane.getTabs().add(todTab);
        contentTabPane.getSelectionModel().select(todTab);
        topMenuBar.addMenu(todTab.getTodController().getTod());
        dataInterface.getDataConnection().disconnect();
    }

    @Override
    public void open(Tod tod) {
        for (Tab tab : contentTabPane.getTabs()) {
            if (tab.getClass().equals(TodTab.class)) {
                TodTab todTab = (TodTab)tab;
                if (todTab.getController().getTod().equals(tod)) {
                    contentTabPane.getSelectionModel().select(tab);
                    return;
                }
            }
        }
        TodTab todTab = new TodTab(this, tod);
        contentTabPane.getTabs().add(todTab);
        contentTabPane.getSelectionModel().select(todTab);
    }

    @Override
    public void delete(Tod tod) {
        Alert alert = Message.confirmDeletionTod();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent())
            if (result.get() == ButtonType.OK) {
                deleteConfirmed(tod);
            } else if (result.get() == ButtonType.CANCEL) {
            }
    }

    private void deleteConfirmed(Tod tod) {
        // delete in database
        dataInterface.delete(tod);
        // close tab if it is open
        TodTab todTabToRemove = null;
        for (Tab tab : contentTabPane.getTabs()) {
            if (tab.getClass().equals(TodTab.class)) {
                TodTab todTab = (TodTab)tab;
                if (todTab.getTodController().getTod().equals(tod)) {
                    todTabToRemove = todTab;
                }
            }
        }
        this.contentTabPane.getTabs().remove(todTabToRemove);
        // update list of Tod in menu
        this.topMenuBar.updateTodMenu();
    }

    @Override
    public void duplicate(Tod tod) {

    }

    @Override
    public void openAboutWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLSource.ABOUT.getUrl()));
        try {
            Parent parent = fxmlLoader.load();
            Stage stage1 = new Stage();
            stage1.setTitle("About");
            stage1.setScene(new Scene(parent));
            stage1.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Special class for debugging
     */
    @Override
    public void openDebug1() {
        Circle circle = new Circle(20);
        TodScrollPane todScrollPane = new TodScrollPane(circle);

        Stage stage1 = new Stage();
        stage1.setTitle("Debug1");
        stage1.setScene(new Scene(todScrollPane));

        stage1.show();
    }

    @Override
    public void openFccTableTab() {
        FccTableTab fccTableTab = new FccTableTab();
        contentTabPane.getTabs().add(fccTableTab);
        contentTabPane.getSelectionModel().select(fccTableTab);
    }

    @Override
    public void log(String e) {
        log.register(e);
    }

    @Override
    public void logSQLException() {
        log("It seems like you're not connected to the Internet. Otherwise please inform the developers of this error.");
    }
/*
    public static void logError(String s) {
        log("e");
    }*/
/*
    @Override
    public Node getStyleableNode() {
        return null;
    }*/
}
