package com.amuyana.app.data;

import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeHasRegister{
	private Register register;
	private Time time;

	public TimeHasRegister(Time time, Register register) { 
		this.register = register;
		this.time = time;
	}

	//Metodos atributo: register
	public Register getRegister() {
		return register;
	}
	public void setRegister(Register register) {
		this.register = register;
	}
	//Metodos atributo: time
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
    public static void loadList(Connection connection, 
            ObservableList<TimeHasRegister> listTimeHasRegisters,
            ObservableList<Time> listTimes,
            ObservableList<Register> listRegisters){
        String sql = "SELECT id_time, id_register FROM " + DataConnection.DATABASE + ".tbl_time_has_tbl_register";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){
                for(Time t: listTimes){
                    if(t.getIdTime()==result.getInt("id_time")){
                        for(Register r: listRegisters){
                            if(r.getIdRegister()==result.getInt("id_register")){
                                listTimeHasRegisters.add(
                                        new TimeHasRegister(t,r));
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public int saveData(Connection connection){
        String sql="INSERT INTO " + DataConnection.DATABASE + ".tbl_time_has_tbl_register (id_time, id_register) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            
            instruction.setInt(1,this.getTime().getIdTime());
            instruction.setInt(2,this.getRegister().getIdRegister());
            
            return instruction.executeUpdate();
            
        } catch (SQLException ex) {
            return 0;
        }
                
    }
    
    public int updateData(Connection connection){
        return saveData(connection);
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM " + DataConnection.DATABASE + ".tbl_time_has_tbl_register "+
                    "WHERE id_time = ? and id_register = ?";
        try {
                PreparedStatement instruccion = connection.prepareStatement(sql);
                instruccion.setInt(1, this.getTime().getIdTime());
                instruccion.setInt(2, this.getRegister().getIdRegister());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                return 0;
        }
    }

    @Override
    public String toString(){
        return "TimeHasRegister: " + this.getTime().getIdTime() + " \"has\" " + this.getRegister().getIdRegister();
    }
}