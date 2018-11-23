package extras.tod;

import controllers.AppController;
import controllers.TodController;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class LevelContainer extends Group {

    private static TodController todController;
    private ArrayList<Analogy> listAnalogy;
    private static AppController appController;
    private double scale = 1;

    @Deprecated
    public LevelContainer(ArrayList<Analogy> listAnalogy) {
        this.listAnalogy = listAnalogy;
        setStyle();
    }

    public LevelContainer(Analogy analogy) {
        this.listAnalogy = new ArrayList<Analogy>();
        this.listAnalogy.add(analogy);
        setStyle();
    }
    
    public void setScale(double scale) {
        this.scale = scale;
        //this.setScaleX(scale);
        //this.setScaleY(scale);
    }

    public double getScale() {
        return this.scale;
    }


    public static void setControllers(AppController appController, TodController todController) {
        LevelContainer.appController = appController;
        LevelContainer.todController = todController;
    }

    private void setStyle() {
        //this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        //this.setSpacing(20);
        //this.setAlignment(Pos.CENTER);
        //this.setPickOnBounds(true);
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

            Point2D refAnalogyContainer = analogyContainer.localToScene(0,0);
            Point2D refPointLevelContainer = this.localToScene(0,0);

            double diffX = refAnalogyContainer.getX()-refPointLevelContainer.getX();
            double diffY = refAnalogyContainer.getY()-refPointLevelContainer.getY();
            
            
            double cumulativeHeight=0;
            for(AnalogyContainer previousAnalogyContainer:getAnalogyContainers()){
                if(previousAnalogyContainer.equals(analogyContainer)){
                    break;
                } else if (!previousAnalogyContainer.equals(analogyContainer)){
                    cumulativeHeight+=previousAnalogyContainer.prefHeight(-1);
                    
                }
            }
            
            
            
            analogyContainer.setLayoutX(-diffX);
            analogyContainer.setLayoutY(cumulativeHeight);
            
            /* ONE APPROACH
            int index = getAnalogyContainers().indexOf(analogyContainer);
            double previousAnalogyContainerBoundaryMaxY = 0;
            
            if(index!=0){
                previousAnalogyContainerBoundaryMaxY = getAnalogyContainers().get(index-1).getBoundsInParent().getMaxY();
            }
            */
            
            /* SECOND APPROACH
            double heightForPrevious=0;
            
            for(AnalogyContainer previousAnalogyContainer:getAnalogyContainers()){
                if(previousAnalogyContainer.equals(analogyContainer)){
                    break;
                } else if (!previousAnalogyContainer.equals(analogyContainer)){
                    heightForPrevious+=previousAnalogyContainer.prefHeight(-1);
                }
            }
            */
        }
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
        System.out.println(getParent());
        System.out.println(getParent().getParent());

        return (MultiContainer)getParent().getParent();
    }

}