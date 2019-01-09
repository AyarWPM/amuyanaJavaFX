package main.tod;

import javafx.scene.shape.Circle;

public class Knob extends Circle {

    public FccContainer fccContainer;

    public Knob(FccContainer fccContainer){
        setStyle();
        this.fccContainer = fccContainer;
    }

    private void setStyle(){
        setRadius(4);
        //setStyle("-fx-stroke:transparent;-fx-stroke-type:outside;-fx-stroke-width:2;");
    }
}
