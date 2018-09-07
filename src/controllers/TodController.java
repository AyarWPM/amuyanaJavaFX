package controllers;

import data.CClass;
import data.Fcc;
import data.Inclusion;

import extras.tod.Analogy;
import extras.tod.FccContainer;
import extras.tod.FormulaContainer;
import extras.tod.LevelContainer;
import extras.tod.MultiContainer;
import extras.tod.TodContainer;
import extras.tod.AnalogyContainer;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;


public class TodController implements Initializable {

    private AppController appController;

    private TodContainer todContainer;
    
    @FXML private HBox todContent;
    
    // MENU BAR
    @FXML private TitledPane tdpeFcc;
    @FXML private TitledPane tdpeTod;
    @FXML private Accordion anMenu;
    
    // When nothing is selected, only menu of the TOD

    @FXML private ComboBox<Fcc> cobxFcc;
    @FXML private ComboBox<CClass> cobxCClass;
    @FXML private ComboBox<Inclusion> cobxInclusion;
    
    // When a FCC is selected
    @FXML private ToggleButton tebnExpandInclusions;
    @FXML private ToggleButton tebnExpandPositive;
    @FXML private ToggleButton tebnExpandNegative;
    @FXML private ToggleButton tebnExpandSymmetric;
    
    private static ArrayList<Fcc> listFccsInScene;

    //public static ArrayList<MultiContainer> listMultiContainers;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        listFccsInScene = new ArrayList<>();

        manageEvents();
    }
    
    public void setAppController(AppController aThis) {
        this.appController=aThis;
        
        setControllers(this);
        
    }

    public static ArrayList<Fcc> getListFccsInScene() {
        return listFccsInScene;
    }

    public static void addFccInScene(Fcc fcc) {
        listFccsInScene.add(fcc);
    }

    public static void removeFccInScene(Fcc fcc) {
        listFccsInScene.remove(fcc);
    }

    private void log(String debug, String message) {
        appController.addLog(debug, message);
    }

    public void fillData() {
        cobxFcc.setItems(appController.getListFcc());
        //cobxFcc.getSelectionModel().selectFirst();
    }
    
    public void manageEvents(){
        cobxFcc.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Fcc>() {
            @Override
            public void changed(ObservableValue<? extends Fcc> observable, Fcc oldValue, Fcc newValue) {
                if(newValue!=null){
                    
                    // list of fcc's, setting the first one
                    //listFccsInScene.clear();
                    //listFccsInScene.add(newValue);
                    
                    deployInitial(newValue);
                }
            }
        });
    }

    private void deployInitial(Fcc newValue) {
        
        this.todContainer = new TodContainer(newValue);
        todContent.getChildren().clear();
        todContent.getChildren().add(this.todContainer);
        this.todContainer.deploy();
    }
    
    @FXML
    public void debug(){

    }   

    private void setControllers(TodController todController) {
        TodContainer.setControllers(this.appController, todController);
        LevelContainer.setControllers(this.appController, todController);
        AnalogyContainer.setControllers(this.appController, todController);
        MultiContainer.setControllers(this.appController, todController);
        FccContainer.setControllers(this.appController, todController);
        FormulaContainer.setControllers(this.appController, todController);
    }

    //AnalogyContainer analogyContainerOf(MultiContainer multiContainer){

    //}
}