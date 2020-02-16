package com.amuyana.app.node.tod.expression;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.node.NodeHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

/*public enum FormulaType {
    // #one element
    ELEMENT,

    // #one FCC
    FCC,

    // Fundamental disjunction of contradiction of conjunctionExpressions,
    // Positive fundamental functional of ocntradiction of FCC, ...

    // #one Dynamism
    CONJUNCTIONS, CONJUNCTION0, CONJUNCTION1, CONJUNCTION2,

    // Fundamental disjunction of contradiction of implicationExpressions, ...
    IMPLICATIONS, IMPLICATION0, IMPLICATION1, IMPLICATION2,

    // others in mind
    // SPACE_TIME
    FP_STRUCTURAL, FP_FUNCTIONAL, FP_DYNAMIC,
    PA_STRUCTURAL, PA_FUNCTIONAL, PA_DYNAMIC_SHORT, PA_DYNAMIC_FULL,

    // #two Dynamisms
    INCLUSION,

    // #two or more Dynamisms
    CONJUNCTION, SYLLOGISM, CLASS
}*/
public class Expression extends HBox {
    //Expressions lists
    private static ObservableList<Expression> expressions;

    // Styles

    // Others

    public static final DataInterface dataInterface = NodeHandler.getDataInterface();

    private String algebraCssStyle; // todo associate a css style for all Control (themes)

    public static void initializeLists() {
        expressions = FXCollections.observableArrayList();
    }

    public enum ExpressionType {ALGEBRA, PROPOSITION}

    public Expression() {
        expressions.add(this);
        setMinWidth(this.getPrefWidth());
        setMinWidth(Region.USE_PREF_SIZE);
        setAlignment(Pos.BOTTOM_LEFT);

        //setStyle("-fx-font-family:monospace; -fx-alignment:bottom-left;");
    }

    void setExpressionHolderStyle(Expression expressionHolder) {
        expressionHolder.setId("Expression");
    }

    void setExpressionContentStyle(Expression... expressionContents) {
        for (Expression expression : expressionContents) {
            expression.setId("SubExpression");
        }
    }

/*
         __  ___      ___
        / _\( _ )    / _ \
        \ \ / _ \/\ / /_\/
        _\ \ (_>  </ /_\\
        \__/\___/\/\____/
*/

    static ObservableList<ElementExp> getElementExps() {
        ObservableList<ElementExp> elementExps = FXCollections.observableArrayList();
        for (Expression expression : expressions) {
            if (expression.getClass().equals(ElementExp.class)) {
                elementExps.add((ElementExp)expression);
            }
        }
        return elementExps;
    }

    static ObservableList<FccExp> getFccExps() {
        ObservableList<FccExp> fccExps = FXCollections.observableArrayList();
        for (Expression expression : expressions) {
            if (expression.getClass().equals(FccExp.class)) {
                fccExps.add((FccExp) expression);
            }
        }
        return fccExps;
    }

}
