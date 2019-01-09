package main.data.tod;

import javafx.collections.ObservableList;

import java.sql.*;

public class ConjunctionHasInclusion {
    private Conjunction conjunction;
    private Inclusion inclusion;

    public ConjunctionHasInclusion(Conjunction conjunction, Inclusion inclusion) {
            this.conjunction = conjunction;
            this.inclusion = inclusion;
    }

    //Metodos atributo: inclusion
    public Inclusion getInclusion() {
            return inclusion;
    }
    public void setInclusion(Inclusion inclusion) {
            this.inclusion = inclusion;
    }
    //Metodos atributo: conjunction
    public Conjunction getConjunction() {
            return conjunction;
    }
    public void setConjunction(Conjunction conjunction) {
            this.conjunction = conjunction;
    }
    
    public static void loadList(Connection connection, 
            ObservableList<ConjunctionHasInclusion> conjunctionHasInclusions,
            ObservableList<Conjunction> conjunctions,
            ObservableList<Inclusion> container1s){
        String sql = "SELECT id_conjunction, id_inclusion FROM amuyana.tbl_conjunction_has_tbl_inclusion";
        
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);
            while(result.next()){
                for(Conjunction conjunction:conjunctions){
                    if(conjunction.getIdConjunction()==result.getInt("id_conjunction")){
                        for(Inclusion l : container1s){
                            if(l.getIdInclusion()==result.getInt("id_inclusion")){
                                conjunctionHasInclusions.add(new ConjunctionHasInclusion(conjunction,l));
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
        String sql="INSERT INTO amuyana.tbl_conjunction_has_tbl_inclusion (id_conjunction, id_inclusion) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            
            instruction.setInt(1,this.conjunction.getIdConjunction());
            instruction.setInt(2,this.inclusion.getIdInclusion());
            
            int returnInt = instruction.executeUpdate();
            return returnInt;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                                            "DELETE FROM amuyana.tbl_conjunction_has_tbl_inclusion "+
                                            "WHERE id_conjunction = ? and id_inclusion = ?"
            );
            instruccion.setInt(1, this.conjunction.getIdConjunction());
            instruccion.setInt(2, this.inclusion.getIdInclusion());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}