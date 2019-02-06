package com.amuyana.app.node.content;

import com.amuyana.app.controllers.FXMLSource;
import com.amuyana.app.controllers.TodController;
import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.NodeInterface;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

public class TodContentTab extends Tab {
    private TodController controller;

    public TodContentTab(NodeInterface nodeInterface) {
        loadSource();
        this.controller.setTab(this);
        this.controller.setNodeInterface(nodeInterface);
        this.controller.newTod(nodeInterface.getLogicSystem());
        this.controller.fillData();
        bindProperties();
    }

    public TodContentTab(NodeInterface nodeInterface, Tod tod) {
        loadSource();
        this.controller.setTab(this);
        this.controller.setNodeInterface(nodeInterface);
        this.controller.openTod(tod);
        this.controller.fillData();
        bindProperties();
    }

    void loadSource() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLSource.TOD.getUrl()));
            setContent(fxmlLoader.load());
            this.controller = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bindProperties() {
        StringProperty todString = new SimpleStringProperty(" (ToD)");
        textProperty().bind(
                this.controller.getTod().labelProperty().concat(StringExpression.stringExpression(todString))
        );
    }

    public TodController getTodController() {
        return controller;
    }

    public TodController getController() {
        return this.controller;
    }
}
