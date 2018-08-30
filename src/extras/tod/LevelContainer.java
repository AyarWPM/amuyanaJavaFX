package extras.tod;

import controllers.AppController;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author ayar
 */
public class LevelContainer extends VBox {

    private ArrayList<Analogy> listAnalogy;
    private static AppController appController;

    public LevelContainer(ArrayList<Analogy> listAnalogy) {
        this.listAnalogy = listAnalogy;
        
        setStyle();
        
        deploy();
    }
    
    public static void setAppController(AppController appController) {
        LevelContainer.appController = appController;
    }

    private void setStyle() {
        this.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(5))));
        this.setSpacing(5);
    }

    private void deploy() {
        for(Analogy a:listAnalogy){
            AnalogyContainer analogyContainer = new AnalogyContainer(a);
            this.getChildren().add(analogyContainer);
            analogyContainer.deploy();
        }
    }
    
}
