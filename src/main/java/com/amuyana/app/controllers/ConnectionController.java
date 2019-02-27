package com.amuyana.app.controllers;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.node.MainBorderPane;
import com.amuyana.app.node.NodeInterface;
import com.amuyana.app.node.Message;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionController implements Initializable {

    @FXML private TextField hostTextField;
    @FXML private TextField userTextField;
    @FXML private TextField passwordTextField;
    @FXML private CheckBox useDefaultCheckBox;

    private NodeInterface nodeInterface;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void setDefaultValues() {
        if (useDefaultCheckBox.isSelected()) {
            hostTextField.setText("160.153.71.97:3306");
            userTextField.setText("anonymous");
            passwordTextField.setText("anonymous");
            hostTextField.setDisable(true);
            userTextField.setDisable(true);
            passwordTextField.setDisable(true);
        } else {
            hostTextField.setText("");
            userTextField.setText("");
            passwordTextField.setText("");
            hostTextField.setDisable(false);
            userTextField.setDisable(false);
            passwordTextField.setDisable(false);
        }
        setDataConnectionValues();
    }

    @FXML
    private void connect() {
        MainBorderPane.getDataInterface().setDataConnectionValues(hostTextField.getText(),userTextField.getText(),passwordTextField.getText());
        if (Message.testConnection(MainBorderPane.getDataInterface().testConnection())) {
            // load data
            MainBorderPane.getDataInterface().loadData();
        // enable button in topMenuBar
            nodeInterface.getTopMenuBar().enableLogicSystemButton();
        } else {

        }

    }

    public void setNodeInterface(NodeInterface nodeInterface) {
        this.nodeInterface = nodeInterface;
    }

    private void setDataConnectionValues() {
        MainBorderPane.getDataInterface().setDataConnectionValues(hostTextField.getText(),userTextField.getText(),passwordTextField.getText());
    }

    public TextField getHostTextField() {
        return hostTextField;
    }


    public TextField getUserTextField() {
        return userTextField;
    }


    public TextField getPasswordTextField() {
        return passwordTextField;
    }

}
