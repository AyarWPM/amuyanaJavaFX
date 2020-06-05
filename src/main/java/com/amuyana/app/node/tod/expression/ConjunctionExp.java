package com.amuyana.app.node.tod.expression;

import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.Element;
import com.amuyana.app.node.tod.expression.Expression;
import javafx.scene.Node;

// Not for FCC
public class ConjunctionExp extends Expression {
    Dynamism dynamism;

    public ConjunctionExp(Dynamism dynamism) {
        this.dynamism = dynamism;
        Element element = dataInterface.getElement(dynamism.getFcc(),0);
        Element antiElement = dataInterface.getElement(dynamism.getFcc(),1);

        switch (dynamism.getOrientation()) {
            case 0:{
                ElementExp elementExp = new ElementExp(element, Sign.Value.ACTUAL);
                ElementExp antiElementExp = new ElementExp(antiElement, Sign.Value.POTENTIAL);
                this.getChildren().addAll(
                        elementExp,
                        Sign.cDot(),
                        antiElementExp
                );
            }
            break;
            case 1:{
                ElementExp elementExp = new ElementExp(element, Sign.Value.POTENTIAL);
                ElementExp antiElementExp = new ElementExp(antiElement, Sign.Value.ACTUAL);
                this.getChildren().addAll(
                        antiElementExp,
                        Sign.cDot(),
                        elementExp
                );
            }break;
            case 2:{
                ElementExp elementExp = new ElementExp(element, Sign.Value.SYMMETRIC);
                ElementExp antiElementExp = new ElementExp(antiElement, Sign.Value.SYMMETRIC);
                this.getChildren().addAll(
                        elementExp,
                        Sign.cDot(),
                        antiElementExp
                );
            }break;
        }
    }

    public ConjunctionExp getExpression(int withParenthesis) {
        return this;
    }
}
