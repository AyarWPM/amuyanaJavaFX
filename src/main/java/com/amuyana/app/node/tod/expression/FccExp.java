package com.amuyana.app.node.tod.expression;

import com.amuyana.app.data.Fcc;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class FccExp extends Expression {
    private final double height;
    private final double width;
    private Fcc fcc;
    private ExpressionType notationExpressionType;
    private Label fccPropositionLabel;

    public FccExp(Fcc fcc, ExpressionType expressionType, double width, double height) {
        this.fcc = fcc;
        this.width = width;
        this.height = height;

        build(expressionType);
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
            fccPropositionLabel.setMaxWidth(width);
            fccPropositionLabel.setMaxHeight(height);
            fccPropositionLabel.setWrapText(true);
            Sign.StyleFccProposition(fccPropositionLabel);
            getChildren().setAll(fccPropositionLabel);
        }
    }

    private void changeExpressionType(ExpressionType notationExpressionType) {
        this.notationExpressionType = notationExpressionType;
        build(notationExpressionType);
    }

}
