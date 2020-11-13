package com.amuyana.app.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Implication{

    public static int currentAutoIncrement;

    private IntegerProperty idImplication;
    private IntegerProperty orientation;
    private Fcc fcc;

    public Implication(int idImplication, int orientation, Fcc fcc) { 
            this.idImplication = new SimpleIntegerProperty(idImplication);
            this.orientation = new SimpleIntegerProperty(orientation);
            this.fcc = fcc;
    }

    //Metodos atributo: idImplication
    public int getIdImplication() {
            return idImplication.get();
    }
    public void setIdImplication(int idImplication) {
            this.idImplication = new SimpleIntegerProperty(idImplication);
    }
    public IntegerProperty IdImplicationProperty() {
            return idImplication;
    }
    //Metodos atributo: orientation
    public int getOrientation() {
            return orientation.get();
    }
    public void setOrientation(int orientation) {
            this.orientation = new SimpleIntegerProperty(orientation);
    }
    public IntegerProperty OrientationProperty() {
            return this.orientation;
    }
    //Metodos atributo: idFcc
    public Fcc getFcc() {
            return fcc;
    }
    public void setIdFcc(Fcc fcc) {
            this.fcc = fcc;
    }
   

    public static void loadList(Connection connection, 
            ObservableList<Implication> listImplication, 
            ObservableList<Fcc> listFcc) {
        String sql = "SELECT id_implication, "
                + "orientation, "
                + "id_fcc FROM " + DataConnection.DATABASE + ".tbl_implication";
        try {
            Statement statement = connection.createStatement();
            
            ResultSet r = statement.executeQuery(sql);
            
            
            while(r.next()){
                for(Fcc f:listFcc){
                    if(f.getIdFcc()==r.getInt("id_fcc")){
                        listImplication.add(new Implication(
                                r.getInt("id_implication"),
                                r.getInt("orientation"),
                                f));
                    }
                }
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Implication.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    public int saveData(Connection connection){
        String sql = "INSERT INTO " + DataConnection.DATABASE + ".tbl_implication (id_implication, orientation, id_fcc)"
                    + "VALUES (?,?,?)";
        try {            
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, this.getIdImplication());
            statement.setInt(2, this.getOrientation());
            statement.setInt(3, this.getFcc().getIdFcc());
            
            int result = statement.executeUpdate();
            
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                Implication.currentAutoIncrement = rs.getInt(1);
            }
            
            return result;
            
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    @Override
    public String toString(){
        return "Fcc ID: " + this.getFcc().getIdFcc()+ ", Orientation: " + this.getOrientation();
    }
    
    public int deleteData(Connection connection){
        try {
                PreparedStatement instruccion = connection.prepareStatement(
                                                "DELETE FROM " + DataConnection.DATABASE + ".tbl_implication "+
                                                "WHERE id_implication = ?"
                );
                instruccion.setInt(1, this.idImplication.get());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                e.printStackTrace();
                return 0;
        }
    }
}
