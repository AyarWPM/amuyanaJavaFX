package com.amuyana.app.controllers;

import com.amuyana.app.data.*;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.Message;
import com.amuyana.app.node.content.FccEditorTab;
import com.amuyana.app.node.tod.expression.Expression;
import com.amuyana.app.node.tod.expression.ElementExp;
import com.amuyana.app.node.tod.expression.FccExp;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class FccEditorController implements Initializable {
    private TodController todController;
    private DataInterface dataInterface;
    private Fcc fcc;

    @FXML private Button closeButton;
    @FXML private Button saveAndCloseButton;
    @FXML private Button editOrSaveButton;

    @FXML private VBox fccExpressionVBox;
    @FXML private Label idFccLabel;
    @FXML private TextField nameFccTextField;
    @FXML private Label nameFccLabel;
    @FXML private TextArea descriptionFccTextArea;
    @FXML private Text descriptionText;

    @FXML private TextField elementTextField;
    @FXML private HBox elementHBox;
    @FXML private HBox antiElementHBox;
    @FXML private TextField antiElementTextField;
    @FXML private CheckBox defaultSymbolCheckBox;

    @FXML private Label positivePropositionLabel;
    @FXML private TextField positivePropositionTextField;
    @FXML private TextArea positiveDescriptionTextArea;
    @FXML private Text positiveDescriptionText;

    @FXML private Label negativePropositionLabel;
    @FXML private TextField negativePropositionTextField;
    @FXML private TextArea negativeDescriptionTextArea;
    @FXML private Text negativeDescriptionText;

    @FXML private Label symmetricPropositionLabel;
    @FXML private TextField symmetricPropositionTextField;
    @FXML private TextArea symmetricDescriptionTextArea;
    @FXML private Text symmetricDescriptionText;

    private BooleanProperty editMode;
    private FccEditorTab fccEditorTab;

    private ElementExp elementExpression;
    private ElementExp antiElementExpression;
    private FccExp fccExpression;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editMode = new SimpleBooleanProperty(true);
        //setEditMode(editMode.getValue());
        this.dataInterface = NodeHandler.getDataInterface();
    }

    private void manageEvents() {
        defaultSymbolCheckBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            Element element = dataInterface.getElement(fcc,0);
            Element antiElement = dataInterface.getElement(fcc,1);
            if (observableValue.getValue()) {
                antiElementTextField.textProperty().unbindBidirectional(antiElement.symbolProperty());
                antiElementTextField.textProperty().bind(elementTextField.textProperty());
                antiElement.symbolProperty().bind(element.symbolProperty());

            } else {
                antiElementTextField.textProperty().unbind();
                antiElementTextField.textProperty().bindBidirectional(antiElement.symbolProperty());
                antiElement.symbolProperty().unbind();
            }
        });
    }

    public void setInterfaces(TodController todController) {
        this.todController = todController;
    }

    public void setValues(FccEditorTab fccEditorTab, Fcc fcc) {
        this.fccEditorTab = fccEditorTab;
        this.fcc = fcc;
// Initialize

        this.elementExpression = new ElementExp(dataInterface.getElement(fcc,0));
        this.antiElementExpression = new ElementExp(dataInterface.getElement(fcc,1));
        this.fccExpression = new FccExp(fcc, Expression.ExpressionType.ALGEBRA, 100, 50);

// Add to children
        this.elementHBox.getChildren().setAll(this.elementExpression);
        this.antiElementHBox.getChildren().setAll(this.antiElementExpression);
        this.fccExpressionVBox.getChildren().setAll(this.fccExpression);

        bind();
    }

    @FXML
    private void useDefaultSymbol() {
        //bindElements();

    }

    // editOrSaveButton activated
    public void setEditMode(boolean editMode) {
        // i have to save it with unselected settings
        this.editMode.setValue(editMode);
        if (isEditMode()) {
            // Buttons
            editOrSaveButton.setText("Save");
            // Fcc
            descriptionFccTextArea.setText(fcc.getDescription());
            //antiElementTextField.setText(dataInterface.getElement(fcc,1).getSymbol();
            // Dynamisms
            positiveDescriptionTextArea.setText(dataInterface.getDynamism(fcc, 0).getDescription());
            negativeDescriptionTextArea.setText(dataInterface.getDynamism(fcc, 1).getDescription());
            symmetricDescriptionTextArea.setText(dataInterface.getDynamism(fcc, 2).getDescription());

            positivePropositionTextField.setText(dataInterface.getDynamism(fcc, 0).getProposition());
            negativePropositionTextField.setText(dataInterface.getDynamism(fcc, 1).getProposition());
            symmetricPropositionTextField.setText(dataInterface.getDynamism(fcc, 2).getProposition());
        }
        if(!isEditMode()) {
            // SAVING
            editOrSaveButton.setText("Edit");
            //fcc.setName(nameFccTextField.getText());
            //fcc.setDescription(descriptionFccTextArea.getText());

            dataInterface.getDynamism(fcc,0).setProposition(positivePropositionTextField.getText());
            dataInterface.getDynamism(fcc,1).setProposition(negativePropositionTextField.getText());
            dataInterface.getDynamism(fcc,2).setProposition(symmetricPropositionTextField.getText());

            dataInterface.update(fcc);

            // Unbind!
            Element element = dataInterface.getElement(fcc,0);
            Element antiElement = dataInterface.getElement(fcc,1);
            antiElementTextField.textProperty().unbindBidirectional(element.symbolProperty());
            antiElementTextField.textProperty().unbindBidirectional(elementTextField.textProperty());
            antiElementTextField.textProperty().unbindBidirectional(antiElement.symbolProperty());

            // update menus?
            todController.getTree().updateFruitsMenus();
            // Redraw the tree to make ties be adjusted again
            //todController.showTree();
            //todController.showTree();
            //todController.getTree().update();
        }
    }

    private void bind() {
        Element element = dataInterface.getElement(fcc,0);
        Element antiElement = dataInterface.getElement(fcc,1);
        Dynamism dynamism0 = dataInterface.getDynamism(fcc,0);
        Dynamism dynamism1 = dataInterface.getDynamism(fcc,1);
        Dynamism dynamism2 = dataInterface.getDynamism(fcc,2);

        // Button, checkbox
        saveAndCloseButton.disableProperty().bind(editMode.not());
        defaultSymbolCheckBox.managedProperty().bind(editMode);
        defaultSymbolCheckBox.visibleProperty().bind(editMode);

        // anti-element negation of element
        antiElementTextField.editableProperty().bind(defaultSymbolCheckBox.selectedProperty().not());
        antiElementTextField.textProperty().bindBidirectional(antiElement.symbolProperty());
        // Element
        elementTextField.textProperty().bindBidirectional(element.symbolProperty());
        element.symbolProperty().bindBidirectional(elementTextField.textProperty());

// ManagedProperty : textFields and textAreas
        nameFccTextField.managedProperty().bind(editMode);
        descriptionFccTextArea.managedProperty().bind(editMode);
        elementTextField.managedProperty().bind(editMode);
        antiElementTextField.managedProperty().bind(editMode);

        positiveDescriptionTextArea.managedProperty().bind(editMode);
        negativeDescriptionTextArea.managedProperty().bind(editMode);
        symmetricDescriptionTextArea.managedProperty().bind(editMode);
        positivePropositionTextField.managedProperty().setValue(editMode.getValue());
        negativePropositionTextField.managedProperty().setValue(editMode.getValue());
        symmetricPropositionTextField.managedProperty().setValue(editMode.getValue());

// ManagedProperty : label, Texts and Expressions
        nameFccLabel.managedProperty().bind(editMode.not());
        descriptionText.managedProperty().bind(editMode.not());
        //elementExpression.managedProperty().bind(editMode.not());
        elementHBox.managedProperty().bind(editMode.not());
        //antiElementExpression.managedProperty().bind(editMode.not());
        antiElementHBox.managedProperty().bind(editMode.not());

        positiveDescriptionText.managedProperty().bind(editMode.not());
        negativeDescriptionText.managedProperty().bind(editMode.not());
        symmetricDescriptionText.managedProperty().bind(editMode.not());
        positivePropositionLabel.managedProperty().bind(editMode.not());
        negativePropositionLabel.managedProperty().bind(editMode.not());
        symmetricPropositionLabel.managedProperty().bind(editMode.not());

// TextProperty :  textfields and TextAreas

        nameFccTextField.textProperty().bindBidirectional(fcc.nameProperty());
        descriptionFccTextArea.textProperty().bindBidirectional(fcc.descriptionProperty());
        //elementTextField.textProperty().bindBidirectional(element.symbolProperty());
        //antiElementTextField.textProperty().bindBidirectional(element1.symbolProperty());

        positiveDescriptionTextArea.textProperty().bindBidirectional(dynamism0.descriptionProperty());
        negativeDescriptionTextArea.textProperty().bindBidirectional(dynamism1.descriptionProperty());
        symmetricDescriptionTextArea.textProperty().bindBidirectional(dynamism2.descriptionProperty());
        positivePropositionTextField.textProperty().bindBidirectional(dynamism0.propositionProperty());
        negativePropositionTextField.textProperty().bindBidirectional(dynamism1.propositionProperty());
        symmetricPropositionTextField.textProperty().bindBidirectional(dynamism2.propositionProperty());

// TextProperty : Labels, Texts (Expressions manage their own text properties)
        idFccLabel.textProperty().bind(fcc.idFccProperty().asString());
        nameFccLabel.textProperty().bind(fcc.nameProperty());
        descriptionText.textProperty().bind(fcc.descriptionProperty());

        positiveDescriptionText.textProperty().bind(dynamism0.descriptionProperty());
        negativeDescriptionText.textProperty().bind(dynamism1.descriptionProperty());
        symmetricDescriptionText.textProperty().bind(dynamism2 .descriptionProperty());
        positivePropositionLabel.textProperty().bind(dynamism0.propositionProperty());
        negativePropositionLabel.textProperty().bind(dynamism1.propositionProperty());
        symmetricPropositionLabel.textProperty().bind(dynamism2.propositionProperty());

// VisibleProperty : TextFields and TextAreas
        nameFccTextField.visibleProperty().bind(editMode);
        descriptionFccTextArea.visibleProperty().bind(editMode);
        elementTextField.visibleProperty().bind(editMode);
        antiElementTextField.visibleProperty().bind(editMode);

        positiveDescriptionTextArea.visibleProperty().bind(editMode);
        negativeDescriptionTextArea.visibleProperty().bind(editMode);
        symmetricDescriptionTextArea.visibleProperty().bind(editMode);
        positivePropositionTextField.visibleProperty().bind(editMode);
        negativePropositionTextField.visibleProperty().bind(editMode);
        symmetricPropositionTextField.visibleProperty().bind(editMode);

        // VisibleProperty : Labels, Texts and expression containers
        nameFccLabel.visibleProperty().bind(editMode.not());
        descriptionText.visibleProperty().bind(editMode.not());
        elementHBox.visibleProperty().bind(editMode.not());
        antiElementHBox.visibleProperty().bind(editMode.not());

        positiveDescriptionText.visibleProperty().bind(editMode.not());
        negativeDescriptionText.visibleProperty().bind(editMode.not());
        symmetricDescriptionText.visibleProperty().bind(editMode.not());
        positivePropositionLabel.visibleProperty().bind(editMode.not());
        negativePropositionLabel.visibleProperty().bind(editMode.not());
        symmetricPropositionLabel.visibleProperty().bind(editMode.not());
    }

    @FXML
    public void close() {
        if (editMode.get()) {
            ButtonType saveAndExitButtonType = new ButtonType("Save and close");
            ButtonType exitButtonType = new ButtonType("Don't save and close");
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> result = Message.confirmClosing(saveAndExitButtonType, exitButtonType, cancelButtonType);
            if (result.isPresent()) {
                if (result.get().equals(saveAndExitButtonType)) {
                    saveAndClose();
                } else if (result.get().equals(exitButtonType)) {
                    this.todController.closeTab(this.fccEditorTab);
                } /*else if (result.get().equals(cancelButtonType)) {
                    // do nothing, look at the sky
                }*/
            }
        } else {
            // Close
            todController.closeTab(fccEditorTab);
        }
    }

    @FXML
    private void saveAndClose() {
        editOrSave();
        todController.closeTab(fccEditorTab);
    }

    @FXML
    private void editOrSave() {
        setEditMode(!editMode.get());
    }

    public void fillData() {
        elementTextField.setText(dataInterface.getElement(fcc,0).getSymbol());
        antiElementTextField.setText(dataInterface.getElement(fcc,1).getSymbol());
        manageEvents();
        defaultSymbolCheckBox.setSelected(true);
    }

    private boolean isEditMode() {
        return editMode.get();
    }

    public BooleanProperty editModeProperty() {
        return editMode;
    }

    public Fcc getFcc() {
        return this.fcc;
    }
}