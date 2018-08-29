package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;


public class FccHasLogicSystem{
    private Fcc fcc;
    private LogicSystem logicSystem;

    public FccHasLogicSystem(Fcc fcc, LogicSystem logicSystem) { 
            this.fcc = fcc;
            this.logicSystem = logicSystem;
    }

    //Metodos atributo: logicSystem
    public LogicSystem getLogicSystem() {
            return logicSystem;
    }
    public void setLogicSystem(LogicSystem logicSystem) {
            this.logicSystem = logicSystem;
    }
    //Metodos atributo: fcc
    public Fcc getFcc() {
            return fcc;
    }
    public void setFcc(Fcc fcc) {
            this.fcc = fcc;
    }
    
    public static void loadList(Connection connection, 
            ObservableList<FccHasLogicSystem> listFccHasLogicSystem,
            ObservableList<Fcc> listFcc,
            ObservableList<LogicSystem> listLogicSystem){
        String sql = "SELECT id_fcc, id_logic_system FROM amuyana.tbl_fcc_has_tbl_logic_system";
        
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);
            
            while(result.next()){
                for(Fcc f:listFcc){
                    if(f.getIdFcc()==result.getInt("id_fcc")){
                        for(LogicSystem l : listLogicSystem){
                            if(l.getIdLogicSystem()==result.getInt("id_logic_system")){
                                listFccHasLogicSystem.add(new FccHasLogicSystem(f,l));
                            }
                        }
                    }
                }
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(FccHasLogicSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_fcc_has_tbl_logic_system (id_fcc, id_logic_system) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            
            instruction.setInt(1,this.fcc.getIdFcc());
            instruction.setInt(2,this.logicSystem.getIdLogicSystem());
            
            int returnInt = instruction.executeUpdate();
            return returnInt;
            
        } catch (SQLException ex) {
            Logger.getLogger(FccHasLogicSystem.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                                            "DELETE FROM amuyana.tbl_fcc_has_tbl_logic_system "+
                                            "WHERE id_fcc = ? and id_logic_system = ?"
            );
            instruccion.setInt(1, this.fcc.getIdFcc());
            instruccion.setInt(2, this.logicSystem.getIdLogicSystem());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}