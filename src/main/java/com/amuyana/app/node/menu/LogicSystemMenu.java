package com.amuyana.app.node.menu;

import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.node.NodeInterface;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class LogicSystemMenu extends Menu {
    private LogicSystem logicSystem;
    private NodeInterface nodeInterface;

    public LogicSystemMenu(LogicSystem logicSystem, NodeInterface nodeInterface) {
        super(logicSystem.getLabel());
        this.logicSystem = logicSystem;
        this.nodeInterface = nodeInterface;
        initializeMenu();
    }

    public LogicSystem getLogicSystem() {
        return this.logicSystem;
    }

    private void initializeMenu() {
        MenuItem loadMenuItem = new MenuItem("Load");
        MenuItem editMenuItem = new MenuItem("Edit");

        loadMenuItem.setOnAction(event -> {
            nodeInterface.load(logicSystem);
        });

        editMenuItem.setOnAction(event -> {
            nodeInterface.edit(logicSystem);
        });

        getItems().addAll(loadMenuItem,editMenuItem);
    }
}
