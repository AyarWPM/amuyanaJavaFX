package com.amuyana.app.node.tod;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.node.MainBorderPane;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.shape.Line;

import java.util.List;

public class Tie {
    private final Fruit ascendantFruit;
    private final Fruit descendantFruit;
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
        initialize();
        buildLines();
        setOrientations();
        //manageListeners();
    }

    private void initialize() {
        lines= FXCollections.observableArrayList();
        positiveOrientation = new SimpleBooleanProperty();
        negativeOrientation = new SimpleBooleanProperty();
        symmetricOrientation = new SimpleBooleanProperty();
        line1 = new Line();
        line2 = new Line();
        line3 = new Line();
        lines.addAll(line1, line2, line3);
    }

    public void buildLines() {
        getAscendantFruit().getTrunk().levelProperty();

        DoubleProperty two = new SimpleDoubleProperty(2);
        line1.strokeWidthProperty().bind(two.divide(getAscendantFruit().getTrunk().levelProperty()));
        line2.strokeWidthProperty().bind(two.divide(getAscendantFruit().getTrunk().levelProperty()));
        line3.strokeWidthProperty().bind(two.divide(getAscendantFruit().getTrunk().levelProperty()));

        line1.setStyle("-fx-stroke:#222222;");
        line2.setStyle("-fx-stroke:#222222;");
        line3.setStyle("-fx-stroke:#222222;");

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
                () -> knob1Bind.get().getMaxX(),
                knob1Bind));
        line1.startYProperty().bind(Bindings.createDoubleBinding(
                () -> knob1Bind.get().getMaxY(),
                knob1Bind));

        line1.endXProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getMaxX()-1,
                knob0Bind));
        line1.endYProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getMaxY()-1,
                knob0Bind));

        // Line  2
        line2.startXProperty().bind(Bindings.createDoubleBinding(
                () -> knob2Bind.get().getMaxX(),
                knob2Bind));
        line2.startYProperty().bind(Bindings.createDoubleBinding(
                () -> knob2Bind.get().getMaxY(),
                knob2Bind));

        line2.endXProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getMaxX()-1,
                knob0Bind));
        line2.endYProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getMaxY()-1,
                knob0Bind));

        // Line  3
        line3.startXProperty().bind(Bindings.createDoubleBinding(
                () -> knob3Bind.get().getMaxX(),
                knob3Bind));
        line3.startYProperty().bind(Bindings.createDoubleBinding(
                () -> knob3Bind.get().getMaxY(),
                knob3Bind));

        line3.endXProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getMaxX()-1,
                knob0Bind));
        line3.endYProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getMaxY()-1,
                knob0Bind));
    }

    /*private void manageListeners() {
        ObservableList<Inclusion> inclusions = MainBorderPane.getDataInterface().getListInclusions();
        inclusions.addListener(updateOrientationsListener());
    }*/

    public void setOrientations() {
        Dynamism positiveDescendantDynamism = dataInterface.getDynamism(descendantFruit.getFcc(),0);
        Dynamism negativeDescendantDynamism = dataInterface.getDynamism(descendantFruit.getFcc(),1);
        Dynamism symmetricDescendantDynamism = dataInterface.getDynamism(descendantFruit.getFcc(),2);
        Dynamism positiveAscendantDynamism = dataInterface.getDynamism(ascendantFruit.getFcc(),0);
        Dynamism negativeAscendantDynamism = dataInterface.getDynamism(ascendantFruit.getFcc(),1);
        Dynamism symmetricAscendantDynamism = dataInterface.getDynamism(ascendantFruit.getFcc(),2);

        boolean pp = dataInterface.isInclusion(positiveDescendantDynamism, positiveAscendantDynamism);
        boolean pn = dataInterface.isInclusion(negativeDescendantDynamism, positiveAscendantDynamism);
        boolean ps = dataInterface.isInclusion(symmetricDescendantDynamism, positiveAscendantDynamism);
        boolean np = dataInterface.isInclusion(positiveDescendantDynamism, negativeAscendantDynamism);
        boolean nn = dataInterface.isInclusion(negativeDescendantDynamism, negativeAscendantDynamism);
        boolean ns = dataInterface.isInclusion(symmetricDescendantDynamism, negativeAscendantDynamism);
        boolean sp = dataInterface.isInclusion(positiveDescendantDynamism, symmetricAscendantDynamism);
        boolean sn = dataInterface.isInclusion(negativeDescendantDynamism, symmetricAscendantDynamism);
        boolean ss = dataInterface.isInclusion(symmetricDescendantDynamism, symmetricAscendantDynamism);

        if (pp || pn || ps) {
            positiveOrientation.set(true);
        } else if (!pp && !pn && !ps) {
            positiveOrientation.set(false);
        }

        if (np || nn || ns) {
            negativeOrientation.set(true);
        } else if (!np && !nn && !ns) {
            negativeOrientation.set(false);
        }

        if (sp || sn || ss) {
            symmetricOrientation.set(true);
        } else if (!sp && !sn && !ss) {
            symmetricOrientation.set(false);
        }
    }

    public Fruit getAscendantFruit() {
        return ascendantFruit;
    }

    public Fruit getDescendantFruit() {
        return descendantFruit;
    }

    List<Line> getLines() {
        return this.lines;
    }

    boolean getPositiveOrientation() {
        return positiveOrientation.get();
    }

    public BooleanProperty positiveOrientationProperty() {
        return positiveOrientation;
    }

    public void setPositiveOrientation(boolean positiveOrientation) {
        this.positiveOrientation.set(positiveOrientation);
    }

    boolean getNegativeOrientation() {
        return negativeOrientation.get();
    }

    public BooleanProperty negativeOrientationProperty() {
        return negativeOrientation;
    }

    public void setNegativeOrientation(boolean negativeOrientation) {
        this.negativeOrientation.set(negativeOrientation);
    }

    boolean getSymmetricOrientation() {
        return symmetricOrientation.get();
    }

    public BooleanProperty symmetricOrientationProperty() {
        return symmetricOrientation;
    }

    public void setSymmetricOrientation(boolean symmetricOrientation) {
        this.symmetricOrientation.set(symmetricOrientation);
    }
}
