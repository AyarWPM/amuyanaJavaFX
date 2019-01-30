package com.amuyana.app;

import com.amuyana.app.data.DataHandler;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.node.MainBorderPane;
import com.amuyana.app.node.NodeInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String args[]) {
        launch(args);
        System.exit(0);
    }

    @Override
    public void start(Stage stage) throws Exception {
        DataInterface dataInterface = new DataHandler();
        dataInterface.setDataConnectionValues("localhost", "amuyana", "");
        dataInterface.loadData();

        NodeInterface nodeInterface = new MainBorderPane(dataInterface);
        nodeInterface.setStage(stage);

        Scene scene = new Scene((BorderPane) nodeInterface.getRootNode(), 900, 700);
        scene.getStylesheets().addAll("/css/mainApp.css");
        //"/css/expressionNOT.css" taken out

        stage.setTitle("Amuyaña");
        stage.setScene(scene);
        stage.show();
    }
}