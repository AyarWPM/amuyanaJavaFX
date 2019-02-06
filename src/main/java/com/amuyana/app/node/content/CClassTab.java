package com.amuyana.app.node.content;

import com.amuyana.app.controllers.FXMLSource;
import com.amuyana.app.controllers.CClassController;
import com.amuyana.app.controllers.TodController;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.node.NodeInterface;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class CClassTab extends RightPanelTab {

    private CClassController cClassController;

    public CClassTab(TodController todController, NodeInterface nodeInterface, Fcc fcc) {
        loadSource();

        this.cClassController.setInterfaces(todController, nodeInterface);
        //this.cClassController.setValues(this, fcc);

        //this.cClassController.fillData();
        this.cClassController.setEditMode(true);

        bindProperties();
    }

    void loadSource() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLSource.FCCEDITOR.getUrl()));
            Node node = fxmlLoader.load();
            setSource(node);
            this.cClassController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bindProperties() {
        textProperty().bind(this.getcClassController().getcClass().nameProperty());
        //setText(this.getFccController().getFruit().getName());
        setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                event.consume();
                cClassController.close();
            }
        });
    }

    public CClassController getcClassController() {
        return this.cClassController;
    }

}
