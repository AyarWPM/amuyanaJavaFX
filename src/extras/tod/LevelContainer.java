package extras.tod;

import controllers.AppController;
import controllers.TodController;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class LevelContainer extends Group {

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
        //this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        //this.setSpacing(20);
        //this.setAlignment(Pos.CENTER);
        setPickOnBounds(false);
    }

    public void deploy() {
        for(Analogy a:this.listAnalogy){
            AnalogyContainer analogyContainer = new AnalogyContainer(a);
            this.getChildren().add(analogyContainer);
            analogyContainer.deploy();
        }
    }

    public void positionMultiContainers() {
        for (AnalogyContainer analogyContainer : getAnalogyContainers()) {
            analogyContainer.positionMultiContainers();
        }
    }

    public void positionAnalogyContainers() {
        for (AnalogyContainer analogyContainer : getAnalogyContainers()) {

            FccContainer frontFccContainer = analogyContainer.getFrontFccContainer();

            Point2D refAnalogyContainer = analogyContainer.localToScene(0,0);
            Point2D refPointLevelContainer = this.localToScene(0,0);

            double diffX = refAnalogyContainer.getX()-refPointLevelContainer.getX();
            double diffY = refAnalogyContainer.getY()-refPointLevelContainer.getY();
            
            int index = getAnalogyContainers().indexOf(analogyContainer);
            double previousAnalogyContainerBoundaryMaxY = 0;
            
            if(index!=0){
                previousAnalogyContainerBoundaryMaxY = getAnalogyContainers().get(index-1).getBoundsInParent().getMaxY();
            }
            
            analogyContainer.setLayoutX(-diffX);
            analogyContainer.setLayoutY(-diffY+previousAnalogyContainerBoundaryMaxY+30);

        }
    }

    public LevelType getLevelType() {
        return levelType;
    }

    public ObservableList<AnalogyContainer> getAnalogyContainers() {
        ObservableList<AnalogyContainer> listAnalogies = FXCollections.observableArrayList();
        for (Node node : this.getChildren()) {
            listAnalogies.add((AnalogyContainer) node);
        }
        return listAnalogies;

    }

    public ObservableList<FccContainer> getFccContainers() {
        ObservableList<FccContainer> listFccContainers = FXCollections.observableArrayList();
        for (AnalogyContainer analogyContainer : getAnalogyContainers()) {
            listFccContainers.addAll(analogyContainer.getFccContainers());
        }
        return listFccContainers;
    }

    /**
     * It assumes this method is called for a levelContainer that has a multiContainerParent. This condition should
     * be tested before calling this method.
     * @return
     */
    public MultiContainer getMultiContainerParent() {
        return (MultiContainer)getParent().getParent();
    }

}