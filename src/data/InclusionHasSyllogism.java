package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

public class InclusionHasSyllogism{
	private Inclusion inclusion;
	private Syllogism syllogism;

	public InclusionHasSyllogism(Inclusion inclusion, Syllogism syllogism) { 
		this.inclusion = inclusion;
		this.syllogism = syllogism;
	}

	//Metodos atributo: inclusion
	public Inclusion getInclusion() {
		return inclusion;
	}
	public void setInclusion(Inclusion inclusion) {
		this.inclusion = inclusion;
	}
	//Metodos atributo: syllogism
	public Syllogism getSyllogism() {
		return syllogism;
	}
	public void setSyllogism(Syllogism syllogism) {
		this.syllogism = syllogism;
	}
        
        public static void loadList(Connection connection, 
            ObservableList<InclusionHasSyllogism> listInclusionHasSyllogisms,
            ObservableList<Inclusion> listInclusions,
            ObservableList<Syllogism> listSyllogisms){
        String sql = "SELECT id_inclusion, id_syllogism FROM amuyana.tbl_inclusion_has_tbl_syllogism";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){

                for(Inclusion i: listInclusions){
                    if(i.getIdInclusion()==result.getInt("id_inclusion")){
                        for(Syllogism s: listSyllogisms){
                            if(s.getIdSyllogism()==result.getInt("id_syllogism")){
                                listInclusionHasSyllogisms.add(
                                        new InclusionHasSyllogism(i,s));
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_inclusion_has_tbl_syllogism (id_inclusion, id_syllogism)"
                    + "VALUES (?,?)";
        try {
            // Cual es la instruction sql para insertar datos?
            PreparedStatement instruction = connection.prepareStatement(sql);
            
            instruction.setInt(1,this.getInclusion().getIdInclusion());
            instruction.setInt(2,this.getSyllogism().getIdSyllogism());
            
            return instruction.executeUpdate();
            
        } catch (SQLException ex) {
            return 0;
        }
                
    }
    
    public int updateData(Connection connection){
        return saveData(connection);
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_inclusion_has_tbl_syllogism "+
                    "WHERE id_inclusion = ? and id_syllogism = ?";
        try {
                PreparedStatement instruccion = connection.prepareStatement(sql);
                instruccion.setInt(1, this.getInclusion().getIdInclusion());
                instruccion.setInt(2, this.getSyllogism().getIdSyllogism());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                return 0;
        }
    }

    @Override
    public String toString(){
        return "InclusionHasSyllogism: " + this.getInclusion().getIdInclusion() + " \"has\" " + this.getSyllogism().getIdSyllogism();
    }
}