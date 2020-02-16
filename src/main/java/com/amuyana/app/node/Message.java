package com.amuyana.app.node;

import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.CClass;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

import java.util.Optional;

public abstract class Message {

    public static boolean testConnection(boolean result) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.setTitle("Connection to MYSQL database");
        alert.setHeaderText("Result");
        if (result) {
            alert.setContentText("The connection was successful");
        } else {
            alert.setContentText("The connection was unsuccessful. Possible problems are wrong data or no connection to the Internet.");
        }
        alert.showAndWait();
        return result;
    }

    public static void completeAllFieldsAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.setTitle("Unable to proceed");
        alert.setHeaderText(null);
        alert.setContentText("Please complete all fields.");
        alert.showAndWait();
    }

    public static void createdLogicSystemAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.setTitle("Logic System");
        alert.setHeaderText(null);
        alert.setContentText("A new Logic System has been created.");
        alert.showAndWait();
    }

    public static void updatedLogicSystemAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.setTitle("Logic System");
        alert.setHeaderText(null);
        alert.setContentText("The Logic System has been updated.");
        alert.showAndWait();
    }

    public static void loadedLogicSystem() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.setTitle("Logic System");
        alert.setHeaderText(null);
        alert.setContentText("The Logic System has been loaded.");
        alert.showAndWait();
    }

    public static Alert confirmDeletionLogicSystem() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.setTitle("Confirm deletion of the Logic system");
        alert.setHeaderText("Are you sure you want to delete the Logic System?");
        //alert.setContentText("All the associated FCCs will be lost unless they are imported in another logic system.");
        return alert;
    }

    public static Alert confirmDeletionTod() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.setTitle("Confirm deletion of the Table of deductions");
        alert.setHeaderText("Are you sure you want to delete the Table of deductions? ");
        alert.setContentText("This operation is irreversible. " +
                "However the Fundamental conjunctions of contradiction (FCCs) or notions, conjunctions and classes are not being deleted, " +
                "only the arrangement (i.e. the Table) is deleted. You can create a new Table of deduction with the notions that you already created.");
        return alert;
    }



    public static void alreadyEditingLogicSystem() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setWidth(400);
        alert.setHeight(250);
        alert.setResizable(true);
        alert.setTitle("Logic System");
        alert.setHeaderText(null);
        alert.setContentText("The Logic System is already being edited.");
        alert.showAndWait();
    }

    public static Optional<ButtonType> confirmClosing(ButtonType saveAndExitButtonType, ButtonType exitButtonType, ButtonType cancelButtonType) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.setTitle("Closing tab procedure interrupted");
        alert.setHeaderText("Would you like to save changes?");
        alert.setContentText("This operation is irreversible at the moment.");
//        alert.setContentText("This operation is irreversible. " +
//                "However the Fundamental conjunctions of contradiction (FCCs) or notions, conjunctions and classes are not being deleted, " +
//                "only the arrangement (i.e. the Table) is deleted. You can create a new Table of deduction with the notions that you already created.");

        alert.getButtonTypes().setAll(saveAndExitButtonType,exitButtonType,cancelButtonType);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    /*public static Optional<ButtonType> confirmRemoval(CClass cClass, Fcc fcc, ButtonType option1, ButtonType option2, ButtonType option3) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setWidth(400);
        alert.setHeight(250);
        alert.setResizable(true);
        alert.setTitle("Save changes");
        alert.setHeaderText("The FCC is part of a Class, what would you like to do?");
        alert.setContentText("Your options are:\n" +
                "  Option 1. Maintain the Class\n" +
                "  Option 2. Dissolve the Class\n" +
                "  Cancel the removal of the FCC from the Table of deductions");
//        alert.setContentText("This operation is irreversible. " +
//                "However the Fundamental conjunctions of contradiction (FCCs) or notions, conjunctions and classes are not being deleted, " +
//                "only the arrangement (i.e. the Table) is deleted. You can create a new Table of deduction with the notions that you already created.");

        alert.getButtonTypes().setAll(option1,option2,option3);
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }*/
    public static Alert confirmRemovalFcc() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.setTitle("Confirm deletion of the Logic system");
        alert.setHeaderText("Are you sure you want to remove this FCC from the Table of deductions?");
        alert.setContentText("This operation is irreversible.\n" +
                "However the Fundamental conjunctions of contradiction (FCC) or notion will exist in the Memory, " +
                "to delete it permanently select the option \"Fundamental Conjunctions of Contradiction\" in the menu" +
                "\"Logic System\"");
        return alert;
    }
}
