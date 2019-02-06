package com.amuyana.app.node.content;

import com.amuyana.app.controllers.FXMLSource;
import com.amuyana.app.controllers.ConnectionController;
import com.amuyana.app.node.MainBorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

public class ConnectionContentTab extends Tab {
    ConnectionController connectionController;

    public ConnectionContentTab(MainBorderPane mainBorderPane) {

        setText("Connexion");
        loadSource(FXMLSource.CONNEXION);
        //GuiInterface guiInterface = gui;
        this.connectionController.setNodeInterface(mainBorderPane);
    }

    private void loadSource(FXMLSource fxmlSource) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlSource.getUrl()));
            setContent(fxmlLoader.load());
            this.connectionController = (ConnectionController)fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
