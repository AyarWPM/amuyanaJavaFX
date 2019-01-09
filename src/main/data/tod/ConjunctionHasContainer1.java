package main.data.tod;

import main.data.tod.containers.Container1;
import javafx.collections.ObservableList;
import java.sql.*;

public class ConjunctionHasContainer1 {
    private Conjunction conjunction;
    private Container1 container1;

    public ConjunctionHasContainer1(Conjunction conjunction, Container1 container1) {
            this.conjunction = conjunction;
            this.container1 = container1;
    }

    //Metodos atributo: container1
    public Container1 getContainer1() {
            return container1;
    }
    public void setContainer1(Container1 container1) {
            this.container1 = container1;
    }
    //Metodos atributo: conjunction
    public Conjunction getConjunction() {
            return conjunction;
    }
    public void setConjunction(Conjunction conjunction) {
            this.conjunction = conjunction;
    }
    
    public static void loadList(Connection connection, 
            ObservableList<ConjunctionHasContainer1> conjunctionHasContainer1s,
            ObservableList<Conjunction> conjunctions,
            ObservableList<Container1> container1s){
        String sql = "SELECT id_conjunction, id_container_1 FROM amuyana.tbl_conjunction_has_tbl_container_1";
        
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);
            while(result.next()){
                for(Conjunction conjunction:conjunctions){
                    if(conjunction.getIdConjunction()==result.getInt("id_conjunction")){
                        for(Container1 l : container1s){
                            if(l.getIdContainer1()==result.getInt("id_container_1")){
                                conjunctionHasContainer1s.add(new ConjunctionHasContainer1(conjunction,l));
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
        String sql="INSERT INTO amuyana.tbl_conjunction_has_tbl_container_1 (id_conjunction, id_container_1) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            
            instruction.setInt(1,this.conjunction.getIdConjunction());
            instruction.setInt(2,this.container1.getIdContainer1());
            
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
                                            "DELETE FROM amuyana.tbl_conjunction_has_tbl_container_1 "+
                                            "WHERE id_conjunction = ? and id_container_1 = ?"
            );
            instruccion.setInt(1, this.conjunction.getIdConjunction());
            instruccion.setInt(2, this.container1.getIdContainer1());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}