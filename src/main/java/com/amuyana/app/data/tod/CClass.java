package com.amuyana.app.data.tod;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CClass {
    private IntegerProperty idCClass;
    private StringProperty name;

    public static int currentAutoIncrement;
    
    public CClass(int idCClass, String name) {
            this.idCClass = new SimpleIntegerProperty(idCClass);
            this.name = new SimpleStringProperty(name);
    }

    //Metodos atributo: idCClass
    public int getIdCClass() {
            return idCClass.get();
    }
    public void setIdCClass(int idCClass) {
            this.idCClass = new SimpleIntegerProperty(idCClass);
    }
    public IntegerProperty idCClassProperty() {
        return idCClass;
    }

    //Metodos atributo: name
    public String getName() {
            return name.get();
    }
    public void setName(String name) {
            this.name = new SimpleStringProperty(name);
    }
    public StringProperty nameProperty() {
        return name;
    }

    public static void loadList(Connection connection,ObservableList<CClass> cClasses) {
        
        String sql = "SELECT id_c_class, name FROM amuyana.tbl_c_class";
        
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while(result.next()){
                cClasses.add(new CClass(result.getInt("id_c_class"), result.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_c_class (id_c_class, name) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql, 
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,this.idCClass.get());
            instruction.setString(2,this.name.get());
            
            int returnInt = instruction.executeUpdate();
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                CClass.currentAutoIncrement = rs.getInt(1);
            }
            return returnInt;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_c_class SET name = ? "
                + "WHERE id_c_class = ?";
        try {
            PreparedStatement instruccion =
                            connection.prepareStatement(sql);
            instruccion.setString(1, name.get());
            instruccion.setInt(2, idCClass.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_c_class " +
                        "WHERE id_c_class = ? ";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            instruction.setInt(1, this.idCClass.get());
            return instruction.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        
    }
    
    @Override
    public String toString(){
        return this.getName();
    }
}