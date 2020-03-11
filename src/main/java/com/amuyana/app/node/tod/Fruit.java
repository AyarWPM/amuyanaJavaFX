package com.amuyana.app.node.tod;

import com.amuyana.app.controllers.FXMLSource;
import com.amuyana.app.controllers.FruitController;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.tod.expression.Expression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import com.amuyana.app.data.Fcc;

import java.io.IOException;

public class Fruit {
    private final DataInterface dataInterface = NodeHandler.getDataInterface();
    private final Fcc fcc;
    private SubBranch subBranch;
    private FruitController fruitController;

    // for loading an existing Tree or
    // for loading a new fcc created
    public Fruit(SubBranch subBranch) {
        this.subBranch = subBranch;
        this.fcc = subBranch.getContainer2().getFcc();
    }

    // For loading a new Tree
    public Fruit(SubBranch subBranch, Fcc fcc){
        this.subBranch = subBranch;
        this.fcc = fcc;
    }

/*    void loadFruitSource() {
        //getTree().addFruit(this);
        HBox hBox = new HBox(new Group(loadFruit()));
        hBox.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(hBox);
    }*/
    /**
     *
     * @param fcc The fcc tested descendant of this.fruit
     * @return True if fruit in parameter is descendant of fruit calling the method
     */
    private boolean isDescendant(Fcc fcc) {
        for (Inclusion inclusion : dataInterface.getListInclusions()) {
            if (inclusion.getTod().equals(getTree().getTodController().getTod())) {
                Dynamism particular = inclusion.getParticular();
                Dynamism general = inclusion.getGeneral();
                if (fcc.getIdFcc()==particular.getFcc().getIdFcc()) {
                    if (this.getFcc().getIdFcc()==general.getFcc().getIdFcc()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks 1) if the fruit and fruit in parameter are one on the left or right container of the other and
     * 2) if there's an inclusion between them
     * @param fruit The descendant, ie situated at the right hand side of this.fruit
     * @return true if both conditions are true, false if either is false
     */
    public boolean isDescendant(Fruit fruit) {
        // Left fruits: in leftTrunk of subBranch or of Branch
        ObservableList<Fruit> leftFruits = FXCollections.observableArrayList();
        for (Branch branch1 : fruit.getSubBranch().getLeftTrunk().getBranches()) {
            for (SubBranch subBranch1 : branch1.getSubBranches()) {
                leftFruits.addAll(subBranch1.getFruit());
            }
        }
        for (Branch branch1 : fruit.getBranch().getLeftTrunk().getBranches()) {
            for (SubBranch subBranch1 : branch1.getSubBranches()) {
                leftFruits.addAll(subBranch1.getFruit());
            }
        }

        // Right fruits: in rightTrunk of subBranch or of Branch
        ObservableList<Fruit> rightFruits = FXCollections.observableArrayList();
        for (Branch branch1 : this.getSubBranch().getRightTrunk().getBranches()) {
            for (SubBranch subBranch1 : branch1.getSubBranches()) {
                rightFruits.addAll(subBranch1.getFruit());
            }
        }
        for (Branch branch1 : this.getBranch().getRightTrunk().getBranches()) {
            for (SubBranch subBranch1 : branch1.getSubBranches()) {
                rightFruits.addAll(subBranch1.getFruit());
            }
        }

        for (Inclusion inclusion : dataInterface.getListInclusions()) {
            if (inclusion.getTod().equals(getTree().getTodController().getTod())) {
                Dynamism particular = inclusion.getParticular();
                Dynamism general = inclusion.getGeneral();
                if (fruit.getFcc().getIdFcc()==particular.getFcc().getIdFcc()) {
                    if (this.getFcc().getIdFcc()==general.getFcc().getIdFcc()) {
                        // this.fruit is in right trunk of fruit
                        if (rightFruits.contains(fruit)) {
                            return true;
                        }
                        // this.fruit is in left trunk of fruit
                        if (leftFruits.contains(this)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    /**
     * Checks 1) if the fruit and fruit in parameter are one on the left or right container of the other and
     * 2) if there's an inclusion between them
     * @param fruit The descendant, ie situated at the right hand side of this.fruit
     * @return true if both conditions are true, false if either is false
     */
    public boolean isDescendantOnlyFcc(Fruit fruit) {
        // Left fruits: in leftTrunk of subBranch or of Branch
        ObservableList<Fruit> leftFruits = FXCollections.observableArrayList();
        for (Branch branch1 : fruit.getSubBranch().getLeftTrunk().getBranches()) {
            for (SubBranch subBranch1 : branch1.getSubBranches()) {
                leftFruits.addAll(subBranch1.getFruit());
            }
        }

        // Right fruits: in rightTrunk of subBranch or of Branch
        ObservableList<Fruit> rightFruits = FXCollections.observableArrayList();
        for (Branch branch1 : this.getSubBranch().getRightTrunk().getBranches()) {
            for (SubBranch subBranch1 : branch1.getSubBranches()) {
                rightFruits.addAll(subBranch1.getFruit());
            }
        }

        for (Inclusion inclusion : dataInterface.getListInclusions()) {
            if (inclusion.getTod().equals(getTree().getTodController().getTod())) {
                Dynamism particular = inclusion.getParticular();
                Dynamism general = inclusion.getGeneral();
                if (fruit.getFcc().getIdFcc()==particular.getFcc().getIdFcc()) {
                    if (this.getFcc().getIdFcc()==general.getFcc().getIdFcc()) {
                        // this.fruit is in right trunk of fruit
                        if (rightFruits.contains(fruit)) {
                            return true;
                        }
                        // this.fruit is in left trunk of fruit
                        if (leftFruits.contains(this)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Tests if fruit descends in the sense inclusion from this.fruit, if fruit is descendant it can be in
     * a parent node or a child node
     * @param fruit
     * @return
     */
    public boolean isDescendantConjunction(Fruit fruit) {
        // Left fruits: in leftTrunk of Branch; is fruit in parent node?
        ObservableList<Fruit> leftFruits = FXCollections.observableArrayList();
        for (Branch branch1 : fruit.getBranch().getLeftTrunk().getBranches()) {
            for (SubBranch subBranch1 : branch1.getSubBranches()) {
                leftFruits.addAll(subBranch1.getFruit());
            }
        }

        // Right fruits: in rightTrunk of Branch; is fruit in child node?
        ObservableList<Fruit> rightFruits = FXCollections.observableArrayList();
        for (Branch branch1 : this.getBranch().getRightTrunk().getBranches()) {
            for (SubBranch subBranch1 : branch1.getSubBranches()) {
                rightFruits.addAll(subBranch1.getFruit());
            }
        }

        for (Inclusion inclusion : dataInterface.getListInclusions()) {
            if (inclusion.getTod().equals(getTree().getTodController().getTod())) {
                Dynamism particular = inclusion.getParticular();
                Dynamism general = inclusion.getGeneral();
                if (fruit.getFcc().getIdFcc()==particular.getFcc().getIdFcc()) {
                    if (this.getFcc().getIdFcc()==general.getFcc().getIdFcc()) {
                        // this.fruit is in right trunk of fruit
                        if (rightFruits.contains(fruit)) {
                            return true;
                        }
                        // this.fruit is in left trunk of fruit
                        if (leftFruits.contains(this)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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

    public  Branch getBranch() {
        return subBranch.getBranch();
    }

    Node loadFruit() {
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

    FruitController getFruitController() {
        return fruitController;
    }

    // Ok, takes subBranch's and branch's trunks
    public ObservableList<Fruit> getDescendantFruits() {
        ObservableList<Fruit> descendantFruits = FXCollections.observableArrayList();

// CHILD NODES OF SUBRANCH
        for (Branch rightTrunkBranch: getSubBranch().getRightTrunk().getBranches()) {
            for (SubBranch subBranch : rightTrunkBranch.getSubBranches()) {
                // If that other fruit's fcc is a descendant
                if (isDescendant(subBranch.getFruit().getFcc())) {
                    descendantFruits.addAll(subBranch.getFruit());
                }
            }
        }
        // second consider this fruit is inside a leftTrunk of a subBranch that has
        // deployed this fruit as ascendant
        if (getTrunk().getTrunkType().equals(Trunk.TrunkType.SUBBRANCH)) {
            if (!getTrunk().isSide()) {
                descendantFruits.addAll(getTrunk().getSubBranch().getFruit());
            }
        }

// CHILD NODES OF BRANCH
        for (Branch rightTrunkBranch : getBranch().getRightTrunk().getBranches()) {
            for (SubBranch subBranch : rightTrunkBranch.getSubBranches()) {
                if (isDescendant(subBranch.getFruit().getFcc())) {
                    descendantFruits.addAll(subBranch.getFruit());
                }
            }
        }
        // now the case in which this fruit is in a leftTrunk of another fruits' Branch (not subBranch)

        if (getTrunk().getTrunkType().equals(Trunk.TrunkType.BRANCH)) {
            if (!getTrunk().isSide()) {
                for (SubBranch subBranch : getTrunk().getBranch().getSubBranches()) {
                    descendantFruits.addAll(subBranch.getFruit());
                }
            }
        }
        return descendantFruits;
    }


    public ObservableList<Fruit> getDescendantFruitsOnlyFcc() {
        ObservableList<Fruit> descendantFruits = FXCollections.observableArrayList();

// CHILD NODES OF SUBRANCH
        for (Branch rightTrunkBranch: getSubBranch().getRightTrunk().getBranches()) {
            for (SubBranch subBranch : rightTrunkBranch.getSubBranches()) {
                // If that other fruit's fcc is a descendant
                if (isDescendant(subBranch.getFruit().getFcc())) {
                    descendantFruits.addAll(subBranch.getFruit());
                }
            }
        }
        // second consider this fruit is inside a leftTrunk of a subBranch that has
        // deployed this fruit as ascendant
        if (getTrunk().getTrunkType().equals(Trunk.TrunkType.SUBBRANCH)) {
            if (!getTrunk().isSide()) {
                descendantFruits.addAll(getTrunk().getSubBranch().getFruit());
            }
        }

        return descendantFruits;
    }

    public ObservableList<Fruit> getDescendantFruitsConjunction() {
        ObservableList<Fruit> descendantFruits = FXCollections.observableArrayList();

// CHILD NODES OF BRANCH
        for (Branch rightTrunkBranch : getBranch().getRightTrunk().getBranches()) {
            for (SubBranch subBranch : rightTrunkBranch.getSubBranches()) {
                if (isDescendant(subBranch.getFruit().getFcc())) {
                    descendantFruits.addAll(subBranch.getFruit());
                }
            }
        }
        // now the case in which this fruit is in a leftTrunk of another fruits' Branch (not subBranch)

        if (getTrunk().getTrunkType().equals(Trunk.TrunkType.BRANCH)) {
            if (!getTrunk().isSide()) {
                for (SubBranch subBranch : getTrunk().getBranch().getSubBranches()) {
                    descendantFruits.addAll(subBranch.getFruit());
                }
            }
        }
        return descendantFruits;
    }

    public ObservableList<Fruit> getAscendantFruits() {
        ObservableList<Fruit> ascendantFruits = FXCollections.observableArrayList();
        // 1. check left Trunk of subBranch
        for (Branch leftTrunkBranch : getSubBranch().getLeftTrunk().getBranches()) {
            for (SubBranch subBranch : leftTrunkBranch.getSubBranches()) {
                if (subBranch.getFruit().isDescendant(getFcc())) {
                    ascendantFruits.addAll(subBranch.getFruit());
                }
            }
        }
        // 2. consider this fruit is inside a rightTrunk of another fruit's subBranch that has deployed
        // this fruit as descendant
        if (getTrunk().getTrunkType().equals(Trunk.TrunkType.SUBBRANCH)) {
            if (getTrunk().isSide()) {
                ascendantFruits.addAll(getTrunk().getSubBranch().getFruit());
            }
        }
        // CHILD NODES OF BRANCH
        for (Branch leftTrunkBranch : getBranch().getLeftTrunk().getBranches()) {
            for (SubBranch subBranch : leftTrunkBranch.getSubBranches()) {
                if (subBranch.getFruit().isDescendant(getFcc())) {
                    ascendantFruits.addAll(subBranch.getFruit());
                }
            }
        }
        // now the case in which this fruit is in a leftTrunk of another fruits' Branch (not subBranch)
        if (getTrunk().getTrunkType().equals(Trunk.TrunkType.BRANCH)) {
            if (getTrunk().isSide()) {
                for (SubBranch subBranch : getTrunk().getBranch().getSubBranches()) {
                    ascendantFruits.addAll(subBranch.getFruit());
                }
            }
        }
        return ascendantFruits;
    }

    public ObservableList<Fruit> getAscendantFruitsOnlyFcc() {
        ObservableList<Fruit> ascendantFruits = FXCollections.observableArrayList();
        // 1. check left Trunk of subBranch
        for (Branch leftTrunkBranch : getSubBranch().getLeftTrunk().getBranches()) {
            for (SubBranch subBranch : leftTrunkBranch.getSubBranches()) {
                if (subBranch.getFruit().isDescendant(getFcc())) {
                    ascendantFruits.addAll(subBranch.getFruit());
                }
            }
        }
        // 2. consider this fruit is inside a rightTrunk of another fruit's subBranch that has deployed
        // this fruit as descendant
        if (getTrunk().getTrunkType().equals(Trunk.TrunkType.SUBBRANCH)) {
            if (getTrunk().isSide()) {
                ascendantFruits.addAll(getTrunk().getSubBranch().getFruit());
            }
        }
        return ascendantFruits;
    }

    public ObservableList<Fruit> getAscendantFruitsConjunction() {
        ObservableList<Fruit> ascendantFruits = FXCollections.observableArrayList();
// CHILD NODES OF BRANCH
        for (Branch leftTrunkBranch : getBranch().getLeftTrunk().getBranches()) {
            for (SubBranch subBranch : leftTrunkBranch.getSubBranches()) {
                if (subBranch.getFruit().isDescendant(getFcc())) {
                    ascendantFruits.addAll(subBranch.getFruit());
                }
            }
        }
        // now the case in which this fruit is in a leftTrunk of another fruits' Branch (not subBranch)
        if (getTrunk().getTrunkType().equals(Trunk.TrunkType.BRANCH)) {
            if (getTrunk().isSide()) {
                for (SubBranch subBranch : getTrunk().getBranch().getSubBranches()) {
                    ascendantFruits.addAll(subBranch.getFruit());
                }
            }
        }
        return ascendantFruits;
    }

    // Ok, I check SubBranch and Branch
    boolean isChild(Fruit fruit) {
        for (Branch branch : subBranch.getLeftTrunk().getBranches()) {
            for (Fruit fruit1 : branch.getFruits()) {
                if (fruit1.equals(fruit)) {
                    return true;
                }
            }
        }
        for (Branch branch : subBranch.getRightTrunk().getBranches()) {
            for (Fruit fruit1 : branch.getFruits()) {
                if (fruit1.equals(fruit)) {
                    return true;
                }
            }
        }
        for (Branch branch : getBranch().getLeftTrunk().getBranches()) {
            for (Fruit fruit1 : branch.getFruits()) {
                if (fruit1.equals(fruit)) {
                    return true;
                }
            }
        }
        for (Branch branch : getBranch().getRightTrunk().getBranches()) {
            for (Fruit fruit1 : branch.getFruits()) {
                if (fruit1.equals(fruit)) {
                    return true;
                }
            }
        }
        return false;
    }

    // For changing notation type of expressions
    public void changeFCCsNotationType(Expression.ExpressionType expressionType) {
        fruitController.setFccExpressionType(expressionType);
    }

    public void changeDynamismsNotationType(Expression.ExpressionType expressionType) {
        fruitController.setDynamismsExpressionType(expressionType);
    }


    public void setHoverStylePositiveImplication() {
        fruitController.setHoverStylePositiveImplication();
    }

    public void setHoverStyleNegativeImplication() {
        fruitController.setHoverStyleNegativeImplication();
    }

    public void setHoverStyleSymmetricImplication() {
        fruitController.setHoverStyleSymmetricImplication();
    }

    public void setNormalStylePositiveImplication() {
        fruitController.setNormalStylePositiveImplication();
    }

    public void setNormalStyleNegativeImplication() {
        fruitController.setNormalStyleNegativeImplication();
    }

    public void setNormalStyleSymmetricImplication() {
        fruitController.setNormalStyleSymmetricImplication();
    }

}