package com.amuyana.app.node.menu;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.MainBorderPane;
import com.amuyana.app.node.NodeInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

public class TopMenuBar extends MenuBar {
    NodeInterface nodeInterface;
    DataInterface dataInterface;

    private Menu fileMenu;
    private Menu logicSystemMenu;
    private Menu todMenu;
    private Menu dialecticsMenu;
    private Menu statisticsMenu;
    private Menu tutorialsMenu;
    private Menu aboutMenu;

    private ObservableList<LogicSystemMenu> logicSystemMenus;
    private ObservableList<TodMenu> todMenus;

    public TopMenuBar(NodeInterface nodeInterface) {
        this.nodeInterface = nodeInterface;
        this.dataInterface = MainBorderPane.getDataInterface();

        initialize();
        setInitialValues();
        debug();
    }

    private void debug() {
        Menu debugMenu = new Menu("Debug tools");
        MenuItem debug1 = new MenuItem("Load LS and Open the first ToD");

        debug1.setOnAction(actionEvent -> {
            // Open the first ToD
            nodeInterface.load(dataInterface.getListLogicSystem().get(0));
            nodeInterface.open(dataInterface.getTods(dataInterface.getListLogicSystem().get(0)).get(0));
        });

        MenuItem debug2 = new MenuItem("Load lS and a new ToD");
        debug2.setOnAction(e->{
            // New ToD
            nodeInterface.load(dataInterface.getListLogicSystem().get(0));
            todMenu.getItems().get(0).fire();

        });

        debugMenu.getItems().addAll(debug1,debug2);
        getMenus().add(debugMenu);
    }

    private void setInitialValues() {
        this.todMenu.setDisable(true);
        this.dialecticsMenu.setDisable(true);
        this.statisticsMenu.setDisable(true);
    }

    public void logicSystemIsLoaded(boolean isLoaded) {
        if (isLoaded) {
            todMenu.setDisable(false);
            dialecticsMenu.setDisable(false);
            statisticsMenu.setDisable(false);
        } else {
            todMenu.setDisable(true);
            dialecticsMenu.setDisable(true);
            statisticsMenu.setDisable(true);
        }

    }

    private void initialize() {
        this.fileMenu = new Menu("File");
        this.logicSystemMenu = new Menu("Logic System");
        this.todMenu = new Menu("Table of Deductions");

        initializeFileMenu();
        initializeLogicSystemMenu();
        initializeTodMenu();
        initializeDialecticsMenu();
        initializeStatisticsMenu();
        initializeTutorialsMenu();
        initializeAboutMenu();
        getMenus().addAll(fileMenu,logicSystemMenu,todMenu,dialecticsMenu,statisticsMenu,tutorialsMenu,aboutMenu);

    }

    private void initializeFileMenu() {
        MenuItem connexionMenuItem = new MenuItem("Connexion");
        connexionMenuItem.setOnAction(actionEvent -> {
            openConnectionTab();
        });

        MenuItem reinitializeDatabase = new MenuItem("Reinitianalize database");
        reinitializeDatabase.setOnAction(e->{
            dataInterface.reinitializeDatabase();
        });

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> {
            nodeInterface.exitAmuyana();
        });
        fileMenu.getItems().addAll(connexionMenuItem,reinitializeDatabase,exitMenuItem);
    }

    private void initializeLogicSystemMenu() {
        logicSystemMenus = FXCollections.observableArrayList();
        MenuItem newLogicSystemMenuItem = new MenuItem("New");
        newLogicSystemMenuItem.setOnAction(actionEvent -> nodeInterface.openLogicSystemTab());

        logicSystemMenu.getItems().addAll(newLogicSystemMenuItem,new SeparatorMenuItem());

        for (LogicSystem logicSystem : dataInterface.getListLogicSystem()) {
            LogicSystemMenu logicSystemMenu = new LogicSystemMenu(logicSystem, nodeInterface);
            logicSystemMenu.textProperty().bind(logicSystem.labelProperty());
            this.logicSystemMenu.getItems().add(logicSystemMenu);
            this.logicSystemMenus.add(logicSystemMenu);
        }
    }

    public void initializeTodMenu() {
        this.todMenus = FXCollections.observableArrayList();
        MenuItem newTodMenuItem = new MenuItem("New");
        newTodMenuItem.setOnAction(actionEvent -> nodeInterface.newTodTab());
        this.todMenu.getItems().addAll(newTodMenuItem,new SeparatorMenuItem());
    }

    private void initializeDialecticsMenu() {
        this.dialecticsMenu = new Menu("Dialectics");
    }

    private void initializeStatisticsMenu() {
        this.statisticsMenu = new Menu("Statistics");
    }

    private void initializeTutorialsMenu() {
        this.tutorialsMenu = new Menu("Tutorials");
    }

    private void initializeAboutMenu() {
        this.aboutMenu = new Menu("About");
    }

    private void openConnectionTab() {
        nodeInterface.openConnectionTab();
    }

    // LOGIC SYSTEM
    public void removeLogicSystemMenu(LogicSystem logicSystem) {
        LogicSystemMenu toRemove = null;
        for (LogicSystemMenu logicSystemMenu : this.logicSystemMenus) {
            if (logicSystemMenu.getLogicSystem().equals(logicSystem)) {
                toRemove=logicSystemMenu;
            }
        }
        this.logicSystemMenu.getItems().remove(toRemove);
        this.logicSystemMenus.remove(toRemove);
    }

    public void addMenu(LogicSystem logicSystem) {
        LogicSystemMenu logicSystemMenu = new LogicSystemMenu(logicSystem, nodeInterface);
        logicSystemMenu.textProperty().bind(logicSystem.labelProperty());
        this.logicSystemMenu.getItems().add(logicSystemMenu);
        this.logicSystemMenus.add(logicSystemMenu);
    }

    // TOD
    public void addMenu(Tod tod) {
        TodMenu todMenu = new TodMenu(tod, nodeInterface);
        todMenu.textProperty().bind(tod.labelProperty());
        this.todMenu.getItems().add(todMenu);
        this.todMenus.add(todMenu);
    }

    public void updateTodMenu() {
        clearTodMenu();
        fillTodMenu();
    }

    public void clearTodMenu() {
        this.todMenus = FXCollections.observableArrayList();
        ObservableList<TodMenu> tempToRemove = FXCollections.observableArrayList();

        for (MenuItem menuItem : this.todMenu.getItems()) {
            if (menuItem.getClass().equals(TodMenu.class)) {
                tempToRemove.add((TodMenu)menuItem);
            }
        }
        todMenu.getItems().removeAll(tempToRemove);
    }

    public void fillTodMenu() {
        for (Tod tod : dataInterface.getTods(nodeInterface.getLogicSystem())) {
            TodMenu todMenu = new TodMenu(tod, nodeInterface);
            todMenu.textProperty().bind(tod.labelProperty());
            this.todMenus.add(todMenu);
        }
        for (TodMenu todMenu : todMenus) {
            this.todMenu.getItems().add(todMenu);
        }
    }
}
