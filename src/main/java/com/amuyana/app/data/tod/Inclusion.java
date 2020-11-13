package com.amuyana.app.data.tod;

import com.amuyana.app.data.DataConnection;
import com.amuyana.app.data.DataHandler;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.tod.containers.Tod;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class Inclusion{
    private IntegerProperty idInclusion;
    private Dynamism particular;
    private Dynamism general;
    private Tod tod;
    
    public static int currentAutoIncrement;

    public Inclusion(int idInclusion, Dynamism particular, Dynamism general, Tod tod) {
        this.idInclusion = new SimpleIntegerProperty(idInclusion);
        this.particular = particular;
        this.general = general;
        this.tod = tod;
    }

    public int getIdInclusion() {
        return idInclusion.get();
    }

    public IntegerProperty idInclusionProperty() {
        return idInclusion;
    }

    private void setIdInclusion(int idInclusion) {
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

    public Tod getTod() {
        return tod;
    }

    public void setTod(Tod tod) {
        this.tod = tod;
    }

    public static void loadList(Connection connection,
                                ObservableList<Inclusion> listInclusions,
                                ObservableList<Dynamism> listDynamisms,
                                ObservableList<Tod> listTods) {
        
        String sql = "SELECT id_inclusion, id_particular, id_general, id_tod "
                + "FROM " + DataConnection.DATABASE + ".tbl_inclusion";
        
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                for (Tod tod : listTods) {
                    if (tod.getIdTod() == result.getInt("id_tod")) {
                        for(Dynamism possibleParticular:listDynamisms){
                            if(possibleParticular.getIdDynamism()==result.getInt("id_particular")){
                                for(Dynamism possibleGeneral:listDynamisms) {
                                    if (possibleGeneral.getIdDynamism() == result.getInt("id_general")) {
                                        listInclusions.add(
                                                new Inclusion(result.getInt(
                                                        "id_inclusion"),
                                                        possibleParticular,
                                                        possibleGeneral,
                                                        tod
                                                )
                                        );
                                    }
                                }
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
        String sql="INSERT INTO " + DataConnection.DATABASE + ".tbl_inclusion (id_inclusion, id_particular, id_general, id_tod) "
                    + "VALUES (?,?,?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql, 
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,this.idInclusion.get());
            instruction.setInt(2,this.particular.getIdDynamism());
            instruction.setInt(3,this.general.getIdDynamism());
            instruction.setInt(4,this.tod.getIdTod());

            
            int returnInt = instruction.executeUpdate();
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                Inclusion.currentAutoIncrement = rs.getInt(1);
                setIdInclusion(Inclusion.currentAutoIncrement);
            }
            return returnInt;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM " + DataConnection.DATABASE + ".tbl_inclusion " +
                        "WHERE id_inclusion = ?";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            instruction.setInt(1, this.idInclusion.get());
            return instruction.executeUpdate();
        } catch (SQLException e) {
            DataHandler.getNodeInterface().logSQLException();
            e.printStackTrace();
            return 0;
        }
            }
    
    @Override
    public String toString(){
        return this.getParticular() + " ( " + this.getGeneral();
    }
}