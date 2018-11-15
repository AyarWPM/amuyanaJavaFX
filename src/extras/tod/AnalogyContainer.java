package extras.tod;

import com.sun.org.apache.xpath.internal.operations.Mult;
import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.shape.Circle;


public class AnalogyContainer extends Group {
    public final static double TRANSLATE_X=20;
    public final static double TRANSLATE_Y=28;
    //private final static double TRANSPARENCY=0.2;
    
    private static AppController appController;
    private static TodController todController;

    Analogy analogy;
    
    private static int xMove;
    private static int yMove;

    // List of MultiContainers of the analogous FCC's
    // private ArrayList<MultiContainer> listMultiContainers;

    AnalogyContainer(Analogy analogy){
        this.analogy = analogy;
        setStyle();
        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });
    }

    public static void setControllers(AppController appController, TodController todController) {
        AnalogyContainer.appController = appController;
        AnalogyContainer.todController = todController;
    }

    void deploy(){

        for(Fcc f:this.analogy){
            MultiContainer multiContainer = new MultiContainer(f);
            this.getChildren().add(multiContainer);
            multiContainer.deploy();
        }
    }


    private void setStyle() {
        //this.setStyle("-fx-border-width:1px;-fx-border-color:black;-fx-border-style:solid;");
        //this.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(4))));
        //this.setAlignment(Pos.CENTER);
        //this.setPickOnBounds(true);
    }

    public void positionMultiContainers() {
        for (MultiContainer multiContainer : getMultiContainers()) {
            int index = getChildren().indexOf(multiContainer);
            
            Point2D refPointFccContainer = multiContainer.getFccContainer().localToScene(0,0);
            Point2D refPointMultiContainer = multiContainer.localToScene(0,0);

            double diffX = refPointFccContainer.getX()-refPointMultiContainer.getX();
            double diffY = refPointFccContainer.getY()-refPointMultiContainer.getY();

            multiContainer.setLayoutX(-diffX);
            multiContainer.setLayoutY(-diffY+index*30);
            //multiContainer.toFront();
        }
    }

    public FccContainer getFrontFccContainer() {
        return getMultiContainers().get(getMultiContainers().size()-1).getFccContainer();
    }

    public ObservableList<MultiContainer> getMultiContainers() {
        ObservableList<MultiContainer> listMultiContainers = FXCollections.observableArrayList();
        for (Node node : this.getChildren()) {
            MultiContainer multiContainer = (MultiContainer)node;
            listMultiContainers.add(multiContainer);
        }
        return listMultiContainers;
    }

    /**
     * It gets the FccContainers that are in the central position of the MultiContainer only.
     * @return
     */
    public ObservableList<FccContainer> getFccContainers() {
        ObservableList<FccContainer> listFccContainers = FXCollections.observableArrayList();
        for (Node node : this.getChildren()) {
            MultiContainer multiContainer = (MultiContainer)node;
            listFccContainers.add(multiContainer.getFccContainer());
        }
        return listFccContainers;
    }


    public LevelContainer getLevelContainerParent() {
        return (LevelContainer)getParent();
    }
}
