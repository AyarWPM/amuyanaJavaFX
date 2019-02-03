package com.amuyana.app.node.tod;

import com.amuyana.app.controllers.TodController;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.CClass;
import com.amuyana.app.data.tod.Conjunction;
import com.amuyana.app.node.MainBorderPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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

    private ObservableList<Fruit> fruits;
    private ObservableList<Tie> ties;

    private Group linesGroup;

    // constructor to loadExistingTree an existing Tree
    public Tree(TodController todController) {
        this.todController = todController;
        initializeAndBind();
        setId("Tree");
    }

    // new Tree from one Fcc which is created beforehand because it is added in FccEditor
    // normally container0 is instantiated in Trunk, but because it is given already in Tod we call it,
    // Also instead of creating a new one the user could have selected it from the list

    public Tree(TodController todController, Fcc fcc) {
        this.todController = todController;
        initializeAndBind();
        setId("Tree");
    }
    public Tree(TodController todController, Conjunction conjunction) {

    }
    public Tree(TodController todController, CClass cClass) {

    }

    private void initializeAndBind() {
        linesGroup = new Group();
        fruits = FXCollections.observableArrayList();
        ties = FXCollections.observableArrayList();

        viewScale = new SimpleDoubleProperty();
        maxLevel = new SimpleDoubleProperty();

        Slider slider = todController.getScaleSlider();
        viewScale.bind(slider.valueProperty());
        fruits.addListener(new ListChangeListener<Fruit>() {
            @Override
            public void onChanged(Change<? extends Fruit> change) {
                if (change.next()) {
                    if (change.wasRemoved()) {
                        for (Fruit fruit : change.getList()) {
                            Tie tieToRemove=null;
                            for (Tie tie : ties) {
                                if (tie.getDescendantFruit().equals(fruit)) {
                                    tieToRemove=tie;
                                }
                                if (tie.getAscendantFruit().equals(fruit)) {
                                    tieToRemove=tie;
                                }
                            }
                            ties.remove(tieToRemove);
                            linesGroup.getChildren().removeAll(tieToRemove.getLines());
                        }
                    }
                }
            }
        });
    }

    /**
     * Called after Tree is initialized with constructor with TodController parameter
     */
    public void loadExistingTree() {
        this.mainTrunk = new Trunk(this, todController.getTod().getContainer0());
        getChildren().addAll(this.linesGroup, this.mainTrunk);
        this.mainTrunk.loadBranches();
        bindScale();
    }

    public void loadNewTree() {
        Fcc newFcc = MainBorderPane.getDataInterface().newFcc(todController.getTod().getLogicSystem());
        this.mainTrunk = new Trunk(this, todController.getTod().getContainer0());
        this.mainTrunk.loadNewBranch(newFcc);
        bindScale();
        getChildren().addAll(this.linesGroup, this.mainTrunk);
        todController.openFccEditor(newFcc);
    }

    public void loadNewTreeFromExistingFcc(Fcc fcc) {
        this.mainTrunk = new Trunk(this, todController.getTod().getContainer0());
        this.mainTrunk.loadNewBranch(fcc);

        bindScale();
        getChildren().addAll(this.linesGroup, this.mainTrunk);
        todController.openFccEditor(fcc);
    }

    private void bindScale() {
        scaleXProperty().bind(viewScale);
        scaleYProperty().bind(viewScale);
    }

    void updateMaxLevel(double level) {
        if (level > maxLevel.getValue()) {
            setMaxLevel(level);
        }
    }

    public void addTie(Tie tie) {
        ties.add(tie);
        for (Line line : tie.getLines()) {
            linesGroup.getChildren().add(line);
        }
    }

    public void remove(Tie tie) {
        linesGroup.getChildren().remove(tie);
    }

    public List<Fruit> getFruits() {
        return fruits;
    }

    public ObservableList<Fruit> getObservableFruits() {
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

    public void addFruit(Fruit fruit) {
        fruits.add(fruit);
    }

    public void buildFruitsMenus() {
        for (Fruit fruit : fruits) {
            fruit.getFruitController().buildMenus();
        }
    }

    public ObservableList<Tie> getTies() {
        return this.ties;
    }

    // removes the fruit in both the fruits field and observableList of fruits in tree nodes
    public void remove(Fruit fruit) {
        fruits.remove(fruit);
        SubBranch subBranchToRemove = fruit.getSubBranch();
        Branch branchOfSubBranchToRemove = subBranchToRemove.getBranch();

        // remove its subBranch and the left and right Trunks and container2 and container0In2
        branchOfSubBranchToRemove.remove(subBranchToRemove);

        // If branch is left alone remove it as well and its left right and containers1 and c0IN1
        Trunk trunkOfBranch = branchOfSubBranchToRemove.getTrunk();
        if (branchOfSubBranchToRemove.getSubBranches().isEmpty()) {
            trunkOfBranch.remove(branchOfSubBranchToRemove);
        }
    }
}