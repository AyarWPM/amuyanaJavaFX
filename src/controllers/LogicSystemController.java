package controllers;

import data.DataConnection;
import data.DataInterface;
import data.LogicSystem;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import extras.AmuyanaAlert;
import extras.AmuyanaTab;
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

        DataConnection dataConnection = dataInterface.getDataConnection();
        dataConnection.connect();

        // Second, Is new logic system or not?
        // If the logic system is new
        if (this.isLogicSystemNew) {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            LogicSystem ls = new LogicSystem(0, labelTextField.getText(), descriptionTextArea.getText(), timestamp);
            int result = ls.saveData(dataConnection.getConnection());
            ls.setIdLogicSystem(LogicSystem.currentAutoIncrement);
            if (result == 1){
                dataInterface.getListLogicSystem().add(ls);
            }
            logicSystem = ls;
            //logicSystemTab.setText(ls.getLabel());
            logicSystemTab.textProperty().bind(logicSystem.LabelProperty());
            logicSystemTab.setLogicSystem(logicSystem);
            idLabel.setText(String.valueOf(logicSystem.getIdLogicSystem()));
            creationDateLabel.setText(String.valueOf(logicSystem.getCreationDate()));

            this.isLogicSystemNew=false;
            loadButton.setDisable(false);
            deleteButton.setDisable(false);
            appController.addToMenuListLogicSystem(logicSystem);
            AmuyanaAlert.createdLogicSystemAlert();
        }
        // If the logic system exists already
        else if (!this.isLogicSystemNew) {
            //logicSystem.setLabel(labelTextField.getText());
            logicSystem.LabelProperty().setValue(labelTextField.getText());
            logicSystem.DescriptionProperty().setValue(descriptionTextArea.getText());
            logicSystem.updateData(dataConnection.getConnection());
           //appController.refreshInMenuListLogicSystem(logicSystem);
            AmuyanaAlert.updatedLogicSystemAlert();
        }

        dataConnection.disconnect();
    }

    @FXML
    void load() {
        appController.loadLogicSystem(logicSystem);
    }
    
    @FXML
    public void delete(){
        appController.deleteLogicSystem(logicSystem);
    }

    private void showCompleteFieldsAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save Logic System");
        alert.setHeaderText(null);
        alert.setContentText("Please complete all fields.");
        alert.showAndWait();
    }
}