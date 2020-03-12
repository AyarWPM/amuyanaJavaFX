package com.amuyana.app.node.tod.expression;

import com.amuyana.app.data.Fcc;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class FccExp extends Expression {
    private double maxHeight;
    private double maxWidth;
    private Fcc fcc;
    private ExpressionType expressionType;
    private Label fccPropositionLabel;

    public FccExp(Fcc fcc, ExpressionType expressionType) {
        this.fcc = fcc;
        this.expressionType=expressionType;
        build(expressionType);
    }

    public FccExp(Fcc fcc, ExpressionType expressionType, double maxWidth, double maxHeight) {
        this.fcc = fcc;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        build(expressionType);
    }
    public void setHoverStyle() {
        setEffect(new DropShadow(1, Color.BLACK));
        fccPropositionLabel.setTextFill(Color.rgb(204, 0,0));
    }

    public void setNormalStyle() {
        setEffect(null);
        fccPropositionLabel.setTextFill(Color.BLACK);
    }
    private void build(ExpressionType expressionType) {
        if (expressionType.equals(ExpressionType.ALGEBRA)) {
            ElementExp elementExp = new ElementExp(dataInterface.getElement(fcc,0));
            ElementExp antiElementExp = new ElementExp(dataInterface.getElement(fcc,1));
            this.getChildren().setAll(
                    Sign.OpeningParenthesis(),
                    elementExp,
                    Sign.cDot(),
                    antiElementExp,
                    Sign.ClosingParenthesis()
            );
        } else if (expressionType.equals(ExpressionType.PROPOSITION)){
            this.fccPropositionLabel = new Label();
            fccPropositionLabel.textProperty().bind(fcc.nameProperty());
            fccPropositionLabel.setAlignment(Pos.CENTER);
            //fccPropositionLabel.setMaxWidth(maxWidth);
            //fccPropositionLabel.setMaxHeight(maxHeight);
            fccPropositionLabel.setWrapText(true);
            Sign.StyleFccProposition(fccPropositionLabel);
            getChildren().setAll(fccPropositionLabel);
        }
    }

    public void changeExpressionType(ExpressionType notationExpressionType) {
        this.expressionType = notationExpressionType;
        build(notationExpressionType);
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }
}
