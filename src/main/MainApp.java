package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.DepthTest;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/App.fxml"));
        Parent rootNode = loader.load();

        Scene scene = new Scene(rootNode, 800, 600, true);
        //rootNode.setStyle("-fx-background: transparent;");
        
        rootNode.setDepthTest(DepthTest.ENABLE);
        
        scene.getStylesheets().add("/resources/styles/mainApp.css");

        stage.setTitle("Amuyaña");
        stage.setScene(scene);
        stage.show();
        //stage.close();
    }
}
