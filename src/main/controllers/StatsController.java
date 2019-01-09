package main.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class StatsController implements Initializable {

    private AppController appController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void setAppController(AppController aThis) {
        this.appController=aThis;
    }
    
}
