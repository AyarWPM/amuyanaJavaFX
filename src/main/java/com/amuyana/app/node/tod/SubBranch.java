package com.amuyana.app.node.tod;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.containers.Container0;
import com.amuyana.app.data.tod.containers.Container2;
import com.amuyana.app.node.MainBorderPane;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.HBox;

public class SubBranch extends HBox {
    private final DataInterface dataInterface = MainBorderPane.getDataInterface();
    private Fruit fruit;

    private Container2 container2;

    private Branch branch; // the branch this subBranch is in
    private Trunk leftTrunk;
    private Trunk rightTrunk;

    // For loading an existing tree
    SubBranch(Branch branch, Container2 container2) {
        this.container2 = container2;
        this.branch = branch;
        makeStyle();
    }

    void loadFruitAndTrunks() {
        Container0 leftContainer0 = dataInterface.getSideContainer0(container2,false);
        Container0 rightContainer0 = dataInterface.getSideContainer0(container2,true);

        this.leftTrunk = new Trunk(getTrunk().getTree(), leftContainer0, this, false);
        this.leftTrunk.loadBranches();

        this.rightTrunk = new Trunk(getTrunk().getTree(), rightContainer0, this, true);
        this.rightTrunk.loadBranches();

        this.fruit = new Fruit(this);

        this.getChildren().addAll(leftTrunk, fruit,rightTrunk);
        this.fruit.loadFruitSource();
        this.fruit.getFruitController().buildTies();
    }

    // For the creation of the first Fruit
    SubBranch(Branch branch) {
        this.branch = branch;
        makeStyle();
    }

    void loadFruitAndTrunks(Fcc fcc) {
        this.container2 = MainBorderPane.getDataInterface().newContainer2(fcc, branch.getContainer1());
        this.leftTrunk = new Trunk(getTrunk().getTree(), this, false);
        this.rightTrunk = new Trunk(getTrunk().getTree(), this, true);
        this.fruit = new Fruit(this, fcc);
        this.getChildren().addAll(leftTrunk, fruit,rightTrunk);
        this.fruit.loadFruitSource();
        this.fruit.getFruitController().buildTies(); // ?<- should this be here?
    }

    private void makeStyle() {
        this.spacingProperty().bind(Bindings.divide(50,branch.getTrunk().levelProperty()));

        if (branch.getTrunk().isSide()) {
            setId("RightSide");
        } else {
            setId("LeftSide");
        }
    }

    private Trunk getTrunk() {
        return getBranch().getTrunk();
    }

    public Fruit getFruit() {
        return fruit;
    }

    /**
     *
     * @return The branch (container1) this subBranch is in
     */
    Branch getBranch() {
        return this.branch;
    }

    Container2 getContainer2() {
        return this.container2;
    }

    public Fruit addToLeftTrunk(Fcc fcc) {
        Branch branch = new Branch(this.leftTrunk);
        SubBranch subBranch = branch.newSubBranch(fcc);
        this.leftTrunk.addBranch(branch);
        return subBranch.getFruit();
    }

    public Fruit addToRightTrunk(Fcc fcc) {
        Branch branch = new Branch(this.rightTrunk);
        SubBranch subBranch = branch.newSubBranch(fcc);
        this.rightTrunk.addBranch(branch);
        return subBranch.getFruit();
    }

    public Trunk getLeftTrunk() {
        return this.leftTrunk;
    }

    public Trunk getRightTrunk() {
        return this.rightTrunk;
    }


    void initRightTrunk() {
        Container0 rightContainer0 = dataInterface.getSideContainer0(container2,true);
        this.rightTrunk = new Trunk(getTrunk().getTree(), rightContainer0, this, true);
    }

    void initLeftTrunk() {
        Container0 leftContainer0 = dataInterface.getSideContainer0(container2,false);
        this.leftTrunk = new Trunk(getTrunk().getTree(), leftContainer0, this, false);
    }
}
