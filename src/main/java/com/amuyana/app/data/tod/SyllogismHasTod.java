package com.amuyana.app.data.tod;

import javafx.collections.ObservableList;
import com.amuyana.app.data.Syllogism;
import com.amuyana.app.data.tod.containers.Tod;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SyllogismHasTod{
    private Syllogism syllogism;
    private Tod tod;

    public SyllogismHasTod(Syllogism syllogism, Tod tod) {
        this.syllogism = syllogism;
        this.tod = tod;
    }

    //Metodos atributo: syllogism
    public Syllogism getSyllogism() {
        return syllogism;
    }
    public void setSyllogism(Syllogism syllogism) {
        this.syllogism = syllogism;
    }
    //Metodos atributo: tod
    public Tod getTod() {
        return tod;
    }
    public void setTod(Tod tod) {
        this.tod = tod;
    }

    public static void loadList(Connection connection,
                                ObservableList<SyllogismHasTod> syllogismHasTods,
                                ObservableList<Syllogism> syllogisms,
                                ObservableList<Tod> tods){
        String sql = "SELECT id_syllogism, id_tod FROM amuyana.tbl_syllogism_has_tbl_tod";

        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){
                for(Syllogism syllogism:syllogisms){
                    if(syllogism.getIdSyllogism()==result.getInt("id_syllogism")){
                        for(Tod tod : tods){
                            if(tod.getIdTod()==result.getInt("id_tod")){
                                syllogismHasTods.add(new SyllogismHasTod(syllogism,tod));
                            }
                        }
                    }
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(SyllogismHasTod.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_syllogism_has_tbl_tod (id_syllogism, id_tod) "
                + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);

            instruction.setInt(1,this.syllogism.getIdSyllogism());
            instruction.setInt(2,this.tod.getIdTod());

            int returnInt = instruction.executeUpdate();
            return returnInt;

        } catch (SQLException ex) {
            Logger.getLogger(SyllogismHasTod.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int deleteData(Connection connection){
        try {
            PreparedStatement instruction = connection.prepareStatement(
                    "DELETE FROM amuyana.tbl_syllogism_has_tbl_tod "+
                            "WHERE id_syllogism = ? and id_tod = ?"
            );
            instruction.setInt(1, this.syllogism.getIdSyllogism());
            instruction.setInt(2, this.tod.getIdTod());
            return instruction.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SyllogismHasTod.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}

