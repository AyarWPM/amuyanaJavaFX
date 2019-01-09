package main.controllers;

import main.data.*;
import main.data.DataConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import main.data.tod.CClass;
import main.data.tod.CClassHasFcc;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


public class CClassController implements Initializable {
    private AppController appController;

    @FXML Button bnSave;
    @FXML Button bnUpdate;
    @FXML Button bnDelete;
    @FXML Button bnNew;
    
    @FXML ListView<CClass> ltvwCClass;
    @FXML TextField ttfdLabel;
    
    @FXML ListView<Fcc> ltvwSelectableFccs;
    @FXML ListView<Fcc> ltvwCollection;
    @FXML Button bnAddToCollection;
    @FXML Button bnRemoveFromCollection;
    
    private ObservableList<CClass> listCClass;
    private ObservableList<CClassHasFcc> listCClassHasFcc;
    private ObservableList<Fcc> listCollection;
    private ObservableList<Fcc> listSelectableFccs;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listCClass = FXCollections.observableArrayList();
        listCClassHasFcc = FXCollections.observableArrayList();
        
        listCollection = FXCollections.observableArrayList();
        ltvwCollection.setItems(listCollection);
        
        listSelectableFccs = FXCollections.observableArrayList();
        
        ltvwSelectableFccs.setItems(listSelectableFccs);
        
