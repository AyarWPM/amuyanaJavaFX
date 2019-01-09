package main.controllers;

import main.data.*;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import main.data.tod.containers.Container0;
import main.data.tod.containers.Container1;
import main.data.tod.containers.Container2;
import main.data.tod.containers.Tod;
import main.extras.AmuyanaAlert;
import main.extras.AmuyanaTab;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class LogicSystemController implements Initializable {

    // COMPONENTES GUI
    @FXML private AppController appController;
    private DataInterface dataInterface;

    @FXML private TextField labelTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private Label idLabel;
    @FXML private Label creationDateLabel;

    @FXML private Button saveButton;
    @FXML private Button loadButton;
    @FXML private Button deleteButton;

    private LogicSystem logicSystem;

    private boolean isLogicSystemNew;
    private AmuyanaTab logicSystemTab;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.isLogicSystemNew=true;
    }

    public void setAppController(AppController aThis) {
        this.appController=aThis;
        this.dataInterface = appController.getDataInterface();
    }

    public void setLogicSystem(LogicSystem logicSystem) {
        this.isLogicSystemNew = false;
        this.logicSystem = logicSystem;
    }

    void setTab(AmuyanaTab logicSystemTab) {
        this.logicSystemTab = logicSystemTab;
    }

    void setDisableLoadButton(boolean state) {
        this.loadButton.setDisable(state);
    }

    void setDisableDeleteButton(boolean state) {
        this.deleteButton.setDisable(state);
    }

    public void fillData(){
        labelTextField.setText(logicSystem.getLabel());
        descriptionTextArea.setText(logicSystem.getDescription());
        idLabel.setText(Integer.toString(logicSystem.getIdLogicSystem()));
        creationDateLabel.setText(logicSystem.getCreationDate().toString());
    }

    /**
     * Method for both when a logic system exists and does not exist yet...
     */
    @FXML
    public void save(){
        //first check that all fields are filled
        if(labelTextField.getText().isEmpty()|descriptionTextArea.getText().isEmpty()){
            AmuyanaAlert.completeAllFieldsAlert();
            return;
        }
        // Second, Is new logic system or not?
        // If the logic system is new
        if (this.isLogicSystemNew) {
            saveData();
        }
        // If the logic system exists already
        else {
            updateData();
        }
    }

    private void saveData() {
        DataConnection dataConnection = dataInterface.getDataConnection();
        dataConnection.connect();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        // LogicSystem
        LogicSystem ls = new LogicSystem(0, labelTextField.getText(), descriptionTextArea.getText(), timestamp);
        int resultLS = ls.saveData(dataConnection.getConnection());
        ls.setIdLogicSystem(LogicSystem.currentAutoIncrement);
        if (resultLS == 1){
            dataInterface.getListLogicSystem().add(ls);
        }
        logicSystem = ls;
        //logicSystemTab.setText(ls.getLabel());
        logicSystemTab.textProperty().bind(logicSystem.labelProperty());
        logicSystemTab.setLogicSystem(logicSystem);
        idLabel.setText(String.valueOf(logicSystem.getIdLogicSystem()));
        creationDateLabel.setText(String.valueOf(logicSystem.getCreationDate()));

        this.isLogicSystemNew=false;
        loadButton.setDisable(false);
        deleteButton.setDisable(false);
        appController.addToMenuListLogicSystem(logicSystem);

        // By default we create a tod that contains one fccContainer
        // Container0
        Container0 container0 = new Container0(0);
        if (container0.saveData(dataConnection.getConnection()) == 1) {
            container0.setIdContainer0(Container0.currentAutoIncrement);
        }

        // Tod
        Tod tod = new Tod(0,ls.getLabel(), this.logicSystem,container0);
        if (tod.saveData(dataConnection.getConnection()) == 1) {
            tod.setIdTod(Tod.currentAutoIncrement);
            dataInterface.getListTod().add(tod);
        }

        // Container1
        Container1 container1 = new Container1(0,container0);
        if (container1.saveData(dataConnection.getConnection()) == 1) {
            container1.setIdContainer1(Container1.currentAutoIncrement);
        }

        // Fcc
        Fcc fcc = new Fcc(0, ls.getLabel(),"New Fundamental conjunction of contradiction (FCC)");
        FccHasLogicSystem fccHasLogicSystem = new FccHasLogicSystem(fcc,this.logicSystem);
        int resultFcc = fcc.saveData(dataConnection.getConnection());
        fcc.setIdFcc(Fcc.currentAutoIncrement);
        int resultFccHasLogicSystem = fccHasLogicSystem.saveData(dataConnection.getConnection());
        if (resultFcc == 1 && resultFccHasLogicSystem==1) {
            dataInterface.getListFcc().add(fcc);
            dataInterface.getListFccHasLogicSystem().add(fccHasLogicSystem);
        }

        // Element0
        Element element0 = new Element(0,"e",0,fcc);
        if (element0.saveData(dataConnection.getConnection())==1) {
            element0.setIdElement(Element.currentAutoIncrement);
        }

        // Element0
        Element element1 = new Element(0,"e",1,fcc);
        if (element1.saveData(dataConnection.getConnection())==1) {
            element1.setIdElement(Element.currentAutoIncrement);
        }

        // Container2
        Container2 container2 = new Container2(0,fcc,container1);
        if (container2.saveData(dataConnection.getConnection()) == 1) {
            container2.setIdContainer2(Container2.currentAutoIncrement);
        }

        AmuyanaAlert.createdLogicSystemAlert();
        dataConnection.disconnect();
    }

    private void updateData() {
        DataConnection dataConnection = dataInterface.getDataConnection();
        dataConnection.connect();
        //logicSystem.setLabel(labelTextField.getText());
        logicSystem.labelProperty().setValue(labelTextField.getText());
        logicSystem.descriptionProperty().setValue(descriptionTextArea.getText());
        logicSystem.updateData(dataConnection.getConnection());
        //appController.refreshInMenuListLogicSystem(logicSystem);
        AmuyanaAlert.updatedLogicSystemAlert();
        dataConnection.disconnect();
    }

    @FXML
    void load() {
        appController.load(logicSystem);
    }
    
    @FXML
    public void delete(){
        appController.delete(logicSystem);
    }

    private void showCompleteFieldsAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save Logic System");
        alert.setHeaderText(null);
        alert.setContentText("Please complete all fields.");
        alert.showAndWait();
    }
}