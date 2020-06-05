package com.amuyana.app.node.tod.expression;

import com.amuyana.app.data.Element;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class ElementExp extends Expression {
    private Element element;
    private Label elementLabel;

    public ElementExp(Element element) {
        this.element = element;

        this.elementLabel = new Label(this.element.getSymbol());
        this.elementLabel.textProperty().bindBidirectional(this.element.symbolProperty());
        setStyle();

        manageEvents();

        getChildren().add(this.elementLabel);
    }

    // Element with a sign (it never changes, we clear() and reposition new containers where needed...)
    public ElementExp(Element element, Sign.Value VALUE) {
        this.element = element;

        manageEvents();

        //setExpressionHolderStyle(this);
        this.elementLabel = new Label();
        this.elementLabel.textProperty().bind(this.element.symbolProperty());
        setStyle();

        switch (VALUE) {
            case ACTUAL:{
                getChildren().addAll(elementLabel,Sign.actualIndex());
                break;
            }
            case POTENTIAL:{
                getChildren().addAll(elementLabel,Sign.potentialIndex());
                break;
            }
            case SYMMETRIC:{
                getChildren().addAll(elementLabel,Sign.thirdIndex());
                break;
            }
        }
    }

    private void manageEvents() {
        this.element.symbolProperty().addListener((observable, oldValue, newValue) -> setStyle());
    }

    private void setStyle() {
        Element antiElement = null;
        if (element.getPolarity() == 0) {
            antiElement=dataInterface.getElement(element.getFcc(),1);
        } else if (element.getPolarity() == 1) {
            antiElement=dataInterface.getElement(element.getFcc(),0);
        }

        // If they are equal
        if (element.getSymbol().equals(antiElement.getSymbol())) {
            if (element.getPolarity() == 0) {
                setNoBar(element);// even though this element is never with hat we apply the style because of the size
                setBar(antiElement);
            } else if (element.getPolarity() == 1) {
                setBar(element);
            }
        }
        // If they are not equal
        else if(!antiElement.getSymbol().equals(element.getSymbol())) {
            if (element.getPolarity() == 0) {
                setNoBar(antiElement);
            } else if (element.getPolarity() == 1) {
                setNoBar(element);
            }
        }
    }

    public void setBar(Element element) {
        for (ElementExp elementExp : Expression.getElementExps()) {
            if (element.equals(elementExp.getElement())) {
                elementExp.getElementLabel().setStyle("-fx-border-width: 1 0 0 0;" +
                        "-fx-border-style:solid;" +
                        "-fx-border-color:black;" +
                        "-fx-font-size:110%;");
            }
        }
    }

    public void setNoBar(Element element) {
        for (ElementExp elementExp : Expression.getElementExps()) {
            if (element.equals(elementExp.getElement())) {
                elementExp.getElementLabel().setStyle("-fx-border-width: 0;" +
                        "-fx-font-size:110%;");
            }
        }
    }

    public Element getElement() {
        return this.element;
    }

    private ElementExp getExpression() {
        return this;
    }

    private Label getElementLabel() {
        return this.elementLabel;
    }
}
