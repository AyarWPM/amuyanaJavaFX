package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TodRightPanelController implements Initializable {

    private static TodController todController;
    private AppController appController;

    @FXML Label idLSLabel;
    @FXML Label dateLSLabel;
    @FXML TextField labelLSTextField;
    @FXML TextArea descriptionLSTextArea;
    @FXML Button saveLSButton;
    @FXML Button cancelLSButton;
    @FXML Button deleteLSButton;


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
