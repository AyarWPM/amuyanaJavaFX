package com.amuyana.app.controllers;

import com.amuyana.app.data.*;
import com.amuyana.app.node.NodeInterface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FccsController implements Initializable {
    private NodeInterface nodeInterface;
    private DataInterface dataInterface;

    private Fcc fcc;

    @FXML private Button closeButton; // new
    @FXML private Button saveAndCloseButton;
    @FXML private Button editOrSaveButton;
    @FXML private Button deleteButton;

    @FXML private Button bnDuplicateFcc; // to delete
    @FXML private Button bnNewFcc; // to delete

    @FXML private Label idFccLabel;
    @FXML private TextField nameFccTextField;
    @FXML private Label nameFccLabel; // new
    @FXML private TextArea descriptionFccTextArea; // It was html viewer
    @FXML private Label descriptionLabel; // new

    @FXML private TextField elementTextField;
    @FXML private Label elementLabel; // new
    @FXML private TextField antiElementTextField;
    @FXML private Label antiElementLabel;
    @FXML private CheckBox defaultSymbolCheckBox;

    @FXML private ListView<LogicSystem> logicSystemListView;
    @FXML private ComboBox<LogicSystem> logicSystemComboBox;
    @FXML private Button addButton;
    @FXML private Button removeButton;

    @FXML private VBox positiveFormulaVBox;
    @FXML private Label positivePropositionLabel;
    @FXML private TextField positivePropositionTextField;
    @FXML private TextArea positiveDescriptionTextArea;
    @FXML private Label positiveDescriptionLabel; // new

    @FXML private VBox negativeFormulaVBox;
    @FXML private Label negativePropositionLabel;
    @FXML private TextField negativePropositionTextField;
    @FXML private TextArea negativeDescriptionTextArea;
    @FXML private Label negativeDescriptionLabel; // new

    @FXML private VBox symmetricFormulaVBox;
    @FXML private Label symmetricPropositionLabel;
    @FXML private TextField symmetricPropositionTextField;
    @FXML private TextArea symmetricDescriptionTextArea;
    @FXML private Label symmetricDescriptionLabel; // new

    @FXML private TableView tevwFcc; // to delete
    @FXML private TableColumn<Fcc,String> tecnFccLabel; // to delete
    @FXML private TableColumn<Fcc,String> tecnFccDescription; // to delete
    @FXML private HBox hxAntiElementSymbol; // idk where this is, to delete

    private ObservableList<Fcc> listFcc;
    private ObservableList<FccHasLogicSystem> listFccHasLogicSystem;

    private ObservableList<Element> listElement;
    private ObservableList<Dynamism> listDynamisms;
    private ObservableList<User> listUser;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        manageEvents();
    }

    @FXML
    private void close() {

    }

    @FXML
    private void saveAndClose() {

    }

    @FXML
    private void editOrSave() {

    }

    @FXML
    private void delete() {

    }



    public void fillData() {
        tevwFcc.setItems(listFcc);

        tecnFccLabel.setCellValueFactory(
            new PropertyValueFactory<Fcc,String>("label"));
        tecnFccDescription.setCellValueFactory(
            new PropertyValueFactory<Fcc,String>("description"));


    }



    public void manageEvents(){
        // TABLE VIEW WITH FCC
        tevwFcc.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Fcc>() {
                @Override
                public void changed(ObservableValue<? extends Fcc> observable,
                    Fcc oldValue, Fcc newValue) {

                    Fcc selectedFcc = (Fcc)tevwFcc.getSelectionModel().getSelectedItem();
                    DataConnection dataConnection = dataInterface.getDataConnection();
                    dataConnection.connect();
                    if(newValue != null){
                        saveAndCloseButton.setDisable(true);
                        editOrSaveButton.setDisable(false);
                        deleteButton.setDisable(false);
                        bnDuplicateFcc.setDisable(false);

                        // FCC section
                        idFccLabel.setText(String.valueOf(newValue.getIdFcc()));
                        nameFccTextField.setText(newValue.getName());
                        descriptionFccTextArea.setText(newValue.getDescription());

                        // FCC HAS LOGIC SYSTEM section
                        ObservableList<LogicSystem> ls1 = FXCollections.observableArrayList();
                        ObservableList<LogicSystem> ls2 = FXCollections.observableArrayList();

                        ls2.addAll(dataInterface.getListLogicSystem());

                        for(FccHasLogicSystem fhls : dataInterface.getListFccHasLogicSystem()){
                            if(fhls.getFcc().equals(newValue)){
                                ls2.remove(fhls.getLogicSystem());
                                ls1.add(fhls.getLogicSystem());
                            }
                        }

                        logicSystemComboBox.setItems(ls1);
                        logicSystemComboBox.setItems(ls2);
                        logicSystemComboBox.setDisable(false);

                        // ELEMENTS section
                        Element e = dataInterface.getElement(selectedFcc,0);
                        Element ae= dataInterface.getElement(selectedFcc,0);

                        elementTextField.setText(e.getSymbol());

                        if(e.getSymbol().equals(ae.getSymbol())){

                            hxAntiElementSymbol.getChildren().clear();
                            antiElementLabel.setText(null);
                            hxAntiElementSymbol.getChildren().add(antiElementLabel);
                            antiElementLabel.setText(ae.getSymbol());
                            defaultSymbolCheckBox.setSelected(true);
                        } else if(!e.getSymbol().equals(ae.getSymbol())){
                            hxAntiElementSymbol.getChildren().clear();
                            antiElementTextField.setText(null);
                            hxAntiElementSymbol.getChildren().add(antiElementTextField);
                            antiElementTextField.setText(ae.getSymbol());
                            defaultSymbolCheckBox.setSelected(false);
                        }

                        // DYNAMISMS section (previously known as CONJUNCTIONS)

                        Dynamism c0 = dataInterface.getDynamism(selectedFcc, 0);
                        Dynamism c1 = dataInterface.getDynamism(selectedFcc, 1);
                        Dynamism c2 = dataInterface.getDynamism(selectedFcc, 2);

                        positivePropositionLabel.setText(c0.toString());
                        positivePropositionTextField.setText(c0.getProposition());
                        positiveDescriptionTextArea.setText(c0.getDescription());

                        negativePropositionLabel.setText(c0.toString());
                        negativePropositionTextField.setText(c1.getProposition());
                        negativeDescriptionTextArea.setText(c1.getDescription());

                        symmetricPropositionLabel.setText(c2.toString());
                        symmetricPropositionTextField.setText(c2.getProposition());
                        symmetricDescriptionTextArea.setText(c2.getDescription());

                    } else if (newValue==null){
                        newFcc();
                    }

                }
            }
        );

        //LIST VIEW WITH LOGIC SYSTEMS
        logicSystemListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LogicSystem>() {
            @Override
            public void changed(ObservableValue<? extends LogicSystem> observable, LogicSystem oldValue, LogicSystem newValue) {
                if(newValue!=null){
                    removeButton.setDisable(false);
                } else if (newValue==null){
                    removeButton.setDisable(true);
                }

            }
        });

        //COMBOBOX WITH LOGIC SYSTEMS
        logicSystemComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LogicSystem>(){
            @Override
            public void changed(ObservableValue<? extends LogicSystem> observable, LogicSystem oldValue, LogicSystem newValue) {
                if(newValue!=null){
                    addButton.setDisable(false);
                } else if(newValue==null){
                    addButton.setDisable(true);
                }
            }
        });

        // CHECKBOX OF ANTIELEMENT DEFAULT SYMBOL
        defaultSymbolCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true){
                    hxAntiElementSymbol.getChildren().clear();
                    hxAntiElementSymbol.getChildren().add(antiElementLabel);
                } else if (newValue==false){
                    hxAntiElementSymbol.getChildren().clear();
                    hxAntiElementSymbol.getChildren().add(antiElementTextField);
                    antiElementLabel.setText(elementTextField.getText());
                }
            }
        });

        elementTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                antiElementLabel.setText(newValue);

            }
        });
    }

    @FXML
    public void saveFcc(){

        if(!checkFill()){
            return;
        }

        DataConnection dataConnection = new DataConnection();
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        dataConnection.connect();

        //FCC
        Fcc fcc = new Fcc(0, nameFccTextField.getText(), descriptionFccTextArea.getText());

        int resultFcc = fcc.saveData(dataConnection.getConnection());
        fcc.setIdFcc(Fcc.currentAutoIncrement);

        if (resultFcc == 1){
            listFcc.add(fcc);
        }

        // Element
        Element e0 = new Element(0, elementTextField.getText(), 0, fcc);
        Element e1 = null;
        if(defaultSymbolCheckBox.isSelected()==true) {
            e1 = new Element(0, antiElementLabel.getText(), 1, fcc);
        } else if(defaultSymbolCheckBox.isSelected()==false) {
            e1 = new Element(0, antiElementTextField.getText(), 1, fcc);
        }


        int resultE0 = e0.saveData(dataConnection.getConnection());
        e0.setIdElement(Element.currentAutoIncrement);
        int resultE1 = e1.saveData(dataConnection.getConnection());
        e1.setIdElement(Element.currentAutoIncrement);

        if (resultE0 == 1 && resultE1 == 1){
            listElement.addAll(e0,e1);
        }

        // Dynamisms
        Dynamism c0 = new Dynamism(0, 0, positivePropositionTextField.getText(), positiveDescriptionTextArea.getText(), fcc);
        int resultC0 = c0.saveData(dataConnection.getConnection());
        c0.setIdDynamism(Dynamism.currentAutoIncrement);
        Dynamism c1 = new Dynamism(0, 1, negativePropositionTextField.getText(), negativeDescriptionTextArea.getText(), fcc);
        int resultC1 = c1.saveData(dataConnection.getConnection());
        c1.setIdDynamism(Dynamism.currentAutoIncrement);
        Dynamism c2 = new Dynamism(0, 2, symmetricPropositionTextField.getText(), symmetricDescriptionTextArea.getText(), fcc);
        int resultC2 = c2.saveData(dataConnection.getConnection());
        c2.setIdDynamism(Dynamism.currentAutoIncrement);

        if(resultC0==1 && resultC1 == 1 && resultC2 == 1){
            listDynamisms.addAll(c0,c1,c2);
        }
        dataConnection.disconnect();

        tevwFcc.getSelectionModel().selectLast();

        // UPDATE LIST IN INCLUSION
        //nodeInterface.refreshDataInclusionModule();
        //nodeInterface.refreshDataClassModule();
    }

    private boolean checkFill() {
        boolean allFilled = true;

        String message="The following fields cannot be empty: \n\n";

        if(elementTextField.getText()==null){
            allFilled = false;
            message += "Symbol of Element\n";
        }

        if(antiElementTextField.getText()==null&&!defaultSymbolCheckBox.isSelected()){
            allFilled = false;
            message += "Symbol of Anti-Element\n";
        }

        if(nameFccTextField.getText()==null){
            allFilled = false;
            message += "Label of FCC\n";
        }

        if(positivePropositionTextField.getText()==null){
            allFilled = false;
            message += "Formulation of the Positive Conjunction\n";
        }

        if(negativePropositionTextField.getText()==null){
            allFilled = false;
            message += "Formulation of the Negative Conjunction\n";
        }

        if(symmetricPropositionTextField.getText()==null){
            allFilled = false;
            message += "Formulation of the Symmetric Conjunction\n";
        }

        if(allFilled == false){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setResizable(true);
            alert.setTitle("Save Logic System");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
        return allFilled;
    }

    @FXML
    public void updateFcc(){
        Fcc selectedFcc = (Fcc)tevwFcc.getSelectionModel().getSelectedItem();

        String newFccLabel = nameFccTextField.getText();
        String newFccDescription = descriptionFccTextArea.getText();

        String newESymbol = elementTextField.getText();
        String newAESymbol = null;
        // Anti element "default" checkbox has to be checked
        if(defaultSymbolCheckBox.isSelected()==true) {
            newAESymbol = antiElementLabel.getText();
        } else if(defaultSymbolCheckBox.isSelected()==false) {
            newAESymbol = antiElementTextField.getText();
        }


        String newPropDynamism0 = positivePropositionTextField.getText();
        String newPropDynamism1 = negativePropositionTextField.getText();
        String newPropDynamism2 = symmetricPropositionTextField.getText();

        String newDescDynamism0 = positiveDescriptionTextArea.getText();
        String newDescDynamism1 = negativeDescriptionTextArea.getText();
        String newDescDynamism2 = symmetricDescriptionTextArea.getText();

        int result;


        selectedFcc.setName(newFccLabel);
        selectedFcc.setDescription(newFccDescription);



        DataConnection dataConnection = dataInterface.getDataConnection();
        dataConnection.connect();

        result = selectedFcc.updateData(dataConnection.getConnection());

        if (result == 1){
            listFcc.set(listFcc.indexOf(selectedFcc), selectedFcc);
        }

        // ELEMENT
        Element e0 = dataInterface.getElement(selectedFcc,0);

        Element e1 = dataInterface.getElement(selectedFcc,0);

        int iE0=listElement.indexOf(e0);
        int iE1=listElement.indexOf(e1);

        e0.setSymbol(newESymbol);
        result = e0.updateData(dataConnection.getConnection());
        if (result == 1){
            listElement.set(iE0, e0);
        }

        e1.setSymbol(newAESymbol);
        result = e1.updateData(dataConnection.getConnection());
        if (result == 1){
            listElement.set(iE1, e1);
        }



        // DYNAMISM
        Dynamism c0 = dataInterface.getDynamism(selectedFcc, 0);
        Dynamism c1 = dataInterface.getDynamism(selectedFcc, 1);;
        Dynamism c2 = dataInterface.getDynamism(selectedFcc, 2);;
        int iC0=listDynamisms.indexOf(c0);
        int iC1=listDynamisms.indexOf(c1);
        int iC2=listDynamisms.indexOf(c2);

        c0.setProposition(newPropDynamism0);
        c0.setDescription(newDescDynamism0);
        c1.setProposition(newPropDynamism1);
        c1.setDescription(newDescDynamism1);
        c2.setProposition(newPropDynamism2);
        c2.setDescription(newDescDynamism2);

        result = c0.updateData(dataConnection.getConnection());
        if (result == 1){
            listDynamisms.set(iC0, c0);
        }

        result = c1.updateData(dataConnection.getConnection());
        if (result == 1){
            listDynamisms.set(iC1, c1);
        }

        result = c2.updateData(dataConnection.getConnection());
        if (result == 1){
            listDynamisms.set(iC2, c2);
        }


        reselectFcc();

        dataConnection.disconnect();

        // UPDATE LIST IN INCLUSION
        //nodeInterface.refreshDataInclusionModule();
        //nodeInterface.refreshDataClassModule();
    }

    @FXML
    public void deleteFcc(){
        DataConnection dataConnection = dataInterface.getDataConnection();
        dataConnection.connect();

        Fcc fcc = (Fcc)this.tevwFcc.getSelectionModel().getSelectedItem();

        ArrayList<Dynamism> tempListDynamisms = new ArrayList<>();
        ArrayList<Element> elements = new ArrayList<>();
        ArrayList<FccHasLogicSystem> fhlss = new ArrayList<>();

        // Dynamism
        for(Dynamism c:listDynamisms){
            if(c.getFcc().equals(fcc)){
                int response = c.deleteData(dataConnection.getConnection());
                if(response==1){
                    tempListDynamisms.add(c);
                }
            }
        }
        for(Dynamism c:tempListDynamisms){
            listDynamisms.remove(c);
        }

        // element
        for(Element e:listElement){
            if(e.getFcc().equals(fcc)){
                if(e.deleteData(dataConnection.getConnection())==1){
                    elements.add(e);
                }
            }
        }
        for(Element e:elements){
            listElement.remove(e);
        }

        // logicSystem
        for(FccHasLogicSystem fhls : listFccHasLogicSystem){
            if(fhls.getFcc().equals(fcc)){
                if(fhls.deleteData(dataConnection.getConnection())==1){
                    fhlss.add(fhls);
                }
            }
        }
        for(FccHasLogicSystem f:fhlss){
            listFccHasLogicSystem.remove(f);
        }
        // fcc
        int resultado = fcc.deleteData(dataConnection.getConnection());
        if (resultado == 1){
                listFcc.remove(fcc);
        }

        dataConnection.disconnect();
        // UPDATE LIST IN INCLUSION
        //nodeInterface.refreshDataInclusionModule();
        //nodeInterface.refreshDataClassModule();
    }

    @FXML
    public void duplicateFcc(){
        DataConnection dataConnection = dataInterface.getDataConnection();
        //Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        dataConnection.connect();

        //current id of selected fcc
        Fcc selectedFcc = (Fcc)tevwFcc.getSelectionModel().getSelectedItem();
        String copy = "(Copy of FCC " +
                String.valueOf(selectedFcc.getIdFcc()) + ") ";
        //FCC
        Fcc fcc = new Fcc(0, nameFccTextField.getText(), descriptionFccTextArea.getText());

        int resultFcc = fcc.saveData(dataConnection.getConnection());
        fcc.setIdFcc(Fcc.currentAutoIncrement);

        if (resultFcc == 1){
            listFcc.add(fcc);
        }

        // Element
        Element e0 = new Element(0, elementTextField.getText(), 0, fcc);

        Element e1 = null;
        if(defaultSymbolCheckBox.isSelected()==true) {
            e1 = new Element(0, antiElementLabel.getText(), 1, fcc);
        } else if(defaultSymbolCheckBox.isSelected()==false) {
            e1 = new Element(0, antiElementTextField.getText(), 1, fcc);
        }


        int resultE0 = e0.saveData(dataConnection.getConnection());
        e0.setIdElement(Element.currentAutoIncrement);
        int resultE1 = e1.saveData(dataConnection.getConnection());
        e1.setIdElement(Element.currentAutoIncrement);

        if (resultE0 == 1 && resultE1 == 1){
            listElement.addAll(e0,e1);
        }

        // Dynamism
        Dynamism c0 = new Dynamism(0, 0, positivePropositionTextField.getText(), positiveDescriptionTextArea.getText(), fcc);
        int resultC0 = c0.saveData(dataConnection.getConnection());
        c0.setIdDynamism(Dynamism.currentAutoIncrement);

        Dynamism c1 = new Dynamism(0, 1, negativePropositionTextField.getText(), negativeDescriptionTextArea.getText(), fcc);
        int resultC1 = c1.saveData(dataConnection.getConnection());
        c1.setIdDynamism(Dynamism.currentAutoIncrement);

        Dynamism c2 = new Dynamism(0, 2, symmetricPropositionTextField.getText(), symmetricDescriptionTextArea.getText(), fcc);
        int resultC2 = c2.saveData(dataConnection.getConnection());
        c2.setIdDynamism(Dynamism.currentAutoIncrement);

        if(resultC0==1 && resultC1 == 1 && resultC2 == 1){
            listDynamisms.addAll(c0,c1,c2);
        }
        dataConnection.disconnect();

        tevwFcc.getSelectionModel().selectLast();

        // UPDATE LIST IN INCLUSION
        //nodeInterface.refreshDataInclusionModule();
        //nodeInterface.refreshDataClassModule();
    }

    @FXML
    public void newFcc(){
        // take into account that this might be called when selection is already clear
        if(!tevwFcc.getSelectionModel().isEmpty()){
            tevwFcc.getSelectionModel().clearSelection();
        }

//        ltvwImplication.setItems(null);
        logicSystemListView.setItems(null);
        logicSystemComboBox.getSelectionModel().clearSelection();
        logicSystemComboBox.setDisable(true);
//        cobxImplication.getSelectionModel().clearSelection();
//        cobxImplication.setDisable(true);
        positivePropositionLabel.setText(null);
        negativePropositionLabel.setText(null);
        symmetricPropositionLabel.setText(null);
        descriptionFccTextArea.setText(null);
        negativeDescriptionTextArea.setText(null);
        positiveDescriptionTextArea.setText(null);
        symmetricDescriptionTextArea.setText(null);
        antiElementTextField.setText(null);
        elementTextField.setText(null);
        idFccLabel.setText(null);
        nameFccTextField.setText(null);
        negativePropositionTextField.setText(null);
        positivePropositionTextField.setText(null);
        symmetricPropositionTextField.setText(null);

        saveAndCloseButton.setDisable(false);
        editOrSaveButton.setDisable(true);
        deleteButton.setDisable(true);
        bnDuplicateFcc.setDisable(true);


    }

    @FXML
    public void addLogicSystem(){
        DataConnection dataConnection = dataInterface.getDataConnection();
        dataConnection.connect();

        FccHasLogicSystem fhls = new FccHasLogicSystem(
                (Fcc)tevwFcc.getSelectionModel().getSelectedItem(),
                (LogicSystem) logicSystemComboBox.getSelectionModel().getSelectedItem()
        );

        int result = fhls.saveData(dataConnection.getConnection());

        if (result == 1){
            listFccHasLogicSystem.add(fhls);
            reselectFcc();
        }
        dataConnection.disconnect();
    }

    @FXML
    public void removeLogicSystem(){
        DataConnection dataConnection = dataInterface.getDataConnection();
        dataConnection.connect();

        FccHasLogicSystem fhls = getFccHasLogicSystem();
        int resultado = fhls.deleteData(dataConnection.getConnection());
        dataConnection.disconnect();

        if(resultado==1){
            listFccHasLogicSystem.remove(fhls);
            reselectFcc();
        }
    }

    private void reselectFcc() {
        Fcc r = (Fcc)tevwFcc.getSelectionModel().getSelectedItem();
        tevwFcc.getSelectionModel().clearSelection();
        tevwFcc.getSelectionModel().select(r);
    }

    private FccHasLogicSystem getFccHasLogicSystem() {
        for(FccHasLogicSystem fcls:listFccHasLogicSystem){
            if(fcls.getFcc()==tevwFcc.getSelectionModel().getSelectedItem()){
                if(fcls.getLogicSystem()== logicSystemListView.getSelectionModel().getSelectedItem()){
                    return fcls;
                }
            }
        }
        return null;
    }

    /*
           ___     _   _                 ___    __      _   _
          / _ \___| |_| |_ ___ _ __ ___ ( _ )  / _\ ___| |_| |_ ___ _ __ ___
         / /_\/ _ \ __| __/ _ \ '__/ __|/ _ \/\\ \ / _ \ __| __/ _ \ '__/ __|
        / /_\\  __/ |_| ||  __/ |  \__ \ (_>  <_\ \  __/ |_| ||  __/ |  \__ \
        \____/\___|\__|\__\___|_|  |___/\___/\/\__/\___|\__|\__\___|_|  |___/

     */


    public Fcc getFcc() {
        return fcc;
    }

    public void setFcc(Fcc fcc) {
        this.fcc = fcc;
    }

    public ObservableList<Fcc> getListFcc() {
        return this.listFcc;
    }

    public ObservableList<FccHasLogicSystem> getListFccHasLogicSystem() {
        return this.listFccHasLogicSystem;
    }

    public ObservableList<Element> getListElement(){
        return this.listElement;
    }

    public ObservableList<Dynamism> getListDynamisms(){
        return this.listDynamisms;
    }

}
