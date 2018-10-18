package extras.tod;

import javafx.scene.shape.Circle;

public class Knob extends Circle {

    public FccContainer fccContainer;

    public Knob(FccContainer fccContainer){
        setStyle();
        this.fccContainer = fccContainer;
    }

    private void setStyle(){
        setRadius(3);
        setStyle("-fx-stroke:white;-fx-stroke-type:outside;-fx-stroke-width:2;");
    }
}
