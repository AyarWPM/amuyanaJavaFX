package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.shape.Line;

public class TodContainer extends Group {

    private static AppController appController;
    private static TodController todController;
    private Fcc initialFcc;

    final private LevelContainer mainLevelContainer;

    // All Classes, all conjunctions!
    
    // Then all Fccs that are added
    
    public TodContainer(Fcc initialFcc) {
        this.initialFcc = initialFcc;
        setStyle();
        ArrayList<Analogy> listAnalogy = appController.getListAnalogyForInitial(initialFcc);
        this.mainLevelContainer = new LevelContainer(listAnalogy, LevelContainer.LevelType.MAIN);
    }

    public static void setControllers(AppController appController, TodController todController) {
        TodContainer.appController = appController;
        TodContainer.todController = todController;
    }

    private void setStyle() {
        //this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(6))));
    }

    public LevelContainer getMainLevelContainer() {
        return mainLevelContainer;
    }

    public void deploy() {
        this.getChildren().clear();
        addRules();
        this.getChildren().add(mainLevelContainer);

        mainLevelContainer.deploy();
    }

    private void addRules() {
        Line xAxis = new Line(0,0,400,0);
        Line yAxis = new Line(0,0,0,400);

        xAxis.setStrokeWidth(2);
        yAxis.setStrokeWidth(2);

        this.getChildren().addAll(xAxis,yAxis);

        for(int i=0;i<=400;i+=10){
            Line markX = new Line();
            Line markY = new Line();

            markX.setStartX(i);
            markX.setEndX(i);
            markX.setStartY(-2);
            markX.setEndY(3);

            markY.setStartX(-2);
            markY.setEndX(3);
            markY.setStartY(i);
            markY.setEndY(i);

            markX.setStrokeWidth(1);

            if(i==50||i==100||i==150||i==200||i==250||i==300||i==350||i==400){
                markX.setStrokeWidth(2);
                markX.setEndY(10);
                markY.setStrokeWidth(2);
                markY.setEndX(10);
            }

            this.getChildren().addAll(markX,markY);
        }
    }
}
