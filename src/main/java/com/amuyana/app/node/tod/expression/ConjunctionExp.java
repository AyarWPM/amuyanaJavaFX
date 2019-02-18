package com.amuyana.app.node.tod.expression;

import com.amuyana.app.data.Dynamism;
import com.amuyana.app.node.tod.expression.Expression;

// Not for FCC
public class ConjunctionExp extends Expression {
    Dynamism dynamism;

    private void buildConjunction0() {

        this.getChildren().clear();
        this.getChildren().addAll(
                // Move to ConjunctionLabel
                Sign.OpeningParenthesis(),
                //Sign.element(dataInterface.getElement(fcc,0), Sign.Value.ACTUAL),
                // Assuming only the first dynamism of the list is pertinent
//!                Sign.element(dataInterface.getElement(dynamisms.get(0),0)),
                Sign.actualIndex(),
                Sign.cDot(),
                //Sign.element(dataInterface.getElement(fcc,1), Sign.Value.POTENTIAL),
//!                Sign.element(dataInterface.getElement(dynamisms.get(0),1)),
                Sign.potentialIndex(),
                Sign.ClosingParenthesis()
        );
    }

}
