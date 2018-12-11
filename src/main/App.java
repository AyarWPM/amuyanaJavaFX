package main;

import controllers.*;
import data.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.DepthTest;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class contains the javaFX logic
 */
public class App extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Display main windows
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/App.fxml"));
        Parent rootNode = loader.load();

        Scene scene = new Scene(rootNode, 800, 600,true);

        rootNode.setDepthTest(DepthTest.ENABLE);

        scene.getStylesheets().add("/resources/styles/mainApp.css");

        stage.setTitle("Amuyaña");
        stage.setScene(scene);
        stage.show();
        //stage.close();


    }



}
