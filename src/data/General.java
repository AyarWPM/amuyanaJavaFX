package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

public class General{

    
    private Dynamism dynamism;
    private Inclusion inclusion;

    public General(Dynamism dynamism, Inclusion inclusion) { 
            this.dynamism = dynamism;
            this.inclusion = inclusion;
    }

    //Metodos atributo: idDynamism
    public Dynamism getDynamism() {
            return dynamism;
    }
    public void setDynamism(Dynamism dynamism) {
            this.dynamism = dynamism;
    }
    //Metodos atributo: idInclusion
    public Inclusion getInclusion() {
            return inclusion;
    }
    public void setInclusion(Inclusion inclusion) {
            this.inclusion = inclusion;
    }
 
    public static void loadList(Connection connection,
        ObservableList<General> listGenerals, 
        ObservableList<Dynamism> listDynamisms,
        ObservableList<Inclusion> listInclusions) {

        String sql = "SELECT id_dynamism, id_inclusion "
                + "FROM amuyana.tbl_general";

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                for(Inclusion i:listInclusions){
                    if(i.getIdInclusion()==result.getInt("id_inclusion")){
                        for(Dynamism c:listDynamisms){
                            if(c.getIdDynamism()==result.getInt("id_dynamism")){
                                listGenerals.add(new General(c,i));
                            }
                        }
                    }
                }
                
            }
        } catch (SQLException ex) {

            Logger.getLogger(Inclusion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int saveData(Connection connection){
        String sql = "INSERT INTO amuyana.tbl_general (id_dynamism, id_inclusion)"
                    + "VALUES (?,?)";
        try {            
            PreparedStatement statement = connection.prepareStatement(sql, 
                    Statement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, this.getDynamism().getIdDynamism());
            statement.setInt(2, this.getInclusion().getIdInclusion());
            int returnInt = statement.executeUpdate();
            
//            ResultSet rs = statement.getGeneratedKeys();
//            if(rs.next()){
//                General.currentAutoIncrement = rs.getInt(1);
//            }
            return returnInt;
            
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }
    
    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                                            "DELETE FROM amuyana.tbl_general "+
                                            "WHERE id_dynamism = ? and id_inclusion = ?"
            );
            instruccion.setInt(1, this.dynamism.getIdDynamism());
            instruccion.setInt(2, this.inclusion.getIdInclusion());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int dropData(Connection connection, int idInclusion) {
        String sql = "DELETE FROM amuyana.tbl_general " +
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
}