package com.amuyana.app.node.content;

import com.amuyana.app.controllers.FXMLSource;
import com.amuyana.app.controllers.FccEditorController;
import com.amuyana.app.controllers.TodController;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.node.NodeInterface;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class FccEditorTab extends RightPanelTab {

    private FccEditorController fccEditorController;

    public FccEditorTab(TodController todController, NodeInterface nodeInterface, Fcc fcc) {
        loadSource();
        this.setClosable(false);
        this.fccEditorController.setInterfaces(todController);
        this.fccEditorController.setValues(this, fcc);

        this.fccEditorController.fillData();
        this.fccEditorController.setEditMode(true);

        bindProperties();
    }

    private void loadSource() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLSource.FCCEDITOR.getUrl()));
            Node node = fxmlLoader.load();
            setSource(node);
            this.fccEditorController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void bindProperties() {
        textProperty().bind(this.getFccEditorController().getFcc().nameProperty());
        //setText(this.getFccController().getFruit().getName());
        /*setOnCloseRequest(event -> {

            fccEditorController.close();
        });*/
    }

    public FccEditorController getFccEditorController() {
        return this.fccEditorController;
    }

}
