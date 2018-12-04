package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import data.Inclusion;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class Conjunction {
    private static AppController appController;
    private static TodController todController;

    ArrayList<Inclusion> listInclusions;
    Fcc particularFcc;
    ArrayList<Fcc> GeneralFccs;

    Conjunction(ArrayList<Inclusion> listInclusions) {
        this.listInclusions=listInclusions;

    }

    public static void setControllers(AppController appController, TodController todController) {
        Conjunction.appController = appController;
        Conjunction.todController = todController;
    }

    public Label getLabelConjunction() {
        Label label = new Label();
        String text;
        text ="";
        label.setText(text);
        label.setAccessibleHelp("accessibleHelp!");
        return label;
    }

    @Override
    public String toString() {
        Inclusion anyInclusion = listInclusions.get(0);
        int particularFccID = anyInclusion.getDynamism().getFcc().getIdFcc();

        ArrayList<Integer> generalFccsIds = new ArrayList<>();

        return "Conjunction";
    }

}
