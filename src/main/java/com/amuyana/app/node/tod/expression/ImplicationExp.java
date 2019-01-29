package com.amuyana.app.node.tod.expression;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.node.MainBorderPane;

public class ImplicationExp extends Expression {
    private static DataInterface dataInterface = MainBorderPane.getDataInterface();
    Dynamism dynamism;

    public ImplicationExp(Dynamism dynamism) {
        this.dynamism = dynamism;

        ElementExp elementExp = null;
        ElementExp antiElementExp = null;

        switch (dynamism.getOrientation()) {
            case 0: {
                elementExp = new ElementExp(dataInterface.getElement(dynamism,0), Sign.Value.ACTUAL);
                antiElementExp = new ElementExp(dataInterface.getElement(dynamism,1), Sign.Value.POTENTIAL);

                this.getChildren().addAll(
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

                this.getChildren().addAll(
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

                this.getChildren().addAll(
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
}
