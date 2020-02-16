package com.amuyana.app.node.tod.expression;

import com.amuyana.app.controllers.TodController;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.node.NodeHandler;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ImplicationExp extends Expression {
    private static DataInterface dataInterface = NodeHandler.getDataInterface();
    private ExpressionType expressionType;
    private Dynamism dynamism;
    private TodController todController;
    private Label label;

    public ImplicationExp(TodController todController, Dynamism dynamism, ExpressionType expressionType, Pos alignment) {
        this.todController = todController;
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
        setEffect(new DropShadow(4,Color.rgb(204, 0,0)));
        /*label.setTextFill(Color.rgb(99,00,00));
        */
        /*label.setFont(Font.font("Monospaced", FontWeight.BOLD,13));*/
        /*DropShadow dropShadow = new DropShadow(1, Color.rgb(99,00,00));
        setEffect(dropShadow);*/
    }

    public void setNormalStyle() {
        /*label.setTextFill(Color.rgb(0,0,0));*/
        /*label.setFont(Font.font("Monospaced",13));*/

        setEffect(null);
    }


}
