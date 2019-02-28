package com.amuyana.app.node;

import com.amuyana.app.controllers.FXMLSource;
import com.amuyana.app.controllers.LogicSystemController;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.tod.expression.Expression;
import com.amuyana.app.node.content.ConnectionContentTab;
import com.amuyana.app.node.content.ContentTabPane;
import com.amuyana.app.node.content.LogicSystemContentTab;
import com.amuyana.app.node.content.TodContentTab;
import com.amuyana.app.node.menu.TopMenuBar;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;


public class MainBorderPane extends BorderPane implements NodeInterface {
    private Stage stage;
    private TopMenuBar topMenuBar;
    private TabPane contentTabPane;
    private static DataInterface dataInterface;
    private LogicSystem logicSystem;

    public MainBorderPane(DataInterface dataInterface) {
        this.dataInterface = dataInterface;
        Expression.initializeLists();
        this.topMenuBar = new TopMenuBar(this);
        this.contentTabPane = new ContentTabPane();

        setTop(this.topMenuBar);
        setCenter(this.contentTabPane);
    }

    public static DataInterface getDataInterface() {
        return dataInterface;
    }

    /**
     * Added for debug purposes
     * @return
     */
    @Override
    public TopMenuBar getTopMenuBar() {
        return topMenuBar;
    }

    private void setLogicSystem(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public javafx.scene.Node getRootNode() {
        return this;
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
            if (tab.getClass().equals(ConnectionContentTab.class)) {
                contentTabPane.getSelectionModel().select(tab);
                return;
            }
        }

        ConnectionContentTab connectionContentTab = new ConnectionContentTab(this);
        contentTabPane.getTabs().add(connectionContentTab);
        contentTabPane.getSelectionModel().select(connectionContentTab);
    }

    @Override
    public void openLogicSystemTab() {
        // opening a new ContentTab...
        LogicSystemContentTab logicSystemContentTab = new LogicSystemContentTab(this);
        contentTabPane.getTabs().add(logicSystemContentTab);
        contentTabPane.getSelectionModel().select(logicSystemContentTab);
    }

    @Override
    public void addToLogicSystemMenu(LogicSystem logicSystem) {
        topMenuBar.addMenu(logicSystem);
    }

    @Override
    public void load(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
        stage.setTitle("Amuyaña - " + logicSystem.getLabel());
        topMenuBar.logicSystemIsLoaded(true);
        topMenuBar.updateTodMenu();
        Message.loadedLogicSystem();
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

        if (result.get() == ButtonType.OK) {
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
            //
            if (this.logicSystem != null) {
                if (this.logicSystem.equals(logicSystem)) {
                    setLogicSystem(null);
                    stage.setTitle("Amuyaña");
                    // Menus
                    topMenuBar.removeLogicSystemMenu(logicSystem);
                    topMenuBar.logicSystemIsLoaded(false);
                    topMenuBar.updateTodMenu();
                }
            }
        } else if (result.get() == ButtonType.CANCEL) {
            return;
        }
    }

    @Override
    public void newTodTab() {
        TodContentTab todContentTab = new TodContentTab(this);
        contentTabPane.getTabs().add(todContentTab);
        contentTabPane.getSelectionModel().select(todContentTab);
        topMenuBar.addMenu(todContentTab.getTodController().getTod());
    }

    @Override
    public void open(Tod tod) {
        for (Tab tab : contentTabPane.getTabs()) {
            if (tab.getClass().equals(TodContentTab.class)) {
                TodContentTab todContentTab = (TodContentTab)tab;
                if (todContentTab.getController().getTod().equals(tod)) {
                    contentTabPane.getSelectionModel().select(tab);
                    return;
                }
            }
        }
        TodContentTab todContentTab = new TodContentTab(this, tod);
        contentTabPane.getTabs().add(todContentTab);
        contentTabPane.getSelectionModel().select(todContentTab);
    }

    @Override
    public void delete(Tod tod) {
        Alert alert = Message.confirmDeletionTod();
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // delete in database
                dataInterface.delete(tod);

            // close tab if it is open
            TodContentTab todContentTabToRemove = null;
            for (Tab tab : contentTabPane.getTabs()) {
                if (tab.getClass().equals(TodContentTab.class)) {
                    TodContentTab todContentTab = (TodContentTab)tab;
                    if (todContentTab.getTodController().getTod().equals(tod)) {
                        todContentTabToRemove =todContentTab;
                    }
                }
            }
            this.contentTabPane.getTabs().remove(todContentTabToRemove);

            // update list of Tod in menu
            this.topMenuBar.updateTodMenu();
        } else if (result.get() == ButtonType.CANCEL) {
            return;
        }
    }

    @Override
    public void duplicate(Tod tod) {

    }

    @Override
    public void openAboutWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLSource.ABOUT.getUrl()));
        try {
            Parent parent = (Parent)fxmlLoader.load();
            Stage stage1 = new Stage();
            stage1.setTitle("About");
            stage1.setScene(new Scene(parent));
            stage1.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
