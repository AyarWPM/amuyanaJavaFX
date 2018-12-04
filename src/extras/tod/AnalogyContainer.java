package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;


public class AnalogyContainer extends Group {
    public final static double TRANSLATE_X=20;
    public final static double TRANSLATE_Y=28;
    //private final static double TRANSPARENCY=0.2;
    
    private static AppController appController;
    private static TodController todController;

    Analogy analogy;

    ObservableList<MultiContainer> permanentListMultiContainer;

    private static int xMove;
    private static int yMove;

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
        // Adding title of the analogyContainer
        Label title = new Label(analogy.toString());

        this.getChildren().add(title);

        title.setContextMenu(getContextMenu());
        //title.setOnMouseClicked();

        if (analogy.size() >0 & analogy.size()<6) {
            // Add them all
            for (Fcc f : this.analogy) {
                MultiContainer multiContainer = new MultiContainer(f);
                //displayedMultiContainers.add(multiContainer);
            }
        } else if (analogy.size() >5) {
            // Add the three dots then the first 5 fcc-multiContainers
        }




        makeIgnoreListForDefault();

        deployRetained();
    }

    void makeIgnoreListForDefault() {

        if (analogy.size() >0 & analogy.size()<6) {
            // Add them all
            //for(MultiContainer multiContainer)
        } else if (analogy.size() >5) {
            // Add the first 5 and then three dots...
        }

    }

    void deployRetained() {

        for(Fcc f:this.analogy){
            MultiContainer multiContainer = new MultiContainer(f);
            this.getChildren().add(multiContainer);
            multiContainer.deploy();
        }
    }

    private ContextMenu getContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        // SHOW MENU
        Menu showMenu = new Menu("Show");
        MenuItem defaultFccs = new MenuItem("Default FCCs");
        //recommended.selectedProperty().bind

        defaultFccs.setOnAction(showDefaultFccs());

        MenuItem all = new MenuItem("All FCCs");
        all.setOnAction(showAllFccs());

        Menu specific = new Menu("Specific FCCs");

        for (MultiContainer multiContainer : getMultiContainers()) {
            CheckMenuItem checkMenuItem = new CheckMenuItem(multiContainer.getFccContainer().getFcc().toString());
            checkMenuItem.setOnAction(showHideSpecific(multiContainer));
            specific.getItems().add(checkMenuItem);
        }

        showMenu.getItems().addAll(defaultFccs,all,specific);

        // MOVE MENU
        Menu moveMenu = new Menu("Move");

        MenuItem up = new MenuItem("Up");
        up.setOnAction(moveToUp());
        MenuItem down = new MenuItem("Down");
        down.setOnAction(moveToDown());
        MenuItem lower = new MenuItem("Lower Level");
        lower.setOnAction(moveToLower());
        MenuItem higher = new MenuItem("Higher Level");
        higher.setOnAction(moveToHigher());

        moveMenu.getItems().addAll(up,down,lower,higher);

        contextMenu.getItems().addAll(showMenu,moveMenu);

        return contextMenu;
    }

    /**
     * In this method we create the list of FCCs to ignore/consider
     * @return The EventHandler for the
     */
    private EventHandler<ActionEvent> showDefaultFccs() {
        EventHandler<ActionEvent> event = event1 -> {
            makeIgnoreListForDefault();
            deployRetained();
        };

        return event;
    }

    private EventHandler<ActionEvent> showAllFccs() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO
            }
        };
    }

    private EventHandler<ActionEvent> showHideSpecific(MultiContainer multiContainer) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO
            }
        };
    }


    private EventHandler<ActionEvent> moveToUp() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO
            }
        };
    }

    private EventHandler<ActionEvent> moveToDown() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO
            }
        };
    }


    private EventHandler<ActionEvent> moveToLower() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO
            }
        };
    }

    private EventHandler<ActionEvent> moveToHigher() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO
            }
        };
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
            multiContainer.setLayoutY(-diffY+(index)*30);
            //multiContainer.toFront();
        }
    }

    public FccContainer getFrontFccContainer() {
        return getMultiContainers().get(getMultiContainers().size()-1).getFccContainer();
    }

    public ObservableList<MultiContainer> getMultiContainers() {
        ObservableList<MultiContainer> listMultiContainers = FXCollections.observableArrayList();
        for (Node node : this.getChildren()) {
            if (node.getClass().equals(MultiContainer.class)) {
                MultiContainer multiContainer = (MultiContainer)node;
                listMultiContainers.add(multiContainer);
            }
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
            if (node.getClass().equals(MultiContainer.class)) {
                MultiContainer multiContainer = (MultiContainer)node;
                listFccContainers.add(multiContainer.getFccContainer());
            }

        }
        return listFccContainers;
    }


    public LevelContainer getLevelContainerParent() {
        return (LevelContainer)getParent();
    }


}
