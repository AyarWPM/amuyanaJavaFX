package com.amuyana.app.node.content;

import com.amuyana.app.controllers.FXMLSource;
import com.amuyana.app.controllers.ConnectionController;
import com.amuyana.app.node.NodeHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

import java.io.IOException;

public class ConnectionTab extends Tab {
    ConnectionController connectionController;

    public ConnectionTab(NodeHandler nodeHandler) {
        setText("Connexion");
        loadSource(FXMLSource.CONNEXION);
        this.connectionController.setNodeInterface(nodeHandler);
    }

    private void loadSource(FXMLSource fxmlSource) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlSource.getUrl()));
            setContent(fxmlLoader.load());
            this.connectionController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
