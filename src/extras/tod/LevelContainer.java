package extras.tod;

import controllers.AppController;
import java.util.ArrayList;
import javafx.scene.layout.VBox;

public class LevelContainer extends VBox {

    private ArrayList<Analogy> listAnalogy;
    private static AppController appController;

    protected LevelContainer(ArrayList<Analogy> listAnalogy) {
        this.listAnalogy = listAnalogy;
        
        for(Analogy a:listAnalogy){
            AnalogyContainer analogyContainer = new AnalogyContainer(a);
            super.getChildren().add(analogyContainer);
        }
        
        super.setSpacing(5);
        
    }
    
    public static void setAppController(AppController appController) {
        LevelContainer.appController = appController;
    }
    
}
