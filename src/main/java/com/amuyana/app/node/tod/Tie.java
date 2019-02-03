package com.amuyana.app.node.tod;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.node.MainBorderPane;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.shape.Line;

import java.util.List;

public class Tie {
    private final Fruit ascendantFruit;
    private final Fruit descendantFruit;
    private final Tree tree;
    private final DataInterface dataInterface = MainBorderPane.getDataInterface();

    private BooleanProperty positiveOrientation;
    private BooleanProperty negativeOrientation;
    private BooleanProperty symmetricOrientation;

    private ObservableList<Line> lines;
    private Line line1;
    private Line line2;
    private Line line3;

    // Used when user deploys to a new FCC or Tree, so we create three inclusions
    public Tie(Fruit descendantFruit, Fruit ascendantFruit){
        this.descendantFruit = descendantFruit;
        this.ascendantFruit = ascendantFruit;
        this.tree = descendantFruit.getTree();
        initialize();
        buildLines();
        manageListeners();
    }

    private void initialize() {
        lines= FXCollections.observableArrayList();
        positiveOrientation = new SimpleBooleanProperty();
        negativeOrientation = new SimpleBooleanProperty();
        symmetricOrientation = new SimpleBooleanProperty();
    }

    public void buildLines() {
        this.line1 = new Line();
        this.line2 = new Line();
        this.line3 = new Line();

        line1.visibleProperty().bind(positiveOrientation);
        line2.visibleProperty().bind(negativeOrientation);
        line3.visibleProperty().bind(symmetricOrientation);
        line1.managedProperty().bind(positiveOrientation);
        line2.managedProperty().bind(negativeOrientation);
        line3.managedProperty().bind(symmetricOrientation);

        ObjectBinding<Bounds> knob1Bind =  ascendantFruit.getFruitController().knob1BoundsInTreeBindingProperty();
        ObjectBinding<Bounds> knob2Bind =  ascendantFruit.getFruitController().knob2BoundsInTreeBindingProperty();
        ObjectBinding<Bounds> knob3Bind =  ascendantFruit.getFruitController().knob3BoundsInTreeBindingProperty();
        ObjectBinding<Bounds> knob0Bind =  descendantFruit.getFruitController().knob0BoundsInTreeBindingProperty();

        // Line  1
        line1.startXProperty().bind(Bindings.createDoubleBinding(
                () -> knob1Bind.get().getCenterX(),
                knob1Bind));
        line1.startYProperty().bind(Bindings.createDoubleBinding(
                () -> knob1Bind.get().getCenterY(),
                knob1Bind));

        line1.endXProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getCenterX(),
                knob0Bind));
        line1.endYProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getCenterY(),
                knob0Bind));

        // Line  2
        line2.startXProperty().bind(Bindings.createDoubleBinding(
                () -> knob2Bind.get().getCenterX(),
                knob2Bind));
        line2.startYProperty().bind(Bindings.createDoubleBinding(
                () -> knob2Bind.get().getCenterY(),
                knob2Bind));

        line2.endXProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getCenterX(),
                knob0Bind));
        line2.endYProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getCenterY(),
                knob0Bind));

        // Line  3
        line3.startXProperty().bind(Bindings.createDoubleBinding(
                () -> knob3Bind.get().getCenterX(),
                knob3Bind));
        line3.startYProperty().bind(Bindings.createDoubleBinding(
                () -> knob3Bind.get().getCenterY(),
                knob3Bind));

        line3.endXProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getCenterX(),
                knob0Bind));
        line3.endYProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getCenterY(),
                knob0Bind));


        lines.addAll(line1,line2,line3);
    }

    private void manageListeners() {
        ObservableList<Inclusion> inclusions = MainBorderPane.getDataInterface().getListInclusions();
        inclusions.addListener(listenInclusions());
    }


    private ListChangeListener<Inclusion> listenInclusions() {
        return new ListChangeListener<Inclusion>() {
            @Override
            public void onChanged(Change<? extends Inclusion> change) {
                updateOrientations();
            }
        };
    }

    public void updateOrientations() {
        positiveOrientation.set(false);
        negativeOrientation.set(false);
        symmetricOrientation.set(false);

        Dynamism positiveAscendantDynamism = dataInterface.getDynamism(ascendantFruit.getFcc(),0);
        Dynamism negativeAscendantDynamism = dataInterface.getDynamism(ascendantFruit.getFcc(),1);
        Dynamism symmetricAscendantDynamism = dataInterface.getDynamism(ascendantFruit.getFcc(),2);

        if (dataInterface.isInclusion(descendantFruit.getFcc(), positiveAscendantDynamism)) {
            positiveOrientation.set(true);
        }
        if (dataInterface.isInclusion(descendantFruit.getFcc(), negativeAscendantDynamism)) {
            negativeOrientation.set(true);
        }
        if (dataInterface.isInclusion(descendantFruit.getFcc(), symmetricAscendantDynamism)) {
            symmetricOrientation.set(true);
        }

        // If all 3 are false then we remove the descendantFruit... except if that one has descendants or ascendants
        // on its own child.
        if (!positiveOrientation.get() & !negativeOrientation.get() & !symmetricOrientation.get()) {
            tree.remove(descendantFruit);  // removes from both fruits and observableFruits lists
            tree.buildFruitsMenus();
        }
    }

    public Fruit getAscendantFruit() {
        return ascendantFruit;
    }

    public Fruit getDescendantFruit() {
        return descendantFruit;
    }

    public List<Line> getLines() {
        return this.lines;
    }

    public boolean getPositiveOrientation() {
        return positiveOrientation.get();
    }

    public BooleanProperty positiveOrientationProperty() {
        return positiveOrientation;
    }

    public void setPositiveOrientation(boolean positiveOrientation) {
        this.positiveOrientation.set(positiveOrientation);
    }

    public boolean getNegativeOrientation() {
        return negativeOrientation.get();
    }

    public BooleanProperty negativeOrientationProperty() {
        return negativeOrientation;
    }

    public void setNegativeOrientation(boolean negativeOrientation) {
        this.negativeOrientation.set(negativeOrientation);
    }

    public boolean getSymmetricOrientation() {
        return symmetricOrientation.get();
    }

    public BooleanProperty symmetricOrientationProperty() {
        return symmetricOrientation;
    }

    public void setSymmetricOrientation(boolean symmetricOrientation) {
        this.symmetricOrientation.set(symmetricOrientation);
    }
}
