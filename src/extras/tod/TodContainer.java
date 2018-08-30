package extras.tod;

import controllers.AppController;
import data.Fcc;
import java.util.ArrayList;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class TodContainer extends Pane {

    private static AppController appController;
    private Fcc initialFcc;
    LevelContainer firstLevel;

    // All Classes, all conjunctions!
    
    // Then all Fccs that are added
    
    public TodContainer(Fcc initialFcc) {
        this.initialFcc = initialFcc;
        
        setStyle();
        
        
        ArrayList<Analogy> listAnalogy = appController.getListAnalogyForInitial(initialFcc);
        firstLevel = new LevelContainer(listAnalogy);
        deploy();
        
    }
    
    
    public static void setAppController(AppController appController) {
        TodContainer.appController = appController;
        
    }

    private void setStyle() {
        this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(6))));
    }

    public void deploy() {
        this.getChildren().clear();
        this.getChildren().add(firstLevel);
    }
    
    
    
}
