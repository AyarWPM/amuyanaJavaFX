package com.amuyana.app.node.tod.expression;

import com.amuyana.app.data.Fcc;
import javafx.scene.Node;
import javafx.scene.control.Label;

public abstract class Sign {

    public static void StyleFccProposition(Label fccPropositionLabel) {
        fccPropositionLabel.setStyle("-fx-font-size:130%; " +
                "-fx-font-style:italic;");
    }

    public static Label Inclusion() {
        Label label = new Label("(");
        label.setStyle("-fx-font-size:200%;"); //18px
        return label;
    }

    public enum Value{
        ACTUAL,POTENTIAL,SYMMETRIC
    }

/*
   ___                _              _
  / __\___  _ __  ___| |_ __ _ _ __ | |_ ___
 / /  / _ \| '_ \/ __| __/ _` | '_ \| __/ __|
/ /__| (_) | | | \__ \ || (_| | | | | |_\__ \
\____/\___/|_| |_|___/\__\__,_|_| |_|\__|___/

 */

    public static Label OpeningParenthesis() {
        Label label = new Label("(");
        label.setStyle("-fx-font-size:140%;"); //18px
        return label;
    }

    public static Label ClosingParenthesis() {
        Label label = new Label(")");
        label.setStyle("-fx-font-size:140%;");
        return label;
    }

    public static Label cDot() {
        Label label = new Label("\u00B7");
        label.setStyle("-fx-font-size:110%;");
        return label;
    }

    public static Label implies() {
        Label label = new Label("\u2283");
        label.setStyle("-fx-font-size:130%;" +
                "-fx-padding:0 2 0 0;");
        return label;
    }

    private static Label indexLabel(String value) {
        Label styledLabel = new Label(value);
        styledLabel.setStyle("-fx-translate-y:6;" +
                "-fx-padding: 0 2 0 1;" +
                "-fx-font-size: 100%;");
        return styledLabel;
    }



    static Label actualIndex() {
        return indexLabel("A");
    }
    static Label potentialIndex() {
        return indexLabel("P");
    }
    static Label thirdIndex() {
        return indexLabel("T");
    }

    public static Label actualValue() {
        Label label = new Label("A");
        label.setStyle("-fx-font-size: 120%;");
        return label;
    }
    public static Label potentialValue() {
        Label label = new Label("P");
        label.setStyle("-fx-font-size: 120%;");
        return label;
    }
    public static Label thirdValue() {
        Label label = new Label("T");
        label.setStyle("-fx-font-size: 120%;");
        return label;
    }


}
