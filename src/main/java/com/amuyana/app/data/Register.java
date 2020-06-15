package com.amuyana.app.data;

import com.amuyana.app.node.NodeHandler;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Register{

    private static int currentAutoIncrement;
	private IntegerProperty idRegister;
	private Dynamism dynamism;
	private Timestamp date;
    private Timestamp start;
    private Timestamp end;

	public Register(int idRegister, Dynamism dynamism, Timestamp date, Timestamp start,Timestamp end) {
		this.idRegister = new SimpleIntegerProperty(idRegister);
		this.dynamism = dynamism;
		this.date = date;
		this.start = start;
		this.end = end;
	}

	//Metodos atributo: idRegister
	public int getIdRegister() {
		return idRegister.get();
	}
	public void setIdRegister(int idRegister) {
		this.idRegister = new SimpleIntegerProperty(idRegister);
	}
    public IntegerProperty idRegisterProperty() {
        return idRegister;
    }

    public Dynamism getDynamism() {
        return dynamism;
    }

    public void setDynamism(Dynamism dynamism) {
        this.dynamism = dynamism;
    }

    //Metodos atributo: date
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}

    public Timestamp getStart() {
        return start;
    }
    public void setStart(Timestamp start) {
        this.start = start;
    }
    public Timestamp getEnd() {
        return end;
    }
    public void setEnd(Timestamp end) {
        this.end = end;
    }
    public static void loadList(Connection connection,
                                ObservableList<Register> listRegisters, ObservableList<Dynamism> listDynamisms){
        String sql = "SELECT id_register, "
                        + "id_dynamism, "
                        + "date, "
                        + "start, "
                        + "end "
                + "FROM amuyana.tbl_register";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){
                for (Dynamism dynamism : listDynamisms) {
                    if (dynamism.getIdDynamism() == result.getInt("id_dynamism")) {
                        listRegisters.add(
                                new Register(
                                        result.getInt("id_register"),
                                        dynamism,
                                        result.getTimestamp("date"),
                                        result.getTimestamp("start"),
                                        result.getTimestamp("end")
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
        String sql="INSERT INTO amuyana.tbl_register (id_register, id_dynamism, date, start, end) "
                    + "VALUES (?,?,?,?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            instruction.setInt(1, this.getIdRegister());
            instruction.setInt(2, this.getDynamism().getIdDynamism());
            instruction.setTimestamp(3,this.getDate());
            instruction.setTimestamp(4,this.getStart());
            instruction.setTimestamp(5,this.getEnd());
            int result = instruction.executeUpdate();
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                Register.currentAutoIncrement = rs.getInt(1);
            }
            return result;
        } catch (SQLException ex) {
            System.err.println(ex);
            return 0;
        }
    }
    /*
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
    }*/
    
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

    public static int getCurrentAutoIncrement() {
        return currentAutoIncrement;
    }

    @Override
    public String toString(){
        return getStart().toLocalDateTime().toString() + " \u2192 " + getEnd().toLocalDateTime().toString();
    }
}