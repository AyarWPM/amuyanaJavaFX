package controllers;

import data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;

import extras.tod.Analogy;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Module;

/**
 * This class is the main window (App.fxml) controller...
 * Other controllers are instantiated here, controllers of components:
 *
 * - [x] Logic System
 * - Table of Deductions
 * - [x] Dialectics
 * - [x] Statistics
 *
 * Controllers of System:
 * - [x] Preferences
 * - [x] Tutorials
 *
 */
public class AppController {
    DataInterface dataInterface;

    @FXML private TabPane contentTabPane;

    @FXML private LogicSystemController logicSystemController;
    @FXML private TodController todController;
    @FXML private DialecticController dialecticController;
    @FXML private StatsController statsController;


    // CONTROLLERS
/*
    @FXML private DualitiesController dualitiesController;
    @FXML private InclusionController inclusionController;
    @FXML private CClassController cClassController;
    @FXML private TodToolbarController todToolbarController;
    @FXML private TodLeftPanelController todLeftPanelController;
    @FXML private TodRightPanelController todRightPanelController;

    @FXML private StcController stcController;
    @FXML private SyllogismController syllogismController;

    @FXML private SettingsController settingsController;
*/

    public void initialize() throws IOException {
        dataInterface = new DataHandler();

        loadModules();
        fillData();
    }

    public DataInterface getDataInterface() {
        return dataInterface;
    }

    /**
     *
     * @throws IOException If can't load data from database
     */
    private void loadModules() throws IOException {
        for(Module m:Module.values()){

            FXMLLoader loader = new FXMLLoader(getClass().getResource(m.getUrl()));

            Node node = loader.load();
            m.setNode(node);

            switch(m){
                case LOGIC_SYSTEM:{
                    this.logicSystemController = loader.getController();
                    this.logicSystemController.setAppController(this);
                    break;
                }

                case TOD:{
                    this.todController = loader.getController();
                    this.todController.setAppController(this);
                    break;
                }

                case DIALECTIC:{
                    this.dialecticController = loader.getController();
                    this.dialecticController.setAppController(this);
                    break;
                }

                case STATS:{
                    this.statsController = loader.getController();
                    this.statsController.setAppController(this);
                    break;
                }

            }
        }

    }

    private void fillData() {

    }


}