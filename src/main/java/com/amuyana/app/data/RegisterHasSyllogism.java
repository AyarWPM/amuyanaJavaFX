package com.amuyana.app.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterHasSyllogism {
    private Register register;
	private Syllogism syllogism;

	public RegisterHasSyllogism(Register register, Syllogism syllogism) {
        this.register = register;
        this.syllogism = syllogism;
	}

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public Syllogism getSyllogism() {
        return syllogism;
    }

    public void setSyllogism(Syllogism syllogism) {
        this.syllogism = syllogism;
    }

    public static void loadList(Connection connection,
                                ObservableList<RegisterHasSyllogism> listRegisterHasSyllogism, ObservableList<Register> registers, ObservableList<Syllogism> syllogisms){
        String sql = "SELECT id_register, "
                        + "id_syllogism "
                + "FROM amuyana.tbl_register_has_tbl_syllogism";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);
            Register register=null;
            Syllogism syllogism=null;
            while(result.next()){
                for (Register register1 : registers) {
                    if (result.getInt("id_register") == register1.getIdRegister()) {
                        register=register1;
                    }
                }
                for (Syllogism syllogism1 : syllogisms) {
                    if (result.getInt("id_syllogism") == syllogism1.getIdSyllogism()) {
                        syllogism = syllogism1;
                    }
                }
                listRegisterHasSyllogism.add(
                        new RegisterHasSyllogism(register,syllogism));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_register_has_tbl_syllogism (id_register, id_syllogism) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            instruction.setInt(1, this.getRegister().getIdRegister());
            instruction.setInt(2,this.getSyllogism().getIdSyllogism());
            return instruction.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FccHasLogicSystem.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_register_has_tbl_syllogism "+
                    "WHERE id_register = ? and id_syllogism=?";
        try {
                PreparedStatement instruccion = connection.prepareStatement(sql);
                instruccion.setInt(1, this.getRegister().getIdRegister());
                instruccion.setInt(2, this.getSyllogism().getIdSyllogism());

                return instruccion.executeUpdate();
        } catch (SQLException e) {
                return 0;
        }
    }

    @Override
    public String toString(){
        return "Register " + this.getRegister().getIdRegister() + " AND Syllogism " + this.getSyllogism().getIdSyllogism();
    }
}