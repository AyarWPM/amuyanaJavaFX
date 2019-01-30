package com.amuyana.app.node.tod;

import com.amuyana.app.FXMLSource;
import com.amuyana.app.controllers.FruitController;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.node.MainBorderPane;
import com.sun.tools.javac.Main;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.amuyana.app.data.Fcc;

import java.io.IOException;

public class Fruit extends VBox {
    private final DataInterface dataInterface = MainBorderPane.getDataInterface();
    private final Fcc fcc;
    private SubBranch subBranch;
    private FruitController fruitController;

    // for loading an existing Tree or
    // for loading a new fcc created
    public Fruit(SubBranch subBranch) {
        this.subBranch = subBranch;
        this.fcc = subBranch.getContainer2().getFcc();

        HBox hBox = new HBox(new Group(loadFruit()));
        hBox.setAlignment(Pos.CENTER);
        hBox.setFillHeight(false);

        this.setAlignment(Pos.CENTER);
        this.getChildren().add(hBox);
        this.setFillWidth(false);
        getTree().addObservableFruit(this);
    }

    // For loading a new Tree
    public Fruit(SubBranch subBranch, Fcc fcc){
        this.subBranch = subBranch;
        this.fcc = fcc;

        HBox hBox = new HBox(new Group(loadFruit()));
        hBox.setAlignment(Pos.CENTER);

        this.setAlignment(Pos.CENTER);
        this.getChildren().add(hBox);
        getTree().addObservableFruit(this);
    }

    private boolean descendsFrom(Fruit fruit) {
        boolean descendsFrom=false;
        if (dataInterface.descendsFrom(this.getFcc(), dataInterface.getDynamism(fruit.getFcc(), 0)) ||
                dataInterface.descendsFrom(this.getFcc(), dataInterface.getDynamism(fruit.getFcc(), 1)) ||
                dataInterface.descendsFrom(this.getFcc(), dataInterface.getDynamism(fruit.getFcc(), 2))) {
            descendsFrom = true;
        }
        return descendsFrom;
    }

    private Fruit getThis() {
        return this;
    }

    public Fcc getFcc(){
        return this.fcc;
    }

    public Tree getTree() {
        return getTrunk().getTree();
    }

    public Trunk getTrunk() {
        return getBranch().getTrunk();
    }

    /**
     *
     * @return The subBranch (container2) this fruit is in.
     */
    public SubBranch getSubBranch() {
        return this.subBranch;
    }

    /**
     * Get the Branch this Fruit is in, ie where the this.subBranch is
     * @return
     */
    Branch getBranch() {
        return subBranch.getBranch();
    }

    private Node loadFruit() {
        Node fruit=null;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLSource.FRUIT.getUrl()));
        try {
            fruit = fxmlLoader.load();
            this.fruitController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.fruitController.initialize(getThis());
        return fruit;
    }

    public FruitController getFruitController() {
        return fruitController;
    }

    public void updateKnobsPositionProperties() {
        fruitController.updateKnobsPositionProperties();
    }
}