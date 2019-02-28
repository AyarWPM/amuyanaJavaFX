package com.amuyana.app.main;

import com.amuyana.app.data.DataHandler;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.node.MainBorderPane;
import com.amuyana.app.node.NodeInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Amuyana extends Application {

    @Override
    public void start(Stage stage) {
        DataInterface dataInterface = new DataHandler();
        NodeInterface nodeInterface = new MainBorderPane(dataInterface);
        nodeInterface.setStage(stage);

        Scene scene = new Scene((BorderPane) nodeInterface.getRootNode(), 900, 700);

        stage.setTitle("Amuyaña prototype");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }
}
