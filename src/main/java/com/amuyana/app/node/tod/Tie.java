package com.amuyana.app.node.tod;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.NodeHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.util.List;

public class Tie {
    private final Fruit ascendantFruit;
    private final Fruit descendantFruit;
    private final DataInterface dataInterface = NodeHandler.getDataInterface();

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

    /**
     * ! This method will throw a exceptions (after moving the mouse wheel to zoom after having add one fruit)
     * and I don't know how to catch it...
     */
    public void buildLines() {
        //getAscendantFruit().getTrunk().levelProperty();

        /*DoubleProperty two = new SimpleDoubleProperty(2);
        line1.strokeWidthProperty().bind(two.divide(getAscendantFruit().getTrunk().levelProperty()));
        line2.strokeWidthProperty().bind(two.divide(getAscendantFruit().getTrunk().levelProperty()));
        line3.strokeWidthProperty().bind(two.divide(getAscendantFruit().getTrunk().levelProperty()));*/

        setNormalStyle(line1, line2, line3);

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

        // todo replace getMax by getCenter
        // Line  1
        line1.startXProperty().bind(Bindings.createDoubleBinding(
                () -> knob1Bind.get().getMinX()+(knob1Bind.get().getMaxX()-knob1Bind.get().getMinX())/2,
                knob1Bind));
        line1.startYProperty().bind(Bindings.createDoubleBinding(
                () -> knob1Bind.get().getMinY()+(knob1Bind.get().getMaxY()-knob1Bind.get().getMinY())/2,
                knob1Bind));

        line1.endXProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getMinX()+(knob0Bind.get().getMaxX()-knob0Bind.get().getMinX())/2,
                knob0Bind));
        line1.endYProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getMinY()+(knob0Bind.get().getMaxY()-knob0Bind.get().getMinY())/2,
                knob0Bind));

        // Line  2
        line2.startXProperty().bind(Bindings.createDoubleBinding(
                () -> knob2Bind.get().getMinX()+(knob2Bind.get().getMaxX()-knob2Bind.get().getMinX())/2,
                knob2Bind));
        line2.startYProperty().bind(Bindings.createDoubleBinding(
                () -> knob2Bind.get().getMinY()+(knob2Bind.get().getMaxY()-knob2Bind.get().getMinY())/2,
                knob2Bind));

        line2.endXProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getMinX()+(knob0Bind.get().getMaxX()-knob0Bind.get().getMinX())/2,
                knob0Bind));
        line2.endYProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getMinY()+(knob0Bind.get().getMaxY()-knob0Bind.get().getMinY())/2,
                knob0Bind));

        // Line  3
        line3.startXProperty().bind(Bindings.createDoubleBinding(
                () -> knob3Bind.get().getMinX()+(knob3Bind.get().getMaxX()-knob3Bind.get().getMinX())/2,
                knob3Bind));
        line3.startYProperty().bind(Bindings.createDoubleBinding(
                () -> knob3Bind.get().getMinY()+(knob3Bind.get().getMaxY()-knob3Bind.get().getMinY())/2,
                knob3Bind));

        line3.endXProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getMinX()+(knob0Bind.get().getMaxX()-knob0Bind.get().getMinX())/2,
                knob0Bind));
        line3.endYProperty().bind(Bindings.createDoubleBinding(
                () -> knob0Bind.get().getMinY()+(knob0Bind.get().getMaxY()-knob0Bind.get().getMinY())/2,
                knob0Bind));
    }

    private void setNormalStyle(Line ... lines) {
        for (Line line : lines) {
            line.setStrokeWidth(3);
            line.setStroke(Color.rgb(22,22,22));
            line.setEffect(null);
        }
    }

    private void setHoverStyle(Line ... lines) {
        for (Line line : lines) {
            line.setStrokeWidth(3);
            line.setStroke(Color.rgb(150,00,00));
            /*DropShadow dropShadow = new DropShadow(2,Color.rgb(99,00,00));
            line.setEffect(dropShadow);*/
        }
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

        Tod tod = ascendantFruit.getTree().getTodController().getTod();
        boolean pp = dataInterface.isInclusion(positiveDescendantDynamism, positiveAscendantDynamism, tod);
        boolean pn = dataInterface.isInclusion(negativeDescendantDynamism, positiveAscendantDynamism, tod);
        boolean ps = dataInterface.isInclusion(symmetricDescendantDynamism, positiveAscendantDynamism, tod);
        boolean np = dataInterface.isInclusion(positiveDescendantDynamism, negativeAscendantDynamism, tod);
        boolean nn = dataInterface.isInclusion(negativeDescendantDynamism, negativeAscendantDynamism, tod);
        boolean ns = dataInterface.isInclusion(symmetricDescendantDynamism, negativeAscendantDynamism, tod);
        boolean sp = dataInterface.isInclusion(positiveDescendantDynamism, symmetricAscendantDynamism, tod);
        boolean sn = dataInterface.isInclusion(negativeDescendantDynamism, symmetricAscendantDynamism, tod);
        boolean ss = dataInterface.isInclusion(symmetricDescendantDynamism, symmetricAscendantDynamism, tod);

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


    public void setHoverStyleLine1() {
        setHoverStyle(line1);
    }

    public void setHoverStyleLine2() {
        setHoverStyle(line2);
    }

    public void setHoverStyleLine3() {
        setHoverStyle(line3);
    }

    public void setNormalStyleLine1() {
        setNormalStyle(line1);
    }

    public void setNormalStyleLine2() {
        setNormalStyle(line2);
    }

    public void setNormalStyleLine3() {
        setNormalStyle(line3);
    }



}
