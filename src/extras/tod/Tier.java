package extras.tod;

import controllers.AppController;
import controllers.TodController;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class Tier extends Line {
    private static AppController appController;
    private static TodController todController;
    private static ArrayList<Tier> listTiers = new ArrayList<>();

    private Knob knobStart;
    private Knob knobFinal;

    public Tier(){
        Tier.listTiers.add(this);
        setStyle();
    }

    public static void setControllers(AppController appController, TodController todController) {
        Tier.appController = appController;
        Tier.todController = todController;
    }

    private void setStyle(){
        this.setStrokeWidth(2);
    }

    public void setKnobStart(Knob knob){
        this.knobStart = knob;
    }

    public void setKnobFinal(Knob knob){
        this.knobFinal = knob;
    }
/*
    public Knob getKnobStart() {
        return knobStart;
    }

    public Knob getKnobFinal() {
        return knobFinal;
    }
*/

    public static ArrayList<Tier> getListTiers() {
        return listTiers;
    }

/*
    public static boolean isTied(FccContainer fccContainer1,FccContainer fccContainer2){
        for(Tier t:Tier.listTiers){

        }
        return false;
    }
*/

    public static ArrayList<Tier> listTiersOf(FccContainer fccContainer){
        ArrayList<Tier> listTiers = new ArrayList<>();
        for(Tier t:getListTiers()){
            if(t.knobStart.fccContainer.equals(fccContainer)){
                listTiers.add(t);
            }
            if(t.knobFinal.fccContainer.equals(fccContainer)){
                listTiers.add(t);
            }
        }
        return listTiers;
    }


}
