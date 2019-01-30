package com.amuyana.app.node.tod;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.containers.Container0;
import com.amuyana.app.data.tod.containers.Container1;
import com.amuyana.app.node.MainBorderPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class Trunk extends VBox {
    private final DataInterface dataInterface = MainBorderPane.getDataInterface();
    private Container0 container0;

    private Tree tree;
    private Branch branch;
    private SubBranch subBranch;
    private TrunkType trunkType;

    private DoubleProperty level;
    private boolean side;

    public enum TrunkType {
        TREE,BRANCH,SUBBRANCH;
    }
    // constructor to loadExistingTree a Tree with only one Fcc on it
    // Container0 is already provided because it was needed to instantiate the Tod, and the Fcc the FccEditor
    Trunk(Tree tree, Container0 container0, Fcc fcc) {
        level = new SimpleDoubleProperty(1);
        this.tree = tree;
        this.container0 = container0;
        this.trunkType = TrunkType.TREE;
        setStyle();
        Branch branch = new Branch(this);
        SubBranch subBranch = branch.newSubBranch(fcc); // does nothing with subBranch but newSubBranch does instantiation
        addBranch(branch);
    }

    // Constructor to loadExistingTree an existing Tod
    Trunk(Tree tree, Container0 container0) {
        level = new SimpleDoubleProperty(1);
        this.tree = tree;
        this.container0 = container0;
        this.trunkType = TrunkType.TREE;
        setStyle();
    }

    /**
     * @param tree
     * @param container0
     * @param branch
     * @param side The side with respect to the Fruit for the SubBranch and the SubBranches for the Branch, false
     */
    Trunk(Tree tree, Container0 container0, Branch branch, boolean side) {
        level = new SimpleDoubleProperty(branch.getTrunk().getLevel()+1);
        this.tree = tree;
        this.container0 = container0;
        this.branch = branch;
        setSide(side);
        setSide(this.side);

        this.trunkType = TrunkType.BRANCH;
        setStyle();
    }

    // Loading from inside subBranches

    Trunk(Tree tree, Container0 container0, SubBranch subBranch, boolean side) {
        level = new SimpleDoubleProperty(subBranch.getBranch().getTrunk().getLevel()+1);
        this.tree = tree;
        this.container0 = container0;
        this.subBranch = subBranch;
        this.trunkType = TrunkType.SUBBRANCH;
        setSide(side);
        setStyle();
    }
    // called after the (two) above constructor(s) only
    void loadBranches() {
        for (Container1 container1 : dataInterface.getContainer1s(container0)) {
            Branch branch = new Branch(this, container1);
            addBranch(branch);
            branch.loadSubBranchesAndTrunks();
        }
    }

    // Will create new Container0, when loading a Tree
    Trunk(Tree tree, Branch branch, boolean side) {
        level = new SimpleDoubleProperty(branch.getTrunk().getLevel()+1);
        this.tree = tree;
        this.branch = branch;
        this.trunkType = TrunkType.BRANCH;
        this.container0 = dataInterface.newContainer0(branch.getContainer1(),side);
        setSide(side);
        setStyle();
    }

    Trunk(Tree tree, SubBranch subBranch, boolean side) {
        level = new SimpleDoubleProperty(subBranch.getBranch().getTrunk().getLevel()+1);
        this.tree = tree;
        this.subBranch = subBranch;
        this.trunkType = TrunkType.SUBBRANCH;
        this.container0 = dataInterface.newContainer0(subBranch.getContainer2(), side);
        setSide(side);
        setStyle();

    }

    private void setStyle() {
        getStylesheets().add("/css/fruit.css");
        setId("Trunk");
        this.spacingProperty().bind(Bindings.divide(10,levelProperty()));
        this.tree.updateMaxLevel(level.getValue());
    }

    boolean isSide() {
        return this.side;
    }

    private void setSide(boolean side) {
        this.side = side;
    }

    Tree getTree() {
        return this.tree;
    }

    void addBranch(Branch branch) {
        this.getChildren().add(branch);
    }

    public ObservableList<Branch> getBranches() {
        ObservableList<Branch> branches = FXCollections.observableArrayList();
        for (Node node : getChildren()) {
            Branch branch = (Branch)node;
            branches.add(branch);
        }
        return branches;
    }

    Container0 getContainer0() {
        return this.container0;
    }

    public TrunkType getTrunkType() {
        return this.trunkType;
    }

    // Returns the Trunk this Trunk is in. This method is called after checking that this trunkType is not Tree
    public Trunk getTrunk() {
        Trunk trunk = null;
        if (trunkType.equals(TrunkType.BRANCH)) {
            trunk = this.branch.getTrunk();
        } else if (trunkType.equals(TrunkType.SUBBRANCH)) {
            trunk = this.subBranch.getBranch().getTrunk();
        }
        return trunk;
    }

    public double getLevel() {
        return level.get();
    }

    public DoubleProperty levelProperty() {
        return level;
    }

    public void setLevel(double level) {
        this.level.set(level);
    }

    public Branch getBranch() {
        return branch;
    }

    public SubBranch getSubBranch() {
        return subBranch;
    }
}