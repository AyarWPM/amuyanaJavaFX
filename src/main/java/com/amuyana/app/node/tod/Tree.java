package com.amuyana.app.node.tod;

import com.amuyana.app.controllers.TodController;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.CClass;
import com.amuyana.app.data.tod.Conjunction;
import com.amuyana.app.node.MainBorderPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.Slider;
import javafx.scene.shape.Line;

import java.util.List;

/**
 * Equivalent to tod in data package: this.todController.getTod();
 */
public class Tree extends Group {
    public DoubleProperty viewScale;
    // General
    private TodController todController;

    // Layout
    private Trunk mainTrunk; //Container0
    private DoubleProperty maxLevel;

    private List<Fruit> fruits;
    private ObservableList<Tie> ties;

    // constructor to loadExistingTree an existing Tree
    public Tree(TodController todController) {
        this.todController = todController;
        initializeAndBind();
        setId("Tree");
        manageListeners();
    }

    // new Tree from one Fcc which is created beforehand because it is added in FccEditor
    // normally container0 is instantiated in Trunk, but because it is given already in Tod we call it,
    // Also instead of creating a new one the user could have selected it from the list

    public Tree(TodController todController, Fcc fcc) {
        this.todController = todController;
        initializeAndBind();
        setId("Tree");
        manageListeners();
    }
    public Tree(TodController todController, Conjunction conjunction) {
        this.todController = todController;
        initializeAndBind();
        manageListeners();
    }
    public Tree(TodController todController, CClass cClass) {
        this.todController = todController;
        initializeAndBind();
        manageListeners();
    }

    private void manageListeners() {
        scaleXProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                for (Fruit fruit : getFruits()) {
                    fruit.updateKnobsPositionProperties();
                }
            }
        });
    }

    private void initializeAndBind() {
        fruits = FXCollections.observableArrayList();
        ties = FXCollections.observableArrayList();
        viewScale = new SimpleDoubleProperty();
        maxLevel = new SimpleDoubleProperty();

        Slider slider = todController.getScaleSlider();
        viewScale.bind(slider.valueProperty());

        scaleXProperty().bind(viewScale);
        scaleYProperty().bind(viewScale);
    }

    /**
     * Called after Tree is initialized with constructor with TodController parameter
     */
    public void loadExistingTree() {
        this.mainTrunk = new Trunk(this, todController.getTod().getContainer0());
        getChildren().add(this.mainTrunk);
        this.mainTrunk.loadBranches();
    }

    public void loadNewTree() {
        Fcc newFcc = MainBorderPane.getDataInterface().newFcc(todController.getTod().getLogicSystem());
        this.mainTrunk = new Trunk(this, todController.getTod().getContainer0(), newFcc);
        getChildren().add(this.mainTrunk);
        todController.openFccEditor(newFcc);
    }

    public void loadNewTreeFromExistingFcc(Fcc fcc) {
        this.mainTrunk = new Trunk(this, todController.getTod().getContainer0(), fcc);
        getChildren().add(this.mainTrunk);
        todController.openFccEditor(fcc);
    }

    public void updateMaxLevel(double level) {
        if (level > maxLevel.getValue()) {
            setMaxLevel(level);
        }
    }

    private List<Fruit> getFruits() {
        ObservableList<Fruit> fruits = FXCollections.observableArrayList();
        for (Branch branch : mainTrunk.getBranches()) {
            fruits.addAll(branch.getFruits());
        }
        return fruits;
    }

    public TodController getTodController() {
        return this.todController;
    }

    public double getViewScale() {
        return viewScale.get();
    }

    public DoubleProperty viewScaleProperty() {
        return viewScale;
    }

    public void setViewScale(double viewScale) {
        this.viewScale.set(viewScale);
    }

    public Trunk getMainTrunk() {
        return this.mainTrunk;
    }

    public double getMaxLevel() {
        return maxLevel.get();
    }

    public DoubleProperty maxLevelProperty() {
        return maxLevel;
    }

    public void setMaxLevel(double maxLevel) {
        this.maxLevel.set(maxLevel);
    }

    public void addObservableFruit(Fruit fruit) {
        fruits.add(fruit);
    }

    public void updateFruits() {
        for (Fruit fruit : fruits) {
            fruit.getFruitController().buildMenus();
            fruit.getFruitController().updateKnobsPositionProperties();
        }
    }

    public ObservableList<Tie> getTies() {
        return this.ties;
    }

    public void addTie(Tie tie) {
        ties.add(tie);
        for (Line line : tie.getLines()) {
            getChildren().add(line);
        }
    }
}