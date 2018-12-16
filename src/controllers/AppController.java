package controllers;

import data.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.io.IOException;
import javafx.fxml.FXML;
import main.FXMLSource;

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

    @FXML private TabPane mainTabPane;

    @FXML private LogicSystemController logicSystemController;
    @FXML private TodController todController;
    @FXML private DialecticController dialecticController;
    @FXML private StatsController statsController;


    // CONTROLLERS
/*
    @FXML private DualitiesController dualitiesController;
    @FXML private InclusionController inclusionController;
    @FXML private CClassController cClassController;

    @FXML private StcController stcController;
    @FXML private SyllogismController syllogismController;

    @FXML private SettingsController settingsController;
*/

    public void initialize() throws IOException {
        this.dataInterface = new DataHandler();

        // For debug or default mysql parameters:
        this.dataInterface.setDataConnectionValues("localhost", "amuyana", "");
        this.dataInterface.loadData();
        loadFXMLSources();
        fillData();
    }

    public DataInterface getDataInterface() {
        return this.dataInterface;
    }

    /**
     *
     * @throws IOException If can't load data from database
     */
    private void loadFXMLSources() throws IOException {
        for(FXMLSource fxmlSource: FXMLSource.values()){

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlSource.getUrl()));

            Node node = loader.load();
            fxmlSource.setNode(node);

            switch(fxmlSource){
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

    public void databaseMenuItemAction(ActionEvent actionEvent) {

    }

    @FXML
    private void buttonTOD() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXMLSource.TOD.getUrl()));
        TodController todController = loader.getController();
        //todController.setAppController(this);
        try {
            mainTabPane.getTabs().add(new Tab("TOD",loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*
    // called from dualityController to reload the list of dynamisms once we
    // create or duplicate new fccs
    public void refreshDataInclusionModule(){
        inclusionController.refreshData();
    }

    public void refreshDataClassModule(){
        cClassController.refreshData();
    }
*/
}