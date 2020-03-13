package com.amuyana.app.data.tod.containers;

import com.amuyana.app.data.DataHandler;
import com.amuyana.app.node.NodeHandler;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import java.sql.*;

public class Container0 {
    private IntegerProperty idContainer0;
    public static int currentAutoIncrement;

    public Container0(int idContainer0) {
        this.idContainer0 = new SimpleIntegerProperty(idContainer0);
    }

    //Metodos atributo: idContainer0
    public int getIdContainer0() {
        return idContainer0.get();
    }
    public void setIdContainer0(int idContainer0) {
        this.idContainer0 = new SimpleIntegerProperty(idContainer0);
    }
    public IntegerProperty idContainer0Property() {
        return idContainer0;
    }

    public int saveData(Connection connection){
        String sql = "INSERT INTO amuyana.tbl_container_0 (id_container_0) "
                + "VALUES (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, this.getIdContainer0());

            int result = statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                Container0.currentAutoIncrement = rs.getInt(1);
            }

            return result;

        } catch (SQLException ex) {
            DataHandler.getNodeInterface().logSQLException();
            ex.printStackTrace();
        }

        return 0;
    }

    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_container_0 SET _blank_ = ? WHERE id_container_0 = ?";
        try {
            PreparedStatement instruccion =
                    connection.prepareStatement(sql);
            instruccion.setInt(1, idContainer0.get()); // change these
            instruccion.setInt(2, idContainer0.get()); // change these

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "DELETE FROM amuyana.tbl_container_0 "+
                            "WHERE id_container_0 = ?"
            );
            instruccion.setInt(1, this.idContainer0.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void loadList(Connection connection,
                                ObservableList<Container0> container0s){
        try {

            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(
                    "SELECT id_container_0 "
                            + "FROM amuyana.tbl_container_0"
            );

            while(result.next()){
                container0s.add(new Container0(result.getInt("id_container_0")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}