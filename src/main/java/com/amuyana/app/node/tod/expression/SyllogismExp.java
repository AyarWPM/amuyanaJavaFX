package com.amuyana.app.node.tod.expression;

import com.amuyana.app.data.Syllogism;
import com.amuyana.app.data.tod.Inclusion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SyllogismExp extends Expression{

    /**
     * Constructor for a Syllogism algebraic expression
     * @param listInclusions List of inclusions that form a syllogism. The inclusions are ordered from right to left
     *                       as they appear in the table of deductions, which is a copy list is created with reverse order
     */
    public SyllogismExp(List<Inclusion> listInclusions) {
        setPadding(new Insets(10));
        // We have to invert the order of the list because we have registered inclusions in a right-to-left sens,
        // as the sens the Table of deductions is read
        List<Inclusion> reverseListInclusions = new ArrayList<>(listInclusions);
        Collections.reverse(reverseListInclusions);
        if (reverseListInclusions.size() == 1) {
            ConjunctionExp particularConjunctionExp = new ConjunctionExp(reverseListInclusions.get(0).getParticular());
            ConjunctionExp generalConjunctionExp = new ConjunctionExp(reverseListInclusions.get(0).getGeneral());
            getChildren().addAll(particularConjunctionExp.getExpression(0),Sign.Inclusion(),generalConjunctionExp.getExpression(0));
        } else if (reverseListInclusions.size() > 1) {
            for (Inclusion inclusion : reverseListInclusions) {
                // If it is the first inclusion
                if (reverseListInclusions.indexOf(inclusion) == 0) {
                    ConjunctionExp particularConjunctionExp = new ConjunctionExp(inclusion.getParticular());
                    ConjunctionExp generalConjunctionExp = new ConjunctionExp(inclusion.getGeneral());
                    getChildren().addAll(particularConjunctionExp.getExpression(0),Sign.Inclusion(),generalConjunctionExp.getExpression(0));

                } else if (reverseListInclusions.indexOf(inclusion) > 0) {
                        ConjunctionExp generalConjunctionExp = new ConjunctionExp(inclusion.getGeneral());
                        getChildren().addAll(Sign.Inclusion(),generalConjunctionExp.getExpression(0));

                }
            }
        }
    }

    public SyllogismExp getExpression() {
        return this;
    }
}
