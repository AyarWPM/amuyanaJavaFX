package controllers;

import data.CClass;
import data.Fcc;
import data.Inclusion;
import extras.Styles;

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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;


public class TodController implements Initializable {

    private AppController appController;
    
    private ObservableList<Styles> listStyles;
    
    private TodContainer todContainer;
       
    @FXML private HBox todContent;
    
    // MENU BAR
    @FXML private TitledPane tdpeFcc;
    @FXML private TitledPane tdpeTod;
    @FXML private Accordion anMenu;
    
    // When nothing is selected, only menu of the TOD
    @FXML private ComboBox<Styles> cobxStyle;
    @FXML private ComboBox<Fcc> cobxFcc;
    @FXML private ComboBox<CClass> cobxCClass;
    @FXML private ComboBox<Inclusion> cobxInclusion;
    
    // When a FCC is selected
    @FXML private ToggleButton tebnExpandInclusions;
    @FXML private ToggleButton tebnExpandPositive;
    @FXML private ToggleButton tebnExpandNegative;
    @FXML private ToggleButton tebnExpandSymmetric;
    
    public static ArrayList<Fcc> listFccsInScene;
    
    //public static ArrayList<MultiContainer> listMultiContainers;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listFccsInScene = new ArrayList<>();

        listStyles = FXCollections.observableArrayList();
        
        manageEvents();
    }
    
    public void setAppController(AppController aThis) {
        this.appController=aThis;
        
        TodContainer.setAppController(aThis);
        LevelContainer.setAppController(aThis);
        AnalogyContainer.setAppController(aThis);
        MultiContainer.setAppController(aThis);
        FccContainer.setAppController(aThis);
        FormulaContainer.setAppController(aThis);
    }
    
    public static ArrayList<Fcc> getListFccsInScene() {
        return listFccsInScene;
    }
    
    private void log(String debug, String message) {
        appController.addLog(debug, message);
    }

    public void fillData() {
        cobxFcc.setItems(appController.getListFcc());
        for(Styles f:Styles.values()){
            listStyles.add(f);
        }
        
        cobxStyle.setItems(listStyles);

        cobxFcc.getSelectionModel().selectFirst();
    }
    
    public void manageEvents(){
        cobxFcc.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Fcc>() {
            @Override
            public void changed(ObservableValue<? extends Fcc> observable, Fcc oldValue, Fcc newValue) {
                if(newValue!=null){
                    
                    // list of fcc's, setting the first one
                    listFccsInScene.clear();
                    listFccsInScene.add(newValue);
                    
                    deployInitial(newValue);
                }
            }
        });
        
        cobxStyle.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Styles>() {
            @Override
            public void changed(ObservableValue<? extends Styles> observable, Styles oldValue, Styles newValue) {
                if(newValue!=null){
                    FormulaContainer.setStyle(newValue);
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
        ArrayList<Analogy> listAnalogy = appController.getListAnalogyForInitial(cobxFcc.getSelectionModel().getSelectedItem());   
    }   
}