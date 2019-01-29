package com.amuyana.app.data.tod.containers;

import com.amuyana.app.data.LogicSystem;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Tod {
    private IntegerProperty idTod;
    private StringProperty label;
    private LogicSystem logicSystem;
    private Container0 container0;

    public static int currentAutoIncrement;

    public Tod(int idTod, String label, LogicSystem logicSystem, Container0 container0) {
        this.idTod = new SimpleIntegerProperty(idTod);
        this.label = new SimpleStringProperty(label);
        this.logicSystem = logicSystem;
        //this.container0 = container0;
        setContainer0(container0);
    }

    //Metodos atributo: idTod
    public int getIdTod() {
        return idTod.get();
    }
    public void setIdTod(int idTod) {
        this.idTod = new SimpleIntegerProperty(idTod);
    }
    public IntegerProperty idTodProperty() {
        return idTod;
    }

    //Metodos atributo: label
    public String getLabel() {
        return label.get();
    }
    public void setLabel(String label) {
        this.label = new SimpleStringProperty(label);
    }
    public StringProperty labelProperty() {
        return label;
    }
    // Methods attribute: logicSystem
    public LogicSystem getLogicSystem() {
        return this.logicSystem;
    }
    public void setLogicSystem(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
    }
    // Methods attribute: logicSystem
    public Container0 getContainer0() {
        return container0;
    }

    public void setContainer0(Container0 container0) {
        this.container0 = container0;
    }

    public static void loadList(Connection connection,
                                ObservableList<Tod> tods,
                                ObservableList<LogicSystem> logicSystems,
                                ObservableList<Container0> container0s) {
        String sql = "SELECT id_tod, label, id_logic_system, id_container_0 FROM amuyana.tbl_tod";

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                int idTod = result.getInt("id_tod");
                String label = result.getString("label");
                LogicSystem logicSystem = null;
                Container0 container0 = null;
                for (LogicSystem ls : logicSystems) {
                    if (ls.getIdLogicSystem() == result.getInt("id_logic_system")) {
                        logicSystem = ls;
                    }
                }
                for (Container0 c0 : container0s) {
                    if (c0.getIdContainer0() == result.getInt("id_container_0")) {
                        container0 = c0;
                    }
                }
                tods.add(new Tod(idTod, label, logicSystem,container0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_tod (id_tod, label, id_logic_system, id_container_0) "
                + "VALUES (?,?,?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            instruction.setInt(1,this.idTod.get());
            instruction.setString(2,this.label.get());
            instruction.setInt(3,this.logicSystem.getIdLogicSystem());
            instruction.setInt(4,this.container0.getIdContainer0());

            int returnInt = instruction.executeUpdate();
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                Tod.currentAutoIncrement = rs.getInt(1);
            }
            return returnInt;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_tod SET label = ? "
                + "WHERE id_tod = ?";
        try {
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            statement.setString(1, label.get());
            statement.setInt(2, idTod.get());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_tod " +
                "WHERE id_tod = ? ";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            instruction.setInt(1, this.idTod.get());
            return instruction.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString(){
        return this.getLabel();
    }

}
