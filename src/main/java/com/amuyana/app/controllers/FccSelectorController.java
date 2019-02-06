package com.amuyana.app.controllers;

import com.amuyana.app.data.Fcc;
import com.amuyana.app.node.MainBorderPane;
import com.amuyana.app.node.NodeInterface;
import com.amuyana.app.node.content.TodContentTab;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class FccSelectorController implements Initializable {
    @FXML private MenuButton fccsMenuButton;
    @FXML private Button newFccButton;
    private TodContentTab todContentTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    void initialize(NodeInterface nodeInterface, TodContentTab todContentTab) {
        this.todContentTab = todContentTab;

        for (Fcc fcc : MainBorderPane.getDataInterface().getFccs(nodeInterface.getLogicSystem())) {
            MenuItem menuItem = new MenuItem(fcc.getName());
            menuItem.setOnAction(actionEvent -> open(fcc));
            fccsMenuButton.getItems().add(menuItem);
        }
    }

    @FXML
    private void newFcc() {
        todContentTab.getTodController().showNewTree();
    }

    private void open(Fcc fcc) {
        todContentTab.getTodController().showNewTree(fcc);
    }
}
