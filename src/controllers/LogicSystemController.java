package controllers;

import data.Conexion;
import data.LogicSystem;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class LogicSystemController implements Initializable {

    // COMPONENTES GUI
    @FXML private AppController appController;
    
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
    
    private ObservableList<LogicSystem> listLogicSystem;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listLogicSystem = FXCollections.observableArrayList();
        manageEvents();
        
    }

    public void setAppController(AppController aThis) {
        this.appController=aThis;
    }
    
    public void fillData(){
        tevwLogicSystem.setItems(listLogicSystem);
        
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
        Conexion conexion = appController.getConexion();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        
        conexion.establecerConexion();
        
        LogicSystem ls = new LogicSystem(0, ttfdLabel.getText(), ttaaDescription.getText(), timestamp);
        
        int result = ls.saveData(conexion.getConnection());
        ls.setIdLogicSystem(LogicSystem.currentAutoIncrement);
        
        if (result == 1){
            listLogicSystem.add(ls);
            
        }
        conexion.cerrarConexion();
        
        // Log message
        log("System", "New Logic System created: " + ls);
    }
    
    @FXML
    public void deleteLogicSystem(){
        Conexion conexion = appController.getConexion();
        conexion.establecerConexion();
        
        LogicSystem ls = (LogicSystem)this.tevwLogicSystem.getSelectionModel().getSelectedItem();
        int resultado = ls.deleteData(conexion.getConnection());
        conexion.cerrarConexion();

        if (resultado == 1){
                listLogicSystem.remove(tevwLogicSystem.getSelectionModel().getSelectedIndex());
                //JDK 8u>40
                                 log("User", "Deleted Logic System " + ls);
        }
    }
    
    @FXML
    public void updateLogicSystem(){
        Conexion conexion = appController.getConexion();
        int index = tevwLogicSystem.getSelectionModel().getSelectedIndex();
        
        LogicSystem ls = new LogicSystem(
                Integer.valueOf(ttfdId.getText()),
                ttfdLabel.getText(), 
                ttaaDescription.getText(),
                Timestamp.valueOf(lblCreationDate.getText()));
        
        conexion.establecerConexion();
        int result = ls.updateData(conexion.getConnection());
        
        conexion.cerrarConexion();

        if (result == 1){
                listLogicSystem.set(index,ls);
                //JDK 8u>40
                log("User", "Updated a Logic System data: " + ls);
        }
        tevwLogicSystem.getSelectionModel().clearAndSelect(index);
        
    }

    public ObservableList<LogicSystem> getListLogicSystem() {
        return listLogicSystem;
    }


    private void log(String type, String message) {
        appController.addLog(type, message);
    }
    
    
}