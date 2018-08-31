package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class AnalogyContainer extends Group {

    private static AppController appController;
    private static TodController todController;

    Analogy analogy;
    
    private static int xMove;
    private static int yMove;

    // List of MultiContainers of the analogous FCC's
    // private ArrayList<MultiContainer> listMultiContainers;

    AnalogyContainer(Analogy analogy){
        this.analogy = analogy;
        
        setStyle();
        
    }

    public static void setControllers(AppController appController, TodController todController) {
        AnalogyContainer.appController = appController;
        AnalogyContainer.todController = todController;
        
    }
    
    void deploy(){
        for(Fcc f:this.analogy){
            MultiContainer multiContainer = new MultiContainer(f);
            this.getChildren().add(multiContainer);
            multiContainer.setTranslateX(this.getChildren().indexOf(multiContainer)*20);
            multiContainer.setTranslateY(-this.getChildren().indexOf(multiContainer)*28);
            multiContainer.toBack();
            multiContainer.deploy();
        }
    }

    private void setStyle() {
        //this.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(4))));
        //this.setAlignment(Pos.CENTER);
    }
}
