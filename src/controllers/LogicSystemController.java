package controllers;

import data.DataConnection;
import data.DataInterface;
import data.LogicSystem;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class LogicSystemController implements Initializable {

    // COMPONENTES GUI
    @FXML private AppController appController;
    private DataInterface dataInterface;
    
    @FXML private TextField ttfdId;
    @FXML private TextField ttfdLabel;
    @FXML private Label lblCreationDate;
    @FXML private TextArea ttaaDescription;

    @FXML private TableView tevwLogicSystem;
    
    @FXML TableColumn<LogicSystem, String> tecnLabel;
    @FXML TableColumn<LogicSystem, String> tecnDescription;
    @FXML TableColumn<LogicSystem, Timestamp> tecnCreationDate;
    
    @FXML Button bnSave;
    @FXML Button bnUpdate;
    @FXML Button bnDelete;
    @FXML Button bnNew;
    @FXML Button bnDuplicate;
    @FXML Button bnJoin;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        manageEvents();
        
    }

    public void setAppController(AppController aThis) {
        this.appController=aThis;
        this.dataInterface = appController.getDataInterface();
    }
    
    public void fillData(){
        tevwLogicSystem.setItems(appController.dataInterface.getListLogicSystem());
        
        tecnLabel.setCellValueFactory(new PropertyValueFactory<LogicSystem,String>("label"));
        tecnDescription.setCellValueFactory(new PropertyValueFactory<LogicSystem,String>("description"));
        tecnCreationDate.setCellValueFactory(new PropertyValueFactory<LogicSystem,Timestamp>("creationDate"));
                
    }
    private void manageEvents(){
        tevwLogicSystem.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<LogicSystem>() {
                @Override
                public void changed(ObservableValue<? extends LogicSystem> arg0,
                    LogicSystem oldValue, LogicSystem newValue) {
                    if (newValue!=null){
                        ttfdId.setText(String.valueOf(newValue.getIdLogicSystem()));
                        ttfdLabel.setText(newValue.getLabel());
                        lblCreationDate.setText(newValue.getCreationDate().toString());
                        ttaaDescription.setText(newValue.getDescription());
                        
                        bnSave.setDisable(true);
                        bnUpdate.setDisable(false);
                        bnDelete.setDisable(false);

                        bnDuplicate.setDisable(false);
                        bnJoin.setDisable(false);
                    } else if (newValue==null){
                        newLogicSystem();
                    }
                    

                }

            }
        );
    }
    
    @FXML
    public void newLogicSystem(){
        ttfdId.setText(null);
        ttfdLabel.setText(null);
        lblCreationDate.setText(null);
        ttaaDescription.setText(null);
        // take into account that this might be called when selection is already clear
        if(!tevwLogicSystem.getSelectionModel().isEmpty()){
            tevwLogicSystem.getSelectionModel().clearSelection();
        }
        
        
        bnSave.setDisable(false);
        bnUpdate.setDisable(true);
        bnDelete.setDisable(true);

        bnDuplicate.setDisable(true);
        bnJoin.setDisable(true);
    }
    
    @FXML
    public void saveLogicSystem(){
        //first check that all fields are filled
        if(ttfdLabel.getText()==null||ttaaDescription.getText()==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Logic System");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields");
            alert.showAndWait();
            return;
        }

        DataConnection dataConnection = dataInterface.getDataConnection();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        
        dataConnection.connect();
        
        LogicSystem ls = new LogicSystem(0, ttfdLabel.getText(), ttaaDescription.getText(), timestamp);
        
        int result = ls.saveData(dataConnection.getConnection());
        ls.setIdLogicSystem(LogicSystem.currentAutoIncrement);
        
        if (result == 1){
            dataInterface.getListLogicSystem().add(ls);
            
        }
        dataConnection.disconnect();

    }
    
    @FXML
    public void deleteLogicSystem(){
        DataConnection dataConnection = dataInterface.getDataConnection();
        dataConnection.connect();
        
        LogicSystem ls = (LogicSystem)this.tevwLogicSystem.getSelectionModel().getSelectedItem();
        int resultado = ls.deleteData(dataConnection.getConnection());
        dataConnection.disconnect();

        if (resultado == 1){
                dataInterface.getListLogicSystem().remove(tevwLogicSystem.getSelectionModel().getSelectedIndex());
        }
    }
    
    @FXML
    public void updateLogicSystem(){
        DataConnection dataConnection = dataInterface.getDataConnection();
        int index = tevwLogicSystem.getSelectionModel().getSelectedIndex();
        
        LogicSystem ls = new LogicSystem(
                Integer.valueOf(ttfdId.getText()),
                ttfdLabel.getText(), 
                ttaaDescription.getText(),
                Timestamp.valueOf(lblCreationDate.getText()));
        
        dataConnection.connect();
        int result = ls.updateData(dataConnection.getConnection());
        
        dataConnection.disconnect();

        if (result == 1){
                dataInterface.getListLogicSystem().set(index,ls);
        }
        tevwLogicSystem.getSelectionModel().clearAndSelect(index);
        
    }

}