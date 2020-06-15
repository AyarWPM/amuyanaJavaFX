package com.amuyana.app.node.menu;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.NodeInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class TopMenuBar extends HBox {
    private NodeInterface nodeInterface;
    private DataInterface dataInterface;

    private Button connectionButton;
    private MenuButton logicSystemMenuButton;
    private MenuButton todMenuButton;
    private Button syllogismButton;
    private Button fccButton;
    private Button tutorialButton;
    private Button aboutButton;
    private Button statisticsButton;

    //private Menu statisticsMenu;
    private Menu dialecticsMenu;

    private ObservableList<LogicSystemMenu> logicSystemMenus;
    private ObservableList<TodMenu> todMenus;

    public TopMenuBar(NodeInterface nodeInterface) {
        this.nodeInterface = nodeInterface;
        this.dataInterface = NodeHandler.getDataInterface();
        buildMenu();
        setInitialStates();
    }

    private void setInitialStates() {
        this.logicSystemMenuButton.setDisable(true);
        this.todMenuButton.setDisable(true);

    }

    public void logicSystemIsLoaded(boolean isLoaded) {
        if (isLoaded) {
            todMenuButton.setDisable(false);
        } else {
            todMenuButton.setDisable(true);
        }
    }

    private void buildMenu() {
        buildConnectionMenu();
        buildLogicSystemMenu();
        buildTodMenu();
        buildSyllogismMenu();
        buildFccMenu();
        buildStatisticsButton();
        buildTutorialMenu();
        buildAboutMenu();

        getChildren().addAll(connectionButton,logicSystemMenuButton,todMenuButton,syllogismButton,fccButton,statisticsButton,tutorialButton, aboutButton);

        //getMenus().addAll(connectionMenu,logicSystemMenu,todMenu, helpMenu);
        //getMenus().addAll(fileMenu,logicSystemMenu,todMenu,dialecticsMenu,statisticsMenu,tutorialsMenu,helpMenu);
        //initializeDialecticsMenu();
        //initializeStatisticsMenu();
    }

    private void buildStatisticsButton() {
        this.statisticsButton = new Button("Statistics");
        ImageView statisticsImageView = new ImageView(new Image("/images/icons/menu/statistics.png", 50, 50, true, true));
        this.statisticsButton.setGraphic(statisticsImageView);
        this.statisticsButton.setContentDisplay(ContentDisplay.TOP);
        this.statisticsButton.setOnAction(actionEvent -> nodeInterface.openStatisticsTab());
    }

    private void buildConnectionMenu() {
        this.connectionButton = new Button("Connection");
        ImageView connectionImageView = new ImageView(
                new Image("/images/icons/menu/connection.png",50,50,true,true));
        this.connectionButton.setGraphic(connectionImageView);
        this.connectionButton.setContentDisplay(ContentDisplay.TOP);
        this.connectionButton.setOnAction(actionEvent -> nodeInterface.openConnectionTab());
    }

    private void buildLogicSystemMenu() {
        logicSystemMenus = FXCollections.observableArrayList();
        this.logicSystemMenuButton = new MenuButton("Logic System");
        ImageView systemImageView = new ImageView(
                new Image("/images/icons/menu/system.png",50,50,true,true));
        this.logicSystemMenuButton.setGraphic(systemImageView);
        this.logicSystemMenuButton.setContentDisplay(ContentDisplay.TOP);
        MenuItem newLogicSystemMenuItem = new MenuItem("New");
        newLogicSystemMenuItem.setOnAction(actionEvent -> nodeInterface.openLogicSystemTab());
        logicSystemMenuButton.getItems().addAll(newLogicSystemMenuItem,new SeparatorMenuItem());
    }

    private void buildTodMenu() {
        this.todMenus = FXCollections.observableArrayList();
        this.todMenuButton = new MenuButton("Table of Deductions");
        ImageView todImageView = new ImageView(
                new Image("/images/icons/menu/table.png",50,50,true,true));
        this.todMenuButton.setGraphic(todImageView);
        this.todMenuButton.setContentDisplay(ContentDisplay.TOP);
        MenuItem newTodMenuItem = new MenuItem("New");
        newTodMenuItem.setOnAction(actionEvent -> nodeInterface.newTodTab());
        this.todMenuButton.getItems().addAll(newTodMenuItem,new SeparatorMenuItem());
    }

    private void buildSyllogismMenu() {
        this.syllogismButton = new Button("Syllogisms");
        ImageView imageView = new ImageView(
                new Image("/images/icons/menu/syllogism.png",50,50,true,true));
        this.syllogismButton.setGraphic(imageView);
        this.syllogismButton.setContentDisplay(ContentDisplay.TOP);
        this.syllogismButton.setOnAction(actionEvent -> {
            nodeInterface.openSyllogismTab();
        });
    }

    private void buildFccMenu() {
        this.fccButton = new Button("Dualities");
        ImageView imageView = new ImageView(
                new Image("/images/icons/menu/fcc.png",50,50,true,true));
        this.fccButton.setGraphic(imageView);
        this.fccButton.setContentDisplay(ContentDisplay.TOP);
        this.fccButton.setOnAction(actionEvent -> nodeInterface.openFccTableTab());
    }

    private void initializeDialecticsMenu() {
        this.dialecticsMenu = new Menu("Dialectics");
    }
/*

    private void initializeStatisticsMenu() {
        this.statisticsMenu = new Menu("Statistics");
    }
*/

    private void buildTutorialMenu() {
        this.tutorialButton = new Button("Tutorial");
        ImageView imageView = new ImageView(
                new Image("/images/icons/menu/tutorial.png",50,50,true,true));
        this.tutorialButton.setGraphic(imageView);
        this.tutorialButton.setContentDisplay(ContentDisplay.TOP);
        this.tutorialButton.setOnAction(actionEvent -> {
            nodeInterface.log("Not yet implemented");
        });
    }

    private void buildAboutMenu() {
        this.aboutButton = new Button("About");
        ImageView imageView = new ImageView(
                new Image("/images/icons/menu/about.png",50,50,true,true));
        this.aboutButton.setGraphic(imageView);
        this.aboutButton.setContentDisplay(ContentDisplay.TOP);
        this.aboutButton.setOnAction(actionEvent -> {
            nodeInterface.openAboutWindow();
        });
    }


    // LOGIC SYSTEM
    public void removeLogicSystemMenu(LogicSystem logicSystem) {
        LogicSystemMenu toRemove = null;
        for (LogicSystemMenu logicSystemMenu : this.logicSystemMenus) {
            if (logicSystem.equals(logicSystemMenu.getLogicSystem())) {
                toRemove=logicSystemMenu;
            }
        }
        this.logicSystemMenuButton.getItems().remove(toRemove);
        this.logicSystemMenus.remove(toRemove);
    }

    public void addMenu(LogicSystem logicSystem) {
        LogicSystemMenu logicSystemMenu = new LogicSystemMenu(logicSystem, nodeInterface);
        logicSystemMenu.textProperty().bind(logicSystem.labelProperty());
        this.logicSystemMenuButton.getItems().add(logicSystemMenu);
        this.logicSystemMenus.add(logicSystemMenu);
    }

    public void addMenu(Tod tod) {
        TodMenu todMenu = new TodMenu(tod, nodeInterface);
        todMenu.textProperty().bind(tod.labelProperty());
        this.todMenuButton.getItems().add(todMenu);
        this.todMenus.add(todMenu);
    }

    public void updateTodMenu() {
        clearTodMenu();
        fillTodMenu();
    }

    private void clearTodMenu() {
        this.todMenus = FXCollections.observableArrayList();
        ObservableList<TodMenu> tempToRemove = FXCollections.observableArrayList();
        for (MenuItem menuItem : this.todMenuButton.getItems()) {
            if (menuItem.getClass().equals(TodMenu.class)) {
                tempToRemove.add((TodMenu)menuItem);
            }
        }
        todMenuButton.getItems().removeAll(tempToRemove);
    }

    private void fillTodMenu() {
        for (Tod tod : dataInterface.getTods(nodeInterface.getLogicSystem())) {
            TodMenu todMenu = new TodMenu(tod, nodeInterface);
            todMenu.textProperty().bind(tod.labelProperty());
            this.todMenus.add(todMenu);
        }
        for (TodMenu todMenu : todMenus) {
            this.todMenuButton.getItems().add(todMenu);
        }
    }

    private void clearLogicSystemMenu() {
        this.logicSystemMenus = FXCollections.observableArrayList();
        ObservableList<LogicSystemMenu> tempToRemove = FXCollections.observableArrayList();
        for (MenuItem menuItem : this.logicSystemMenuButton.getItems()) {
            if (menuItem.getClass().equals(LogicSystemMenu.class)) {
                tempToRemove.add((LogicSystemMenu) menuItem);
            }
        }
        this.logicSystemMenuButton.getItems().removeAll(tempToRemove);
    }

    private void fillLogicSystemMenu() {
        for (LogicSystem logicSystem : dataInterface.getListLogicSystem()) {
            LogicSystemMenu logicSystemMenu = new LogicSystemMenu(logicSystem, nodeInterface);
            this.logicSystemMenuButton.getItems().add(logicSystemMenu);
        }
    }

    public void resetMenus() {
        // Logic System
        this.logicSystemMenuButton.setDisable(false);
        clearLogicSystemMenu();
        fillLogicSystemMenu();
        // Tod
        todMenuButton.setDisable(true);
    }
}
