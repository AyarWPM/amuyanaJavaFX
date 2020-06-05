package com.amuyana.app.node.tod.expression;

import com.amuyana.app.controllers.TodController;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.tod.Fruit;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ImplicationExp extends Expression {
    private static DataInterface dataInterface = NodeHandler.getDataInterface();
    private final Fruit fruit;
    private ExpressionType expressionType;
    private Dynamism dynamism;
    private TodController todController;
    private Label label;

    private BooleanProperty selection;

    public ImplicationExp(TodController todController, Fruit fruit, Dynamism dynamism, ExpressionType expressionType, Pos alignment) {
        this.todController = todController;
        this.fruit = fruit;
        this.dynamism = dynamism;
        this.expressionType = expressionType;
        this.label = new Label();
        this.label.setFont(Font.font("Monospaced"));
        this.label.textProperty().bind(dynamism.propositionProperty());
        //buildContextMenu();
        buildExpression(expressionType);
        setAlignment(alignment);
        //setPrefHeight(height);
        setMaxWidth(getPrefWidth());
        //setMaxHeight(getPrefHeight());
        selection = new SimpleBooleanProperty();
    }

    private Expression getThis() {
        return this;
    }

    private void buildExpression(ExpressionType expressionType) {
        switch (expressionType) {
            case ALGEBRA:{
                buildAlgebraic();
                break;
            }
            case PROPOSITION:{
                this.getChildren().setAll(this.label);
                break;
            }
        }

    }

    private void buildAlgebraic() {
        ElementExp elementExp;
        ElementExp antiElementExp;

        switch (dynamism.getOrientation()) {
            case 0: {
                elementExp = new ElementExp(dataInterface.getElement(dynamism,0), Sign.Value.ACTUAL);
                antiElementExp = new ElementExp(dataInterface.getElement(dynamism,1), Sign.Value.POTENTIAL);
                this.getChildren().setAll(
                        Sign.OpeningParenthesis(),
                        elementExp,
                        Sign.implies(),
                        antiElementExp,
                        Sign.ClosingParenthesis()
                );
                break;
            }
            case 1:{
                elementExp = new ElementExp(dataInterface.getElement(dynamism,0), Sign.Value.POTENTIAL);
                antiElementExp = new ElementExp(dataInterface.getElement(dynamism,1), Sign.Value.ACTUAL);

                this.getChildren().setAll(
                        Sign.OpeningParenthesis(),
                        antiElementExp,
                        Sign.implies(),
                        elementExp,
                        Sign.ClosingParenthesis()
                );
                break;
            }
            case 2:{
                elementExp = new ElementExp(dataInterface.getElement(dynamism,0), Sign.Value.SYMMETRIC);
                antiElementExp = new ElementExp(dataInterface.getElement(dynamism,1), Sign.Value.SYMMETRIC);

                this.getChildren().setAll(
                        Sign.OpeningParenthesis(),
                        elementExp,
                        Sign.implies(),
                        antiElementExp,
                        Sign.ClosingParenthesis()
                );
                break;
            }

        }
    }

    public void changeExpressionType(ExpressionType notationExpressionType) {
        this.expressionType = notationExpressionType;
        buildExpression(notationExpressionType);
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }

    public void setHoverStyle() {
        if (!isSelection()) {
            setEffect(new DropShadow(4,Color.rgb(204, 0,0)));
        }
        /*label.setTextFill(Color.rgb(99,00,00));
        */
        /*label.setFont(Font.font("Monospaced", FontWeight.BOLD,13));*/
        /*DropShadow dropShadow = new DropShadow(1, Color.rgb(99,00,00));
        setEffect(dropShadow);*/
    }

    public void setNormalStyle() {
        /*label.setTextFill(Color.rgb(0,0,0));*/
        /*label.setFont(Font.font("Monospaced",13));*/
        if (!isSelection()) {
            setEffect(null);
        }
    }

    public boolean isSelection() {
        return selection.get();
    }

    public BooleanProperty selectionProperty() {
        return selection;
    }

    public Dynamism getDynamism() {
        return dynamism;
    }

    public void setSelection(boolean selection) {
        this.selection.set(selection);
        if (selection) {
            setEffect(new DropShadow(4,Color.rgb(130, 250,130)));
        } else {
            setEffect(null);
        }
    }

    public boolean isAscendantSelectable() {
        boolean selectable=false;
        // Is implicationExp in a fruit at the right side of the fruit of first implicationExp?
        Fruit firstFruit = todController.getTree().getImplicationExps().get(0).getFruit();
        if (getFruit().isDescendant(firstFruit)) {
            // If this implicationExp's dynamism is ascendant (left of first)
            Dynamism firstDynamism = todController.getTree().getImplicationExps().get(0).getDynamism();
            if (NodeHandler.getDataInterface().isInclusion(firstDynamism, getDynamism(),todController.getTod())) {
                selectable=true;
            }
        }
        return selectable;
    }

    public boolean isDescendantSelectable() {
        boolean selectable=false;
        // Is implicationExp in a fruit at the right side of the fruit of first implicationExp?
        Fruit lastFruit = todController.getTree().getImplicationExps().get(todController.getTree().getImplicationExps().size()-1).getFruit();
        if (lastFruit.isDescendant(getFruit())) {
            // if this implicationExp's dynamism is descendant (right of last)
            Dynamism lastDynamism = todController.getTree().getImplicationExps().get(todController.getTree().getImplicationExps().size()-1).getDynamism();
            if (NodeHandler.getDataInterface().isInclusion(getDynamism(), lastDynamism ,todController.getTod())) {
                selectable=true;
            }
        }
        return selectable;
    }

    Fruit getFruit() {
        return fruit;
    }

}
