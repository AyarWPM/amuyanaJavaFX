package com.amuyana.app.data.tod.containers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import com.amuyana.app.data.Fcc;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Container2 {
    public static int currentAutoIncrement;
    private IntegerProperty idContainer2;
    private Fcc fcc;
    private Container1 container1;

    public Container2(int idContainer2, Fcc fcc, Container1 container1) {
        this.idContainer2 = new SimpleIntegerProperty(idContainer2);
        this.fcc = fcc;
        this.container1 = container1;
    }

    //Metodos atributo: idContainer2
    public int getIdContainer2() {
        return idContainer2.get();
    }
    public void setIdContainer2(int idContainer2) {
        this.idContainer2 = new SimpleIntegerProperty(idContainer2);
    }
    public IntegerProperty idContainer2Property() {
        return idContainer2;
    }

    public Fcc getFcc() {
        return fcc;
    }
    public void setFcc(Fcc fcc) {
        this.fcc = fcc;
    }

    public Container1 getContainer1() {
        return container1;
    }

    public void setContainer1(Container1 container1) {
        this.container1 = container1;
    }

    public int saveData(Connection connection){
        String sql = "INSERT INTO amuyana.tbl_container_2 (id_container_2, id_fcc, id_container_1) "
                + "VALUES (?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, this.getIdContainer2());
            statement.setInt(2, this.fcc.getIdFcc());
            statement.setInt(3, this.container1.getIdContainer1());
            int result = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                Container2.currentAutoIncrement = rs.getInt(1);
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    // Maybe not going to be used...
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_container_2 SET  _blank_ = ? WHERE id_container_2 = ?";
        try {
            PreparedStatement instruccion =
                    connection.prepareStatement(sql);
            instruccion.setInt(1, idContainer2.get()); // change these
            instruccion.setInt(2, idContainer2.get()); // change these

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "DELETE FROM amuyana.tbl_container_2 "+
                            "WHERE id_container_2 = ?"
            );
            instruccion.setInt(1, this.idContainer2.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void loadList(Connection connection,
                                ObservableList<Container2> container2s,
                                ObservableList<Fcc> fccs,
                                ObservableList<Container1> container1s){
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(
                    "SELECT id_container_2, id_fcc, id_container_1 "
                            + "FROM amuyana.tbl_container_2"
            );

            while(result.next()){
                for (Fcc fcc : fccs) {
                    if (fcc.getIdFcc() == result.getInt("id_fcc")) {
                        for (Container1 container1 : container1s) {
                            if (container1.getIdContainer1() == result.getInt("id_container_1")) {
                                container2s.add(
                                        new Container2(result.getInt("id_container_2"),fcc,container1)
                                );
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
