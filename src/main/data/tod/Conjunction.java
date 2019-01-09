package main.data.tod;

import main.controllers.AppController;
import main.controllers.TodController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;

public class Conjunction {
    private IntegerProperty idConjunction;

    private static AppController appController;
    private static TodController todController;

    Conjunction(int idConjunction) {
        this.idConjunction = new SimpleIntegerProperty(idConjunction);
    }

    public static void setControllers(AppController appController, TodController todController) {
        Conjunction.appController = appController;
        Conjunction.todController = todController;
    }

    // Methods attribute: idConjunction
    public int getIdConjunction() {
        return idConjunction.get();
    }
    public IntegerProperty idConjunctionProperty() {
        return idConjunction;
    }
    public void setIdConjunction(int idConjunction) {
        this.idConjunction.set(idConjunction);
    }

    public Label getLabelConjunction() {
        Label label = new Label();
        String text;
        text ="";
        label.setText(text);
        label.setAccessibleHelp("accessibleHelp!");
        return label;
    }
}
