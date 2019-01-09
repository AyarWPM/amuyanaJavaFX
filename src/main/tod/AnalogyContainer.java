package main.tod;

import main.controllers.AppController;
import main.controllers.TodController;
import main.data.Fcc;
import main.data.tod.containers.Container0;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class AnalogyContainer extends Group {
    private static TodController todController;
    private static AppController appController;

    private double scale = 1;
    public final static double TRANSLATE_X=20;
    public final static double TRANSLATE_Y=28;

    Analogy analogy;

    private static int xMove;
    private static int yMove;

    public AnalogyContainer() {

    }

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

    private void setStyle() {
        //this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        //this.setSpacing(20);
        //this.setAlignment(Pos.CENTER);
        //this.setPickOnBounds(true);
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
                main.data.tod.containers.Container2 container2 = new main.data.tod.containers.Container2(f);
                //displayedMultiContainers.add(container2);
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
            //for(AnalogyContainer multiContainer)
        } else if (analogy.size() >5) {
            // Add the first 5 and then three dots...
        }
    }

    void deployRetained() {

        for(Fcc f:this.analogy){
            main.data.tod.containers.Container2 container2 = new main.data.tod.containers.Container2(f);
            this.getChildren().add(container2);
            container2.deploy();
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

        for (main.data.tod.containers.Container2 container2 : getMultiContainers()) {
            CheckMenuItem checkMenuItem = new CheckMenuItem(container2.getFccContainer().getFcc().toString());
            checkMenuItem.setOnAction(showHideSpecific(container2));
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

    private EventHandler<ActionEvent> showHideSpecific(main.data.tod.containers.Container2 container2) {
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

    public void positionMultiContainers() {
        for (main.data.tod.containers.Container2 container2 : getMultiContainers()) {
            int index = getChildren().indexOf(container2);

            Point2D refPointFccContainer = container2.getFccContainer().localToScene(0,0);
            Point2D refPointMultiContainer = container2.localToScene(0,0);

            double diffX = refPointFccContainer.getX()-refPointMultiContainer.getX();
            double diffY = refPointFccContainer.getY()-refPointMultiContainer.getY();

            container2.setLayoutX(-diffX);
            container2.setLayoutY(-diffY+(index)*30);
            //container2.toFront();
        }
    }

    public FccContainer getFrontFccContainer() {
        return getMultiContainers().get(getMultiContainers().size()-1).getFccContainer();
    }

    public ObservableList<main.data.tod.containers.Container2> getMultiContainers() {
        ObservableList<main.data.tod.containers.Container2> listContainer2s = FXCollections.observableArrayList();
        for (Node node : this.getChildren()) {
            if (node.getClass().equals(main.data.tod.containers.Container2.class)) {
                main.data.tod.containers.Container2 container2 = (main.data.tod.containers.Container2)node;
                listContainer2s.add(container2);
            }
        }
        return listContainer2s;
    }

    /**
     * It gets the FccContainers that are in the central position of the AnalogyContainer only.
     * @return
     */
    public ObservableList<FccContainer> getFccContainers() {
        ObservableList<FccContainer> listFccContainers = FXCollections.observableArrayList();
        for (Node node : this.getChildren()) {
            if (node.getClass().equals(main.data.tod.containers.Container2.class)) {
                main.data.tod.containers.Container2 container2 = (main.data.tod.containers.Container2)node;
                listFccContainers.add(container2.getFccContainer());
            }

        }
        return listFccContainers;
    }


    public Container0 getLevelContainerParent() {
        return (Container0)getParent();
    }




}