        manageEvents();
    }
    
    public ObservableList<CClass> getListCClass(){
        return this.listCClass;
    }
    
    public ObservableList<CClassHasFcc> getListCClassHasFcc(){
        return this.listCClassHasFcc;
    }
    
    public void setAppController(AppController aThis) {
        this.appController=aThis;
    }

    // this one is only called at startup, avoid modifying it
    public void fillData() {
        ltvwCClass.setItems(listCClass);
        listSelectableFccs.addAll(appController.getDataInterface().getListFcc());
    }
    public void refreshData() {
        ltvwCClass.setItems(null);
        ltvwCClass.setItems(listCClass);
        listSelectableFccs.clear();
        listSelectableFccs.addAll(appController.getDataInterface().getListFcc());
    }
    
    public void manageEvents(){
        ltvwCClass.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CClass>() {
            @Override
            public void changed(ObservableValue<? extends CClass> observable, CClass oldValue, CClass newValue) {
                if(newValue!=null){
                    // BUTTONS
                    bnSave.setDisable(true);
                    bnDelete.setDisable(false);
                    bnUpdate.setDisable(false);
                    
                    // TextField
                    ttfdLabel.setText(newValue.getLabel());
                    
                    listCollection.clear();
                    
                    listCollection.addAll(appController.getDataInterface().fccOf(newValue));
                    
                    listSelectableFccs.clear();
                    listSelectableFccs.addAll(getListCandidates());
                    
                } else if (newValue==null){
                    bnSave.setDisable(true);
                    bnDelete.setDisable(true);
                    bnUpdate.setDisable(true);
                    
                    //ttfdLabel.setText(null);
                    
                    //listCollection.clear();
                    //listSelectableFccs.clear();
                }
            }
        });
        
        ltvwSelectableFccs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Fcc>() {
            @Override
            public void changed(ObservableValue<? extends Fcc> observable, Fcc oldValue, Fcc newValue) {
                if(newValue!=null){
                    bnAddToCollection.setDisable(false);
                } else if (newValue==null){
                    bnAddToCollection.setDisable(true);
                }
            }
        });
        
        ltvwCollection.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Fcc>() {
            @Override
            public void changed(ObservableValue<? extends Fcc> observable, Fcc oldValue, Fcc newValue) {
                if(newValue!=null){
                    bnRemoveFromCollection.setDisable(false);
                } else if (newValue==null){
                    bnRemoveFromCollection.setDisable(true);
                }
            }
        });
        
    }
    
    private ObservableList<Fcc> getListCandidates(){
        
        // this list is based on the listCollection, which is updated even as
        // the user starts modifying the elements of the class -the collection-
        // (adding or removing them from the selectable Fcc list and the 
        // collection list
        ObservableList<Fcc> listCandidates = FXCollections.observableArrayList();
        
        // for each item in listCollection I create an arrayList containing 
        // its "general" conjunctions, 
        ArrayList<ArrayList<Dynamism>> listGeneralsOfCollection = new ArrayList<>();
        ArrayList<Dynamism> listCommonGenerals = new ArrayList<>();
        
        for(Fcc f:listCollection){
            listGeneralsOfCollection.add(appController.getDataInterface().generalsOf(f));
        }
        
        // then I compare all arrayLists and create one containing only elements in 
        // common, which is done by retaining common elements in a list wich 
        // initially has all conjunctions (listCommonGenerals)
        listCommonGenerals.addAll(appController.getDataInterface().getListDynamisms());
        for(ArrayList<Dynamism> alc:listGeneralsOfCollection){
            listCommonGenerals.retainAll(alc);
        }
        
        // Then I find those Fccs whose listOfGenerals has at least one common
        // fcc with the listCommonGenerals
        
        for(Fcc f:appController.getDataInterface().getListFcc()){
            ArrayList<Dynamism> tempList = new ArrayList<>();
            
            tempList.addAll(appController.getDataInterface().generalsOf(f));
            
            tempList.retainAll(listCommonGenerals);
            if(!tempList.isEmpty()){
                if(!listCollection.contains(f)){
                    listCandidates.add(f);
                }
            }
        }
        return listCandidates;
    }
    
    public void reselectCClass(){
        CClass c = (CClass)ltvwCClass.getSelectionModel().getSelectedItem();
        ltvwCClass.getSelectionModel().clearSelection();
        ltvwCClass.getSelectionModel().select(c);
    }
    
    
    @FXML
    public void save(){
        // check that a Label is given
        if(ttfdLabel.getText().isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Save Class");
            alert.setHeaderText(null);
            alert.setContentText("Please provide a label for this class.");
            alert.showAndWait();
            return;
        }
        
        // check that there's at least one FCC in the Collection list
        if(listCollection.isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Save Inclusion");
            alert.setHeaderText(null);
            alert.setContentText("Please select at least one FCC.");
            alert.showAndWait();
            return;
        }
        
        
        DataConnection dataConnection = appController.getDataInterface().getDataConnection();
        dataConnection.connect();
        
        //CCLASS TODO
        CClass newCClass = new CClass(0, ttfdLabel.getText(),new Fcc(0,"new label", "new description"));
        
        int result = newCClass.saveData(dataConnection.getConnection());
        
        newCClass.setIdCClass(CClass.currentAutoIncrement);
        
        if (result == 1){
            listCClass.add(newCClass);
        }
        
        // CClass has Fcc
        // for each fcc of the collection list create an instance of CClassHasFcc
        for(Fcc f:this.listCollection){
            CClassHasFcc newCClassHasFcc = new CClassHasFcc(0, newCClass, f);
            
            
            int result2 = newCClassHasFcc.saveData(dataConnection.getConnection());
            newCClassHasFcc.setIdCClassHasFcc(CClassHasFcc.currentAutoIncrement);
            
            if(result2==1){
                listCClassHasFcc.add(newCClassHasFcc);
            }
        }
        
        ltvwCClass.getSelectionModel().selectLast();
        
        dataConnection.disconnect();
    }
    
    @FXML
    public void update(){
        CClass selectedCClass = ltvwCClass.getSelectionModel().getSelectedItem();
        
        DataConnection dataConnection = appController.getDataInterface().getDataConnection();
        dataConnection.connect();
        
        //LABEL
        selectedCClass.setLabel(ttfdLabel.getText());
        selectedCClass.updateData(dataConnection.getConnection());
        
        // COLLECTION
        ArrayList<Fcc> tempToAdd = new ArrayList<>();
        ArrayList<Fcc> tempToRemove = new ArrayList<>();
        
        for(Fcc f:listCollection){
            if(appController.getDataInterface().fccOf(selectedCClass).contains(f)){
                // the fcc remains there
            } else if(!appController.getDataInterface().fccOf(selectedCClass).contains(f)){
                tempToAdd.add(f);
            } 
        }
            
        for(Fcc f:appController.getDataInterface().fccOf(selectedCClass)){
            if(listCollection.contains(f)){
                // the fcc remains
            } else if (!listCollection.contains(f)){
                tempToRemove.add(f);
            }
        }
        
        for(Fcc f: tempToAdd){
            CClassHasFcc newCClassHasFcc = new CClassHasFcc(0, selectedCClass, f);
            int result = newCClassHasFcc.saveData(dataConnection.getConnection());

            newCClassHasFcc.setIdCClassHasFcc(CClassHasFcc.currentAutoIncrement);
            //System.out.println(CClassHasFcc.currentAutoIncrement);
            if(result==1){
                listCClassHasFcc.add(newCClassHasFcc);
            }
        }
        
        CClassHasFcc temp=null;
        
        for(Fcc f:tempToRemove){
            // find that cClassHasFcc that has the f
            for(CClassHasFcc cchf:listCClassHasFcc){
                if(cchf.getCClass().equals(selectedCClass)&&cchf.getFcc().equals(f)){
                    if(cchf.deleteData(dataConnection.getConnection())==1){
                        temp=cchf;
                    }
                }
            }
            
            listCClassHasFcc.remove(temp);
        }
        
        
        ltvwCClass.refresh();

        dataConnection.disconnect();
    }
    
    @FXML
    public void delete(){
        DataConnection dataConnection = appController.getDataInterface().getDataConnection();
        dataConnection.connect();
        
        CClass selectedCClass = ltvwCClass.getSelectionModel().getSelectedItem();
        
        // first delete all references to the CClass in CClassHasFcc
        ArrayList<CClassHasFcc> tempList = new ArrayList<>();
        
        for(CClassHasFcc cchf:listCClassHasFcc){
            
            if(cchf.getCClass().equals(selectedCClass)){
                tempList.add(cchf);
            }
        }
        
        for(CClassHasFcc cchf:tempList){
            if(cchf.deleteData(dataConnection.getConnection())==1){
                    listCClassHasFcc.remove(cchf);
                }
        }

        if(selectedCClass.deleteData(dataConnection.getConnection())==1){
            listCClass.remove(selectedCClass);
        }
        dataConnection.disconnect();
    }
    
    @FXML
    public void newCClass(){
        // CClass
        ltvwCClass.getSelectionModel().clearSelection();
        ttfdLabel.setText(null);
        
        // collection
        listCollection.clear();
        
        // Selectable FCCs
        listSelectableFccs.clear();
        listSelectableFccs.addAll(appController.getDataInterface().getListFcc());
        ltvwSelectableFccs.setItems(listSelectableFccs);
        
        bnSave.setDisable(false);
    }
    
    @FXML
    public void addToCollection(){
        // Only those that can be selected appear in the list, ie. we filtered
        // them before
        Fcc selectedFcc = ltvwSelectableFccs.getSelectionModel().getSelectedItem();
        
        listCollection.add(selectedFcc);
        listSelectableFccs.setAll(getListCandidates());
    }
    
    @FXML
    public void removeFromCollection(){
        Fcc selectedFcc = ltvwCollection.getSelectionModel().getSelectedItem();
        
        if(listCollection.size()==1){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cannot remove FCC from class");
            alert.setHeaderText(null);
            alert.setContentText("The class must have at least one FCC.");
            alert.showAndWait();
            return;
        }
        listSelectableFccs.add(selectedFcc);
        listCollection.remove(selectedFcc);
    }
    
    public ArrayList<CClass> cClassOf(Fcc fcc){
//        for(General g:listGeneral){
//            if(g.getInclusion().equals(conjunction) && g.getContainer2().equals(inclusion)){
//                return g;
//            }
//        }
        return null;
    }
}
