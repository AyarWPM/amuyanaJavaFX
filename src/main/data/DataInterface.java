package main.data;

import main.data.tod.CClass;
import main.data.tod.CClassHasFcc;
import main.data.tod.Inclusion;
import main.data.tod.containers.Tod;
import main.tod.Analogy;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public interface DataInterface {

    void setDataConnectionValues(String url, String name, String password);

    DataConnection getDataConnection();

    void loadData();

    Element elementOf(int polarity, Fcc fcc);

    Dynamism dynamismOf(int orientation, Fcc fcc);

    ArrayList<Fcc> fccOf(LogicSystem ls);

    ArrayList<Fcc> fccOf(CClass cClass);

    ArrayList<Dynamism> generalsOf(Fcc fcc);

    /**
     * Method used to build the Conjunction analogy.
     *
     * @param inclusion
     * @return The list of all Dynamism that are generals of the inclusion
     */
    ArrayList<Dynamism> generalsOf(Inclusion inclusion);

    ArrayList<CClass> cClassOf(Fcc fcc);

    ArrayList<Analogy> getListAnalogyForInitial(Fcc fcc);

    ArrayList<Analogy> getListAnalogyForAntecedent(Fcc fcc);

    ArrayList<Analogy> getListAnalogyForDescendant(Fcc fcc);

    ArrayList<Analogy> getListAnalogyForDescendant(Dynamism dynamism);

    boolean isDeduction(Dynamism effect, Dynamism cause);

    boolean isDeduction(Fcc deducedFcc, Dynamism cause);

    ObservableList<LogicSystem> getListLogicSystem();
    ObservableList<Fcc> getListFcc();
    ObservableList<FccHasLogicSystem> getListFccHasLogicSystem();
    ObservableList<Tod> getListTod();
    ObservableList<Element> getListElements();
    ObservableList<Dynamism> getListDynamisms();
    ObservableList<User> getListUser();
    ObservableList<CClass> getListCClass();
    ObservableList<CClassHasFcc> getListCClassHasFcc();
    ObservableList<Inclusion> getListInclusions();
    ObservableList<General> getListGenerals();
    ObservableList<Syllogism> getListSyllogisms();
    ObservableList<InclusionHasSyllogism> getListIHS();

    /**
     * Every FCC is in at least one Conjunction. Conjunctions together with
     * Classes are Analogies. This method returns a list with all Conjunctions
     * and Classes the FCC fcc belongs to.
     * @param fcc The fcc that belongs to listAnalogies
     * @return List of all Analogies this dynamism belongs to.
     */
    ArrayList<Analogy> getListAnalogies(Fcc fcc);

    /**
     * This method is used when the user selects for the first time a fcc in the list.
     * @param fcc The fcc selected by the user, the initial fcc of the TOD
     * @return The smallest analogy, if possible of type Conjunction, otherwise of type Class.
     */
    Analogy getInitialAnalogy(Fcc fcc);

    /**
     *
     * @param fcc
     * @return The list of conjunctions the fcc belongs to.
     */
    ArrayList<Analogy> getListConjunctions(Fcc fcc);

    /**
     * Find all the classes the fcc belongs to
     * @param fcc The fcc that is going to be tested
     * @return The list of class analogies in which fcc belongs to
     */
    ArrayList<Analogy> getListClasses(Fcc fcc);
}
