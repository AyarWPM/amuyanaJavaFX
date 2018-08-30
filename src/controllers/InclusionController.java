package controllers;

import data.Conexion;
import data.Dynamism;
import data.General;
import data.Inclusion;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class InclusionController implements Initializable {
    private AppController appController;
    
    @FXML Button bnSave;
    @FXML Button bnDelete;
    @FXML Button bnNew;
    
    @FXML ListView<Inclusion> ltvwInclusions;
    @FXML Label lbFormulaInclusion;
    @FXML ComboBox<Dynamism> cobxParticular;
    
    @FXML ListView<Dynamism> ltvwBroads;
    @FXML ListView<Dynamism> ltvwNotions;
    @FXML Button bnAdd;
    @FXML Button bnRemove;
    
    private ObservableList<Inclusion> listInclusions;
    private ObservableList<General> listGenerals;
    //private ArrayList<General> tempListGenerals;
    //private Inclusion tempInclusion;
    private ObservableList<Dynamism> listParticulars;
    private ObservableList<Dynamism> listNotions;
    private ObservableList<Dynamism> listBroads;
    
    private ObservableList<Dynamism> tempListBroads;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listInclusions = FXCollections.observableArrayList();
        listGenerals = FXCollections.observableArrayList();
        listParticulars = FXCollections.observableArrayList();
        
        // BROADS
        listBroads = FXCollections.observableArrayList();
        tempListBroads = FXCollections.observableArrayList();
        
        manageEvents();
    }
    
    public ObservableList<Inclusion> getListInclusions(){
        return this.listInclusions;
    }
    
    public ObservableList<General> getListGenerals(){
        return this.listGenerals;
    }
    

    public void setAppController(AppController aThis) {
        this.appController=aThis;
    }

    public void refreshData(){
        ltvwInclusions.refresh();
        ltvwNotions.refresh();
        ltvwBroads.refresh();
        
        cobxParticular.setItems(null);
        listParticulars.clear();
        listParticulars.addAll(appController.getListDynamisms());
        
        cobxParticular.setItems(listParticulars);
    }
    
    public void fillData() {
        
        ltvwInclusions.setItems(listInclusions);
        ltvwInclusions.getSelectionModel().selectFirst();
        
        ltvwNotions.setItems(listNotions);
        ltvwBroads.setItems(listBroads);
        listParticulars.addAll(appController.getListDynamisms());
        cobxParticular.setItems(listParticulars);
    }
    
    public void manageEvents(){
        ltvwInclusions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Inclusion>() {
            @Override
            public void changed(ObservableValue<? extends Inclusion> observable, Inclusion oldValue, Inclusion newValue) {
                if(newValue!=null){
                    // BUTTONS
                    bnSave.setDisable(true);
                    bnDelete.setDisable(false);
                    bnAdd.setDisable(true);
                    bnRemove.setDisable(true);
                    // PARTICULAR COMBOBOX
                    cobxParticular.getSelectionModel().select(newValue.getDynamism());
                    cobxParticular.setDisable(true);
                    
                    // NOTIONS
                    listNotions = FXCollections.observableArrayList();
                    listNotions.addAll(appController.getListDynamisms());
                    ltvwNotions.setItems(listNotions);
                    
                        // Remove the particular dynamism and the other two
                    Dynamism c0 = appController.dynamismOf(0, newValue.getDynamism().getFcc());
                    Dynamism c1 = appController.dynamismOf(1, newValue.getDynamism().getFcc());
                    Dynamism c2 = appController.dynamismOf(2, newValue.getDynamism().getFcc());
                    
                    listNotions.remove(c0);
                    listNotions.remove(c1);
                    listNotions.remove(c2);

                    /*
                    BROAD : removing those notions that are contained in the
                    general notion and putting them in listBroad
                    Also remove the other two as they can't be selected
                    */

                    listBroads = FXCollections.observableArrayList();
                    for(General g:listGenerals){
                        if(g.getInclusion()==newValue){
                            listNotions.remove(appController.dynamismOf(0,g.getDynamism().getFcc()));
                            listNotions.remove(appController.dynamismOf(1,g.getDynamism().getFcc()));
                            listNotions.remove(appController.dynamismOf(2,g.getDynamism().getFcc()));

                            //listNotions.remove(g.getDynamism());
                            listBroads.add(g.getDynamism());
                        }
                    }
                    
                    ltvwBroads.setItems(listBroads);
                    
                } else if (newValue==null){
                    bnDelete.setDisable(true);
                    
                    cobxParticular.getSelectionModel().clearSelection();
                    cobxParticular.setDisable(false);
                    
                    ltvwNotions.setItems(null);
                    ltvwBroads.setItems(null);
                    
                    listNotions.clear();
                }
            }
        });
        
        ltvwNotions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Dynamism>() {
            @Override
            public void changed(ObservableValue<? extends Dynamism> observable, Dynamism oldValue, Dynamism newValue) {
                //if(ltvwInclusions.getSelectionModel().isEmpty()){
                    if(newValue!=null){
                        bnAdd.setDisable(false);
                    } else if (newValue==null){
                        bnAdd.setDisable(true);
                    }
                //}
            }
        });
        
        ltvwBroads.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Dynamism>() {
            @Override
            public void changed(ObservableValue<? extends Dynamism> observable, Dynamism oldValue, Dynamism newValue) {
                //if(ltvwInclusions.getSelectionModel().isEmpty()){
                    if(newValue!=null){
                        bnRemove.setDisable(false);
                    } else if (newValue==null){
                        bnRemove.setDisable(true);
                    }
                //}
                    
            }
        });
        
        cobxParticular.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Dynamism>() {
            @Override
            public void changed(ObservableValue<? extends Dynamism> observable, Dynamism oldValue, Dynamism newValue) {
                if(newValue!=null){
                    
                    listNotions = FXCollections.observableArrayList();
                    listNotions.addAll(appController.getListDynamisms());
                    ltvwNotions.setItems(listNotions);
                    
                    // Remove the notions of the fcc whose conjunction is included in general
                    Dynamism c0 = appController.dynamismOf(0, newValue.getFcc());
                    Dynamism c1 = appController.dynamismOf(1, newValue.getFcc());
                    Dynamism c2 = appController.dynamismOf(2, newValue.getFcc());
                    
                    listNotions.remove(c0);
                    listNotions.remove(c1);
                    listNotions.remove(c2);
                    
                    //ltvwBroads.setItems(listBroads);
                    
                    cobxParticular.setDisable(true);
                } else if (newValue==null){
                    listNotions.clear();
                }
            }
        });
    }
    
    public void reselectInclusion(){
        Inclusion i = (Inclusion)ltvwInclusions.getSelectionModel().getSelectedItem();
        ltvwInclusions.getSelectionModel().clearSelection();
        ltvwInclusions.getSelectionModel().select(i);
    }
    
    
    @FXML
    public void save(){
        // check that a Particular notion -a Conjunction- is selected
        if(cobxParticular.getSelectionModel().isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Save Inclusion");
            alert.setHeaderText(null);
            alert.setContentText("Please select a particular notion.");
            alert.showAndWait();
            return;
        }
        
        // check that there's at least one General conjunction in the listGeneralNotions
        if(listBroads.isEmpty()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Save Inclusion");
            alert.setHeaderText(null);
            alert.setContentText("Please select at least one general notion.");
            alert.showAndWait();
            return;
        }
        
        // check that the inclusion doesn't exist yet
        if(!isInclusionUnique()){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Save Inclusion");
            alert.setHeaderText(null);
            alert.setContentText("This Inclusion already exists.");
            alert.showAndWait();
            return;
        }
        
        Conexion conexion = appController.getConexion();
        conexion.establecerConexion();
        
        //INCLUSION
        Inclusion newInclusion = new Inclusion(0, cobxParticular.getSelectionModel().getSelectedItem());
        
        int result = newInclusion.saveData(conexion.getConnection());
        
        newInclusion.setIdInclusion(Inclusion.currentAutoIncrement);
        
        if (result == 1){
            listInclusions.add(newInclusion);
        }
        
        // GENERAL
        for(Dynamism c:tempListBroads){
            General newGeneral = new General(c, newInclusion);
            if(newGeneral.saveData(conexion.getConnection())==1){
                listGenerals.add(newGeneral);
            }
        }
        //cobxParticular.setItems(listAllNotions);
        ltvwInclusions.getSelectionModel().selectLast();
        
        conexion.cerrarConexion();
    }

    @FXML
    public void delete(){
        Conexion conexion = appController.getConexion();
        conexion.establecerConexion();
        
        Inclusion i = ltvwInclusions.getSelectionModel().getSelectedItem();
        
        ArrayList<General> generals = new ArrayList<>();
        
        
        // find out if it has general
        for(General g:listGenerals){
            if(g.getInclusion().equals(i)){
                if(g.deleteData(conexion.getConnection())==1){
                    generals.add(g);
                }
            }
        }
        for(General g:generals){
            listGenerals.remove(g);
        }
        
        
        // Find out if it has syllogism
        // TODO
        
        
        // finally delete Inclusion
        if(i.deleteData(conexion.getConnection())==1){
            listInclusions.remove(i);
        }
        conexion.cerrarConexion();
    }
    
    @FXML
    public void newInclusion(){
        // temporary list of broads
        tempListBroads = FXCollections.observableArrayList();
        
        // Inclusion
        //tempInclusion = new Inclusion();
        ltvwInclusions.getSelectionModel().clearSelection();
        
        // particular
        listParticulars.clear();
        listParticulars.addAll(appController.getListDynamisms());
        cobxParticular.setDisable(false);
        cobxParticular.getSelectionModel().clearSelection();
        // Broad

        listBroads.clear();
        
        // Notions
        ltvwNotions.getSelectionModel().clearSelection();
        
        bnSave.setDisable(false);
        
        bnAdd.setDisable(true);
        bnRemove.setDisable(true);
    }
    
    @FXML
    public void addGeneral(){
        Dynamism selectedDynamism = ltvwNotions.getSelectionModel().getSelectedItem();
        
        // if there's not an existing Inclusion, we add the selected notion to
        // a temporary list of conjunctions that will be in the Broad list 
        if(ltvwInclusions.getSelectionModel().isEmpty()){
            listBroads.add(selectedDynamism);
            ltvwBroads.setItems(listBroads);

            // The user wants to add selectedDynamism, but we also have to remove the other two
            // dynamisms of the same fcc the selectedDynamism belongs to, so that he can no longer select them after
            listNotions.remove(appController.dynamismOf(0,selectedDynamism.getFcc()));
            listNotions.remove(appController.dynamismOf(1,selectedDynamism.getFcc()));
            listNotions.remove(appController.dynamismOf(2,selectedDynamism.getFcc()));

            tempListBroads.add(selectedDynamism);
        }

        // if the inclusion already exist (there's one selected)
        else if (!ltvwInclusions.getSelectionModel().isEmpty()){
            Conexion conexion = appController.getConexion();
            conexion.establecerConexion();
            
            General newGeneral = new General(selectedDynamism,
                    ltvwInclusions.getSelectionModel().getSelectedItem());
            
            if(newGeneral.saveData(conexion.getConnection())==1){
                listBroads.add(selectedDynamism);
                // The user wants to add selectedDynamism, but we also have to remove the other two
                // dynamisms of the same fcc the selectedDynamism belongs to, so that he can no longer select them after
                listNotions.remove(appController.dynamismOf(0,selectedDynamism.getFcc()));
                listNotions.remove(appController.dynamismOf(1,selectedDynamism.getFcc()));
                listNotions.remove(appController.dynamismOf(2,selectedDynamism.getFcc()));
                listGenerals.add(newGeneral);
            }
            conexion.cerrarConexion();
        }
    }
    
    @FXML
    public void removeGeneral(){
        Dynamism selectedDynamism = ltvwBroads.getSelectionModel().getSelectedItem();
        
        // if it would remain at least 1 conjunction then proceed
        if(listBroads.size()>1){
            // If the inclusion is not created yet
            if(ltvwInclusions.getSelectionModel().isEmpty()){
                tempListBroads.remove(selectedDynamism);
                listBroads.remove(selectedDynamism);

                // A general can't have more than one dynamism of the same FCC
                listNotions.add(appController.dynamismOf(0,selectedDynamism.getFcc()));
                listNotions.add(appController.dynamismOf(1,selectedDynamism.getFcc()));
                listNotions.add(appController.dynamismOf(2,selectedDynamism.getFcc()));

            } 
            // If the inclusion exists already
            else if(!ltvwInclusions.getSelectionModel().isEmpty()){
                General general = generalOf(selectedDynamism,
                        ltvwInclusions.getSelectionModel().getSelectedItem());
                
                Conexion conexion = appController.getConexion();
                conexion.establecerConexion();
                
                if(general.deleteData(conexion.getConnection())==1){
                    listGenerals.remove(general);
                    listBroads.remove(selectedDynamism);

                    // A general can't have more than one dynamism of the same FCC
                    listNotions.add(appController.dynamismOf(0,selectedDynamism.getFcc()));
                    listNotions.add(appController.dynamismOf(1,selectedDynamism.getFcc()));
                    listNotions.add(appController.dynamismOf(2,selectedDynamism.getFcc()));
                }
                conexion.cerrarConexion();
            }
        } else if(listBroads.size()==1){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Remove General notion");
            alert.setHeaderText(null);
            alert.setContentText("Can't remove the only General notion.");
            alert.showAndWait();
        }
    }
    
    public General generalOf(Dynamism dynamism, Inclusion inclusion){
        for(General g:listGenerals){
            if(g.getDynamism().equals(dynamism) && g.getInclusion().equals(inclusion)){
                return g;
            }
        }
        return null;
    }

    // This method is called only at the moment of save() only
    private boolean isInclusionUnique() {
        Dynamism particular = cobxParticular.getSelectionModel().getSelectedItem();
        ObservableList<Dynamism> tempListDynamisms;

        for(Inclusion i:listInclusions){
            tempListDynamisms = FXCollections.observableArrayList();
            if(i.getDynamism().equals(particular)){
                for(General g:listGenerals){
                    if(g.getInclusion().equals(i)){
                        tempListDynamisms.add(g.getDynamism());
                    }
                }
            }
            SortedList<Dynamism> list1 = tempListBroads.sorted();
            SortedList<Dynamism> list2 = tempListDynamisms.sorted();

            if(list1.equals(list2)){
                return false;
            }
        }
        return true;
    }
}
