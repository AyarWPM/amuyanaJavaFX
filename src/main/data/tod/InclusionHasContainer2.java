package main.data.tod;

import main.data.tod.containers.Container2;
import javafx.collections.ObservableList;

import java.sql.*;

public class InclusionHasContainer2 {
    private Inclusion inclusion;
    private Container2 container2;

    public InclusionHasContainer2(Inclusion inclusion, Container2 container2) {
            this.inclusion = inclusion;
            this.container2 = container2;
    }

    //Metodos atributo: container2
    public Container2 getContainer2() {
            return container2;
    }
    public void setContainer2(Container2 container2) {
            this.container2 = container2;
    }
    //Metodos atributo: inclusion
    public Inclusion getInclusion() {
            return inclusion;
    }
    public void setInclusion(Inclusion inclusion) {
            this.inclusion = inclusion;
    }

    public static void loadList(Connection connection,
            ObservableList<InclusionHasContainer2> inclusionHasContainer2s,
            ObservableList<Inclusion> inclusions,
            ObservableList<Container2> container2s){
        String sql = "SELECT id_inclusion, id_container_2 FROM amuyana.tbl_inclusion_has_tbl_container_2";

        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);
            while(result.next()){
                for(Inclusion cClass:inclusions){
                    if(cClass.getIdInclusion()==result.getInt("id_inclusion")){
                        for(Container2 l : container2s){
                            if(l.getIdContainer2()==result.getInt("id_container_2")){
                                inclusionHasContainer2s.add(new InclusionHasContainer2(cClass,l));
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
        String sql="INSERT INTO amuyana.tbl_inclusion_has_tbl_container_2 (id_inclusion, id_container_2) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            
            instruction.setInt(1,this.inclusion.getIdInclusion());
            instruction.setInt(2,this.container2.getIdContainer2());
            
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
                                            "DELETE FROM amuyana.tbl_inclusion_has_tbl_container_2 "+
                                            "WHERE id_inclusion = ? and id_container_2 = ?"
            );
            instruccion.setInt(1, this.inclusion.getIdInclusion());
            instruccion.setInt(2, this.container2.getIdContainer2());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}