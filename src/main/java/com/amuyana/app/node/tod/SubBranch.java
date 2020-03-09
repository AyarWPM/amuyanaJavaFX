package com.amuyana.app.node.tod;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.containers.Container0;
import com.amuyana.app.data.tod.containers.Container2;
import com.amuyana.app.node.NodeHandler;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class SubBranch extends HBox {
    private final DataInterface dataInterface = NodeHandler.getDataInterface();
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
        listenerForKnobs();
    }

    // For the creation of the first Fruit

    SubBranch(Branch branch) {
        this.branch = branch;
        makeStyle();
        listenerForKnobs();
    }

    private void makeStyle() {
        this.spacingProperty().bind(Bindings.divide(50,branch.getTrunk().levelProperty()).multiply(branch.getSubBranches().size()));
        //setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.DASHED, new CornerRadii(10), new BorderWidths(3))));
        if (!branch.getTrunk().getTrunkType().equals(Trunk.TrunkType.TREE)) {
            if (branch.getTrunk().isSide()) {
                setAlignment(Pos.CENTER_LEFT);
            } else {
                setAlignment(Pos.CENTER_RIGHT);
            }
        } else {
            setAlignment(Pos.CENTER);
        }
    }

    private void listenerForKnobs() {
        widthProperty().addListener(observable -> {
            getTrunk().getTree().updateKnobsBounds();
        });
        heightProperty().addListener(observable -> {
            getTrunk().getTree().updateKnobsBounds();
        });
        getBranch().widthProperty().addListener(observable -> {
            getTrunk().getTree().updateKnobsBounds();
        });
        getBranch().heightProperty().addListener(observable -> {
            getTrunk().getTree().updateKnobsBounds();
        });
    }

    void loadFruitAndTrunks() {
        Container0 leftContainer0 = dataInterface.getSideContainer0(container2,false);
        Container0 rightContainer0 = dataInterface.getSideContainer0(container2,true);

        this.leftTrunk = new Trunk(getTrunk().getTree(), leftContainer0, this, false);
        this.leftTrunk.loadBranches();

        this.rightTrunk = new Trunk(getTrunk().getTree(), rightContainer0, this, true);
        this.rightTrunk.loadBranches();

        this.fruit = new Fruit(this);

        this.getChildren().addAll(leftTrunk, fruit.loadFruit(),rightTrunk);
        //this.fruit.loadFruit();
    }

    void loadFruitAndTrunks(Fcc fcc) {
        this.container2 = NodeHandler.getDataInterface().newContainer2(fcc, branch.getContainer1());
        this.leftTrunk = new Trunk(getTrunk().getTree(), this, false);
        this.rightTrunk = new Trunk(getTrunk().getTree(), this, true);
        this.fruit = new Fruit(this, fcc);
        this.getChildren().addAll(leftTrunk, this.fruit.loadFruit(),rightTrunk);
        //this.fruit.loadFruitSource();
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
    public Branch getBranch() {
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
