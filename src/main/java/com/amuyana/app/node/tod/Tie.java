package com.amuyana.app.node.tod;

import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.node.MainBorderPane;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.shape.Line;

import java.util.List;

public class Tie {
    private final Fruit ascendantFruit;
    private final Fruit descendantFruit;

    private BooleanProperty positiveOrientation;
    private BooleanProperty negativeOrientation;
    private BooleanProperty symmetricOrientation;

    private ObservableList<Line> lines;

    public Tie(Fruit descendantFruit, Fruit ascendantFruit){
        this.descendantFruit = descendantFruit;
        this.ascendantFruit = ascendantFruit;
        lines= FXCollections.observableArrayList();
        buildLines();
        manageListeners();
    }

    private void buildLines() {
        Line line1 = new Line();
        Line line2 = new Line();
        Line line3 = new Line();

        line1.startXProperty().bind(ascendantFruit.getFruitController().knob1XProperty());
        line1.startYProperty().bind(ascendantFruit.getFruitController().knob1YProperty());
        line1.endXProperty().bind(descendantFruit.getFruitController().knob0XProperty());
        line1.endYProperty().bind(descendantFruit.getFruitController().knob0YProperty());
        line1.visibleProperty().bind(positiveOrientation);
        line1.managedProperty().bind(positiveOrientation);

        line2.startXProperty().bind(ascendantFruit.getFruitController().knob2XProperty());
        line2.startYProperty().bind(ascendantFruit.getFruitController().knob2YProperty());
        line2.endXProperty().bind(descendantFruit.getFruitController().knob0XProperty());
        line2.endYProperty().bind(descendantFruit.getFruitController().knob0YProperty());
        line2.visibleProperty().bind(negativeOrientation);
        line2.managedProperty().bind(negativeOrientation);

        line3.startXProperty().bind(ascendantFruit.getFruitController().knob3XProperty());
        line3.startYProperty().bind(ascendantFruit.getFruitController().knob3YProperty());
        line3.endXProperty().bind(descendantFruit.getFruitController().knob0XProperty());
        line3.endYProperty().bind(descendantFruit.getFruitController().knob0YProperty());
        line3.visibleProperty().bind(symmetricOrientation);
        line3.managedProperty().bind(symmetricOrientation);

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
        ObservableList<Inclusion> inclusions = MainBorderPane.getDataInterface().getListInclusions();
        positiveOrientation.set(false);
        negativeOrientation.set(false);
        symmetricOrientation.set(false);

        for (Inclusion inclusion : inclusions) {
            if (inclusion.getParticular().getFcc().equals(descendantFruit.getFcc())) {
                if (inclusion.getGeneral().getFcc().equals(ascendantFruit.getFcc())) {
                    Dynamism general = inclusion.getGeneral();
                    switch (general.getOrientation()) {
                        case 0:
                            positiveOrientation.set(true);
                            break;
                        case 1:
                            negativeOrientation.set(true);
                            break;
                        case 2:
                            symmetricOrientation.set(true);
                            break;
                    }
                }
            }
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
