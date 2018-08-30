
package extras.tod;

import controllers.AppController;
import data.Fcc;
import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class AnalogyContainer extends StackPane {

    private static AppController appController;

    Analogy analogy;
    
    private static int xMove;
    private static int yMove;

    // List of MultiContainers of the analogous FCC's
    //private ArrayList<MultiContainer> listMultiContainers;

    AnalogyContainer(Analogy analogy){
        this.analogy = analogy;
        
        setStyle();
        
    }

    /**
     * 
     * @param appController 
     */
    public static void setAppController(AppController appController) {
        AnalogyContainer.appController = appController;
    }
    
    void deploy(){
        for(Fcc f:this.analogy){
            
            MultiContainer multiContainer = new MultiContainer(f);

            this.getChildren().add(multiContainer);

            multiContainer.setTranslateX(-this.getChildren().indexOf(multiContainer)*20);
            multiContainer.setTranslateY(this.getChildren().indexOf(multiContainer)*20);
            //multiContainer.setTranslateZ(this.getChildren().indexOf(multiContainer));
        }
    }

    private void setStyle() {
        this.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(4))));
        //this.setAlignment(Pos.CENTER);
    }
}
