package com.amuyana.app.node.content;

import com.amuyana.app.controllers.FXMLSource;
import com.amuyana.app.controllers.LogicSystemController;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.node.NodeInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

public class LogicSystemContentTab extends Tab {
    private LogicSystemController controller;

    public LogicSystemContentTab(NodeInterface nodeInterface) {
        setText("New Logic System");
        loadSource();
        this.controller.setTab(this);
        this.controller.setNodeInterface(nodeInterface);
        this.controller.setLogicSystemNew(true);
    }

    public LogicSystemContentTab(NodeInterface mainBorderPane, DataInterface dataInterface, LogicSystem logicSystem) {
        setText(logicSystem.getLabel());
        loadSource();
        this.controller.setTab(this);
        this.controller.setNodeInterface(mainBorderPane);
        this.controller.fillData(logicSystem);
    }

    void loadSource() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLSource.LOGIC_SYSTEM.getUrl()));
            setContent(fxmlLoader.load());
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LogicSystemController getController() {
        return controller;
    }


}
