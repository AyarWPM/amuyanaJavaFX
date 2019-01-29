package com.amuyana.app.controllers;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.CClass;
import com.amuyana.app.node.MainBorderPane;
import com.amuyana.app.node.Message;
import com.amuyana.app.node.NodeInterface;
import com.amuyana.app.node.content.CClassTab;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ConjunctionController implements Initializable {

    @FXML private Button saveAndCloseButton;
    @FXML private Button editOrSaveButton;

    @FXML VBox fccExpressionVBox;
    @FXML VBox positiveGeneralExpressionVBox;
    @FXML VBox negativeGeneralExpressionVBox;
    @FXML VBox symmetricGeneralExpressionVBox;

    @FXML VBox positiveParticularExpressionVBox;
    @FXML VBox negativeParticularExpressionVBox;
    @FXML VBox symmetricParticularExpressionVBox;

    @FXML Label cClassNameLabel;
    @FXML TextField cClassNameTextField;

    @FXML CheckBox positiveGeneralCheckBox;
    @FXML CheckBox negativeGeneralCheckBox;
    @FXML CheckBox symmetricGeneralCheckBox;

    @FXML CheckBox positiveParticularCheckBox;
    @FXML CheckBox negativeParticularCheckBox;
    @FXML CheckBox symmetricParticularCheckBox;

    @FXML ComboBox<Fcc> fccComboBox;

    @FXML private Button addFccButton;
    @FXML private Button RemoveFccButton;

    @FXML private ListView<Fcc> fccListView;

    private BooleanProperty editMode;

    private DataInterface dataInterface;
    private TodController todController;
    private NodeInterface nodeInterface;

    private CClass cClass;
    private CClassTab cClassTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editMode = new SimpleBooleanProperty(true);
        this.dataInterface = MainBorderPane.getDataInterface();
    }

    public void setInterfaces(TodController todController, NodeInterface nodeInterface) {
        this.todController = todController;
        this.nodeInterface = nodeInterface;
    }

    public void setValues(CClassTab cClassTab, CClass cClass) {
        this.cClassTab = cClassTab;
        this.cClass = cClass;

/*        // Initialize
        this.fccExpression = new FccExp(fcc, Expression.ExpressionType.ALGEBRA);

// Add to children
        this.elementHBox.getChildren().setAll(this.elementExpression);
        this.antiElementHBox.getChildren().setAll(this.antiElementExpression);
        this.fccExpressionVBox.getChildren().setAll(this.fccExpression);*/

        bind();
    }
    private void bind() {

    }

    public CClass getcClass() {
        return cClass;
    }

    public void setcClass(CClass cClass) {
        this.cClass = cClass;
    }

    @FXML
    public void close() {
        if (editMode.get()) {
            ButtonType saveAndExitButtonType = new ButtonType("Save and exit");
            ButtonType exitButtonType = new ButtonType("Don't save and exit");
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = Message.confirmClosing(saveAndExitButtonType, exitButtonType, cancelButtonType);

            if (result.get().equals(saveAndExitButtonType)) {
                saveAndClose();
            } else if (result.get().equals(exitButtonType)) {
                this.todController.closeTab(this.cClassTab);
            } else if (result.get().equals(cancelButtonType)) {
                // do nothing, look at the sky
            }
        } else {
            // Close
            todController.closeTab(cClassTab);
        }
    }

    @FXML
    private void saveAndClose() {
        editOrSave();
        todController.closeTab(cClassTab);
    }

    @FXML
    private void editOrSave() {
        setEditMode(!editMode.get());
    }

    @FXML
    private void delete() {

    }

    @FXML
    private void addFcc() {

    }

    @FXML
    private void removeFcc() {

    }

    public void setEditMode(boolean editMode) {

        // i have to save it with unselected settings
        this.editMode.setValue(editMode);
        if (editMode) {

            // Buttons
            editOrSaveButton.setText("Save");

        }
        if(!editMode) {

            // SAVING
            editOrSaveButton.setText("Edit");
            //fcc.setName(nameFccTextField.getText());
            //fcc.setDescription(descriptionFccTextArea.getText());

            /*dataInterface.getDynamism(fcc,0).setProposition(positivePropositionTextField.getText());
            dataInterface.getDynamism(fcc,1).setProposition(negativePropositionTextField.getText());
            dataInterface.getDynamism(fcc,2).setProposition(symmetricPropositionTextField.getText());

            dataInterface.update(fcc);

            // Unbind!
            Element element = dataInterface.getElement(fcc,0);
            Element antiElement = dataInterface.getElement(fcc,1);
            antiElementTextField.textProperty().unbindBidirectional(element.symbolProperty());
            antiElementTextField.textProperty().unbindBidirectional(elementTextField.textProperty());
            antiElementTextField.textProperty().unbindBidirectional(antiElement.symbolProperty());*/

        }
    }
}
