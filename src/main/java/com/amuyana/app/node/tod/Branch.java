package com.amuyana.app.node.tod;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.containers.Container0;
import com.amuyana.app.data.tod.containers.Container1;
import com.amuyana.app.data.tod.containers.Container2;
import com.amuyana.app.node.NodeHandler;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.*;

// container1 equivalent
public class Branch extends HBox {
    private final DataInterface dataInterface = NodeHandler.getDataInterface();
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

    void loadSubBranchesAndTrunks() {
        this.subBranchesVBox = new VBox();
        this.subBranchesVBox.setId("BetweenBranchSubBranch");

        Container0 leftContainer0 = dataInterface.getSideContainer0(container1,false);
        Container0 rightContainer0 = dataInterface.getSideContainer0(container1,true);

        this.leftTrunk = new Trunk(trunk.getTree(), leftContainer0, this, false);
        this.rightTrunk = new Trunk(trunk.getTree(), rightContainer0, this, true);
        this.getChildren().setAll(leftTrunk, subBranchesVBox,rightTrunk);

        this.leftTrunk.loadBranches();
        this.rightTrunk.loadBranches();

        for (Container2 container2 : dataInterface.getContainer2s(container1)) {
            SubBranch subBranch = new SubBranch(this, container2);
            addSubBranch(subBranch);
            subBranch.loadFruitAndTrunks();
        }

    }

    Branch(Trunk trunk) {
        this.trunk = trunk;
        this.container1 = NodeHandler.getDataInterface().newContainer1(this.trunk.getContainer0());
        this.subBranchesVBox = new VBox();
        this.subBranchesVBox.setId("BranchVBox");

        this.leftTrunk = new Trunk(trunk.getTree(), this, false);
        this.rightTrunk = new Trunk(trunk.getTree(), this, true);

        this.getChildren().setAll(leftTrunk, subBranchesVBox,rightTrunk);

        makeStyle();
    }

    private void makeStyle() {
        this.spacingProperty().bind(Bindings.divide(50,trunk.levelProperty()));
        //setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.DASHED, new CornerRadii(10), new BorderWidths(2))));
        if (trunk.isSide()) {
            setId("RightSide");
        } else {
            setId("LeftSide");
        }
    }

    public Container1 getContainer1() {
        return this.container1;
    }

    ObservableList<SubBranch> getSubBranches() {
        ObservableList<SubBranch> subBranches = FXCollections.observableArrayList();
        VBox vBox = (VBox)this.getChildren().get(1);
        for (Node branchNode : vBox.getChildren()) {
            SubBranch subBranch = (SubBranch) branchNode;
            subBranches.add(subBranch);
        }
        return subBranches;
    }

    private void addSubBranch(SubBranch subBranch) {
        this.subBranchesVBox.getChildren().add(subBranch);
    }

    SubBranch newSubBranch(Fcc fcc) {
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
            // And then for left and right trunks of subBranch get their Fruits too ;)
            for (Branch branch : subBranch.getLeftTrunk().getBranches()) {
                fruits.addAll(branch.getFruits());
            }
            for (Branch branch : subBranch.getRightTrunk().getBranches()) {
                fruits.addAll(branch.getFruits());
            }
        }

        for (Branch branch : leftTrunk.getBranches()) {
            fruits.addAll(branch.getFruits());
        }
        for (Branch branch : rightTrunk.getBranches()) {
            fruits.addAll(branch.getFruits());
        }

        return fruits;
    }

    /*public void remove(Fruit fruit) {
        SubBranch subBranchToRemove = null;
        for (SubBranch subBranch : getSubBranches()) {
            if (subBranch.getFruit().equals(fruit)) {
                subBranchToRemove=subBranch;
            }
        }

        dataInterface.delete(subBranchToRemove.getLeftTrunk().getContainer0(), subBranchToRemove.getContainer2());
        dataInterface.delete(subBranchToRemove.getRightTrunk().getContainer0(), subBranchToRemove.getContainer2());
        dataInterface.delete(subBranchToRemove.getLeftTrunk().getContainer0());
        dataInterface.delete(subBranchToRemove.getRightTrunk().getContainer0());
        dataInterface.delete(subBranchToRemove.getContainer2());

        subBranchesVBox.getChildren().remove(subBranchToRemove);
    }*/

    public void remove(SubBranch subBranch) {
        dataInterface.delete(subBranch.getLeftTrunk().getContainer0(), subBranch.getContainer2());
        dataInterface.delete(subBranch.getRightTrunk().getContainer0(), subBranch.getContainer2());
        dataInterface.delete(subBranch.getLeftTrunk().getContainer0());
        dataInterface.delete(subBranch.getRightTrunk().getContainer0());
        dataInterface.delete(subBranch.getContainer2());

        subBranchesVBox.getChildren().remove(subBranch);
    }

    void initRightTrunk() {
        Container0 rightContainer0 = dataInterface.getSideContainer0(container1,true);
        this.rightTrunk = new Trunk(getTrunk().getTree(), rightContainer0, this, true);
    }

    void initLeftTrunk() {
        Container0 leftContainer0 = dataInterface.getSideContainer0(container1,false);
        this.leftTrunk = new Trunk(getTrunk().getTree(), leftContainer0, this, false);
    }

    public int getBranchOrder() {
        return container1.getBranchOrder();
    }

    public void setBranchOrder(int order) {
        container1.setBranchOrder(order);
        dataInterface.update(container1);
    }
}