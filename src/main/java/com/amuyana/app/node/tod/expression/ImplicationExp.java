package com.amuyana.app.node.tod.expression;

import com.amuyana.app.controllers.TodController;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.node.MainBorderPane;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

public class ImplicationExp extends Expression {
    private static DataInterface dataInterface = MainBorderPane.getDataInterface();
    private Dynamism dynamism;
    private TodController todController;
    private double width;
    private double height;

    public ImplicationExp(TodController todController, Dynamism dynamism, ExpressionType expressionType, double width, double height, Pos alignment) {
        this.todController = todController;
        this.dynamism = dynamism;
        this.width = width;
        this.height = height;
        buildContextMenu();
        buildExpression(expressionType);
        setAlignment(alignment);
        setPrefWidth(width);
        //setPrefHeight(height);
        setMaxWidth(getPrefWidth());
        //setMaxHeight(getPrefHeight());
    }

    public void buildContextMenu() {
        MenuItem showAlgebraicRadioMenuItem = new MenuItem("Show the algebraic representation");
        MenuItem showPropositionalRadioMenuItem = new MenuItem("Show the propositional representation");
        showAlgebraicRadioMenuItem.setOnAction(e -> {
            buildAlgebraic();
            //todController.getTree().buildLines();
            //todController.getTree().buildTies();
        });
        showPropositionalRadioMenuItem.setOnAction(e -> {
            buildPropositional();
            //todController.getTree().buildLines();
            //todController.getTree().buildTies();
        });
        showAlgebraicRadioMenuItem.setStyle("-fx-font-family: sans;");
        showPropositionalRadioMenuItem.setStyle("-fx-font-family: sans;");
        ContextMenu contextMenu = new ContextMenu(showAlgebraicRadioMenuItem, showPropositionalRadioMenuItem);

        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(getThis(), event.getScreenX(), event.getScreenY());
                }
            }
        });

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
                buildPropositional();
                break;
            }
        }

    }

    private void buildAlgebraic() {
        ElementExp elementExp = null;
        ElementExp antiElementExp = null;

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

    private void buildPropositional() {
        Label label =new Label(dynamism.getProposition());
        //setMaxWidth(width);
        //setMaxHeight(height);
        this.getChildren().setAll(label);

        /*switch (dynamism.getOrientation()) {
            case 0: {
                this.getChildren().setAll(
                        new Label(dynamism.getProposition())
                );
                break;
            }
            case 1:{
                this.getChildren().setAll(
                        new Label(dynamism.getProposition())
                );
                break;
            }
            case 2:{
                this.getChildren().setAll(
                        new Label(dynamism.getProposition())
                );
                break;
            }

        }*/
    }
}
