package extras;

import javafx.scene.control.Alert;

public abstract class AmuyanaAlert {

    public static void completeAllFieldsAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Unable to save Logic System");
        alert.setHeaderText(null);
        alert.setContentText("Please complete all fields.");
        alert.showAndWait();
    }

    public static void createdLogicSystemAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logic System created");
        alert.setHeaderText(null);
        alert.setContentText("The Logic System has been created.");
        alert.showAndWait();
    }

    public static void updatedLogicSystemAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logic System updated");
        alert.setHeaderText(null);
        alert.setContentText("The Logic System has been updated.");
        alert.showAndWait();
    }

    public static void loadedLogicSystemAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logic System loaded");
        alert.setHeaderText(null);
        alert.setContentText("The Logic System has been loaded.");
        alert.showAndWait();
    }

    public static Alert confirmDeletionLogicSystem() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm deletion of logic system");
        alert.setHeaderText("Are you sure you want to delete this Logic System?");
        alert.setContentText("All the associated FCCs will be lost unless they are imported in another logic system.");
        return alert;
    }

    public static void alreadyEditingLogicSystem() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Logic System");
        alert.setHeaderText(null);
        alert.setContentText("The Logic System is already being edited.");
        alert.showAndWait();
    }

}
