package extras.tod;

import controllers.AppController;
import data.Fcc;
import javafx.scene.layout.Pane;

import java.util.ArrayList;


public class TodContainer extends Pane {

    private static AppController appController;
    private Fcc initialFcc;

    // All Classes, all conjunctions!
    
    // Then all Fccs that are added
    
    public TodContainer(Fcc initialFcc) {
        this.initialFcc = initialFcc;
        ArrayList<Analogy> listAnalogy = appController.getListAnalogyForInitial(initialFcc);
        
        LevelContainer firstLevel = new LevelContainer(listAnalogy);
        super.getChildren().clear();
        super.getChildren().add(firstLevel);
        
    }
    
    public static void setAppController(AppController appController) {
        TodContainer.appController = appController;
        
    }
    
    
    
}
