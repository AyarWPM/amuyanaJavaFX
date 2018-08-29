package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Syllogism{

    public static int currentAutoIncrement;
	private IntegerProperty idSyllogism;
	private StringProperty label;

	public Syllogism(int idSyllogism, String label) { 
		this.idSyllogism = new SimpleIntegerProperty(idSyllogism);
		this.label = new SimpleStringProperty(label);
	}

	//Metodos atributo: idSyllogism
	public int getIdSyllogism() {
		return idSyllogism.get();
	}
	public void setIdSyllogism(int idSyllogism) {
		this.idSyllogism = new SimpleIntegerProperty(idSyllogism);
	}
	public IntegerProperty IdSyllogismProperty() {
		return idSyllogism;
	}
	//Metodos atributo: label
	public String getLabel() {
		return label.get();
	}
	public void setLabel(String label) {
		this.label = new SimpleStringProperty(label);
	}
	public StringProperty LabelProperty() {
		return label;
	}
        
    public static void loadList(Connection connection, 
            ObservableList<Syllogism> listSyllogisms){
        String sql = "SELECT id_syllogism, label FROM amuyana.tbl_syllogism";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){
                listSyllogisms.add(
                        new Syllogism(
                                result.getInt("id_syllogism"), 
                                result.getString("label")
                        )
                );

            }
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_syllogism (id_syllogism, label)"
                    + "VALUES (?,?)";
        try {
            // Cual es la instruction sql para insertar datos?
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,idSyllogism.get());
            instruction.setString(2,label.get());
            
            int result = instruction.executeUpdate();
            
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                Syllogism.currentAutoIncrement = rs.getInt(1);
            }
            
            return result;
            
        } catch (SQLException ex) {
            return 0;
        }
                
    }
    
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_syllogism SET label = ? "
                + "WHERE id_syllogism = ?";
        try {
            PreparedStatement instruccion =
                            connection.prepareStatement(sql);
            instruccion.setString(1, this.getLabel());
            instruccion.setInt(2, this.getIdSyllogism());
            
            return instruccion.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_syllogism "+
                    "WHERE id_ = ?";
        try {
                PreparedStatement instruccion = connection.prepareStatement(sql);
                instruccion.setInt(1, this.getIdSyllogism());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                return 0;
        }
    }

    @Override
    public String toString(){
        return "Syllogism: " + this.getLabel();
    }
}