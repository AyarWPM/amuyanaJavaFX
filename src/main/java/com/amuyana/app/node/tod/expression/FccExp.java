package com.amuyana.app.node.tod.expression;

import com.amuyana.app.data.Fcc;
import javafx.scene.control.Label;

public class FccExp extends Expression {
    private Fcc fcc;
    private ExpressionType notationExpressionType;
    private Label fccPropositionLabel;

    public FccExp(Fcc fcc, ExpressionType expressionType) {
        this.fcc = fcc;
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
            fccPropositionLabel.setWrapText(true);
            fccPropositionLabel.textProperty().bind(fcc.nameProperty());
            Sign.StyleFccProposition(fccPropositionLabel);
            getChildren().setAll(fccPropositionLabel);
        }
    }

    private void changeExpressionType(ExpressionType notationExpressionType) {
        this.notationExpressionType = notationExpressionType;
        build(notationExpressionType);
    }

    public void setTheMaxWidth(double maxWidth) {
        fccPropositionLabel.setMaxWidth(maxWidth);
    }
}
