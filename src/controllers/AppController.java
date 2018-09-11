package controllers;

import main.Module;
import data.CClass;
import data.CClassHasFcc;
import data.Conexion;
import data.Dynamism;
import data.Element;
import data.Fcc;
import data.FccHasLogicSystem;
import data.General;
import data.Inclusion;
import data.InclusionHasSyllogism;
import data.Syllogism;
import data.Log;
import data.LogicSystem;
import data.User;

import extras.tod.Analogy;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AppController {
    private Conexion conexion;

    @FXML private SplitPane stpeContents;

    // CONTROLLERS
    @FXML private LogicSystemController logicSystemController;
    @FXML private DualitiesController dualitiesController;
    @FXML private InclusionController inclusionController;
    @FXML private CClassController cClassController;
    @FXML private TodController todController;
    @FXML private DialecticController dialecticController;
    @FXML private StcController stcController;
    @FXML private SyllogismController syllogismController;
    @FXML private StatsController statsController;
    @FXML private SettingsController settingsController;

    // MAIN TABS
    @FXML Tab tbLogicSystem;
    @FXML Tab tbDualities;
    @FXML Tab tbInclusions;
    @FXML Tab tbClasses;
    @FXML Tab tbSyllogism;
    @FXML Tab tbTod;
    @FXML Tab tbDialectic;
    @FXML Tab tbStc;
    @FXML Tab tbStats;
    @FXML Tab tbSettings;

    // Why is it instantiated this one?
    private final ObservableList<Log> listLog = FXCollections.observableArrayList();

    // FOR LOG
    @FXML private MenuItem muimShowHideLog;

    private ScrollPane slpeLog;
    private TableView tevwLog;

    TableColumn<Log, Timestamp> tecnDate;
    TableColumn<Log, String> tecnType;
    TableColumn<Log, String> tecnMessage;




    public void initialize() throws IOException {
        initLog();

        this.conexion = new Conexion();

        loadModules();

        settingsController.autoClicks();
        addLog("System", "Welcome to Amuyaña! Waiting for user actions.");
    }

    public ObservableList<LogicSystem> getListLogicSystem(){
        return logicSystemController.getListLogicSystem();
    }

    public ObservableList<Fcc> getListFcc(){
        return dualitiesController.getListFcc();
    }

    public ObservableList<FccHasLogicSystem> getListFccHasLogicSystem(){
        return dualitiesController.getListFccHasLogicSystem();
    }

    public ObservableList<Element> getListElements(){
        return dualitiesController.getListElement();
    }

    public ObservableList<Dynamism> getListDynamisms(){
        return dualitiesController.getListDynamisms();
    }

    public ObservableList<CClass> getListCClass() {
        return cClassController.getListCClass();
    }

    public ObservableList<CClassHasFcc> getListCClassHasFcc() {
        return cClassController.getListCClassHasFcc();
    }

    public ObservableList<Inclusion> getListInclusions(){
        return inclusionController.getListInclusions();
    }

    public ObservableList<General> getListGenerals(){
        return inclusionController.getListGenerals();
    }

    public ObservableList<User> getListUser(){
        return settingsController.getListUser();
    }

    public Conexion getConexion(){
        return this.conexion;
    }

    public ObservableList<Log> getListLog() {
        return listLog;
    }

    public AppController getAppController(){
        return this;
    }

    private void loadModules() throws IOException {
        for(Module m:Module.values()){

            FXMLLoader loader = new FXMLLoader(getClass().getResource(m.getUrl()));

            Node node = loader.load();
            m.setNode(node);

            switch(m){
                case LOGIC_SYSTEM:{
                    this.logicSystemController = loader.getController();
                    this.logicSystemController.setAppController(this);
                    this.tbLogicSystem.setContent(m.getNode());
                    break;
                }
                case DUALITIES:{
                    this.dualitiesController = loader.getController();
                    this.dualitiesController.setAppController(this);
                    this.tbDualities.setContent(m.getNode());
                    break;
                }
                case INCLUSIONS:{
                    this.inclusionController = loader.getController();
                    this.inclusionController.setAppController(this);
                    this.tbInclusions.setContent(m.getNode());
                    break;
                }
                case CLASSES:{
                    this.cClassController = loader.getController();
                    this.cClassController.setAppController(this);
                    this.tbClasses.setContent(m.getNode());
                    break;
                }
                case SYLLOGISM:{
                    this.syllogismController = loader.getController();
                    this.syllogismController.setAppController(this);
                    this.tbSyllogism.setContent(m.getNode());
                    break;
                }
                case TOD:{
                    this.todController = loader.getController();
                    this.todController.setAppController(this);
                    this.tbTod.setContent(m.getNode());
                    break;
                }
                case DIALECTIC:{
                    this.dialecticController = loader.getController();
                    this.dialecticController.setAppController(this);
                    this.tbDialectic.setContent(m.getNode());
                    break;
                }
                case STC:{
                    this.stcController = loader.getController();
                    this.stcController.setAppController(this);
                    this.tbStc.setContent(m.getNode());
                    break;
                }
                case STATS:{
                    this.statsController = loader.getController();
                    this.statsController.setAppController(this);
                    this.tbStats.setContent(m.getNode());
                    break;
                }
                case SETTINGS:{
                    this.settingsController = loader.getController();
                    this.settingsController.setAppController(this);
                    this.tbSettings.setContent(m.getNode());
                    break;
                }
            }
        }
    }

    public void loadData() {
        this.conexion.establecerConexion();

//        Dialectic.loadList(conexion.getConnection(), listDialectic);
//        Register.loadList(conexion.getConnection(), listRegister);
//        Space.loadList(conexion.getConnection(), listSpace);
//        Time.loadList(conexion.getConnection(), listTime);
//        Quantum.loadList(conexion.getConnection(), listQuantum);


        for(Module m:Module.values()){
            switch(m){
                case LOGIC_SYSTEM:{
                    LogicSystem.loadList(this.conexion.getConnection(), logicSystemController.getListLogicSystem());
                    logicSystemController.fillData();
                    break;
                }
                case DUALITIES:{
                    Fcc.loadList(this.conexion.getConnection(), dualitiesController.getListFcc());

                    FccHasLogicSystem.loadList(this.conexion.getConnection(),
                            dualitiesController.getListFccHasLogicSystem(),
                            dualitiesController.getListFcc(),
                            logicSystemController.getListLogicSystem());

                    Element.loadList(this.conexion.getConnection(),
                            dualitiesController.getListElement(),
                            dualitiesController.getListFcc());

                    Dynamism.loadData(this.conexion.getConnection(),
                            dualitiesController.getListDynamisms(),
                            dualitiesController.getListFcc());

                    dualitiesController.fillData();

                    break;
                }
                case INCLUSIONS:{
                    Inclusion.loadList(this.conexion.getConnection(),
                            inclusionController.getListInclusions(),
                            dualitiesController.getListDynamisms());

                    General.loadList(this.conexion.getConnection(),
                            inclusionController.getListGenerals(),
                            dualitiesController.getListDynamisms(),
                            inclusionController.getListInclusions());

                    inclusionController.fillData();
                    break;
                }
                case CLASSES:{
                    CClass.loadList(this.conexion.getConnection(),
                            cClassController.getListCClass());

                    CClassHasFcc.loadList(this.conexion.getConnection(),
                            cClassController.getListCClassHasFcc(),
                            cClassController.getListCClass(),
                            dualitiesController.getListFcc());

                    cClassController.fillData();
                    break;
                }
                case SYLLOGISM:{

                    Syllogism.loadList(this.conexion.getConnection(),
                            syllogismController.getListSyllogisms());

                    InclusionHasSyllogism.loadList(this.conexion.getConnection(),
                            syllogismController.getListIHS(),
                            inclusionController.getListInclusions(),
                            syllogismController.getListSyllogisms() );

                    syllogismController.fillData();
                    break;
                }
                case TOD:{
                    todController.fillData();
                    break;
                }
//                case DIALECTIC:{
//                    this.listLogicSystem = FXCollections.observableArrayList();
//                    logicSystemController.fillData(conexion, this.listLogicSystem);
//                    break;
//                }
//                case STC:{
//                    this.listLogicSystem = FXCollections.observableArrayList();
//                    logicSystemController.fillData(conexion, this.listLogicSystem);
//                    break;
//                }
//                case SYLLOGISM:{
//                    this.listLogicSystem = FXCollections.observableArrayList();
//                    logicSystemController.fillData(conexion, this.listLogicSystem);
//                    break;
//                }
//                case STATS:{
//                    this.listLogicSystem = FXCollections.observableArrayList();
//                    logicSystemController.fillData(conexion, this.listLogicSystem);
//                    break;
//                }
                case SETTINGS:{
                    User.loadList(this.conexion.getConnection(), settingsController.getListUser());
                    break;
                }
            }
        }

        conexion.cerrarConexion();
    }

    @FXML public void showHideLog(){
        if("Show Log panel".equals(muimShowHideLog.getText())) {
            stpeContents.getItems().add(slpeLog);
            muimShowHideLog.setText("Hide Log panel");

        } else if ("Hide Log panel".equals(muimShowHideLog.getText())){
            stpeContents.getItems().remove(stpeContents.getItems().size()-1);
            muimShowHideLog.setText("Show Log panel");
        }
    }

    public void clearLists(){
        logicSystemController.getListLogicSystem().clear();

    }

    // called from dualityController to reload the list of dynamisms once we
    // create or duplicate new fccs
    public void refreshDataInclusionModule(){
        inclusionController.refreshData();
    }

    public void refreshDataClassModule(){
        cClassController.refreshData();
    }

    public void initLog(){

        tevwLog = new TableView(listLog);

        slpeLog = new ScrollPane(tevwLog);
        slpeLog.setFitToWidth(true);
        slpeLog.setFitToHeight(true);

        tecnDate = new TableColumn<>("Date");
        tecnType = new TableColumn<>("Type");
        tecnMessage = new TableColumn<>("Message");

        tecnDate.setCellValueFactory(new PropertyValueFactory<Log,Timestamp>("date"));
        tecnType.setCellValueFactory(new PropertyValueFactory<Log,String>("type"));
        tecnMessage.setCellValueFactory(new PropertyValueFactory<Log,String>("message"));

        tevwLog.getColumns().addAll(tecnDate,tecnType,tecnMessage);

        //listLog = FXCollections.observableArrayList();
        tevwLog.setItems(listLog);

//        tevwLog.getSelectionModel().selectedItemProperty().addListener(
//				new ChangeListener<Log>() {
//					@Override
//					public void changed(ObservableValue<? extends Log> arg0,
//							Log valorAnterior, Log valorSeleccionado) {
//                                            // TODO
//                                            System.out.println("selection was " + valorAnterior + " y ahora es: " + valorSeleccionado);
//                                    }
//                                }
//		);


    }

    public void addLog(String type, String message){
        listLog.add(new Log(Timestamp.valueOf(LocalDateTime.now()), type, message));
    }

    public Element elementOf(int polarity, Fcc fcc){
        for(Element e:getListElements()){
            if(e.getFcc().equals(fcc)){
                if(e.getPolarity()==polarity){
                    return e;
                }
            }
        }
        return null;
    }

    public Dynamism dynamismOf(int orientation, Fcc fcc){
        for(Dynamism d:getListDynamisms()){
            if(d.getFcc().equals(fcc)){
                if(d.getOrientation()==orientation){
                    return d;
                }
            }
        }
        return null;
    }

    //Called from LogicSystemController
    public ArrayList<Fcc> fccOf(LogicSystem ls){
        ArrayList<Fcc> list = new ArrayList<>();

        for(FccHasLogicSystem fhls:getListFccHasLogicSystem()){
            if(fhls.getLogicSystem().equals(ls)){
                list.add(fhls.getFcc());
            }
        }
        return list;
    }

    public ArrayList<Fcc> fccOf(CClass cClass){
        ArrayList<Fcc> list = new ArrayList<>();

        for(CClassHasFcc chf:getListCClassHasFcc()){
            if(cClass.equals(chf.getCClass())){
                list.add(chf.getFcc());
            }
        }
        return list;
    }

    public ArrayList<Dynamism> generalsOf(Fcc fcc) {
        ArrayList<Dynamism> listGeneralsOf = new ArrayList<>();
        for(Inclusion i:getListInclusions()){
            if(i.getDynamism().equals(dynamismOf(0, fcc))||
                    i.getDynamism().equals(dynamismOf(1, fcc))||
                    i.getDynamism().equals(dynamismOf(2, fcc))){
                for(General g:getListGenerals()){
                    if(g.getInclusion().equals(i)){
                        listGeneralsOf.add(g.getDynamism());
                    }
                }
            }
        }
        return listGeneralsOf;
    }

    public ArrayList<Dynamism> generalsOf(Inclusion inclusion) {
        ArrayList<Dynamism> listGeneralsOf = new ArrayList<>();
        for(General g:this.getListGenerals()){
            if(g.getInclusion().equals(inclusion)){
                listGeneralsOf.add(g.getDynamism());
            }
        }
        return listGeneralsOf;
    }

    public ArrayList<CClass> cClassOf(Fcc fcc){
        ArrayList<CClass> listCClassOf = new ArrayList<>();

        for(CClassHasFcc cchf:cClassController.getListCClassHasFcc()){
            if(cchf.getFcc()==fcc){
                listCClassOf.add(cchf.getCClass());
            }
        }

        return listCClassOf;
    }

    public ArrayList<Dynamism> debugDynamisms(){
        int[] idDynamisms = new int[]{36};
        ArrayList<Dynamism> listDynamisms = new ArrayList<>();

        for(int i:idDynamisms){
            for(Dynamism d:getListDynamisms()){
                if(d.getIdDynamism()==i){
                    listDynamisms.add(d);
                }
            }
        }

        return listDynamisms;
    }


    /**
     * This method returns a list of lists (Analogy is a list) of FCC's that are generals
     * @param fcc
     * @return
     */
    private ArrayList<Analogy> getListAnalogyInclusionOf(Fcc fcc){

        ArrayList<Analogy> list = new ArrayList<>();

        ArrayList<Inclusion> tempListInclusion = new ArrayList<>();

        // 1. From inclusions it belongs as general.
        for(Inclusion i:getListInclusions()){
            if(generalsOf(i).contains(dynamismOf(0, fcc))||
                    generalsOf(i).contains(dynamismOf(1, fcc))||
                    generalsOf(i).contains(dynamismOf(2, fcc))){
                tempListInclusion.add(i);
            }
        }

        // Transform the inclusion list into fcc list
        // (we are assuming that in each general list there are not two
        // generals who belong to the same fcc - that you have to modify it in
        // the visual module when the user selects generals)

        Analogy tempList;
        for(Inclusion i:tempListInclusion){
            //listAnalogyInclusion = new ArrayList<>();
            tempList = new Analogy();
            for(General g:getListGenerals()){
                if(g.getInclusion().equals(i)){
                    tempList.add(g.getDynamism().getFcc());
                }
            }
            list.add(tempList);
        }

        return list;
    }

    private ArrayList<Analogy> getListAnalogyCClassOf(Fcc fcc){

        // Get all Classes the initial FCC belongs in, convert each class
        // into an Analogy containing fccs only

        ArrayList<Analogy> list = new ArrayList<>();

        Analogy tempAnalogy = new Analogy();

        for(CClass c:cClassOf(fcc)){
            tempAnalogy = new Analogy();
            tempAnalogy.addAll(fccOf(c));
            list.add(tempAnalogy);
        }
        return list;
    }

    private void removeDuplicates(ArrayList<Analogy> listAnalogy){
        Set<Analogy> listAnalogyWithoutDuplicates = new HashSet<>(listAnalogy);
        listAnalogy.clear();
        listAnalogy.addAll(listAnalogyWithoutDuplicates);

        ArrayList<Analogy> tempList = new ArrayList<>();

        for(Analogy a:listAnalogy){
            for(Analogy b:listAnalogy){
                if(a!=b){
                    if(a.containsAll(b)&&b.containsAll(a)){
                        if(!tempList.contains(b)){
                            tempList.add(a);
                        }
                    }
                }
            }
        }

        listAnalogy.removeAll(tempList);
    }

    private void orderAnalogyList(ArrayList<Analogy> listAnalogyForInitial) {
        Collections.sort(listAnalogyForInitial, new Comparator<ArrayList>(){
            @Override
            public int compare(ArrayList a1, ArrayList a2) {
                return a1.size() - a2.size(); // assumes you want biggest to smallest
            }
        });
    }

    /**
     *
     * @param fcc The central FCC.
     * @return
     */
    public ArrayList<Analogy> getListAnalogyForInitial(Fcc fcc){
        ArrayList<Analogy> listAnalogyForInitial = new ArrayList<>();

        ArrayList<Analogy> listAnalogyInclusion = getListAnalogyInclusionOf(fcc);

        ArrayList<Analogy> listAnalogyCClass = getListAnalogyCClassOf(fcc);

        listAnalogyForInitial.addAll(listAnalogyInclusion);
        listAnalogyForInitial.addAll(listAnalogyCClass);

        removeDuplicates(listAnalogyForInitial);

        orderAnalogyList(listAnalogyForInitial);
        
        // There's a case in which FCC will not appear because it doesn't 
        // belong to any general list nor a cClass, in that case we add it
        // manually
        for(Analogy a:listAnalogyForInitial){
            if(a.contains(fcc)){
                return listAnalogyForInitial;
            }
        }
        
        // if it gets here it hasn't found an analogy containing the fcc, so we 
        // create an analogy for it and add it to the listAnalogyForInitial
        Analogy analogy = new Analogy();
        analogy.add(fcc);
        listAnalogyForInitial.add(analogy);
        
        return listAnalogyForInitial;
    }

    public ArrayList<Analogy> getListAnalogyForInclusion(Fcc fcc){
        ArrayList<Analogy> listAnalogyInclusion = new ArrayList<>();

        ArrayList<Fcc> tempListFcc = new ArrayList<>();

        // 1. get all fccs (in the ForInitial level it was only one, here there are many)
        for(Inclusion i:getListInclusions()){
            if(i.getDynamism().getFcc().equals(fcc)){
                for(General g:getListGenerals()){
                    if(g.getInclusion().equals(i)){
                        if(!tempListFcc.contains(g.getDynamism().getFcc())){
                            tempListFcc.add(g.getDynamism().getFcc());
                        }
                    }
                }
            }
        }

        for(Fcc f:tempListFcc){
            listAnalogyInclusion.addAll(getListAnalogyInclusionOf(f));
            listAnalogyInclusion.addAll(getListAnalogyCClassOf(f));
        }

        removeDuplicates(listAnalogyInclusion);
        orderAnalogyList(listAnalogyInclusion);

        return listAnalogyInclusion;
    }

    public ArrayList<Analogy> getListAnalogyForDeduction(Dynamism dynamism){
        ArrayList<Analogy> listAnalogy = new ArrayList<>();
        ArrayList<Fcc> tempList = new ArrayList<>();
        
        for(General g:getListGenerals()){
            if(g.getDynamism().equals(dynamism)){
                tempList.add(g.getInclusion().getDynamism().getFcc());
            }
        }

        for(Fcc f:tempList){
            listAnalogy.addAll(getListAnalogyInclusionOf(f));
            listAnalogy.addAll(getListAnalogyCClassOf(f));
        }

        // Take into account those FCC's that aren't part of a general nor a cClass (aka "simple particulars")
        for(Fcc f:tempList){
            if(!listAnalogy.contains(f)){
                Analogy analogy = new Analogy();
                analogy.add(f);
                listAnalogy.add(analogy);
            }
        }

        removeDuplicates(listAnalogy);
        orderAnalogyList(listAnalogy);

        return listAnalogy;
    }

    /*
    public ArrayList<Analogy> getListAnalogyForNegativeDeduction(Dynamism dynamism){
        ArrayList<Analogy> listAnalogy = new ArrayList<>();

        return listAnalogy;
    }

    public ArrayList<Analogy> getListAnalogyForSymmetricDeduction(Dynamism dynamism){
        ArrayList<Analogy> listAnalogy = new ArrayList<>();

        return listAnalogy;
    }
    */
}