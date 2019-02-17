package com.amuyana.app.data;

import com.amuyana.app.data.tod.CClass;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.data.tod.containers.Container0;
import com.amuyana.app.data.tod.containers.Container1;
import com.amuyana.app.data.tod.containers.Container2;
import javafx.collections.ObservableList;
import com.amuyana.app.data.tod.CClassHasInclusion;
import com.amuyana.app.data.tod.containers.Tod;

public interface DataInterface {

    void setDataConnectionValues(String url, String name, String password);
    boolean testConnection();
    void reinitializeDatabase();

    void loadData();

    Element getElement(Fcc fcc, int polarity);
    Element getElement(Dynamism dynamism, int polarity);

    Dynamism getDynamism(Fcc fcc, int orientation);

    ObservableList<LogicSystem> getListLogicSystem();
    ObservableList<Fcc> getListFcc();
    ObservableList<FccHasLogicSystem> getListFccHasLogicSystem();
    ObservableList<Tod> getListTod();
    ObservableList<Element> getListElements();
    ObservableList<Dynamism> getListDynamisms();
    ObservableList<User> getListUser();
    ObservableList<CClass> getListCClass();
    ObservableList<CClassHasInclusion> getListCClassHasInclusion();
    ObservableList<Inclusion> getListInclusions();
    ObservableList<Syllogism> getListSyllogisms();
    ObservableList<InclusionHasSyllogism> getListIHS();

    // Containers
    ObservableList<Tod> getTods(LogicSystem logicSystem);
    ObservableList<Fcc> getFccs(LogicSystem logicSystem);

    ObservableList<Container2> getContainer2s(Container1 container1);
    ObservableList<Container1> getContainer1s(Container0 container0);
    Container0 getSideContainer0(Container2 container2, boolean side); // 0 is left, 1 is right
    Container0 getSideContainer0(Container1 container1, boolean side); // 0 is left, 1 is right

    ObservableList<Fcc> getFccs(Container1 container1);
    ObservableList<Fcc> getFccs(Container0 container0);
    ObservableList<Fcc> getFccs(Tod tod);

    DataConnection getDataConnection();

    // Logic System
    void delete(LogicSystem logicSystem);
    LogicSystem newLogicSystem(String label, String description);
    void update(LogicSystem logicSystem);

    // Table of deductions
    Tod newTod(String label, LogicSystem logicSystem);
    void update(Tod tod);
    void delete(Tod tod);

    // Fcc

    Fcc newFcc(LogicSystem logicSystem);
    void update(Fcc fcc);
    void update(Element element);
    void update(Dynamism dynamism);

    // Container0
    Container0 newContainer0(Container1 container1, boolean side);
    Container0 newContainer0(Container2 container2, boolean side);

    void delete(Container0 container0);
    void delete(Container0 container0, Container1 container1);
    void delete(Container0 container0, Container2 container2);

    // Container1
    void delete(Container1 container1);
    Container1 newContainer1(Container0 container0);

    // Container2
    void delete(Container2 container2);
    Container2 newContainer2(Fcc fcc, Container1 container1);

    void addContainer0in2(Container0 container0, Container2 container2, boolean side);
    void addContainer0in1(Container0 container0, Container1 container1, boolean side);

    // Inclusion
    Inclusion newInclusion(Dynamism particularDynamism, Dynamism generalDynamism, Tod tod);
    void delete(Inclusion inclusionToRemove);

    /**
     *
     * @param fcc The descendant
     * @param dynamism The ascendant dynamism
     * @return True if any of the dynamisms of fcc descends from dynamism (which is of course of another Fcc),
     * false otherwise
     */
    // complex getter
    boolean descendsFrom(Fcc fcc, Dynamism dynamism);
    boolean isInclusion(Dynamism descendantDynamism, Dynamism ascendantDynamism);
    boolean isInclusion(Fcc descendantFcc, Dynamism ascendantDynamism);
}
