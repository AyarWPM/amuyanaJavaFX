package controllers;

import data.DataConnection;
import data.DataInterface;
import data.LogicSystem;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

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
    @FXML private Button loadButton;

    private LogicSystem logicSystem;

    private boolean isLogicSystemNew;
    private Tab logicSystemTab;


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

    void setTab(Tab logicSystemTab) {
        this.logicSystemTab = logicSystemTab;
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Logic System");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields.");
            alert.showAndWait();
            return;
        }

        DataConnection dataConnection = dataInterface.getDataConnection();
        dataConnection.connect();

        // Second, Is new logic system or not?
        if (this.isLogicSystemNew) {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            LogicSystem ls = new LogicSystem(0, labelTextField.getText(), descriptionTextArea.getText(), timestamp);
            int result = ls.saveData(dataConnection.getConnection());
            ls.setIdLogicSystem(LogicSystem.currentAutoIncrement);
            if (result == 1){
                dataInterface.getListLogicSystem().add(ls);
            }
            logicSystem = ls;
            logicSystemTab.setText(ls.getLabel());
            idLabel.setText(String.valueOf(logicSystem.getIdLogicSystem()));
            creationDateLabel.setText(String.valueOf(logicSystem.getCreationDate()));
            this.isLogicSystemNew=false;
            loadButton.setDisable(false);
        } else if (!this.isLogicSystemNew) {
            System.out.println("it's not new");
        }

        dataConnection.disconnect();
    }

    @FXML
    void load() {
        appController.setLogicSystem(logicSystem);
        appController.loadLogicSystem(logicSystem);
    }
    
    @FXML
    public void delete(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm deletion of logic system");
        alert.setHeaderText("Are you sure you want to delete this Logic System?");
        alert.setContentText("All the associated FCCs will be lost unless they are imported in another logic system.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            DataConnection dataConnection = dataInterface.getDataConnection();
            dataConnection.connect();
            int resultado = this.logicSystem.deleteData(dataConnection.getConnection());
            dataConnection.disconnect();

            if (resultado == 1){
                dataInterface.getListLogicSystem().remove(logicSystem);
            }
            appController.getMainTabPane().getTabs().remove(logicSystemTab);
        } else {
            return;
        }

    }
}