package extras.tod;

import controllers.AppController;
import data.Fcc;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;


public class AnalogyContainer extends Group {

    private static AppController appController;
    Analogy analogy;
    
    //private Fcc fcc;
    
    // List of analogous fcc's (either a class or a conjunction)
    private ArrayList<Fcc> listAnalogousFccs;
    
    // List of MultiContainers of the analogous FCC's
    private ArrayList<MultiContainer> listMultiContainers;
    
    public AnalogyContainer(){
        
    }
    
    public AnalogyContainer(Analogy analogy){
        this.analogy = analogy;
        
        for(Fcc f:analogy){
            
            MultiContainer multiContainer = new MultiContainer(f);
                        
            super.getChildren().add(multiContainer);
            
            multiContainer.setAlignment(Pos.CENTER_LEFT);
            
            multiContainer.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    multiContainer.deployInclusions();
                    System.out.println("i press indeed");
                }
            });
        }
    }

    /**
     * 
     * @param appController controller of the main app
     */
    public static void setAppController(AppController appController) {
        AnalogyContainer.appController = appController;
        
    }
    
    
    
    
}
