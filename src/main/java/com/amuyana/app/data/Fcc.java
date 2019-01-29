package com.amuyana.app.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fcc{
    private IntegerProperty idFcc;
    private StringProperty name;
    private StringProperty description;
    public static int currentAutoIncrement;

    public Fcc(int idFcc, String name, String description) {
            this.idFcc = new SimpleIntegerProperty(idFcc);
            this.name = new SimpleStringProperty(name);
            this.description = new SimpleStringProperty(description);
    }

    //Metodos atributo: idFcc
    public int getIdFcc() {
            return idFcc.get();
    }
    public void setIdFcc(int idFcc) {
            this.idFcc = new SimpleIntegerProperty(idFcc);
    }
    public IntegerProperty idFccProperty() {
            return idFcc;
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
    //Metodos atributo: description
    public String getDescription() {
            return description.get();
    }
    public void setDescription(String description) {
            this.description = new SimpleStringProperty(description);
    }
    public StringProperty descriptionProperty() {
            return description;
    }


    public int saveData(Connection connection){
        String sql = "INSERT INTO amuyana.tbl_fcc (id_fcc, name, description)"
                    + "VALUES (?,?,?)";
        try {            
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, this.getIdFcc());
            statement.setString(2, this.getName());
            statement.setString(3, this.getDescription());
            
            int result = statement.executeUpdate();
            
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                Fcc.currentAutoIncrement = rs.getInt(1);
            }
            
            return result;
            
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }

    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_fcc SET name = ?,  "+
            " description = ? WHERE id_fcc = ?";
        try {
            PreparedStatement instruccion =
                            connection.prepareStatement(sql);
            instruccion.setString(1, name.get());
            instruccion.setString(2, description.get());
            instruccion.setInt(3, idFcc.get());
            
            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteData(Connection connection){
        try {
                PreparedStatement instruccion = connection.prepareStatement(
                                                "DELETE FROM amuyana.tbl_fcc "+
                                                "WHERE id_fcc = ?"
                );
                instruccion.setInt(1, this.idFcc.get());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                e.printStackTrace();
                return 0;
        }
    }

    public static void loadList(Connection connection, 
            ObservableList<Fcc> listFcc){

        try {

            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(
                "SELECT id_fcc, "
                        + "name, "
                        + "description "
                + "FROM amuyana.tbl_fcc"
            );

            while(result.next()){
                listFcc.add(
                        new Fcc(
                                result.getInt("id_fcc"), 
                                result.getString("name"),
                                result.getString("description")
                        )
                );

            }


        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    @Override
    public String toString(){
        return "(FCC" + this.idFcc.get() + ") " + this.getName();
    }
}
