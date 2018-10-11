package extras.tod;

import controllers.AppController;
import controllers.TodController;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class LevelContainer extends VBox {

    private static TodController todController;
    private ArrayList<Analogy> listAnalogy;
    private static AppController appController;
    private final LevelType levelType;

    public enum LevelType{MAIN, INCLUSION, DEDUCTION}

    public LevelContainer(ArrayList<Analogy> listAnalogy, LevelType levelType) {
        this.listAnalogy = listAnalogy;
        this.levelType = levelType;
        setStyle();
    }

    public static void setControllers(AppController appController, TodController todController) {
        LevelContainer.appController = appController;
        LevelContainer.todController = todController;
    }

    private void setStyle() {
        this.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER);
    }

    public void deploy() {
        for(Analogy a:this.listAnalogy){
            AnalogyContainer analogyContainer = new AnalogyContainer(a);
            this.getChildren().add(analogyContainer);
            analogyContainer.deploy();
        }
    }

    public LevelType getLevelType() {
        return levelType;
    }
}