package com.amuyana.app.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Register{

    private static int currentAutoIncrement;
	private IntegerProperty idRegister;
	private Timestamp date;
	private IntegerProperty value;
	private Syllogism syllogism;

	public Register(int idRegister, Timestamp date, int value, 
            Syllogism syllogism) {
		this.idRegister = new SimpleIntegerProperty(idRegister);
		this.date = date;
		this.value = new SimpleIntegerProperty(value);
		this.syllogism = syllogism;
	}

	//Metodos atributo: idRegister
	public int getIdRegister() {
		return idRegister.get();
	}
	public void setIdRegister(int idRegister) {
		this.idRegister = new SimpleIntegerProperty(idRegister);
	}
	public IntegerProperty IdRegisterProperty() {
		return idRegister;
	}
	//Metodos atributo: date
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	//Metodos atributo: value
	public int getValue() {
		return value.get();
	}
	public void setValue(int value) {
		this.value = new SimpleIntegerProperty(value);
	}
	public IntegerProperty ValueProperty() {
		return value;
	}
	//Metodos atributo: syllogism
	public Syllogism getSyllogism() {
		return syllogism;
	}
	public void setSyllogism(Syllogism syllogism) {
		this.syllogism = syllogism;
	}
        
        public static void loadList(Connection connection, 
            ObservableList<Register> listRegisters,
            ObservableList<Syllogism> listSyllogisms){
        String sql = "SELECT id_register, "
                        + "date, "
                        + "value, "
                        + "id_syllogism "
                + "FROM amuyana.tbl_register";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){

                for(Syllogism s:listSyllogisms){
                    if(result.getInt("id_syllogism")==s.getIdSyllogism()){
                        listRegisters.add(
                        new Register(
                                result.getInt("id_register"), 
                                result.getTimestamp("date"),
                                result.getInt("value"),
                                s
                                )
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_register (id_register, date, value, id_syllogism) "
                    + "VALUES (?,?,?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1, this.getIdRegister());
            instruction.setTimestamp(2,this.getDate());
            instruction.setInt(3,this.getValue());
            instruction.setInt(4,this.getSyllogism().getIdSyllogism());
            
            int result = instruction.executeUpdate();
            
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                Register.currentAutoIncrement = rs.getInt(1);
            }
            
            return result;
            
        } catch (SQLException ex) {
            return 0;
        }
                
    }
    
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_register SET value = ? WHERE id_register = ?";
        try {
            PreparedStatement instruccion =
                            connection.prepareStatement(sql);
            instruccion.setInt(1, this.getValue());
            instruccion.setInt(2, this.getIdRegister());
            
            return instruccion.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_register "+
                    "WHERE id_register = ?";
        try {
                PreparedStatement instruccion = connection.prepareStatement(sql);
                instruccion.setInt(1, this.getIdRegister());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                return 0;
        }
    }

    @Override
    public String toString(){
        return "Register: " + this.getIdRegister();
    }
}