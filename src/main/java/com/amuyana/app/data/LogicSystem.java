package com.amuyana.app.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class LogicSystem{
    private IntegerProperty idLogicSystem;
    private StringProperty label;
    private StringProperty description;
    private Timestamp creationDate;
    public static int currentAutoIncrement;

    public LogicSystem() {
        setIdLogicSystem(0);
        setLabel("New Logic System");
        setDescription("Description of the Logic System, what are the cycles? " +
                "what are the most interesting actualisations/potentialisations?");
        setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
    }

    public LogicSystem(int idLogicSystem, String label, String description,
                       Timestamp creationDate) {
        this.idLogicSystem = new SimpleIntegerProperty(idLogicSystem);
        this.label = new SimpleStringProperty(label);
        this.description = new SimpleStringProperty(description);
        this.creationDate = creationDate;
    }

    //Metodos atributo: idSystem
    public int getIdLogicSystem() {
            return idLogicSystem.get();
    }
    public void setIdLogicSystem(int idLogicSystem) {
            this.idLogicSystem = new SimpleIntegerProperty(idLogicSystem);
    }
    public IntegerProperty idLogicSystemProperty() {
            return idLogicSystem;
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
    //Metodos atributo: creationDate
    public Timestamp getCreationDate() {
            return creationDate;
    }
    public void setCreationDate(Timestamp creationDate) {
            this.creationDate = creationDate;
    }

    public static void loadList(Connection connection, ObservableList<LogicSystem> listLogicSystems){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery(
                    "SELECT id_logic_system, label, description, creation_date FROM amuyana.tbl_logic_system");

            while(resultado.next()){
                listLogicSystems.add(new LogicSystem(
                        resultado.getInt("id_logic_system"), 
                        resultado.getString("label"), 
                        resultado.getString("description"),
                        resultado.getTimestamp("creation_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.idLogicSystem.get() + ". " + this.label.get();
    }
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_logic_system (id_logic_system, label, description, creation_date)"
                    + "VALUES (?,?,?,?)";
        try {
            // Cual es la instruction sql para insertar datos?
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            instruction.setInt(1,this.idLogicSystem.get());
            instruction.setString(2,this.label.get());
            instruction.setString(3,this.description.get());
            instruction.setTimestamp(4,this.creationDate);

            int result = instruction.executeUpdate();
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                LogicSystem.currentAutoIncrement = rs.getInt(1);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
                
    }
    
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_logic_system SET label = ?, description = ? "+
            "WHERE id_logic_system = ?";
        try {
            PreparedStatement preparedStatement =
                            connection.prepareStatement(sql);
            preparedStatement.setString(1, label.get());
            preparedStatement.setString(2, description.get());
            preparedStatement.setInt(3, idLogicSystem.get());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                                            "DELETE FROM amuyana.tbl_logic_system "+
                                            "WHERE id_logic_system = ?"
            );
            instruccion.setInt(1, this.idLogicSystem.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

}