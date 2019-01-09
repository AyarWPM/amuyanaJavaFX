package main.data.tod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.data.Fcc;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

public class CClassHasFcc{

    public static int currentAutoIncrement;
    private IntegerProperty idCClassHasFcc;
    private CClass cClass;
    private Fcc fcc;

    public CClassHasFcc(int idCClassHasFcc, CClass cClass, Fcc fcc) { 
        this.idCClassHasFcc = new SimpleIntegerProperty(idCClassHasFcc);
        this.cClass = cClass;
        this.fcc = fcc;
    }
    //Metodos atributo: idCClassHasFcc
    public int getIdCClassHasFcc() {
        return idCClassHasFcc.get();
    }
    public void setIdCClassHasFcc(int idCClassHasFcc) {
        this.idCClassHasFcc = new SimpleIntegerProperty(idCClassHasFcc);
    }
    public IntegerProperty IdCClassHasFccProperty() {
        return idCClassHasFcc;
    }
    //Metodos atributo: cClass
    public CClass getCClass() {
            return cClass;
    }
    public void setCClass(CClass cClass) {
            this.cClass = cClass;
    }
    //Metodos atributo: fcc
    public Fcc getFcc() {
            return fcc;
    }
    public void setFcc(Fcc fcc) {
            this.fcc = fcc;
    }
    
    public static void loadList(Connection connection, 
            ObservableList<CClassHasFcc> cClassHasFccs,
            ObservableList<CClass> cClasses,
            ObservableList<Fcc> fccs){
        String sql = "SELECT id_c_class_has_fcc, id_c_class, id_fcc "
                + "FROM amuyana.tbl_c_class_has_tbl_fcc";
        
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);
            
            while(result.next()){
                Integer i = result.getInt("id_c_class_has_fcc");
                for(CClass c:cClasses){
                    if(c.getIdCClass()==result.getInt("id_c_class")){
                        for(Fcc f : fccs){
                            if(f.getIdFcc()==result.getInt("id_fcc")){
                                cClassHasFccs.add(new CClassHasFcc(i, c, f));
                            }
                        }
                    }
                }
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CClassHasFcc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_c_class_has_tbl_fcc (id_c_class_has_fcc, id_c_class, id_fcc) "
                    + "VALUES (?,?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,this.idCClassHasFcc.get());
            instruction.setInt(2,this.cClass.getIdCClass());
            instruction.setInt(3,this.fcc.getIdFcc());
            
            int returnInt = instruction.executeUpdate();
            
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                CClassHasFcc.currentAutoIncrement = rs.getInt(1);
                
            }
            return returnInt;
            
        } catch (SQLException ex) {
            Logger.getLogger(CClassHasFcc.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                                            "DELETE FROM amuyana.tbl_c_class_has_tbl_fcc "+
                                            "WHERE id_c_class_has_fcc = ? "
            );
            instruccion.setInt(1, getIdCClassHasFcc());
            
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}