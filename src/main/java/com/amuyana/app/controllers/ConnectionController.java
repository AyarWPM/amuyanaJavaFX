package com.amuyana.app.controllers;

import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.NodeInterface;
import com.amuyana.app.node.Message;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        //Empty
/*        hostTextField.setText("localhost");
        userTextField.setText("amuyana");
        passwordTextField.setText("");*/
    }

    @FXML
    private void setDefaultValues() {
        if (useDefaultCheckBox.isSelected()) {
            hostTextField.setText("160.153.71.97:3306");
            userTextField.setText("anonymous");
            passwordTextField.setText("anonymous");
            /*hostTextField.setText("localhost");
            userTextField.setText("amuyana");*/
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
        NodeHandler.getDataInterface().setDataConnectionValues(hostTextField.getText(),userTextField.getText(),passwordTextField.getText());
        //todo if connection is not succesful setDataConnectionValues(null, null,null)

        boolean testConnection = NodeHandler.getDataInterface().testConnection();
        if (testConnection) {
            // load data
            NodeHandler.getDataInterface().loadData();
            // logic system in top menu bar
            nodeInterface.resetMenus();
            nodeInterface.closeTabsExceptConnection();
            NodeHandler.getDataInterface().disconnect();
        } else {
            // Look at the sky
        }
        Message.testConnection(testConnection);
    }

    public void setNodeInterface(NodeInterface nodeInterface) {
        this.nodeInterface = nodeInterface;
    }

    private void setDataConnectionValues() {
        NodeHandler.getDataInterface().setDataConnectionValues(hostTextField.getText(),userTextField.getText(),passwordTextField.getText());
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
