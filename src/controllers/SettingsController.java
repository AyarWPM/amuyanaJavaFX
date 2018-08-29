package controllers;

import data.Conexion;
import data.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class SettingsController implements Initializable {

    @FXML private AppController appController;

    @FXML private ToggleButton btnConnectDisconnect;
    @FXML private Button btnLogInOut;
    @FXML private CheckBox ckbxUseDefaultServer;
    
    @FXML private TextField ttfdHostname;
    @FXML private TextField ttfdDbUsername;
    @FXML private TextField ttfdDbPassword;
    
    @FXML private TextField ttfdUserName;
    @FXML private TextField ttfdUserPassword;
    @FXML private Label lblDateJoined;
    
    @FXML final private String DBHOST="localhost";
    @FXML final private String DBUSER="freeclient";
    @FXML final private String DBPASSWORD="";
    
    private User currentUser;
    
    ObservableList<User> listUser;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.listUser = FXCollections.observableArrayList();
    }    

    public void setAppController(AppController aThis) {
        this.appController=aThis;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    
    @FXML
    private void connectDisconnect(){
        if(btnConnectDisconnect.isSelected()){
            if(Conexion.testConexion(ttfdHostname.getText(), ttfdDbUsername.getText(), ttfdDbPassword.getText())){
                Conexion.setUrl(ttfdHostname.getText());
                Conexion.setUsername(ttfdDbUsername.getText());
                Conexion.setPassword(ttfdDbPassword.getText());
                btnConnectDisconnect.setText("Disconnect");
                ttfdDbPassword.setDisable(true);
                ttfdDbUsername.setDisable(true);
                ttfdHostname.setDisable(true);
                ckbxUseDefaultServer.setDisable(true);
                appController.loadData();
                appController.addLog(this.toString(), "Connection to mysql succesfull!");
                
            } else {
                btnConnectDisconnect.setSelected(false);
                appController.addLog(this.toString(), "The connexion could not be stablished!");
            }
        } else {
            Conexion.setUrl(null);
            Conexion.setUsername(null);
            Conexion.setPassword(null);
            btnConnectDisconnect.setText("Connect");
            ckbxUseDefaultServer.setDisable(false);
            if(!ckbxUseDefaultServer.isSelected()){
                ttfdDbPassword.setDisable(false);
                ttfdDbUsername.setDisable(false);
                ttfdHostname.setDisable(false);
            }
            
            appController.clearLists();
            appController.addLog(this.toString(), "Disconnected from mysql!");
        }
        
    }
    
    @FXML
    private void logInOutUser(ActionEvent e){
        // If it is not logged in, try to login
        if(btnLogInOut.getText().equals("Login")){
            for(User user:this.appController.getListUser()){
                if(this.ttfdUserName.getText().equals(user.getUsername())){
                    if(this.ttfdUserPassword.getText().equals(user.getPassword())){
                        setCurrentUser(user);
                        // complete
                        this.ttfdUserName.setDisable(true);
                        this.ttfdUserPassword.setDisable(true);
                        this.lblDateJoined.setText(user.getDate().toString());
                        this.btnLogInOut.setText("Logout");
                        appController.addLog("System", "User logged in!");
                    }
                }
            }
        // If it is logged in, log out
        } else if(btnLogInOut.getText().equals("Logout")){
            setCurrentUser(null);
            this.ttfdUserName.setDisable(false);
            this.ttfdUserPassword.setDisable(false);
            ttfdUserName.setText("");
            ttfdUserPassword.setText("");
            lblDateJoined.setText("");
            btnLogInOut.setText("Login");
        }
    }

    public void autoClicks() {
        
        btnConnectDisconnect.fire();
        
    }
    
    @FXML
    public void useDefaultServer(){
        if(ckbxUseDefaultServer.isSelected()){
            ttfdHostname.setText(this.DBHOST);
            ttfdDbUsername.setText(this.DBUSER);
            ttfdDbPassword.setText(this.DBPASSWORD);
            ttfdHostname.setDisable(true);
            ttfdDbUsername.setDisable(true);
            ttfdDbPassword.setDisable(true);
        } else if(!ckbxUseDefaultServer.isSelected()){
            ttfdHostname.setText(null);
            ttfdDbUsername.setText(null);
            ttfdDbPassword.setText(null);
            ttfdHostname.setDisable(false);
            ttfdDbUsername.setDisable(false);
            ttfdDbPassword.setDisable(false);
        }
        
        
    }

    public ObservableList<User> getListUser() {
        return this.listUser;
    }
    
}
