package com.amuyana.app.data;

import com.amuyana.app.data.tod.containers.Tod;
import javafx.collections.ObservableList;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.data.tod.containers.Tod;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TodHasInclusion {
    private Tod tod;
    private Inclusion inclusion;

    public TodHasInclusion(Tod tod, Inclusion inclusion) {
            this.tod = tod;
            this.inclusion = inclusion;
    }

    //Metodos atributo: idDynamism
    public Tod getTod() {
            return tod;
    }
    public void setTod(Tod tod) {
            this.tod = tod;
    }
    //Metodos atributo: idInclusion
    public Inclusion getInclusion() {
            return inclusion;
    }
    public void setInclusion(Inclusion inclusion) {
            this.inclusion = inclusion;
    }
 
    public static void loadList(Connection connection,
        ObservableList<TodHasInclusion> listTodHasInclusion,
        ObservableList<Tod> listTod,
        ObservableList<Inclusion> listInclusions) {

        String sql = "SELECT id_tod, id_inclusion "
                + "FROM " + DataConnection.DATABASE + ".tbl_tod_has_tbl_inclusion";

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                for(Inclusion i:listInclusions){
                    if(i.getIdInclusion()==result.getInt("id_inclusion")){
                        for(Tod tod:listTod){
                            if(tod.getIdTod()==result.getInt("id_tod")){
                                listTodHasInclusion.add(new TodHasInclusion(tod,i));
                            }
                        }
                    }
                }
                
            }
        } catch (SQLException ex) {

            Logger.getLogger(TodHasInclusion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    public int saveData(Connection connection){
        String sql = "INSERT INTO " + DataConnection.DATABASE + ".tbl_tod (id_dynamism, id_inclusion)"
                    + "VALUES (?,?)";
        try {            
            PreparedStatement statement = connection.prepareStatement(sql, 
                    Statement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, this.getTod().getIdDynamism());
            statement.setInt(2, this.getContainer2().getIdInclusion());

            int returnInt = statement.executeUpdate();

            return returnInt;
            
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                                            "DELETE FROM " + DataConnection.DATABASE + ".tbl_general "+
                                            "WHERE id_dynamism = ? and id_inclusion = ?"
            );
            instruccion.setInt(1, this.tod.getIdDynamism());
            instruccion.setInt(2, this.inclusion.getIdInclusion());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int dropData(Connection connection, int idInclusion) {
        String sql = "DELETE FROM " + DataConnection.DATABASE + ".tbl_general " +
                     "WHERE id_inclusion = ? ";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            
            instruction.setInt(1, idInclusion);

            return instruction.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
     
    public int updateData(Connection connection){
        return saveData(connection);
    }
    */
}