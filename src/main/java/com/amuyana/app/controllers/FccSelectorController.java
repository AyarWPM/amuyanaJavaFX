package com.amuyana.app.controllers;

import com.amuyana.app.data.Fcc;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.NodeInterface;
import com.amuyana.app.node.content.TodTab;
import com.amuyana.app.node.menu.FccMenu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class FccSelectorController implements Initializable {
    @FXML private MenuButton fccsMenuButton;
    @FXML private Button newFccButton;
    private TodTab todTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    void initialize(NodeInterface nodeInterface, TodTab todTab) {
        this.todTab = todTab;
        ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();
        for (Fcc fcc : NodeHandler.getDataInterface().getFccs(nodeInterface.getLogicSystem())) {
            MenuItem menuItem = new MenuItem(fcc.getName());
            menuItem.setOnAction(actionEvent -> open(fcc));
            menuItems.add(menuItem);
        }
        Comparator<MenuItem> comparator = Comparator.comparing(MenuItem::getText);
        FXCollections.sort(menuItems,comparator);
        fccsMenuButton.getItems().addAll(menuItems);
    }

    @FXML
    private void newFcc() {
        NodeHandler.getDataInterface().connect();
        todTab.getTodController().showNewTree();
        NodeHandler.getDataInterface().disconnect();
    }

    private void open(Fcc fcc) {
        NodeHandler.getDataInterface().connect();
        todTab.getTodController().showNewTree(fcc);
        NodeHandler.getDataInterface().disconnect();
    }
}
