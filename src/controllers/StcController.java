package controllers;

import extras.DateTimePicker;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


/**
 * FXML Controller class
 *
 * @author ayar
 */
public class StcController implements Initializable {

    @FXML HBox hbx_starttime;
    @FXML HBox hbx_endtime;
    private AppController appController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DateTimePicker startTimePicker = new DateTimePicker();
        DateTimePicker endTimePicker = new DateTimePicker();
        startTimePicker.setPromptText("YYYY-MM-DD hh:mm:ss");
        endTimePicker.setPromptText("YYYY-MM-DD hh:mm:ss");
        hbx_starttime.getChildren().add(startTimePicker);
        hbx_endtime.getChildren().add(endTimePicker);
    }    

    public void setAppController(AppController aThis) {
        this.appController=aThis;
    }
    
}
