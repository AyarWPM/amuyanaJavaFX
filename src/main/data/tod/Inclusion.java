package main.data.tod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.data.Dynamism;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

public class Inclusion{
    private IntegerProperty idInclusion;
    private Dynamism dynamism;
    
    public static int currentAutoIncrement;

    public Inclusion(){
        
    }
    
    public Inclusion(int idInclusion, Dynamism dynamism) { 
        this.idInclusion = new SimpleIntegerProperty(idInclusion);
        this.dynamism = dynamism;
    }

    //Metodos atributo: idInclusion
    public int getIdInclusion() {
        return idInclusion.get();
    }
    public void setIdInclusion(int idInclusion) {
        this.idInclusion = new SimpleIntegerProperty(idInclusion);
    }
    public IntegerProperty IdInclusionProperty() {
        return idInclusion;
    }
    //Metodos atributo: dynamism
    public Dynamism getDynamism() {
        return dynamism;
    }
    public void setDynamism(Dynamism dynamism) {
        this.dynamism = dynamism;
    }

    public static void loadList(Connection connection,
            ObservableList<Inclusion> listInclusions, 
            ObservableList<Dynamism> listDynamisms 
            ) {
        
        String sql = "SELECT id_inclusion, id_dynamism "
                + "FROM amuyana.tbl_inclusion";
        
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                
                for(Dynamism c:listDynamisms){
                    if(c.getIdDynamism()==result.getInt("id_dynamism")){
                        listInclusions.add(new Inclusion(result.getInt("id_inclusion"),c));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_inclusion (id_inclusion, id_dynamism) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql, 
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,this.idInclusion.get());
            instruction.setInt(2,this.dynamism.getIdDynamism());
            
            int returnInt = instruction.executeUpdate();
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                Inclusion.currentAutoIncrement = rs.getInt(1);
            }
            return returnInt;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_inclusion " +
                        "WHERE id_inclusion = ? and id_dynamism = ? ";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            instruction.setInt(1, this.idInclusion.get());
            instruction.setInt(2, this.dynamism.getIdDynamism());
            return instruction.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
            }
    
    @Override
    public String toString(){
        return "Inclusion: " + this.getIdInclusion();
    }
}