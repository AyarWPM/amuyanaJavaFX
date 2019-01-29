package com.amuyana.app.data.tod;

import com.amuyana.app.data.Dynamism;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class Inclusion{
    private IntegerProperty idInclusion;
    private Dynamism particular;
    private Dynamism general;
    
    public static int currentAutoIncrement;

    public Inclusion(int idInclusion, Dynamism particular, Dynamism general) {
        this.idInclusion = new SimpleIntegerProperty(idInclusion);
        this.particular = particular;
        this.general = general;
    }

    public int getIdInclusion() {
        return idInclusion.get();
    }

    public IntegerProperty idInclusionProperty() {
        return idInclusion;
    }

    public void setIdInclusion(int idInclusion) {
        this.idInclusion.set(idInclusion);
    }

    public Dynamism getParticular() {
        return particular;
    }

    public void setParticular(Dynamism particular) {
        this.particular = particular;
    }

    public Dynamism getGeneral() {
        return general;
    }

    public void setGeneral(Dynamism general) {
        this.general = general;
    }

    public static void loadList(Connection connection,
                                ObservableList<Inclusion> listInclusions,
                                ObservableList<Dynamism> listDynamisms
            ) {
        
        String sql = "SELECT id_inclusion, id_particular, id_general "
                + "FROM amuyana.tbl_inclusion";
        
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                for(Dynamism possibleParticular:listDynamisms){
                    if(possibleParticular.getIdDynamism()==result.getInt("id_particular")){
                        for(Dynamism possibleGeneral:listDynamisms) {
                            if (possibleGeneral.getIdDynamism() == result.getInt("id_general")) {
                                listInclusions.add(
                                        new Inclusion(result.getInt(
                                                "id_inclusion"),
                                                possibleParticular,
                                                possibleGeneral
                                        )
                                );
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_inclusion (id_inclusion, id_particular, id_general) "
                    + "VALUES (?,?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql, 
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,this.idInclusion.get());
            instruction.setInt(2,this.particular.getIdDynamism());
            instruction.setInt(3,this.general.getIdDynamism());
            
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
                        "WHERE id_inclusion = ?";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            instruction.setInt(1, this.idInclusion.get());
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