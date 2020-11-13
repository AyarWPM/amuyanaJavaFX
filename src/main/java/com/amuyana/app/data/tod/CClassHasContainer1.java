package com.amuyana.app.data.tod;

import com.amuyana.app.data.DataConnection;
import javafx.collections.ObservableList;
import com.amuyana.app.data.tod.containers.Container1;

import java.sql.*;

public class CClassHasContainer1 {
    private CClass cClass;
    private Container1 container1;

    public CClassHasContainer1(CClass cClass, Container1 container1) {
            this.cClass = cClass;
            this.container1 = container1;
    }

    //Metodos atributo: container1
    public Container1 getContainer1() {
            return container1;
    }
    public void setContainer1(Container1 container1) {
            this.container1 = container1;
    }
    //Metodos atributo: cClass
    public CClass getcClass() {
            return cClass;
    }
    public void setcClass(CClass cClass) {
            this.cClass = cClass;
    }
    
    public static void loadList(Connection connection, 
            ObservableList<CClassHasContainer1> cClassHasContainer1s,
            ObservableList<CClass> cClasses,
            ObservableList<Container1> container1s){
        String sql = "SELECT id_c_class, id_container_1 FROM " + DataConnection.DATABASE + ".tbl_c_class_has_tbl_container_1";
        
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);
            while(result.next()){
                for(CClass cClass:cClasses){
                    if(cClass.getIdCClass()==result.getInt("id_c_class")){
                        for(Container1 l : container1s){
                            if(l.getIdContainer1()==result.getInt("id_container_1")){
                                cClassHasContainer1s.add(new CClassHasContainer1(cClass,l));
                            }
                        }
                    }
                }
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int saveData(Connection connection){
        String sql="INSERT INTO " + DataConnection.DATABASE + ".tbl_c_class_has_tbl_container_1 (id_c_class, id_container_1) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            
            instruction.setInt(1,this.cClass.getIdCClass());
            instruction.setInt(2,this.container1.getIdContainer1());
            
            int returnInt = instruction.executeUpdate();
            return returnInt;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                                            "DELETE FROM " + DataConnection.DATABASE + ".tbl_c_class_has_tbl_container_1 "+
                                            "WHERE id_c_class = ? and id_container_1 = ?"
            );
            instruccion.setInt(1, this.cClass.getIdCClass());
            instruccion.setInt(2, this.container1.getIdContainer1());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}