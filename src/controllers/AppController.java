package controllers;

import data.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import main.FXMLSource;

/**
 * This class is the main window (App.fxml) controller...
 * Other controllers are instantiated here, controllers of components:
 *
 * - [x] Logic System
 * - [x] Table of Deductions
 * - [x] Dialectics
 * - [x] Statistics
 *
 * Controllers of System:
 * - [x] Preferences
 * - [x] Tutorials
 *
 */
public class AppController {
    // GENERAL APP
    DataInterface dataInterface;
    LogicSystem logicSystem;

    // CONTROLLERS
    /*
    @FXML private LogicSystemController logicSystemController;
    @FXML private TodController todController;
    @FXML private DialecticController dialecticController;
    @FXML private StatsController statsController;
    */

    // NODES
    @FXML private TabPane mainTabPane;
    @FXML private Menu loadLogicSystemMenu;

    @FXML private ListView<LogicSystem> logicSystemListView;
    @FXML private ListView tableOfDeductionsListView;
    @FXML private ListView dialecticsListView;
    private Stage stage;

    public void initialize() throws IOException {
        this.dataInterface = new DataHandler();

        // For debug or default mysql parameters:
        this.dataInterface.setDataConnectionValues("localhost", "amuyana", "");
        // Load logic systems and wait for the user to select one or create a new one
        this.dataInterface.loadData();
        fillMenuLoadLogicSystem();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TabPane getMainTabPane() {
        return this.mainTabPane;
    }

    public DataInterface getDataInterface() {
        return this.dataInterface;
    }

    public LogicSystem getLogicSystem() {
        return this.logicSystem;
    }

    public void setLogicSystem(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
    }

    /**
     * This method will add the data in the Logic System Pane only, then as the user selects one logic system
     * the other panes will be added with information.
     */
    private void fillMenuLoadLogicSystem() {
        for (LogicSystem logicSystem : dataInterface.getListLogicSystem()) {
            MenuItem menuItem = new MenuItem(logicSystem.getLabel());
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(FXMLSource.LOGIC_SYSTEM.getUrl()));

            ScrollPane logicSystemSource = null;
            try {
                logicSystemSource = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            LogicSystemController logicSystemController = loader.getController();
            logicSystemController.setAppController(this);

            Tab logicSystemTab = new Tab(logicSystem.getLabel(), logicSystemSource);
            logicSystemController.setTab(logicSystemTab);
            logicSystemController.setLogicSystem(logicSystem);
            logicSystemController.fillData();
            TODO
            menuItem.setOnAction(event -> {//appController.setLogicSystem(ls);
                //appController.loadLogicSystem(ls);mainTabPane.getTabs().add(logicSystemTab);});
            loadLogicSystemMenu.getItems().add(menuItem);
        }
    }

    private void fillData2() {
        for (Fcc fcc : this.dataInterface.fccOf(this.logicSystem)) {
            MenuItem menuItem = new Menu(fcc.toString());
        }
        //TODO create EventHandler for menuItems...
    }

    private ContextMenu getLogicSystemContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem load = new MenuItem("Load");
        load.setOnAction(event -> loadLogicSystem(logicSystemListView.getSelectionModel().getSelectedItem()));

        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(event -> editLogicSystem(logicSystemListView.getSelectionModel().getSelectedItem()));

        contextMenu.getItems().addAll(load,edit);
        return contextMenu;
    }

    @FXML
    private void createNewLogicSystem() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(FXMLSource.LOGIC_SYSTEM.getUrl()));

        ScrollPane logicSystemSource = null;
        try {
            logicSystemSource = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LogicSystemController logicSystemController = loader.getController();
        logicSystemController.setAppController(this);


        Tab logicSystemTab = new Tab("New Logic System", logicSystemSource);
        logicSystemController.setTab(logicSystemTab);

        mainTabPane.getTabs().add(logicSystemTab);
    }

    /**
     * At startup user selects the logic system he wants to work with.
     * @param logicSystem The logic system to be assumed during execution of the program.
     *                    More than one can be used but not simultaneously, tabs just remain open.
     */
    void loadLogicSystem(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
        fillData2();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logic System Loaded");
        alert.setHeaderText(null);
        alert.setContentText("Logic System \"" + logicSystem.getLabel() + "\" has been loaded.");
        alert.showAndWait();

        stage.setTitle("Amuyaña - " + logicSystem.getLabel());
    }

    /**
     * The user selects to load an existing logic system.
     */
    private void editLogicSystem(LogicSystem logicSystem) {
        // Opens a tab ...
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(FXMLSource.LOGIC_SYSTEM.getUrl()));

        ScrollPane logicSystemSource = null;
        try {
            logicSystemSource = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Tab logicSystemTab = new Tab(logicSystem.getLabel(), logicSystemSource);

        LogicSystemController logicSystemController = loader.getController();

        logicSystemController.setAppController(this);
        logicSystemController.setTab(logicSystemTab);
        logicSystemController.setLogicSystem(logicSystem);
        logicSystemController.fillData();

        mainTabPane.getTabs().add(logicSystemTab);
    }

}