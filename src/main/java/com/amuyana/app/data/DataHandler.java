package com.amuyana.app.data;

import com.amuyana.app.data.tod.*;
import com.amuyana.app.data.tod.containers.*;
import com.amuyana.app.node.tod.Fruit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class DataHandler implements DataInterface {
    // LISTS
    private ObservableList<User> listUser;

    private ObservableList<LogicSystem> listLogicSystem;
    private ObservableList<FccHasLogicSystem> listFccHasLogicSystem;
    private ObservableList<Fcc> listFcc;

    private ObservableList<Tod> listTod;
    private ObservableList<Container0> listContainer0s;
    private ObservableList<Container1> listContainer1s;
    private ObservableList<Container2> listContainer2s;
    private ObservableList<Container0In1> listContainer0In1s;
    private ObservableList<Container0In2> listContainer0In2s;
    private ObservableList<ConjunctionHasContainer1> listConjunctionHasContainer1s; //
    private ObservableList<CClassHasContainer1> listcClassHasContainer1s; //
    private ObservableList<CClass> listCClass;
    private ObservableList<CClassHasInclusion> listCClassHasInclusion; //
    private ObservableList<Conjunction> listConjunction;
    private ObservableList<ConjunctionHasInclusion> listConjunctionHasInclusions; //

    // to check
    private ObservableList<Element> listElements;
    private ObservableList<Dynamism> listDynamisms; //
    private ObservableList<Inclusion> listInclusions; //
    private ObservableList<Syllogism> listSyllogisms;
    private ObservableList<InclusionHasSyllogism> listIHS;


    private DataConnection dataConnection;

    public DataHandler() {
        this.listContainer1s = FXCollections.observableArrayList();
        this.listContainer2s = FXCollections.observableArrayList();
        this.listContainer0In1s = FXCollections.observableArrayList();
        this.listContainer0In2s = FXCollections.observableArrayList();
        this.listConjunctionHasContainer1s = FXCollections.observableArrayList();
        this.listcClassHasContainer1s = FXCollections.observableArrayList();
        this.listConjunction = FXCollections.observableArrayList();
        this.listConjunctionHasInclusions = FXCollections.observableArrayList();

        this.dataConnection = new DataConnection();

        this.listLogicSystem = FXCollections.observableArrayList();
        this.listFcc = FXCollections.observableArrayList();
        this.listFccHasLogicSystem = FXCollections.observableArrayList();
        this.listTod = FXCollections.observableArrayList();
        this.listElements = FXCollections.observableArrayList();
        this.listDynamisms = FXCollections.observableArrayList();
        this.listUser = FXCollections.observableArrayList();
        this.listCClass = FXCollections.observableArrayList();
        this.listCClassHasInclusion = FXCollections.observableArrayList();
        this.listInclusions = FXCollections.observableArrayList();
        this.listSyllogisms = FXCollections.observableArrayList();
        this.listIHS = FXCollections.observableArrayList();
        this.listContainer0s = FXCollections.observableArrayList();
    }

    /**
     * For prototype there's only one notationType of user, however users might need permissions.
     * @param url host address or name of the MYSQL database
     * @param username username for accessing the MYSQL database
     * @param password password of user with username
     */
    public void setDataConnectionValues(String url, String username, String password) {
        DataConnection.setValues(url,username,password);

    }

    @Override
    public DataConnection getDataConnection() {
        return dataConnection;
    }

    public boolean testConnection() {
        boolean connection = false;
        if (this.dataConnection.connect()) {
            connection = true;
            this.dataConnection.disconnect();
        }
        return connection;
    }

    @Deprecated
    @Override
    public void reinitializeDatabase() {
        this.dataConnection.connect();

        Path path = Paths.get("./src/main/resources/mysql/reinitializeScript.txt");
        File file = new File(path.toUri());

        FileInputStream fileInputStream;
        StringBuilder str = new StringBuilder();

        try {
            fileInputStream = new FileInputStream(file);

            int content;
            while ((content = fileInputStream.read()) != -1) {
                // convert to char and display it
                str.append((char) content);
            }

            Statement statement = this.dataConnection.getConnection().createStatement();
            statement.executeUpdate(str.toString());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        this.dataConnection.disconnect();
    }

    public void loadData() {
        this.dataConnection.connect();

        LogicSystem.loadList(dataConnection.getConnection(), this.listLogicSystem);

        Fcc.loadList(dataConnection.getConnection(), this.listFcc);

        FccHasLogicSystem.loadList(this.dataConnection.getConnection(),
                this.listFccHasLogicSystem,
                this.listFcc,
                this.listLogicSystem);

        Container0.loadList(this.dataConnection.getConnection(),this.listContainer0s);

        Container1.loadList(this.dataConnection.getConnection(), this.listContainer1s,this.listContainer0s);

        Container2.loadList(this.dataConnection.getConnection(),listContainer2s,listFcc,listContainer1s);

        Container0In1.loadList(this.dataConnection.getConnection(),this.listContainer0In1s,this.listContainer0s,this.listContainer1s);

        Container0In2.loadList(this.dataConnection.getConnection(),listContainer0In2s,listContainer0s,listContainer2s);

        ConjunctionHasContainer1.loadList(this.dataConnection.getConnection(),listConjunctionHasContainer1s,listConjunction,listContainer1s);

        CClassHasContainer1.loadList(this.dataConnection.getConnection(),listcClassHasContainer1s,listCClass,listContainer1s);

        Conjunction.loadList(this.dataConnection.getConnection(),this.listConjunction);

        ConjunctionHasInclusion.loadList(this.dataConnection.getConnection(),listConjunctionHasInclusions,listConjunction,listInclusions);

        Tod.loadList(this.dataConnection.getConnection(),
                this.listTod,this.listLogicSystem,this.listContainer0s);

        Element.loadList(this.dataConnection.getConnection(),
                this.listElements,
                this.listFcc);

        Dynamism.loadList(this.dataConnection.getConnection(),
                this.listDynamisms,
                this.listFcc);

        Inclusion.loadList(this.dataConnection.getConnection(),
                this.listInclusions,
                this.listDynamisms,
                this.listTod);

        CClass.loadList(this.dataConnection.getConnection(),
                this.listCClass);

        CClassHasInclusion.loadList(this.dataConnection.getConnection(),
                this.listCClassHasInclusion,
                this.listCClass,
                this.listInclusions);

        Syllogism.loadList(this.dataConnection.getConnection(),
                this.listSyllogisms);

        InclusionHasSyllogism.loadList(this.dataConnection.getConnection(),
                this.listIHS,
                this.listInclusions,
                this.listSyllogisms);

//        Dialectic.loadList(this.dataConnection.getConnection(), listDialectic);
//        Register.loadList(this.dataConnection.getConnection(), listRegister);
//        Space.loadList(this.dataConnection.getConnection(), listSpace);
//        Time.loadList(this.dataConnection.getConnection(), listTime);
//        Quantum.loadList(this.dataConnection.getConnection(), listQuantum);

        dataConnection.disconnect();
    }

    // GETTERS OF LISTVIEWS

    @Override
    public ObservableList<LogicSystem> getListLogicSystem() {
        return listLogicSystem;
    }

    @Override
    public ObservableList<Fcc> getListFcc() {
        return listFcc;
    }

    @Override
    public ObservableList<FccHasLogicSystem> getListFccHasLogicSystem() {
        return listFccHasLogicSystem;
    }

    @Override
    public ObservableList<Tod> getListTod() {
        return listTod;
    }

    @Override
    public ObservableList<Element> getListElements() {
        return listElements;
    }

    @Override
    public ObservableList<Dynamism> getListDynamisms() {
        return listDynamisms;
    }

    public ObservableList<User> getListUser() {
        return listUser;
    }

    @Override
    public ObservableList<CClass> getListCClass() {
        return listCClass;
    }

    @Override
    public ObservableList<CClassHasInclusion> getListCClassHasInclusion() {
        return listCClassHasInclusion;
    }

    @Override
    public ObservableList<Inclusion> getListInclusions() {
        return listInclusions;
    }

    @Override
    public ObservableList<Inclusion> getInclusions(Tod tod) {
        ObservableList<Inclusion> inclusions = FXCollections.observableArrayList();
        for (Inclusion inclusion : getListInclusions()) {
            if (inclusion.getTod().getIdTod() == tod.getIdTod()) {
                inclusions.addAll(inclusion);
            }
        }
        return inclusions;
    }

    public ObservableList<Syllogism> getListSyllogisms() {
        return listSyllogisms;
    }

    public ObservableList<InclusionHasSyllogism> getListIHS() {
        return listIHS;
    }

    public ObservableList<Tod> getTods(LogicSystem logicSystem) {
        ObservableList<Tod> todsOf = FXCollections.observableArrayList();
        for (Tod tod : getListTod()) {
            if (tod.getLogicSystem().equals(logicSystem)) {
                todsOf.add(tod);
            }
        }
        return  todsOf;
    }

    @Override
    public ObservableList<Fcc> getFccs(LogicSystem logicSystem) {
        ObservableList<Fcc> fccs = FXCollections.observableArrayList();
        for (Fcc fcc : getListFcc()) {
            for (FccHasLogicSystem fccHasLogicSystem : getListFccHasLogicSystem()) {
                if (fccHasLogicSystem.getLogicSystem().equals(logicSystem)) {
                    if (!fccs.contains(fcc)) {
                        fccs.add(fcc);
                    }
                }
            }
        }
        return fccs;
    }

    @Override
    public ObservableList<Container1> getContainer1s(Container0 container0) {
        ObservableList<Container1> container1s = FXCollections.observableArrayList();
        for (Container1 container1 : listContainer1s) {
            if (container1.getContainer0().getIdContainer0()==container0.getIdContainer0()) {
                container1s.addAll(container1);
            }
        }
        return container1s;
    }

    @Override
    public ObservableList<Container2> getContainer2s(Container1 container1) {
        ObservableList<Container2> container2s = FXCollections.observableArrayList();
        for (Container2 container2 : listContainer2s) {
            if (container2.getContainer1().getIdContainer1()==container1.getIdContainer1()) {
                container2s.add(container2);
            }
        }
        return container2s;
    }

    @Override
    public Container0 getSideContainer0(Container2 container2, boolean side) {
        Container0 container0 = null;
        for (Container0In2 container0In2 : listContainer0In2s) {
            if (container2.equals(container0In2.getContainer2())) {
                if (container0In2.isSide() == side) {
                    container0=container0In2.getContainer0();
                }
            }
        }
        return container0;
    }

    @Override
    public Container0 getSideContainer0(Container1 container1, boolean side) {
        Container0 container0 = null;

        for (Container0In1 container0In1 : listContainer0In1s) {
            if (container1.getIdContainer1()==container0In1.getContainer1().getIdContainer1()) {
                if (container0In1.isSide() == side) {
                    container0=container0In1.getContainer0();
                }
            }
        }
        return container0;
    }

    @Override
    public ObservableList<Fcc> getFccs(Container1 container1) {
        // First get fccs from the center
        ObservableList<Fcc> fccs = FXCollections.observableArrayList();
        for (Container2 container2 : getContainer2s(container1)) {
            // First add fccs from the center
            fccs.setAll(container2.getFcc());
            // from the left container0 of container2
            fccs.addAll(getFccs(getSideContainer0(container2, false)));
            // from the right container0 of container2
            fccs.addAll(getFccs(getSideContainer0(container2, true)));
        }

        // left container0
        fccs.addAll(getFccs(getSideContainer0(container1, false)));
        // right container0
        fccs.addAll(getFccs(getSideContainer0(container1, true)));

        return fccs;
    }

    @Override
    public ObservableList<Fcc> getFccs(Container0 container0) {
        ObservableList<Fcc> fccs = FXCollections.observableArrayList();
        for (Container1 container1 : getContainer1s(container0)) {
            fccs.addAll(getFccs(container1));
        }
        return fccs;
    }

    @Override
    public ObservableList<Fcc> getFccs(Tod tod) {
        return getFccs(tod.getContainer0());
    }

    //
    @Override
    public LogicSystem newLogicSystem(String label, String description) {
        dataConnection.connect();

        LogicSystem logicSystem = new LogicSystem(0,label,description,Timestamp.valueOf(LocalDateTime.now()));
        int resultLS = logicSystem.saveData(dataConnection.getConnection());
        logicSystem.setIdLogicSystem(LogicSystem.currentAutoIncrement);

        if (resultLS == 1){
            listLogicSystem.add(logicSystem);
        }

        dataConnection.disconnect();
        return logicSystem;
    }

    @Override
    public void delete(LogicSystem logicSystem) {
        DataConnection dataConnection = getDataConnection();
        dataConnection.connect();

        // First delete fccHasLogicSystem
        ObservableList<FccHasLogicSystem> tempFccHasLogicSystemToDelete = FXCollections.observableArrayList();
        for (FccHasLogicSystem fccHasLogicSystem : getListFccHasLogicSystem()) {
            if (fccHasLogicSystem.deleteData(dataConnection.getConnection()) == 1) {
                tempFccHasLogicSystemToDelete.add(fccHasLogicSystem);
            }
        }
        listFccHasLogicSystem.removeAll(tempFccHasLogicSystemToDelete);

        ObservableList<Tod> tempTodToRemove = FXCollections.observableArrayList();
        for (Tod tod : getListTod()) {
            if (tod.deleteData(dataConnection.getConnection()) == 1) {
                tempTodToRemove.add(tod);
            }
        }
        listTod.removeAll(tempTodToRemove);

        if (logicSystem.deleteData(dataConnection.getConnection()) == 1){
            getListLogicSystem().remove(logicSystem);
        }
        dataConnection.disconnect();
    }

    @Override
    public void update(LogicSystem logicSystem) {
        DataConnection dataConnection = getDataConnection();
        dataConnection.connect();
        logicSystem.updateData(dataConnection.getConnection());
        dataConnection.disconnect();
    }

    @Override
    public void update(Tod tod) {
        DataConnection dataConnection = getDataConnection();
        dataConnection.connect();
        tod.updateData(dataConnection.getConnection());
        dataConnection.disconnect();
    }

    // FCC
    @Override
    public Fcc newFcc(LogicSystem logicSystem) {
        dataConnection.connect();

        // first fcc
        Fcc fcc = new Fcc(0, "","");
        int resultFcc = fcc.saveData(dataConnection.getConnection());
        fcc.setIdFcc(Fcc.currentAutoIncrement);
        fcc.setName("New FCC "+Fcc.currentAutoIncrement);
        if (resultFcc==1) {
            listFcc.add(fcc);
        }

        // second FccHasLogicSystem
        FccHasLogicSystem fccHasLogicSystem = new FccHasLogicSystem(fcc,logicSystem);
        if (fccHasLogicSystem.saveData(dataConnection.getConnection()) == 1){
            getListFccHasLogicSystem().add(fccHasLogicSystem);
        }

        // Elements
        Element e0 = new Element(0, "e" + fcc.getIdFcc(), 0, fcc);
        Element e1 = new Element(0, "e" + fcc.getIdFcc(), 1, fcc);

        int resultE0 = e0.saveData(dataConnection.getConnection());
        e0.setIdElement(Element.currentAutoIncrement);
        int resultE1 = e1.saveData(dataConnection.getConnection());
        e1.setIdElement(Element.currentAutoIncrement);

        if (resultE0 == 1 && resultE1 == 1){
            getListElements().addAll(e0,e1);
        }

        // Dynamisms
        Dynamism dynamism0 = new Dynamism(0, 0, "Positive orientation of "+fcc.getName(), "", fcc);
        int resultC0 = dynamism0.saveData(dataConnection.getConnection());
        dynamism0.setIdDynamism(Dynamism.currentAutoIncrement);
        Dynamism dynamism1 = new Dynamism(0, 1, "Negative orientation of "+fcc.getName(), "", fcc);
        int resultC1 = dynamism1.saveData(dataConnection.getConnection());
        dynamism1.setIdDynamism(Dynamism.currentAutoIncrement);
        Dynamism dynamism2 = new Dynamism(0, 2, "Symmetric orientation of "+fcc.getName(), "", fcc);
        int resultC2 = dynamism2.saveData(dataConnection.getConnection());
        dynamism2.setIdDynamism(Dynamism.currentAutoIncrement);

        if(resultC0==1 && resultC1 == 1 && resultC2 == 1){
            getListDynamisms().addAll(dynamism0,dynamism1,dynamism2);
        }

        dataConnection.disconnect();
        return fcc;
    }

    @Override
    public void update(Fcc fcc) {
        dataConnection.connect();
        fcc.updateData(dataConnection.getConnection());
        getElement(fcc,0).updateData(dataConnection.getConnection());
        getElement(fcc,1).updateData(dataConnection.getConnection());
        getDynamism(fcc,0).updateData(dataConnection.getConnection());
        getDynamism(fcc,1).updateData(dataConnection.getConnection());
        getDynamism(fcc,2).updateData(dataConnection.getConnection());
        dataConnection.disconnect();
    }

    @Override
    public void update(Element element) {
        dataConnection.connect();
        element.updateData(dataConnection.getConnection());
        dataConnection.disconnect();
    }

    @Override
    public void update(Dynamism dynamism) {
        dataConnection.connect();
        dynamism.updateData(dataConnection.getConnection());
        dataConnection.disconnect();
    }


    // Delete methods

    @Override
    public void delete(Tod tod) {


        // delete Inclusions
        ObservableList<Inclusion> inclusionsToDelete = FXCollections.observableArrayList();
        for (Inclusion inclusion : listInclusions) {
            if (inclusion.getTod().getIdTod() == tod.getIdTod()) {
                inclusionsToDelete.addAll(inclusion);
            }
        }
        for (Inclusion inclusion : inclusionsToDelete) {
            delete(inclusion);
        }
        DataConnection dataConnection = getDataConnection();
        dataConnection.connect();
        // delete tod
        int result = tod.deleteData(dataConnection.getConnection());
        if (result == 1) {
            this.listTod.remove(tod);
        }

        // main container0 and all containers inside
        deleteAll(tod.getContainer0());

        // delete container0
        /*deleteAll(tod.getContainer0(),FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList());*/
        dataConnection.disconnect();

    }

    private void deleteAll(Container0 container0) {
        ObservableList<Container0In1> container0In1sToDelete = FXCollections.observableArrayList();
        ObservableList<Container0In2> container0In2sToDelete = FXCollections.observableArrayList();
        ObservableList<Container1> container1sToDelete = FXCollections.observableArrayList();
        ObservableList<Container2> container2sToDelete = FXCollections.observableArrayList();
        ObservableList<Container0> container0sToDeleteAll = FXCollections.observableArrayList();

        for (Container1 container1 : listContainer1s) {
            if (container1.getContainer0().getIdContainer0() == container0.getIdContainer0()) {

                // left right of each subbranch
                for (Container0In2 container0In2 : listContainer0In2s) {
                    if (container0In2.getContainer2().getContainer1().getIdContainer1() == container1.getIdContainer1()) {
                        container2sToDelete.addAll(container0In2.getContainer2());
                        container0In2sToDelete.addAll(container0In2);
                        container0sToDeleteAll.addAll(container0In2.getContainer0());
                    }
                }

                for (Container0In2 container0In2 : container0In2sToDelete) {
                    delete(container0In2);
                }

                for (Container2 container2 : container2sToDelete) {
                    delete(container2);
                }

                // remove left right container0s, then left right container0s of each subbranch
                for (Container0In1 container0In1 : listContainer0In1s) {
                    if (container0In1.getContainer1().getIdContainer1() == container1.getIdContainer1()) {
                        container1sToDelete.addAll(container1);
                        container0In1sToDelete.addAll(container0In1);
                        container0sToDeleteAll.addAll(container0In1.getContainer0());
                    }
                }
            }
        }

        for (Container0In1 container0In1 : container0In1sToDelete) {
            delete(container0In1);
        }
        for (Container0 container01 : container0sToDeleteAll) {
            deleteAll(container01);
        }
        for (Container1 container1 : container1sToDelete) {
            delete(container1);
        }
        delete(container0);
    }

    private void delete(Container0In2 container0In2) {
        dataConnection.connect();
        if (container0In2.deleteData(dataConnection.getConnection()) == 1) {
            listContainer0In2s.remove(container0In2);
        }
        dataConnection.disconnect();
    }

    private void delete(Container0In1 container0In1) {
        dataConnection.connect();
        if (container0In1.deleteData(dataConnection.getConnection()) == 1) {
            listContainer0In2s.remove(container0In1);
        }
        dataConnection.disconnect();
    }

    @Override
    public void delete(Container0 container0) {
        dataConnection.connect();
        if (container0.deleteData(dataConnection.getConnection()) == 1) {
            listContainer0s.remove(container0);
        }
        dataConnection.disconnect();
    }

    @Override
    public void delete(Container1 container1) {
        dataConnection.connect();
        if (container1.deleteData(dataConnection.getConnection()) == 1) {
            listContainer1s.remove(container1);
        }
        dataConnection.disconnect();
    }

    @Override
    public void delete(Container2 container2) {
        dataConnection.connect();
        if (container2.deleteData(dataConnection.getConnection()) == 1) {
            listContainer2s.remove(container2);
        }
        dataConnection.disconnect();
    }

    @Override
    public void delete(Container0 container0, Container1 container1) {
        dataConnection.connect();
        Container0In1 container0In1ToRemove=null;
        for (Container0In1 container0In1 : listContainer0In1s) {
            if (container0In1.getContainer0().getIdContainer0()==container0.getIdContainer0()) {
                if (container0In1.getContainer1().getIdContainer1()==container1.getIdContainer1()) {
                    if (container0In1.deleteData(dataConnection.getConnection()) == 1) {
                        container0In1ToRemove=container0In1;
                    }
                }
            }
        }
        listContainer0In1s.remove(container0In1ToRemove);
        dataConnection.disconnect();
    }

    @Override
    public void delete(Container0 container0, Container2 container2) {
        dataConnection.connect();
        Container0In2 container0In2ToRemove = null;
        for (Container0In2 container0In2 : listContainer0In2s) {
            if (container0In2.getContainer0().getIdContainer0()==container0.getIdContainer0()) {
                if (container0In2.getContainer2().getIdContainer2()==container2.getIdContainer2()) {
                    if (container0In2.deleteData(dataConnection.getConnection()) == 1) {
                        container0In2ToRemove=container0In2;
                    }
                }
            }
        }
        listContainer0In2s.remove(container0In2ToRemove);
        dataConnection.disconnect();
    }

    @Override
    public Tod newTod(String label, LogicSystem logicSystem) {
        dataConnection.connect();
        // Container0
        Container0 container0 = new Container0(0);
        if (container0.saveData(dataConnection.getConnection()) == 1) {
            container0.setIdContainer0(Container0.currentAutoIncrement);
        }
        // Tod
        Tod tod = new Tod(0,label, logicSystem,container0);
        if (tod.saveData(dataConnection.getConnection()) == 1) {
            tod.setIdTod(Tod.currentAutoIncrement);
            getListTod().add(tod);
        }
        dataConnection.disconnect();
        return tod;
    }

    @Override
    public Container0 newContainer0(Container1 container1, boolean side) {

        dataConnection.connect();
        Container0 container0 = new Container0(0);
        if (container0.saveData(dataConnection.getConnection()) == 1) {
            container0.setIdContainer0(Container0.currentAutoIncrement);
            listContainer0s.add(container0);
        }
        Container0In1 container0In1 = new Container0In1(container0, container1, side);
        listContainer0In1s.add(container0In1);
        container0In1.saveData(dataConnection.getConnection());
        dataConnection.disconnect();
        return container0;
    }

    @Override
    public Container0 newContainer0(Container2 container2, boolean side) {
        dataConnection.connect();
        Container0 container0 = new Container0(0);
        if (container0.saveData(dataConnection.getConnection()) == 1) {
            container0.setIdContainer0(Container0.currentAutoIncrement);
        }
        Container0In2 container0In2 = new Container0In2(container0, container2, side);
        listContainer0In2s.add(container0In2);
        container0In2.saveData(dataConnection.getConnection());
        dataConnection.disconnect();
        return container0;
    }

    // Fcc... fcc is using varargs, there can be zero or any positive amount of fccs
    @Override
    public Container1 newContainer1(Container0 container0) {
        dataConnection.connect();
        Container1 container1 = new Container1(0,container0);
        if (container1.saveData(dataConnection.getConnection()) == 1) {
            container1.setIdContainer1(Container1.currentAutoIncrement);
            listContainer1s.add(container1);
        }
        dataConnection.disconnect();
        return container1;
    }

    @Override
    public Container2 newContainer2(Fcc fcc, Container1 container1) {
        dataConnection.connect();
        Container2 container2 = new Container2(0,fcc,container1);
        if (container2.saveData(dataConnection.getConnection()) == 1) {
            container2.setIdContainer2(Container2.currentAutoIncrement);
            listContainer2s.add(container2);
        }
        dataConnection.disconnect();
        return container2;
    }

    @Override
    public void addContainer0in2(Container0 container0, Container2 container2, boolean side) {
        dataConnection.connect();
        Container0In2 container0In2 = new Container0In2(container0,container2,side);
        if (container0In2.saveData(dataConnection.getConnection()) == 1) {
            listContainer0In2s.add(container0In2);
        }
        dataConnection.disconnect();
    }

    @Override
    public void addContainer0in1(Container0 container0, Container1 container1, boolean side) {
        dataConnection.connect();
        Container0In1 container0In1 = new Container0In1(container0,container1,side);
        if (container0In1.saveData(dataConnection.getConnection()) == 1) {
            listContainer0In1s.add(container0In1);
        }
        dataConnection.disconnect();
    }

    @Override
    public Inclusion newInclusion(Dynamism particularDynamism, Dynamism generalDynamism, Tod tod) {
        dataConnection.connect();
        Inclusion inclusion = new Inclusion(0, particularDynamism, generalDynamism, tod);
        if (inclusion.saveData(dataConnection.getConnection()) == 1) {
            listInclusions.add(inclusion);
        }
        dataConnection.disconnect();
        return inclusion;
    }

    @Override
    public void delete(Inclusion inclusion) {
        DataConnection dataConnection = getDataConnection();
        dataConnection.connect();
        int result = inclusion.deleteData(dataConnection.getConnection());
        if (result == 1) {
            this.listInclusions.remove(inclusion);
        }
        dataConnection.disconnect();
    }
    /*
             _____                       _                        _   _
            /  __ \                     | |                      | | | |
            | /  \/ ___  _ __ ___  _ __ | | _____  __   __ _  ___| |_| |_ ___ _ __ ___
            | |    / _ \| '_ ` _ \| '_ \| |/ _ \ \/ /  / _` |/ _ \ __| __/ _ \ '__/ __|
            | \__/\ (_) | | | | | | |_) | |  __/>  <  | (_| |  __/ |_| ||  __/ |  \__ \
             \____/\___/|_| |_| |_| .__/|_|\___/_/\_\  \__, |\___|\__|\__\___|_|  |___/
                                  | |                   __/ |
                                  |_|                  |___/
     */
    @Override
    public Element getElement(Fcc fcc, int polarity){
        for(Element e: this.listElements){
            if(e.getFcc().equals(fcc)){
                if(e.getPolarity()==polarity){
                    return e;
                }
            }
        }
        try {
            throw new Exception("Could not find any Element for the Fcc.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Element getElement(Dynamism dynamism, int polarity) {
        for (Element element : this.listElements) {
            if (element.getFcc().equals(dynamism.getFcc())) {
                if (polarity==element.getPolarity()) {
                    return element;
                }
            }
        }
        try {
            throw new Exception("Could not find any Element for the Fcc.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Dynamism getDynamism(Fcc fcc, int orientation){
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
     * @param ls the Logic System
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

    @Override
    @Deprecated
    public boolean descendsFrom(Fcc fcc, Dynamism dynamism) {
        for(Inclusion i:this.listInclusions){
            if (i.getParticular().equals(getDynamism(fcc, 0)) ||
                    i.getParticular().equals(getDynamism(fcc, 1)) ||
                    i.getParticular().equals(getDynamism(fcc, 2))) {
                if (i.getGeneral().equals(dynamism)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isInclusion(Dynamism descendantDynamism, Dynamism ascendantDynamism) {
        for (Inclusion inclusion : getListInclusions()) {
            if (inclusion.getGeneral().getIdDynamism()==ascendantDynamism.getIdDynamism()) {
                if (inclusion.getParticular().getIdDynamism()==descendantDynamism.getIdDynamism()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isInclusion(Fcc descendantFcc, Dynamism ascendantDynamism) {
        for (Inclusion inclusion : getListInclusions()) {
            if (inclusion.getGeneral().getIdDynamism()==ascendantDynamism.getIdDynamism()) {
                if (descendantFcc.getIdFcc()==inclusion.getParticular().getFcc().getIdFcc()) {
                    return true;
                }
            }
        }
        return false;
    }
}
