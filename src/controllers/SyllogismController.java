package controllers;

import data.DataConnection;
import data.Inclusion;
import data.InclusionHasSyllogism;
import data.Syllogism;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SyllogismController implements Initializable {

    private AppController appController;
    
    // SYLLOGISM
    @FXML Button bnSave;
    @FXML Button bnDelete;
    @FXML Button bnNew;
    
    @FXML TextField ttfdLabel;
    
    @FXML ListView ltvwSyllogism;
    @FXML ListView ltvwInclusion;
    @FXML ListView ltvwStep;
    
    @FXML Button bnAdd;
    @FXML Button bnRemove;
    
    ObservableList<Syllogism> listSyllogisms;
    ObservableList<InclusionHasSyllogism> listIHS;
    ObservableList<Inclusion> listInclusions;
    ObservableList<Inclusion> listSteps;
    ObservableList<Inclusion> tempListSteps;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listSyllogisms = FXCollections.observableArrayList();
        listInclusions = FXCollections.observableArrayList();
        listSteps = FXCollections.observableArrayList();
    }    
    
    public ObservableList<Syllogism> getListSyllogisms(){
        return this.listSyllogisms;
    }
    
    public ObservableList<InclusionHasSyllogism> getListIHS(){
        return this.listIHS;
    }

    public void setAppController(AppController aThis) {
        this.appController=aThis;
    }

    
    public void fillData() {
        ltvwSyllogism.setItems(listSyllogisms);
        ltvwSyllogism.getSelectionModel().selectFirst();
        //ttfdLabel.setText(ltvwSyllogism.getSelectionModel().getSelectedItem().toString());
    }
    
    public ArrayList<Inclusion> inclusionsOf(Syllogism syllogism){
        ArrayList<Inclusion> inclusions = new ArrayList<>();
        
        for(InclusionHasSyllogism ihs:listIHS){
            if(ihs.getSyllogism().getIdSyllogism()==syllogism.getIdSyllogism()){
                for(Inclusion i:listInclusions){
                    if(ihs.getInclusion().getIdInclusion()==i.getIdInclusion()){
                        inclusions.add(i);
                    }

                }
            }
        }
        return inclusions;
    }
    
    public void manageEvents(){
        ltvwSyllogism.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Syllogism>() {
            @Override
            public void changed(ObservableValue<? extends Syllogism> observable, Syllogism oldValue, Syllogism newValue) {
                if(newValue!=null){
                    // BUTTONS
                    bnSave.setDisable(true);
                    bnDelete.setDisable(false);
                    bnAdd.setDisable(true);
                    bnRemove.setDisable(true);
                    
                    // INCLUSIONS
                    listInclusions = FXCollections.observableArrayList();
                    //listInclusions.addAll(appController.getListInclusions());
                    ltvwInclusion.setItems(listInclusions);
                        
                    // STEPS
                    
                    listSteps = FXCollections.observableArrayList();
                    for(Inclusion i:inclusionsOf(newValue)){
                        listInclusions.remove(i);
                        listSteps.add(i);
                    }
                    ltvwStep.setItems(listSteps);
                    
                } else if (newValue==null){
                    bnDelete.setDisable(true);
                    
                    ltvwInclusion.setItems(null);
                    ltvwStep.setItems(null);
                    
                    listInclusions.clear();
                }
            }
        });
        
        ltvwInclusion.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Inclusion>() {
            @Override
            public void changed(ObservableValue<? extends Inclusion> observable, Inclusion oldValue, Inclusion newValue) {
                if(ltvwSyllogism.getSelectionModel().isEmpty()){
                    if(newValue!=null){
                        bnAdd.setDisable(false);
                    } else if (newValue==null){
                        bnAdd.setDisable(true);
                    }
                }
            }
        });
        
        ltvwStep.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Inclusion>() {
            @Override
            public void changed(ObservableValue<? extends Inclusion> observable, Inclusion oldValue, Inclusion newValue) {
                if(ltvwSyllogism.getSelectionModel().isEmpty()){
                    if(newValue!=null){
                        bnRemove.setDisable(false);
                    } else if (newValue==null){
                        bnRemove.setDisable(true);
                    }
                }
                    
            }
        });
    }
    
    public void reselectSyllogism(){
        Syllogism s = (Syllogism)ltvwSyllogism.getSelectionModel().getSelectedItem();
        ltvwSyllogism.getSelectionModel().clearSelection();
        ltvwSyllogism.getSelectionModel().select(s);
    }
    
    
    @FXML
    public void save(){
        // check that the label is not empty
        if(ttfdLabel.getText().isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Can't save Syllogism");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a label for the syllogism");
            alert.showAndWait();
            return;
        }
        
        // check that there's at least one Inclusion in the listSteps
        if(listSteps.isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Can't save Syllogism");
            alert.setHeaderText(null);
            alert.setContentText("Please select at least one inclusion");
            alert.showAndWait();
            return;
        }
        
        // check that the inclusion doesn't exist yet
        if(!isSyllogismUnique()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Can't save Syllogism");
            alert.setHeaderText(null);
            alert.setContentText("This Syllogism already exists.");
            alert.showAndWait();
            return;
        }
        
        DataConnection dataConnection = appController.getDataInterface().getDataConnection();
        dataConnection.connect();
        
        //INCLUSION
        Syllogism newSyllogism = new Syllogism(0, ttfdLabel.getText());
        
        int result = newSyllogism.saveData(dataConnection.getConnection());
        
        newSyllogism.setIdSyllogism(Syllogism.currentAutoIncrement);
        
        if (result == 1){
            listSyllogisms.add(newSyllogism);
        }
        
        // STEP
        for(Inclusion i:tempListSteps){
            InclusionHasSyllogism newIHS = new InclusionHasSyllogism(i, newSyllogism);
            if(newIHS.saveData(dataConnection.getConnection())==1){
                listIHS.add(newIHS);
            }
        }
        ltvwSyllogism.getSelectionModel().selectLast();

        dataConnection.disconnect();
    }

    @FXML
    public void delete(){
        DataConnection dataConnection = appController.getDataInterface().getDataConnection();
        dataConnection.connect();
        
        Syllogism s = (Syllogism)ltvwSyllogism.getSelectionModel().getSelectedItem();
        
        ArrayList<InclusionHasSyllogism> IHSs = new ArrayList<>();
        
        for(InclusionHasSyllogism ihs:listIHS){
            if(ihs.getSyllogism().equals(s)){
                if(ihs.deleteData(dataConnection.getConnection())==1){
                    IHSs.add(ihs);
                }
            }
        }
        for(InclusionHasSyllogism ihs:IHSs){
            listIHS.remove(ihs);
        }
        
        // finally delete Syllogism
        if(s.deleteData(dataConnection.getConnection())==1){
            listSyllogisms.remove(s);
        }
        dataConnection.disconnect();
    }
    
    @FXML
    public void newSyllogism(){
        // temporary list of broads
        //tempListBroads = FXCollections.observableArrayList();
        
        // Inclusion
        //tempInclusion = new Inclusion();
        ltvwSyllogism.getSelectionModel().clearSelection();
        ttfdLabel.setText("");
        
        listSteps.clear();
        
        // Notions
        listInclusions.clear();
        //listInclusions.addAll(appController.getListInclusions());
        
        bnSave.setDisable(false);
        //bnDelete.setDisable(true);
        bnAdd.setDisable(true);
        bnRemove.setDisable(true);
    }
    
    @FXML
    public void addInclusion(){
        Inclusion selectedInclusion = (Inclusion)ltvwInclusion.getSelectionModel().getSelectedItem();
        
        listSteps.add(selectedInclusion);
        //ltvwBroads.setItems(listBroads);
        
        listInclusions.remove(selectedInclusion);
        tempListSteps.add(selectedInclusion);
        
    }
    
    @FXML
    public void removeInclusion(){
        Inclusion selectedInclusion = (Inclusion)ltvwInclusion.getSelectionModel().getSelectedItem();
        
        // if it would remain at least 1 conjunction then proceed
        if(listSteps.size()>1){
                tempListSteps.remove(selectedInclusion);
                listSteps.remove(selectedInclusion);
                listInclusions.add(selectedInclusion);

        } else if(listSteps.size()==1){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Remove General notion");
            alert.setHeaderText(null);
            alert.setContentText("Can't remove the only General notion.");
            alert.showAndWait();
        }
    }
    
    // This method is called only at the moment of save() only
    private boolean isSyllogismUnique() {
        ObservableList<Inclusion> tempInclusions;
        for(Syllogism s:listSyllogisms){
            tempInclusions = FXCollections.observableArrayList();
            for(InclusionHasSyllogism ihs:listIHS){
                if(ihs.getSyllogism().equals(s)){
                    tempInclusions.add(ihs.getInclusion());
                }
            }
            SortedList<Inclusion> list1 = tempListSteps.sorted();
            SortedList<Inclusion> list2 = tempInclusions.sorted();
            
            if(list1.equals(list2)){
                return false;
            }
        }
        
        return true;
        
//        Conjunction particular = cobxParticular.getSelectionModel().getSelectedItem();
//        ObservableList<Conjunction> tempConjunctions;
//
//        for(Inclusion i:listInclusions){
//            tempConjunctions = FXCollections.observableArrayList();
//            if(i.getConjunction().equals(particular)){
//                for(General g:listGenerals){
//                    if(g.getInclusion().equals(i)){
//                        tempConjunctions.add(g.getConjunction());
//                    }
//                }
//            }
//            SortedList<Conjunction> list1 = tempListBroads.sorted();
//            SortedList<Conjunction> list2 = tempConjunctions.sorted();
//
//            if(list1.equals(list2)){
//                return false;
//            }
//        }
//        return true;
    }
}
