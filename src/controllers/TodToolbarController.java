package controllers;

import data.Fcc;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import main.Module;

import java.net.URL;
import java.util.ResourceBundle;

public class TodToolbarController implements Initializable {

    private static TodController todController;

    private AppController appController;

    @FXML private ComboBox<Fcc> initialFccComboBox;

    public static void setTodController(TodController todController) {
        TodToolbarController.todController = todController;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        manageEvents();
    }

    void fillData() {
        initialFccComboBox.setItems(appController.getListFcc());
    }

    ComboBox<Fcc> getInitialFccComboBox() {
        return initialFccComboBox;
    }

    public void manageEvents(){
        initialFccComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Fcc>() {
            @Override
            public void changed(ObservableValue<? extends Fcc> observable, Fcc oldValue, Fcc newValue) {
                if(newValue!=null){

                    todController.deployTod(appController.getInitialAnalogy(newValue));
                }
            }
        });
    }
    public void setAppController(AppController appController) {
        this.appController = appController;

    }



}
