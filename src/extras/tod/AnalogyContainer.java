package extras.tod;

import com.sun.org.apache.xpath.internal.operations.Mult;
import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import javafx.geometry.Pos;


public class AnalogyContainer extends Group {
    private final static double TRANSLATE_X=20;
    private final static double TRANSLATE_Y=28;
    private final static double TRANSPARENCY=0.2;
    
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
            int index = this.getChildren().indexOf(multiContainer);

            multiContainer.setLayoutX(index*TRANSLATE_X);
            multiContainer.setLayoutY(-index*TRANSLATE_Y);
            //multiContainer.setOpacity(1-index*TRANSPARENCY);
            multiContainer.toBack();
            
            multiContainer.deploy();
        }
    }

    /**
     * This method will change the order
     * @param multiContainer
     */
    public void turnToFront(MultiContainer multiContainer){
        int POS = getChildren().indexOf(multiContainer);
        ArrayList<MultiContainer> listMultiContainer = new ArrayList<>();

        for(int i = POS;i>=0;i--){
            listMultiContainer.add((MultiContainer)getChildren().get(i));
            getChildren().remove(i);
        }

        // Add the multiContainers in list in reverse order
        Collections.reverse(listMultiContainer);
        getChildren().addAll(listMultiContainer);

        // Position the multiContainers
        int index = getChildren().size()-1;
        for(Node n:getChildren()){

            MultiContainer multi = (MultiContainer)n;
            multi.setLayoutX(index*TRANSLATE_X);
            multi.setLayoutY(-index*TRANSLATE_Y);
            //multi.setOpacity(1-index*TRANSPARENCY);
            index--;
        }
    }

    private void setStyle() {
        //this.setStyle("-fx-border-width:1px;-fx-border-color:black;-fx-border-style:solid;");
        //this.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(4))));
        //this.setAlignment(Pos.CENTER);
    }
}
