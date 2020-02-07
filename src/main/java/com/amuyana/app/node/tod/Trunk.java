package com.amuyana.app.node.tod;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.containers.Container0;
import com.amuyana.app.data.tod.containers.Container1;
import com.amuyana.app.node.NodeHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Trunk extends VBox {
    private final DataInterface dataInterface = NodeHandler.getDataInterface();
    private Container0 container0;

    private Tree tree;
    private Branch branch;
    private SubBranch subBranch;
    private TrunkType trunkType;

    private DoubleProperty level;
    private boolean side;

    public enum TrunkType {
        TREE,BRANCH,SUBBRANCH
    }
    // constructor to new tree and loadExistingTree a Tree with only one Fcc on it
    // Container0 is already provided because it was needed to instantiate the Tod, and the Fcc the FccEditor
    public Trunk(Tree tree, Container0 container0) {
        level = new SimpleDoubleProperty(1);
        this.tree = tree;
        this.container0 = container0;
        this.trunkType = TrunkType.TREE;
        setStyle();
    }

    // Accessed after the constructor above
    void loadNewBranch(Fcc fcc) {
        Branch branch = new Branch(this);
        SubBranch subBranch = branch.newSubBranch(fcc); // does nothing with subBranch but newSubBranch does instantiation
        addBranch(branch);
    }
    /**
     * @param tree Tree of the TOD
     * @param container0 The container0 of this.trunk
     * @param branch The Branch where this trunk is
     * @param side The side trunk is wrt the Branch's sides. False is left True is right.
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
        //setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.DASHED, new CornerRadii(10), new BorderWidths(1))));
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

    public void remove(Branch branchToRemove) {
        dataInterface.delete(branchToRemove.getLeftTrunk().getContainer0(), branchToRemove.getContainer1());
        dataInterface.delete(branchToRemove.getRightTrunk().getContainer0(), branchToRemove.getContainer1());
        dataInterface.delete(branchToRemove.getLeftTrunk().getContainer0());
        dataInterface.delete(branchToRemove.getRightTrunk().getContainer0());
        dataInterface.delete(branchToRemove.getContainer1());
        getChildren().remove(branchToRemove);
    }

    public void remove(SubBranch subBranch) {
        Branch branch = subBranch.getBranch();
        branch.remove(subBranch);
        // If branch is empty remove the branch
        if (branch.getSubBranches().isEmpty()) {
            remove(branch);
        }
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

    public void deleteContainer0() {

    }

    TrunkType getTrunkType() {
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

    SubBranch getSubBranch() {
        return subBranch;
    }
}