package com.amuyana.app.data.tod;

import com.amuyana.app.data.DataConnection;
import com.amuyana.app.data.Fcc;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CClassHasInclusion {

    public static int currentAutoIncrement;
    private CClass cClass;
    private Inclusion inclusion;

    public CClassHasInclusion(CClass cClass, Inclusion inclusion) {

        this.cClass = cClass;
        this.inclusion = inclusion;
    }

    //Metodos atributo: cClass
    public CClass getCClass() {
            return cClass;
    }
    public void setCClass(CClass cClass) {
            this.cClass = cClass;
    }
    //Metodos atributo: inclusion
    public Inclusion getInclusion() {
            return inclusion;
    }
    public void setInclusion(Inclusion inclusion) {
            this.inclusion = inclusion;
    }
    
    public static void loadList(Connection connection, 
            ObservableList<CClassHasInclusion> cClassHasInclusions,
            ObservableList<CClass> cClasses,
            ObservableList<Inclusion> inclusions){
        String sql = "SELECT id_c_class, id_inclusion "
                + "FROM " + DataConnection.DATABASE + ".tbl_c_class_has_tbl_inclusion";
        
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);
            
            while(result.next()){
                for(CClass cClass:cClasses){
                    if(cClass.getIdCClass()==result.getInt("id_c_class")){
                        for(Inclusion inclusion : inclusions){
                            if(inclusion.getIdInclusion()==result.getInt("id_inclusion")){
                                cClassHasInclusions.add(new CClassHasInclusion(cClass, inclusion));
                            }
                        }
                    }
                }
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public int saveData(Connection connection){
        String sql="INSERT INTO " + DataConnection.DATABASE + ".tbl_c_class_has_tbl_inclusion (id_c_class, id_inclusion) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            
            instruction.setInt(1,this.cClass.getIdCClass());
            instruction.setInt(2,this.inclusion.getIdInclusion());

            int returnInt = instruction.executeUpdate();

            /*ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                CClassHasInclusion.currentAutoIncrement = rs.getInt(1);
            }*/
            return returnInt;
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                                            "DELETE FROM " + DataConnection.DATABASE + ".tbl_c_class_has_tbl_inclusion "+
                                            "WHERE id_c_class = ? and id_inclusion = ?"
            );
            instruccion.setInt(1, cClass.getIdCClass());
            instruccion.setInt(2, inclusion.getIdInclusion());

            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}