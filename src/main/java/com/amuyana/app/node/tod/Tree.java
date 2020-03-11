package com.amuyana.app.node.tod;

import com.amuyana.app.controllers.TodController;
import com.amuyana.app.data.DataConnection;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.CClass;
import com.amuyana.app.data.tod.Conjunction;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.node.NodeHandler;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.shape.Line;

import java.sql.SQLException;

/**
 * Equivalent to tod in data package: this.todController.getTod();
 */
public class Tree extends Group {
    private DataInterface dataInterface;
    private DoubleProperty viewScale;
    // General
    private TodController todController;

    // Layout
    private Trunk mainTrunk; //Container0
    private DoubleProperty maxLevel;

    //private ObservableList<Fruit> fruits;
    private ObservableList<Tie> ties;

//    private Group linesGroup;

    // constructor to loadExistingTree an existing Tree
    public Tree(TodController todController) {
        this.todController = todController;
        initializeAndBind();
        setId("Tree");
        bindScale();



/*
        Circle circle1 = new Circle(5, Paint.valueOf("RED"));

        circle1.layoutXProperty();
        circle1.setLayoutX(1562);
        Circle circle2 = new Circle(5);
        Circle circle3 = new Circle(5);
        Circle circle4 = new Circle(5);
*/


    }

    // constructor for debug1 - lines
    public Tree() {
//        this.linesGroup = new Group();
        this.ties = FXCollections.observableArrayList();

        this.viewScale = new SimpleDoubleProperty();
        this.maxLevel = new SimpleDoubleProperty();
        Slider slider = todController.getScaleSlider();
        viewScale.bind(slider.valueProperty());

        setId("Tree");
        bindScale();
    }

    public Tree(TodController todController, Conjunction conjunction) {

    }

    public Tree(TodController todController, CClass cClass) {

    }

    public void loadNewTree() {
        Fcc newFcc = NodeHandler.getDataInterface().newFcc(todController.getTod().getLogicSystem());
        this.mainTrunk = new Trunk(this, todController.getTod().getContainer0());
        this.mainTrunk.loadNewBranch(newFcc);
        todController.openFccEditor(newFcc);
        getChildren().add(this.mainTrunk);

        // for debug
        /*this.mainTrunk = new Trunk(this, todController.getTod().getContainer0());
        getChildren().setAll(this.linesGroup, this.mainTrunk);
        Fcc newFcc = NodeHandler.getDataInterface().newFcc(todController.getTod().getLogicSystem());
        this.mainTrunk.loadNewBranch(newFcc);
        todController.openFccEditor(newFcc);*/
    }

    public void loadExistingTree() {
        this.mainTrunk = new Trunk(this, todController.getTod().getContainer0());
        getChildren().add(this.mainTrunk); //todo move one line below
        this.mainTrunk.loadBranches();
    }

    public void loadNewTreeFromExistingFcc(Fcc fcc) {
        this.mainTrunk = new Trunk(this, todController.getTod().getContainer0());
        getChildren().add(this.mainTrunk);
        this.mainTrunk.loadNewBranch(fcc);
        todController.openFccEditor(fcc);
    }

    private void initializeAndBind() {
        this.dataInterface = NodeHandler.getDataInterface();
//        this.linesGroup = new Group();
        this.ties = FXCollections.observableArrayList();
        this.maxLevel = new SimpleDoubleProperty();
        this.viewScale = new SimpleDoubleProperty();
        Slider slider = todController.getScaleSlider();
        viewScale.bind(slider.valueProperty());
    }

    private void bindScale() {
        scaleXProperty().bind(viewScale);
        scaleYProperty().bind(viewScale);

    }

    public void buildTies() {
        ties.clear();

//        linesGroup.getChildren().clear();
        for (Fruit fruit : getObservableFruits()) {
            fruit.getFruitController().buildTies();
        }
    }

    public void updateFruitsMenus() {
        for (Fruit fruit : getObservableFruits()) {
            fruit.getFruitController().buildMenus();
        }
    }

    void updateMaxLevel(double level) {
        if (level > maxLevel.getValue()) {
            setMaxLevel(level);
        }
    }

