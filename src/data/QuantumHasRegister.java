package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

public class QuantumHasRegister{
	private Quantum quantum;
	private Register register;

	public QuantumHasRegister(Quantum quantum, Register register) { 
		this.quantum = quantum;
		this.register = register;
	}

	//Metodos atributo: quantum
	public Quantum getQuantum() {
		return quantum;
	}
	public void setQuantum(Quantum quantum) {
		this.quantum = quantum;
	}
	//Metodos atributo: register
	public Register getRegister() {
		return register;
	}
	public void setRegister(Register register) {
		this.register = register;
	}
        
        
        public static void loadList(Connection connection, 
            ObservableList<QuantumHasRegister> listQuantumHasRegisters,
            ObservableList<Quantum> listQuantums,
            ObservableList<Register> listRegisters){
        String sql = "SELECT id_quantum, id_register FROM amuyana.tbl_quantum_has_tbl_register";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){

                for(Quantum q: listQuantums){
                    if(q.getIdQuantum()==result.getInt("id_quantum")){
                        for(Register r: listRegisters){
                            if(r.getIdRegister()==result.getInt("id_register")){
                                listQuantumHasRegisters.add(
                                        new QuantumHasRegister(q, r));
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
        String sql="INSERT INTO amuyana.tbl_quantum_has_tbl_register (id_quantum, id_register) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            
            instruction.setInt(1,this.getQuantum().getIdQuantum());
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
        String sql = "DELETE FROM amuyana.tbl_quantum_has_tbl_register "+
                    "WHERE id_quantum = ? and id_register = ?";
        try {
                PreparedStatement instruccion = connection.prepareStatement(sql);
                instruccion.setInt(1, this.getQuantum().getIdQuantum());
                instruccion.setInt(2, this.getRegister().getIdRegister());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                return 0;
        }
    }

    @Override
    public String toString(){
        return "QuantumHasRegister: " + this.getQuantum().getIdQuantum() + " \"has\" " + this.getRegister().getIdRegister();
    }
}