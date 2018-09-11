package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class TodContainer extends Group {

    private static AppController appController;
    private static TodController todController;
    private Fcc initialFcc;

    final private LevelContainer firstLevel;

    // All Classes, all conjunctions!
    
    // Then all Fccs that are added
    
    public TodContainer(Fcc initialFcc) {
        this.initialFcc = initialFcc;
        
        setStyle();

        ArrayList<Analogy> listAnalogy = appController.getListAnalogyForInitial(initialFcc);
        firstLevel = new LevelContainer(listAnalogy);
        
    }

    public static void setControllers(AppController appController, TodController todController) {
        TodContainer.appController = appController;
        TodContainer.todController = todController;
        
    }

    private void setStyle() {
        //this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(6))));
        
    }

    public void deploy() {
        this.getChildren().clear();
        this.getChildren().add(firstLevel);
        firstLevel.deploy();
    }
    
    
    
}
