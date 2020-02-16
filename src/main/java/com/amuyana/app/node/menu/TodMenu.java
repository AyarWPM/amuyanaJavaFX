package com.amuyana.app.node.menu;

import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.NodeInterface;
import com.amuyana.app.node.tod.Fruit;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class TodMenu extends Menu {
    private Tod tod;
    private NodeInterface nodeInterface;

    public TodMenu(Tod tod, NodeInterface nodeInterface) {
        super(tod.getLabel());
        this.tod = tod;
        this.nodeInterface = nodeInterface;
        initializeMenu();
    }

    public Tod getTod() {
        return this.tod;
    }

    private void initializeMenu() {
        MenuItem openMenuItem = new MenuItem("Open");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem duplicateMenuItem = new MenuItem("Duplicate");

        openMenuItem.setOnAction(event -> {
            nodeInterface.open(tod);
        });

        deleteMenuItem.setOnAction(event -> {
            NodeHandler.getDataInterface().connect();
            nodeInterface.delete(tod);
            NodeHandler.getDataInterface().disconnect();
        });

        duplicateMenuItem.setOnAction(event -> {
            nodeInterface.duplicate(tod);
        });
        getItems().addAll(openMenuItem,deleteMenuItem);
    }
}
