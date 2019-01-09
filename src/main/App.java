package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.controllers.AppController;

/**
 * This class contains the javaFX logic
 */
public class App extends Application {
    public static void main(String[] args) {
        System.out.println("hi");
        launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {

        // Display main windows
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/App.fxml"));
        Parent rootNode = loader.load();

        AppController appController = loader.getController();
        appController.setStage(stage);

        Scene scene = new Scene(rootNode, 800, 600);

        //rootNode.setDepthTest(DepthTest.ENABLE);

        //scene.getStylesheets().add("/resources/styles/mainApp.css");

        stage.setTitle("Amuyaña");
        stage.setScene(scene);
        stage.show();
    }

}
