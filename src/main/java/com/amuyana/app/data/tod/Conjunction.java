package com.amuyana.app.data.tod;

import com.amuyana.app.data.DataConnection;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class Conjunction{

    public static int currentAutoIncrement;
    private IntegerProperty idConjunction;
    private StringProperty name;

    public Conjunction(int idConjunction, String name) {
        this.idConjunction = new SimpleIntegerProperty(idConjunction);
        this.name = new SimpleStringProperty(name);
    }

    //Metodos atributo: idConjunction
    public int getIdConjunction() {
        return idConjunction.get();
    }
    public void setIdConjunction(int idConjunction) {
        this.idConjunction = new SimpleIntegerProperty(idConjunction);
    }
    public IntegerProperty idConjunctionProperty() {
        return idConjunction;
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

    public static void loadList(Connection connection,
                                ObservableList<Conjunction> listConjunctions){
        String sql = "SELECT id_conjunction, name FROM " + DataConnection.DATABASE + ".tbl_conjunction";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){
                listConjunctions.add(
                        new Conjunction(
                                result.getInt("id_conjunction"),
                                result.getString("name")
                        )
                );

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int saveData(Connection connection){
        String sql="INSERT INTO " + DataConnection.DATABASE + ".tbl_conjunction (id_conjunction, name)"
                + "VALUES (?,?)";
        try {
            // Cual es la instruction sql para insertar datos?
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            instruction.setInt(1, idConjunction.get());
            instruction.setString(2, name.get());

            int result = instruction.executeUpdate();

            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                Conjunction.currentAutoIncrement = rs.getInt(1);
            }

            return result;

        } catch (SQLException ex) {
            return 0;
        }

    }

    public int updateData(Connection connection){
        String sql = "UPDATE " + DataConnection.DATABASE + ".tbl_conjunction SET name = ? "
                + "WHERE id_conjunction = ?";
        try {
            PreparedStatement instruccion =
                    connection.prepareStatement(sql);
            instruccion.setString(1, this.getName());
            instruccion.setInt(2, this.getIdConjunction());

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }

    public int deleteData(Connection connection){
        String sql = "DELETE FROM " + DataConnection.DATABASE + ".tbl_conjunction "+
                "WHERE id_conjunction = ?";
        try {
            PreparedStatement instruccion = connection.prepareStatement(sql);
            instruccion.setInt(1, this.getIdConjunction());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public String toString(){
        return "Conjunction: " + this.getName();
    }
}