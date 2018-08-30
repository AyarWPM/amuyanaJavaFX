
package extras.tod;

import controllers.AppController;
import data.Fcc;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


public class AnalogyContainer extends HBox {

    private static AppController appController;
    Analogy analogy;
    
    private static int xMove;
    private static int yMove;
    
    //private Fcc fcc;
    
    // List of analogous fcc's (either a class or a conjunction)
    private ArrayList<Fcc> listAnalogousFccs;
    
    // List of MultiContainers of the analogous FCC's
    //private ArrayList<MultiContainer> listMultiContainers;
    
    public AnalogyContainer(){
        
    }
    
    public AnalogyContainer(Analogy analogy){
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
    
    public void deploy(){
        
        for(Fcc f:analogy){
            
            MultiContainer multiContainer = new MultiContainer(f);
                        
            this.getChildren().add(multiContainer);
            
            multiContainer.setLayoutX(100);
            
            System.out.println("i try");
            multiContainer.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    //multiContainer.deployInclusions();
                    
                }
            });
        }
    }

    private void setStyle() {
        this.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(4))));
        this.setAlignment(Pos.CENTER);
    }
    
    
    
    
}
