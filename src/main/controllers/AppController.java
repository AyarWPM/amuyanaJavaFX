package main.controllers;

import main.data.*;
import main.data.tod.containers.Tod;
import main.extras.AmuyanaAlert;
import main.extras.AmuyanaMenu;
import main.extras.AmuyanaTab;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import main.FXMLSource;

/**
 * This class is the main window (App.fxml) controller...
 * Other controllers are instantiated here and each has a unique AppController instance as a field.
 *
 */
public class AppController {
    // GENERAL APP
    private DataInterface dataInterface;
    private LogicSystem logicSystem;

    // NODES
    @FXML private TabPane mainTabPane;
    @FXML private Menu savedLogicSystemMenu;
    @FXML private Menu todMenu;
    @FXML private Menu dialecticsMenu;
    @FXML private Menu statisticsMenu;
    @FXML private Menu savedTodMenu;

    @FXML private ListView<LogicSystem> logicSystemListView;
    @FXML private ListView tableOfDeductionsListView;
    @FXML private ListView dialecticsListView;
    private Stage stage;

    @FXML
    private void debug() {
        System.out.println("debug action");
        load(dataInterface.getListLogicSystem().get(0));
    }

    public void initialize() throws IOException {
        this.dataInterface = new DataHandler();

        // For debug or default mysql parameters:
        this.dataInterface.setDataConnectionValues("localhost", "amuyana", "");

        // Load logic systems and wait for the user to select one or create a new one
        this.dataInterface.loadData();

        fillMenuListLogicSystem();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }

    private TabPane getMainTabPane() {
        return this.mainTabPane;
    }

    public DataInterface getDataInterface() {
        return this.dataInterface;
    }

    public LogicSystem getLogicSystem() {
        return this.logicSystem;
    }

    void setLogicSystem(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
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
        mainTabPane.getSelectionModel().select(logicSystemTab);
    }

    /**
     * This method will add the main.data in the Logic System Pane only, then as the user selects one logic system
     * the other panes will be added with information.
     */
    private void fillMenuListLogicSystem() {
        for (LogicSystem logicSystem : dataInterface.getListLogicSystem()) {
            addToMenuListLogicSystem(logicSystem);
        }
    }

    void addToMenuListLogicSystem(LogicSystem logicSystem) {
        AmuyanaMenu menu = new AmuyanaMenu(logicSystem);
        menu.textProperty().bind(logicSystem.labelProperty());

        savedLogicSystemMenu.getItems().add(menu);
        MenuItem loadMenuItem = new MenuItem("Load");
        MenuItem editMenuItem = new MenuItem("Edit");
        MenuItem deleteMenuItem = new MenuItem("Delete");

        loadMenuItem.setOnAction(event -> {
            load(logicSystem);
        });

        editMenuItem.setOnAction(event -> {
            edit(logicSystem);
        });

        deleteMenuItem.setOnAction(event -> {
            delete(logicSystem);
        });
        menu.getItems().addAll(loadMenuItem,editMenuItem,deleteMenuItem);
    }

    private void fillMenuListTod() {
        for (Tod tod : dataInterface.getListTod()) {
            addToMenuListTod(tod);
        }
    }

    void addToMenuListTod(Tod tod) {
        AmuyanaMenu menu = new AmuyanaMenu(tod);
        menu.textProperty().bind(tod.labelProperty());

        savedTodMenu.getItems().add(menu);
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem deleteMenuItem = new MenuItem("Delete");

        openMenuItem.setOnAction(event -> {
            open(tod);
        });

        deleteMenuItem.setOnAction(event -> {
            delete(tod);
        });
        menu.getItems().addAll(openMenuItem,deleteMenuItem);
    }

    // LOGIC SYSTEM MENU METHODS

    /**
     * At startup user selects the logic system he wants to work with.
     * @param logicSystem The logic system to be assumed during execution of the program.
     *                    More than one can be used but not simultaneously, tabs just remain open.
     */
    void load(LogicSystem logicSystem) {
        setLogicSystem(logicSystem);
        AmuyanaAlert.loadedLogicSystemAlert();
        stage.setTitle("Amuyaña - " + logicSystem.getLabel());
        // Menus
        todMenu.setDisable(false);
        dialecticsMenu.setDisable(false);
        statisticsMenu.setDisable(false);

        fillMenuListTod();
    }

    /**
     * The user selects to load an existing logic system.
     */
    private void edit(LogicSystem logicSystem) {
        for (Tab tab : mainTabPane.getTabs()) {
            AmuyanaTab amuyanaTab = (AmuyanaTab)tab;
            if (amuyanaTab.getType().equals(AmuyanaTab.TabType.LOGICSYSTEM)) {
                if (amuyanaTab.getLogicSystem().equals(logicSystem)) {
                    AmuyanaAlert.alreadyEditingLogicSystem();
                    return;
                }
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
        logicSystemTab.textProperty().bind(logicSystem.labelProperty());
        logicSystemTab.setContent(logicSystemSource);
        logicSystemTab.setLogicSystem(logicSystem);

        LogicSystemController logicSystemController = loader.getController();

        logicSystemController.setAppController(this);
        logicSystemController.setTab(logicSystemTab);
        logicSystemController.setLogicSystem(logicSystem);
        logicSystemController.fillData();

        mainTabPane.getTabs().add(logicSystemTab);
        mainTabPane.getSelectionModel().select(logicSystemTab);
    }

    public void delete(LogicSystem logicSystem) {
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
            //
            if (this.logicSystem!=null){
                if (this.logicSystem.equals(logicSystem)) {
                    setLogicSystem(null);
                    stage.setTitle("Amuyaña");
                    // Menus
                    todMenu.setDisable(true);
                    dialecticsMenu.setDisable(true);
                    statisticsMenu.setDisable(true);
                }
            }

        } else if (result.get()==ButtonType.CANCEL){
            System.out.println("cancelling");
            return;
        }
    }

    private void removeFromMenuListLogic(LogicSystem logicSystem) {
        AmuyanaMenu logicSystemMenu = null;
        for (MenuItem menuItem : savedLogicSystemMenu.getItems()) {
            AmuyanaMenu menu = (AmuyanaMenu) menuItem;
            if (menu.getObject().equals(logicSystem)) {
                logicSystemMenu = menu;
            }
        }
        savedLogicSystemMenu.getItems().remove(logicSystemMenu);
    }

    // TABLE OF DEDUCTIONS (TOD) MENU METHODS

    @FXML
    private void createNewTod() {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(FXMLSource.TOD.getUrl()));

        SplitPane todSource = null;
        try {
            todSource = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TodController todController = loader.getController();
        todController.setAppController(this);

        AmuyanaTab todTab = new AmuyanaTab("New Table of deductions", todSource, AmuyanaTab.TabType.TOD);

        todController.setTab(todTab);

        mainTabPane.getTabs().add(todTab);
        mainTabPane.getSelectionModel().select(todTab);
    }

    void open(Tod tod) {

    }

    void delete(Tod tod) {

    }
}