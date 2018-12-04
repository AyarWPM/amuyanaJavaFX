package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TodRightPanelController implements Initializable {

    private static TodController todController;
    private AppController appController;

    public static void setTodController(TodController todController) {
        TodRightPanelController.todController = todController;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    void fillData() {

    }

    public void setAppController(AppController appController) {
        this.appController=appController;
    }

}
