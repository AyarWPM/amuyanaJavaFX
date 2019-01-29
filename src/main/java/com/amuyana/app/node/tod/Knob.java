package com.amuyana.app.node.tod;

import javafx.scene.shape.Circle;

public class Knob extends Circle {

    public Fruit fccContainer;

    public Knob(Fruit fccContainer){
        setStyle();
        this.fccContainer = fccContainer;
    }

    private void setStyle(){
        setRadius(4);
        //setStyle("-fx-stroke:transparent;-fx-stroke-notationType:outside;-fx-stroke-width:2;");
    }
}
