package com.amuyana.app.data;

import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpaceHasRegister{
	private Register register;
	private Space space;

	public SpaceHasRegister(Space space, Register register) { 
		this.register = register;
		this.space = space;
	}

	//Metodos atributo: register
	public Register getRegister() {
		return register;
	}
	public void setRegister(Register register) {
		this.register = register;
	}
	//Metodos atributo: space
	public Space getSpace() {
		return space;
	}
	public void setSpace(Space space) {
		this.space = space;
	}
        
        
    public static void loadList(Connection connection, 
            ObservableList<SpaceHasRegister> listSpaceHasRegisters,
            ObservableList<Space> listSpaces,
            ObservableList<Register> listRegisters){
        String sql = "SELECT id_space, id_register FROM amuyana.tbl_space_has_tbl_register";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){
                for(Space s: listSpaces){
                    if(s.getIdSpace()==result.getInt("id_space")){
                        for(Register r: listRegisters){
                            if(r.getIdRegister()==result.getInt("id_register")){
                                listSpaceHasRegisters.add(
                                        new SpaceHasRegister(s,r));
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
        String sql="INSERT INTO amuyana.tbl_space_has_tbl_register (id_space, id_register) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            
            instruction.setInt(1,this.getSpace().getIdSpace());
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
        String sql = "DELETE FROM amuyana.tbl_space_has_tbl_register "+
                    "WHERE id_space = ? and id_register = ?";
        try {
                PreparedStatement instruccion = connection.prepareStatement(sql);
                instruccion.setInt(1, this.getSpace().getIdSpace());
                instruccion.setInt(2, this.getRegister().getIdRegister());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                return 0;
        }
    }

    @Override
    public String toString(){
        return "SpaceHasRegister: " + this.getSpace().getIdSpace() + " \"has\" " + this.getRegister().getIdRegister();
    }
}