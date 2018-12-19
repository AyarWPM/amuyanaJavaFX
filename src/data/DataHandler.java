package data;

import extras.tod.Analogy;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class DataHandler implements DataInterface {

    // LISTS
    private ObservableList<LogicSystem> listLogicSystem;
    private ObservableList<Fcc> listFcc;
    private ObservableList<FccHasLogicSystem> listFccHasLogicSystem;
    private ObservableList<Element> listElements;
    private ObservableList<Dynamism> listDynamisms;
    private ObservableList<User> listUser;
    private ObservableList<CClass> listCClass;
    private ObservableList<CClassHasFcc> listCClassHasFcc;
    private ObservableList<Inclusion> listInclusions;
    private ObservableList<General> listGenerals;
    private ObservableList<Syllogism> listSyllogisms;
    private ObservableList<InclusionHasSyllogism> listIHS;

    private DataConnection dataConnection;

    public DataHandler() {
        this.dataConnection = new DataConnection();

        this.listLogicSystem = FXCollections.observableArrayList();
        this.listFcc = FXCollections.observableArrayList();
        this.listFccHasLogicSystem = FXCollections.observableArrayList();
        this.listElements = FXCollections.observableArrayList();
        this.listDynamisms = FXCollections.observableArrayList();
        this.listUser = FXCollections.observableArrayList();
        this.listCClass = FXCollections.observableArrayList();
        this.listCClassHasFcc = FXCollections.observableArrayList();
        this.listInclusions = FXCollections.observableArrayList();
        this.listGenerals = FXCollections.observableArrayList();
        this.listSyllogisms = FXCollections.observableArrayList();
        this.listIHS = FXCollections.observableArrayList();
    }

    /**
     * For prototype there's only one type of user, however users might need permissions.
     * @param url host address or name of the MYSQL database
     * @param username username for accessing the MYSQL database
     * @param password password of user with username
     */
    public void setDataConnectionValues(String url, String username, String password) {
        DataConnection.setValues(url,username,password);
    }

    public DataConnection getDataConnection() {
        return this.dataConnection;
    }

    public void loadData() {
        DataConnection dataConnection = new DataConnection();
        dataConnection.connect();

        LogicSystem.loadList(dataConnection.getConnection(), this.listLogicSystem);

        FccHasLogicSystem.loadList(dataConnection.getConnection(),
                this.listFccHasLogicSystem,
                this.listFcc,
                this.listLogicSystem);

        Fcc.loadList(dataConnection.getConnection(), this.listFcc);

        Element.loadList(dataConnection.getConnection(),
                this.listElements,
                this.listFcc);

        Dynamism.loadList(dataConnection.getConnection(),
                this.listDynamisms,
                this.listFcc);

        Inclusion.loadList(dataConnection.getConnection(),
                this.listInclusions,
                this.listDynamisms);

        General.loadList(dataConnection.getConnection(),
                this.listGenerals,
                this.listDynamisms,
                this.listInclusions);

        CClass.loadList(dataConnection.getConnection(),
                this.listCClass);

        CClassHasFcc.loadList(dataConnection.getConnection(),
                this.listCClassHasFcc,
                this.listCClass,
                this.listFcc);

        Syllogism.loadList(dataConnection.getConnection(),
                this.listSyllogisms);

        InclusionHasSyllogism.loadList(dataConnection.getConnection(),
                this.listIHS,
                this.listInclusions,
                this.listSyllogisms);

//        Dialectic.loadList(dataConnection.getConnection(), listDialectic);
//        Register.loadList(dataConnection.getConnection(), listRegister);
//        Space.loadList(dataConnection.getConnection(), listSpace);
//        Time.loadList(dataConnection.getConnection(), listTime);
//        Quantum.loadList(dataConnection.getConnection(), listQuantum);

        dataConnection.disconnect();
    }

    // GETTERS OF LISTVIEWS

    @Override
    public ObservableList<LogicSystem> getListLogicSystem() {
        return listLogicSystem;
    }

    public ObservableList<Fcc> getListFcc() {
        return listFcc;
    }

    public ObservableList<FccHasLogicSystem> getListFccHasLogicSystem() {
        return listFccHasLogicSystem;
    }

    public ObservableList<Element> getListElements() {
        return listElements;
    }

    public ObservableList<Dynamism> getListDynamisms() {
        return listDynamisms;
    }

    public ObservableList<User> getListUser() {
        return listUser;
    }

    public ObservableList<CClass> getListCClass() {
        return listCClass;
    }

    public ObservableList<CClassHasFcc> getListCClassHasFcc() {
        return listCClassHasFcc;
    }

    public ObservableList<Inclusion> getListInclusions() {
        return listInclusions;
    }

    public ObservableList<General> getListGenerals() {
        return listGenerals;
    }

    public ObservableList<Syllogism> getListSyllogisms() {
        return listSyllogisms;
    }

    public ObservableList<InclusionHasSyllogism> getListIHS() {
        return listIHS;
    }

    public Element elementOf(int polarity, Fcc fcc){
        for(Element e: this.listElements){
            if(e.getFcc().equals(fcc)){
                if(e.getPolarity()==polarity){
                    return e;
                }
            }
        }
        return null;
    }

    public Dynamism dynamismOf(int orientation, Fcc fcc){
        for(Dynamism d:this.listDynamisms){
            if(d.getFcc().equals(fcc)){
                if(d.getOrientation()==orientation){
                    return d;
                }
            }
        }
        return null;
    }

    /**
     * Get the list of FCCs that belong to a logic system
     * @param ls
     * @return List of fcc
     */
    public ArrayList<Fcc> fccOf(LogicSystem ls){
        ArrayList<Fcc> list = new ArrayList<>();
        for(FccHasLogicSystem fhls:this.listFccHasLogicSystem){
            if(fhls.getLogicSystem().equals(ls)){
                list.add(fhls.getFcc());
            }
        }
        return list;
    }

    public ArrayList<Fcc> fccOf(CClass cClass){
        ArrayList<Fcc> list = new ArrayList<>();
        for(CClassHasFcc chf:this.listCClassHasFcc){
            if(cClass.equals(chf.getCClass())){
                list.add(chf.getFcc());
            }
        }
        return list;
    }

    /**
     *
     * @param fcc Fcc of particulars
     * @return
     */
    public ArrayList<Dynamism> generalsOf(Fcc fcc) {
        ArrayList<Dynamism> listGeneralsOf = new ArrayList<>();
        for(Inclusion i:this.listInclusions){
            if(i.getDynamism().equals(dynamismOf(0, fcc))||
                    i.getDynamism().equals(dynamismOf(1, fcc))||
                    i.getDynamism().equals(dynamismOf(2, fcc))){
                for(General g:this.listGenerals){
                    if(g.getInclusion().equals(i)){
                        listGeneralsOf.add(g.getDynamism());
                    }
                }
            }
        }
        return listGeneralsOf;
    }
    /**
     * Method used to build the Conjunction analogy.
     *
     * @param inclusion
     * @return The list of all Dynamism that are generals of the inclusion
     */
    public ArrayList<Dynamism> generalsOf(Inclusion inclusion) {
        ArrayList<Dynamism> listGeneralsOf = new ArrayList<>();
        for(General g:this.listGenerals){
            if(g.getInclusion().equals(inclusion)){
                listGeneralsOf.add(g.getDynamism());
            }
        }
        return listGeneralsOf;
    }

    public ArrayList<CClass> cClassOf(Fcc fcc){
        ArrayList<CClass> listCClassOf = new ArrayList<>();

        for(CClassHasFcc cchf:this.listCClassHasFcc){
            if(cchf.getFcc()==fcc){
                listCClassOf.add(cchf.getCClass());
            }
        }

        return listCClassOf;
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
        for(Inclusion i:listInclusions){
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
            for(General g:listGenerals){
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

        if(listAnalogyForInitial.size()==0){
            listAnalogyForInitial.add(new Analogy(fcc));
        }

        // Because we want to draw the fcc in front, we modify the analogies accordingly:
        for(Analogy a:listAnalogyForInitial){
            a.remove(fcc);
            a.add(fcc);
        }

/*

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
*/
        return listAnalogyForInitial;
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

    public ArrayList<Analogy> getListAnalogyForAntecedent(Fcc fcc){
        ArrayList<Analogy> listAnalogyInclusion = new ArrayList<>();

        ArrayList<Fcc> tempListFcc = new ArrayList<>();

        // 1. get all fccs (in the ForInitial level it was only one, here there are many)
        for(Inclusion i:this.listInclusions){
            if(i.getDynamism().getFcc().equals(fcc)){
                for(General g:this.listGenerals){
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

    public ArrayList<Analogy> getListAnalogyForDescendant(Fcc fcc){
        ArrayList<Analogy> listAnalogy = new ArrayList<>();
        ArrayList<Fcc> tempList = new ArrayList<>();

        for(General g:this.listGenerals){
            if(g.getDynamism().equals(dynamismOf(0, fcc))){
                tempList.add(g.getInclusion().getDynamism().getFcc());
            }
            if(g.getDynamism().equals(dynamismOf(1, fcc))){
                tempList.add(g.getInclusion().getDynamism().getFcc());
            }
            if(g.getDynamism().equals(dynamismOf(2, fcc))){
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

    public ArrayList<Analogy> getListAnalogyForDescendant(Dynamism dynamism){
        ArrayList<Analogy> listAnalogy = new ArrayList<>();
        ArrayList<Fcc> tempList = new ArrayList<>();

        for(General g:this.listGenerals){
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

    public boolean isDeduction(Dynamism effect, Dynamism cause){
        System.out.println("entering isDeduction...");
        for(Inclusion i:this.listInclusions){
            System.out.println("A for ok...");
            if(i.getDynamism().equals(effect)){
                System.out.println("A if ok...");
                for(Dynamism d:generalsOf(i)){
                    System.out.println("B for ok...");
                    if(d.equals(cause)){
                        System.out.println("A if ok...");
                        return true;
                    }
                }

            }
        }
        return false;
    }

    public boolean isDeduction(Fcc deducedFcc, Dynamism cause){
        for(Inclusion i:this.listInclusions){
            if(i.getDynamism().equals(dynamismOf(0,deducedFcc))||
                    i.getDynamism().equals(dynamismOf(1,deducedFcc))||
                    i.getDynamism().equals(dynamismOf(2,deducedFcc))){
                for(Dynamism d:generalsOf(i)){
                    if(d.equals(cause)){
                        return true;
                    }
                }

            }
        }
        return false;
    }

    /**
     * Every FCC is in at least one Conjunction. Conjunctions together with
     * Classes are Analogies. This method returns a list with all Conjunctions
     * and Classes the FCC fcc belongs to.
     * @param fcc The fcc that belongs to listAnalogies
     * @return List of all Analogies this dynamism belongs to.
     */
    public ArrayList<Analogy> getListAnalogies(Fcc fcc){
        ArrayList<Analogy> listAnalogies = new ArrayList<>();

        listAnalogies.addAll(getListConjunctions(fcc));
        listAnalogies.addAll(getListClasses(fcc));

        return listAnalogies;
    }

    /**
     * This method is used when the user selects for the first time a fcc in the list.
     * @param fcc The fcc selected by the user, the initial fcc of the TOD
     * @return The smallest analogy, if possible of type Conjunction, otherwise of type Class.
     */
    public Analogy getInitialAnalogy(Fcc fcc) {
        Analogy initialAnalogy = new Analogy();
        if (!getListConjunctions(fcc).isEmpty()) {
            // It gets the analogy 0 because the list is ordered, from smallest to biggest
            initialAnalogy = getListConjunctions(fcc).get(0);
        } else if (getListConjunctions(fcc).isEmpty()) {
            if (!getListClasses(fcc).isEmpty()) {
                // It gets the analogy 0 because the list is ordered, from smallest to biggest
                initialAnalogy = getListClasses(fcc).get(0);
            } else if (getListClasses(fcc).isEmpty()) {
                // If Conjunction and Class analogies are empty, we create an analogy with the fcc
                initialAnalogy = new Analogy(fcc, Analogy.Type.NONE);
            }
        }
        return initialAnalogy;
    }

    /**
     *
     * @param fcc
     * @return The list of conjunctions the fcc belongs to.
     */
    public ArrayList<Analogy> getListConjunctions(Fcc fcc){
        ArrayList<Analogy> listConjunctions = new ArrayList<>();

        // Find all inclusions that have it as general
        // Note that two inclusions can have exactly the same fcc's, because
        // inclusions are defined on dynamisms, not fcc's
        // Therefore we merge two conjunctions that have exactly the same fcc's,
        // we do not need to differentiate them.


        // 1. Get inclusions' list
        ArrayList<Inclusion> listInclusions = new ArrayList<>();
        for(General g:this.listGenerals){

            if(g.getDynamism().getFcc().equals(fcc)){
                // At this point we're sure there are not two same inclusions because
                // there cannot be two generals with the same inclusion and the same
                // dynamisms, even if those dynamisms are different but of the same FCC
                listInclusions.add(g.getInclusion());

            }
        }

        // 2. For each inclusion create an Analogy as long as there's not
        // already one with the same FCC, both in the general and particular sides

        ArrayList<Inclusion> finalListInclusion = new ArrayList<>();
        ArrayList<Inclusion> exclusionList = new ArrayList<>();

        finalListInclusion.addAll(listInclusions);

        for(Inclusion i1:listInclusions){
            for(Inclusion i2:listInclusions){
                if(exclusionList.contains(i2)) continue;
                // Of course don't consider the case i1==i2
                if (i1 != i2) {
                    // If the dynamisms' FCCS that are generals in the inclusions are the same...
                    if (haveSameGeneralFccs(i1, i2)) {
                        // If the dynamisms' FCCS that are particular in the inclusions are the same...
                        //if (i1.getDynamism().getFcc().equals(i2.getDynamism().getFcc())) {
                        // If
                        if (finalListInclusion.contains(i2)) {
                            finalListInclusion.remove(i2);
                        }
                        //}
                    }
                }


            }
            exclusionList.add(i1);
        }

        // All the inclusions in finalListInclusion are unique, that is they don't have the same FCCS
        // in generals and in particular. Now we convert the inclusion into an Analogy.
        for (Inclusion i : finalListInclusion) {
            Analogy newAnalogy = new Analogy(Analogy.Type.CONJUNCTION);
            for (Dynamism d : generalsOf(i)) {
                newAnalogy.add(d.getFcc());
            }
            listConjunctions.add(newAnalogy);
        }

        orderAnalogyList(listConjunctions);

        return listConjunctions;
    }

    /**
     * Find all the classes the fcc belongs to
     * @param fcc The fcc that is going to be tested
     * @return The list of class analogies in which fcc belongs to
     */
    public ArrayList<Analogy> getListClasses(Fcc fcc){
        ArrayList<Analogy> listClasses = new ArrayList<>();
        ArrayList<CClass> tempCClass = new ArrayList<>();

        for (CClassHasFcc cClassHasFcc : this.listCClassHasFcc) {
            if (cClassHasFcc.getFcc().equals(fcc)) {
                tempCClass.add(cClassHasFcc.getCClass());
            }
        }

        for (CClass cClass : tempCClass) {
            Analogy analogy = new Analogy(Analogy.Type.CLASS);
            for (Fcc f : fccOf(cClass)) {
                analogy.add(f);
            }
            listClasses.add(analogy);
        }

        orderAnalogyList(listClasses);

        return listClasses;
    }

    /**
     * This method will sort the analogies inside listAnalogy from smallest to largest, i.e. analogy with 1 fcc first,
     * then with 2, then with 3 and so on...
     * @param listAnalogy The list that is going to be sorted
     */
    private void orderAnalogyList(ArrayList<Analogy> listAnalogy) {
        Collections.sort(listAnalogy, new Comparator<ArrayList>(){
            @Override
            public int compare(ArrayList a1, ArrayList a2) {
                return a1.size() - a2.size(); // assumes you want smallest to biggest
            }
        });
    }

    /**
     * The method compares two inclusions with respect to its FCCs.
     * @param inclusion1
     * @param inclusion2
     * @return True if both inclusions have same FCCS in the general side, False if they are different
     */
    private boolean haveSameGeneralFccs(Inclusion inclusion1, Inclusion inclusion2) {
        ArrayList<Fcc> listFcc1 = new ArrayList<>();
        ArrayList<Fcc> listFcc2 = new ArrayList<>();

        generalsOf(inclusion1).forEach(d -> {listFcc1.add(d.getFcc());});
        generalsOf(inclusion2).forEach(d -> {listFcc2.add(d.getFcc());});

        for (Fcc f1 : listFcc1) {
            if (!listFcc2.contains(f1)) {
                return false;
            }
        }

        for (Fcc f2 : listFcc2) {
            if (!listFcc1.contains(f2)) {
                return false;
            }
        }
        return true;
    }

    /*
    @FXML public void showHideLog(){
        if("Show Log panel".equals(muimShowHideLog.getText())) {
            stpeContents.getItems().add(slpeLog);
            muimShowHideLog.setText("Hide Log panel");

        } else if ("Hide Log panel".equals(muimShowHideLog.getText())){
            stpeContents.getItems().remove(stpeContents.getItems().size()-1);
            muimShowHideLog.setText("Show Log panel");
        }
    }*/
}
