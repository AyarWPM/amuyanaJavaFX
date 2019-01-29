package com.amuyana.app.node.tod;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.containers.Container0;
import com.amuyana.app.data.tod.containers.Container1;
import com.amuyana.app.data.tod.containers.Container2;
import com.amuyana.app.node.MainBorderPane;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Collection;

// container1 equivalent
public class Branch extends HBox {
    private final DataInterface dataInterface = MainBorderPane.getDataInterface();
    private Container1 container1;

    private Trunk trunk; // trunk the branch is in

    private Trunk leftTrunk;
    private Trunk rightTrunk;
    private VBox subBranchesVBox;

    // For loading an existing tree
    public Branch(Trunk trunk, Container1 container1) {
        this.trunk = trunk;
        this.container1 = container1;
        makeStyle();
    }

    public void loadSubBranchesAndTrunks() {
        this.subBranchesVBox = new VBox();
        this.subBranchesVBox.setId("BetweenBranchSubBranch");

        Container0 leftContainer0 = dataInterface.getSideContainer0(container1,false);
        Container0 rightContainer0 = dataInterface.getSideContainer0(container1,true);

        this.leftTrunk = new Trunk(trunk.getTree(), leftContainer0, this, false);
        this.rightTrunk = new Trunk(trunk.getTree(), rightContainer0, this, true);

        this.leftTrunk.loadBranches();
        this.rightTrunk.loadBranches();

        for (Container2 container2 : dataInterface.getContainer2s(container1)) {
            SubBranch subBranch = new SubBranch(this, container2);
            addSubBranch(subBranch);
            subBranch.loadFruitAndTrunks();
        }

        this.getChildren().addAll(leftTrunk, subBranchesVBox,rightTrunk);
    }

    public Branch(Trunk trunk) {
        this.trunk = trunk;
        this.container1 = MainBorderPane.getDataInterface().newContainer1(this.trunk.getContainer0());
        this.subBranchesVBox = new VBox();
        this.subBranchesVBox.setId("BranchVBox");

        this.leftTrunk = new Trunk(trunk.getTree(), this, false);
        this.rightTrunk = new Trunk(trunk.getTree(), this, true);

        this.getChildren().addAll(leftTrunk, subBranchesVBox,rightTrunk);

        makeStyle();
    }

    private void makeStyle() {
        this.spacingProperty().bind(Bindings.divide(5,trunk.levelProperty()));
        if (trunk.isSide()) {
            setId("RightSide");
        } else {
            setId("LeftSide");
        }
    }

    Container1 getContainer1() {
        return this.container1;
    }

    public ObservableList<SubBranch> getSubBranches() {
        ObservableList<SubBranch> subBranches = FXCollections.observableArrayList();
        VBox vBox = (VBox)this.getChildren().get(1);
        for (Node branchNode : vBox.getChildren()) {
            SubBranch subBranch = (SubBranch) branchNode;
            subBranches.add(subBranch);
        }
        return subBranches;
    }

    public void addSubBranch(SubBranch subBranch) {
        this.subBranchesVBox.getChildren().add(subBranch);
    }

    public SubBranch addSubBranch(Fcc fcc) {
        SubBranch subBranch = new SubBranch(this);
        subBranch.loadFruitAndTrunks(fcc);
        addSubBranch(subBranch);
        return subBranch;
    }

    public void addToLeftTrunk(Fcc fcc) {
        Branch branch = new Branch(this.leftTrunk);
        this.leftTrunk.addBranch(branch);
    }

    public void addToRightTrunk(Fcc fcc) {
        Branch branch = new Branch(this.rightTrunk);
        SubBranch subBranch = new SubBranch(this);
        subBranch.loadFruitAndTrunks(fcc);
        addSubBranch(subBranch);
        this.rightTrunk.addBranch(branch);
    }

    public Trunk getTrunk() {
        return this.trunk;
    }

    public Trunk getLeftTrunk() {
        return this.leftTrunk;
    }

    public Trunk getRightTrunk() {
        return this.rightTrunk;
    }

    public ObservableList<Fruit> getFruits() {
        ObservableList<Fruit> fruits = FXCollections.observableArrayList();
        for (SubBranch subBranch : getSubBranches()) {
            fruits.addAll(subBranch.getFruit());
        }
        return fruits;
    }
}