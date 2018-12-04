package controllers;

import data.LogicSystem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TodLeftPanelController implements Initializable {

    private static TodController todController;
    private AppController appController;
    @FXML ListView<LogicSystem> logicSystemsListView;

    public static void setTodController(TodController todController) {
        TodLeftPanelController.todController = todController;
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