    public void addTie(Tie tie) {
        ties.add(tie);
        for (Line line : tie.getLines()) {
            int position = getChildren().size()-1;

            getChildren().add(position,line);
        }
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

    private void setMaxLevel(double maxLevel) {
        this.maxLevel.set(maxLevel);
    }

    public ObservableList<Tie> getTies() {
        return this.ties;
    }

    // removes the fruit in both the fruits field and observableList of fruits in tree nodes
    // this is activated by an inclusions listener in Tie, we can invoke it again by removing all inclusions to child nodes

    public void checkFruitsRemoval() {
        // As the changes are produced one by one we'll find a tie with all orientations false (and no more than 1)
        // But we also have to check that the fruit has no ties to conjunction FCC's
        boolean removeTie = false;
        Tie tieToRemove = null;
        boolean removeFruit = false;
        Fruit fruitToRemove = null;
        for (Tie tie : ties) {
            if (!tie.getPositiveOrientation() && !tie.getNegativeOrientation() && !tie.getSymmetricOrientation()) {
                removeTie = true;
                tieToRemove=tie;
                removeFruit = true;
                if (tie.getAscendantFruit().isChild(tie.getDescendantFruit())) {
                    // Check that tie.getDescendantFruit() is not tied to another fruit of the same conjunction as ascendant
                    Fruit ascendantFruit = tie.getAscendantFruit();
                    for (Fruit fruit : getObservableFruits()) {
                        if(ascendantFruit.equals(fruit)) continue;
                        //If the fruit is in a conjunction that contains ascendantFruit
                        if (fruit.getBranch().getFruits().contains(ascendantFruit)) {
                            // If that fruit also has a tie with descendantFruit
                            for (Tie tie1 : ties) {
                                if (tie1.getAscendantFruit().equals(fruit) && tie1.getDescendantFruit().equals(tie.getDescendantFruit())) {
                                    // dont remove descendant
                                    removeFruit = false;
                                    break;
                                }
                            }
                            if(!removeFruit) break;
                        }
                    }

                    //Remove descendantFruit as it is child
                    if(removeFruit) fruitToRemove=tie.getDescendantFruit();
                } else if (tie.getDescendantFruit().isChild(tie.getAscendantFruit())) {
                    // Check that tie.getAscendantFruit() (potentialRemove) is not tied to another fruit of the same conjunction as descendant
                    Fruit descendantFruit = tie.getDescendantFruit();
                    for (Fruit fruit : getObservableFruits()) {
                        if(descendantFruit.equals(fruit)) continue;
                        //If the fruit is in a conjunction that contains descendantFruit
                        if (fruit.getBranch().getFruits().contains(descendantFruit)) {
                            // If that fruit also has a tie with ascendantFruit
                            for (Tie tie1 : ties) {
                                if (tie1.getDescendantFruit().equals(fruit) && tie1.getAscendantFruit().equals(tie.getAscendantFruit())) {
                                    // dont remove ascendant
                                    removeFruit = false;
                                    break;
                                }
                            }
                            if(!removeFruit) break;
                        }
                    }
                    if(removeFruit) fruitToRemove=tie.getAscendantFruit();
                }
            }
        }
        updateKnobsBounds();
        if(removeTie) remove(tieToRemove);

        if (removeFruit) {
            remove(FXCollections.observableArrayList(),fruitToRemove);
        }
    }

    /*public void checkFruitsRemoval() {
        // As the changes are produced one by one we'll find a tie with all orientations false (and no more than 1)
        // But we also have to check that the fruit has no ties to conjunction FCC's
        boolean removeTie = false;
        Tie tieToRemove = null;
        boolean removeFruit = false;
        Fruit fruitToRemove = null;
        for (Tie tie : ties) {
            if (!tie.getPositiveOrientation() && !tie.getNegativeOrientation() && !tie.getSymmetricOrientation()) {
                removeTie = true;
                tieToRemove=tie;
                removeFruit = true;
                if (tie.getAscendantFruit().isChild(tie.getDescendantFruit())) {
                    // Check there is not another tie with the descendant as descendant...
                    for (Tie tie1 : ties) {
                        // ignore if its the same tie
                        if(tie.equals(tie1)) continue;
                        if (tie1.getDescendantFruit().equals(tie.getDescendantFruit())) {
                            removeFruit=false;
                            break;
                        }
                    }
                    //Remove descendantFruit as it is child
                    if(removeFruit) fruitToRemove=tie.getDescendantFruit();
                } else if (tie.getDescendantFruit().isChild(tie.getAscendantFruit())) {
                    // check other ties as well
                    for (Tie tie1 : ties) {
                        if(tie.equals(tie1)) continue;
                        if (tie1.getAscendantFruit().equals(tie.getAscendantFruit())) {
                            removeFruit=false;
                            break;
                        }
                    }
                    //Remove ascendantFruit as it is child
                    if(removeFruit) fruitToRemove=tie.getAscendantFruit();
                }
            }
        }
        updateKnobsBounds();
        if(removeTie) remove(tieToRemove);

        if (removeFruit) {
            remove(FXCollections.observableArrayList(),fruitToRemove);
        }
    }*/

    public ObservableList<Fruit> remove(ObservableList<Fruit> fruitsToRemove, Fruit fruit) {
        fruitsToRemove.addAll(fruit);
        // removing inclusions to fruits in subBranches of Branches in left and right trunks of fruit's subBranch
        // left
        ObservableList<Branch> leftBranches  = FXCollections.observableArrayList();
        if (!fruit.getSubBranch().getLeftTrunk().getBranches().isEmpty()) {
        }
        leftBranches.addAll(fruit.getSubBranch().getLeftTrunk().getBranches());
        if (!fruit.getBranch().getLeftTrunk().getBranches().isEmpty()) {
        }
        leftBranches.addAll(fruit.getBranch().getLeftTrunk().getBranches());
        // right
        ObservableList<Branch> rightBranches=FXCollections.observableArrayList();
        if (!fruit.getSubBranch().getLeftTrunk().getBranches().isEmpty()) {
        }
        rightBranches.addAll(fruit.getSubBranch().getRightTrunk().getBranches());
        if (!fruit.getBranch().getLeftTrunk().getBranches().isEmpty()) {
        }
        rightBranches.addAll(fruit.getBranch().getRightTrunk().getBranches());

        Dynamism thisPositiveDynamism = dataInterface.getDynamism(fruit.getFcc(),0);
        Dynamism thisNegativeDynamism = dataInterface.getDynamism(fruit.getFcc(),1);
        Dynamism thisSymmetricDynamism = dataInterface.getDynamism(fruit.getFcc(),2);

        // Branches in leftTrunk
        if (!leftBranches.isEmpty()) {
            for (Branch branch1 : leftBranches) {
                for (SubBranch subBranch1 : branch1.getSubBranches()) {
                    remove(fruitsToRemove,subBranch1.getFruit());
                    // Delete inclusions
                    ObservableList<Inclusion> inclusionsToRemove = FXCollections.observableArrayList();
                    for (Inclusion inclusion : dataInterface.getListInclusions()) {
                        Dynamism particular = inclusion.getParticular();
                        Dynamism general = inclusion.getGeneral();

                        Dynamism positiveAscendant = dataInterface.getDynamism(subBranch1.getFruit().getFcc(),0);
                        Dynamism negativeAscendant = dataInterface.getDynamism(subBranch1.getFruit().getFcc(),1);
                        Dynamism symmetricAscendant = dataInterface.getDynamism(subBranch1.getFruit().getFcc(),2);

                        if (particular.getIdDynamism() == thisPositiveDynamism.getIdDynamism()||
                                particular.getIdDynamism() == thisNegativeDynamism.getIdDynamism()||
                                particular.getIdDynamism() == thisSymmetricDynamism.getIdDynamism()) {
                            if (general.getIdDynamism() == positiveAscendant.getIdDynamism() ||
                                    general.getIdDynamism() == negativeAscendant.getIdDynamism() ||
                                    general.getIdDynamism() == symmetricAscendant.getIdDynamism()) {
                                inclusionsToRemove.addAll(inclusion);
                            }
                        }
                    }
                    for (Inclusion inclusion1 : inclusionsToRemove) {
                        dataInterface.delete(inclusion1);
                    }

                    // Remove tie
                    Tie tieToRemove=null;
                    boolean tieBoolean = false;
                    for (Tie tie : getTies()) {
                        if (tie.getAscendantFruit().equals(subBranch1.getFruit()) &&
                                tie.getDescendantFruit().equals(fruit)) {
                            tieToRemove=tie;
                            tieBoolean = true;
                        }
                    }
                    if(tieBoolean) remove(tieToRemove);
                }
            }
        }
        // Branches in rightTrunk : fruit (in param) is ascendant and subBranch1.getFruit() is descendant
        if (!rightBranches.isEmpty()) {
            for (Branch branch1 : rightBranches) {
                for (SubBranch subBranch1 : branch1.getSubBranches()) {
                    remove(fruitsToRemove,subBranch1.getFruit());

                    // Delete inclusions
                    ObservableList<Inclusion> inclusionsToRemove = FXCollections.observableArrayList();
                    for (Inclusion inclusion : dataInterface.getListInclusions()) {
                        Dynamism particular = inclusion.getParticular();
                        Dynamism general = inclusion.getGeneral();

                        Dynamism positiveDescendant = dataInterface.getDynamism(subBranch1.getFruit().getFcc(),0);
                        Dynamism negativeDescendant = dataInterface.getDynamism(subBranch1.getFruit().getFcc(),1);
                        Dynamism symmetricDescendant = dataInterface.getDynamism(subBranch1.getFruit().getFcc(),2);

                        if (general.getIdDynamism() == thisPositiveDynamism.getIdDynamism()||
                                general.getIdDynamism() == thisNegativeDynamism.getIdDynamism()||
                                general.getIdDynamism() == thisSymmetricDynamism.getIdDynamism()) {
                            if (particular.getIdDynamism() == positiveDescendant.getIdDynamism() ||
                                    particular.getIdDynamism() == negativeDescendant.getIdDynamism() ||
                                    particular.getIdDynamism() == symmetricDescendant.getIdDynamism()) {
                                inclusionsToRemove.addAll(inclusion);
                            }
                        }
                    }
                    for (Inclusion inclusion1 : inclusionsToRemove) {
                        dataInterface.delete(inclusion1);
                    }

                    // Remove tie
                    Tie tieToRemove=null;
                    boolean tieBoolean = false;
                    for (Tie tie : getTies()) {
                        if (tie.getAscendantFruit().equals(fruit) &&
                                tie.getDescendantFruit().equals(subBranch1.getFruit())) {
                            tieToRemove=tie;
                            tieBoolean = true;
                        }
                    }
                    if(tieBoolean) remove(tieToRemove);
                }
            }
        }
        fruit.getTrunk().remove(fruit.getSubBranch());
        return fruitsToRemove;
    }

    public void update() {
        updateOrientationTies();
        checkFruitsRemoval();
        updateFruitsMenus();
        getTodController().updateListViews();
        // If there are no fruits show fccSelector
        if (getObservableFruits().isEmpty()) {
            //NodeHandler.getDataInterface().connect();
            todController.showFccSelector();
            //NodeHandler.getDataInterface().disconnect();
        }
    }

    public void clearTies() {
        for (Tie tie : ties) {
            remove(tie);
        }
        ties.clear();
    }

    public void remove(Tie tie) {
        ties.remove(tie);
        ObservableList<Line> lines = FXCollections.observableArrayList();
        for (Node node : getChildren()) {
            if (node.getClass().equals(Line.class)) {
                Line line = (Line)node;
                if (tie.getLines().contains(line)) {
                    lines.add((Line)node);
                }
            }
        }
        getChildren().removeAll(lines);
    }


    public void updateOrientationTies() {
        for (Tie tie : getTies()) {
            tie.setOrientations();
        }
    }

    public void updateKnobsBounds() {
        for (Fruit fruit : getObservableFruits()) {
            if(fruit.getFruitController().isUpdateKnobs())
                fruit.getFruitController().updateKnobsProperty().setValue(false);
            else
                fruit.getFruitController().updateKnobsProperty().setValue(true);
        }
    }
}