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

    @FXML private ToggleButton connectDisconnectToggleButton;

    @FXML private CheckBox useDefaultServerCheckBox;

    @FXML private TextField hostNameTextField;
    @FXML private TextField hostUserTextField;
    @FXML private TextField hostPasswordTextField;
    @FXML private TextField userNameTextField;
    @FXML private Label userNameLabel;
    @FXML private Label userEmailLabel;

    @FXML private TextField userEmailTextField;
    @FXML private TextField userPasswordTextField;
    @FXML private TextField userRepeatPasswordTextField;

    @FXML private Button signInButton;
    @FXML private Button createAccountButton;
    @FXML private Button resetPasswordButton;
    @FXML private Button cancelButton;
    @FXML private Button editButton;

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
        if(connectDisconnectToggleButton.isSelected()){
            if(Conexion.testConexion(hostNameTextField.getText(), hostUserTextField.getText(), hostPasswordTextField.getText())){
                Conexion.setUrl(hostNameTextField.getText());
                Conexion.setUsername(hostUserTextField.getText());
                Conexion.setPassword(hostPasswordTextField.getText());
                connectDisconnectToggleButton.setText("Disconnect");
                hostPasswordTextField.setDisable(true);
                hostUserTextField.setDisable(true);
                hostNameTextField.setDisable(true);
                useDefaultServerCheckBox.setDisable(true);
                appController.loadData();
                appController.addLog(this.toString(), "Connection to mysql succesfull!");
                
            } else {
                connectDisconnectToggleButton.setSelected(false);
                appController.addLog(this.toString(), "The connexion could not be stablished!");
            }
        } else {
            Conexion.setUrl(null);
            Conexion.setUsername(null);
            Conexion.setPassword(null);
            connectDisconnectToggleButton.setText("Connect");
            useDefaultServerCheckBox.setDisable(false);
            if(!useDefaultServerCheckBox.isSelected()){
                hostPasswordTextField.setDisable(false);
                hostUserTextField.setDisable(false);
                hostNameTextField.setDisable(false);
            }
            
            appController.clearLists();
            appController.addLog(this.toString(), "Disconnected from mysql!");
        }
        
    }
    
    @FXML
    private void logInOutUser(ActionEvent e){
        // If it is not logged in, try to login
        if(signInButton.getText().equals("Login")){
            for(User user:this.appController.getListUser()){
                if(this.userNameTextField.getText().equals(user.getUsername())){
                    if(this.userEmailTextField.getText().equals(user.getPassword())){
                        setCurrentUser(user);
                        // complete
                        this.userNameTextField.setDisable(true);
                        this.userEmailTextField.setDisable(true);
                        this.lblDateJoined.setText(user.getDate().toString());
                        this.signInButton.setText("Logout");
                        appController.addLog("System", "User logged in!");
                    }
                }
            }
        // If it is logged in, log out
        } else if(signInButton.getText().equals("Logout")){
            setCurrentUser(null);
            this.userNameTextField.setDisable(false);
            this.userEmailTextField.setDisable(false);
            userNameTextField.setText("");
            userEmailTextField.setText("");
            lblDateJoined.setText("");
            signInButton.setText("Login");
        }
    }

    public void autoClicks() {
        
        connectDisconnectToggleButton.fire();
        
    }
    
    @FXML
    public void useDefaultServer(){
        if(useDefaultServerCheckBox.isSelected()){
            hostNameTextField.setText(this.DBHOST);
            hostUserTextField.setText(this.DBUSER);
            hostPasswordTextField.setText(this.DBPASSWORD);
            hostNameTextField.setDisable(true);
            hostUserTextField.setDisable(true);
            hostPasswordTextField.setDisable(true);
        } else if(!useDefaultServerCheckBox.isSelected()){
            hostNameTextField.setText(null);
            hostUserTextField.setText(null);
            hostPasswordTextField.setText(null);
            hostNameTextField.setDisable(false);
            hostUserTextField.setDisable(false);
            hostPasswordTextField.setDisable(false);
        }
        
        
    }

    public ObservableList<User> getListUser() {
        return this.listUser;
    }
    
}
