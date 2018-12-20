package controllers;

import data.*;
import extras.AmuyanaAlert;
import extras.AmuyanaTab;
import extras.LogicSystemMenu;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

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

    // NODES
    @FXML private TabPane mainTabPane;
    @FXML private Menu listLogicSystemMenu;
    @FXML private Menu tableOfDeductionsMenu;
    @FXML private Menu dialecticsMenu;
    @FXML private Menu statisticsMenu;
    //@FXML private MenuItem editLogicSystemMenuItem;

    @FXML private ListView<LogicSystem> logicSystemListView;
    @FXML private ListView tableOfDeductionsListView;
    @FXML private ListView dialecticsListView;
    private Stage stage;

    public void initialize() throws IOException {
        this.dataInterface = new DataHandler();
//        this.logicSystemMenu.getItems().add();
        // For debug or default mysql parameters:
        this.dataInterface.setDataConnectionValues("localhost", "amuyana", "");
        // Load logic systems and wait for the user to select one or create a new one
        this.dataInterface.loadData();
        fillMenuListLogicSystem();
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
    private void fillMenuListLogicSystem() {
        for (LogicSystem logicSystem : dataInterface.getListLogicSystem()) {
            addToMenuListLogicSystem(logicSystem);
        }
    }

    public void addToMenuListLogicSystem(LogicSystem logicSystem) {
        LogicSystemMenu menu = new LogicSystemMenu(logicSystem);
        menu.textProperty().bind(logicSystem.LabelProperty());

        listLogicSystemMenu.getItems().add(menu);
        MenuItem loadMenuItem = new MenuItem("Load");
        MenuItem editMenuItem = new MenuItem("Edit");
        MenuItem deleteMenuItem = new MenuItem("Delete");

        loadMenuItem.setOnAction(event -> {
            loadLogicSystem(logicSystem);
        });

        editMenuItem.setOnAction(event -> {
            editLogicSystem(logicSystem);
        });

        deleteMenuItem.setOnAction(event -> {
            deleteLogicSystem(logicSystem);
        });
        menu.getItems().addAll(loadMenuItem,editMenuItem,deleteMenuItem);
    }

    public void removeFromMenuListLogic(LogicSystem logicSystem) {
        LogicSystemMenu logicSystemMenu = null;
        for (MenuItem menu : listLogicSystemMenu.getItems()) {
            LogicSystemMenu menu1 = (LogicSystemMenu)menu;
            if (menu1.getLogicSystem().equals(logicSystem)) {
                logicSystemMenu = menu1;
            }
        }
        listLogicSystemMenu.getItems().remove(logicSystemMenu);
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
        logicSystemController.setDisableLoadButton(true);
        logicSystemController.setDisableDeleteButton(true);

        AmuyanaTab logicSystemTab = new AmuyanaTab("New Logic System", logicSystemSource, AmuyanaTab.TabType.LOGICSYSTEM);

        logicSystemController.setTab(logicSystemTab);

        mainTabPane.getTabs().add(logicSystemTab);
    }

    /**
     * At startup user selects the logic system he wants to work with.
     * @param logicSystem The logic system to be assumed during execution of the program.
     *                    More than one can be used but not simultaneously, tabs just remain open.
     */
    void loadLogicSystem(LogicSystem logicSystem) {
        setLogicSystem(logicSystem);
        AmuyanaAlert.loadedLogicSystemAlert();
        stage.setTitle("Amuyaña - " + logicSystem.getLabel());
        // Menus
        tableOfDeductionsMenu.setDisable(false);
        dialecticsMenu.setDisable(false);
        statisticsMenu.setDisable(false);
    }

    /**
     * The user selects to load an existing logic system.
     */
    private void editLogicSystem(LogicSystem logicSystem) {
        for (Tab tab : mainTabPane.getTabs()) {
            AmuyanaTab amuyanaTab = (AmuyanaTab)tab;
            if (amuyanaTab.getLogicSystem().equals(logicSystem)) {
                AmuyanaAlert.alreadyEditingLogicSystem();
                return;
            }
        }
        // Opens a tab if there's not one open already!
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(FXMLSource.LOGIC_SYSTEM.getUrl()));

        ScrollPane logicSystemSource = null;
        try {
            logicSystemSource = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AmuyanaTab logicSystemTab = new AmuyanaTab(AmuyanaTab.TabType.LOGICSYSTEM);
        logicSystemTab.textProperty().bind(logicSystem.LabelProperty());
        logicSystemTab.setContent(logicSystemSource);
        logicSystemTab.setLogicSystem(logicSystem);

        LogicSystemController logicSystemController = loader.getController();

        logicSystemController.setAppController(this);
        logicSystemController.setTab(logicSystemTab);
        logicSystemController.setLogicSystem(logicSystem);
        logicSystemController.fillData();

        mainTabPane.getTabs().add(logicSystemTab);
    }

    public void deleteLogicSystem(LogicSystem logicSystem) {
        Alert alert = AmuyanaAlert.confirmDeletionLogicSystem();

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            DataConnection dataConnection = dataInterface.getDataConnection();
            dataConnection.connect();
            int resultado = logicSystem.deleteData(dataConnection.getConnection());
            dataConnection.disconnect();

            if (resultado == 1){
                dataInterface.getListLogicSystem().remove(logicSystem);
            }

            AmuyanaTab tabToRemove = null;
            for (Tab tab : mainTabPane.getTabs()) {
                AmuyanaTab amuyanaTab = (AmuyanaTab)tab;
                if (amuyanaTab.getType().equals(AmuyanaTab.TabType.LOGICSYSTEM)) {
                    tabToRemove = amuyanaTab;
                }

            }
            getMainTabPane().getTabs().remove(tabToRemove);

            removeFromMenuListLogic(logicSystem);
        } else if (result.get()==ButtonType.CANCEL){
            System.out.println("cancelling");
            return;
        }
    }
}