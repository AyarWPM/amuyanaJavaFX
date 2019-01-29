package com.amuyana.app.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Space{

    private static int currentAutoIncrement;
	private IntegerProperty idSpace;
	private StringProperty label;
	private StringProperty description;

	public Space(int idSpace, String label, String description) { 
		this.idSpace = new SimpleIntegerProperty(idSpace);
		this.label = new SimpleStringProperty(label);
		this.description = new SimpleStringProperty(description);
	}

	//Metodos atributo: idSpace
	public int getIdSpace() {
		return idSpace.get();
	}
	public void setIdSpace(int idSpace) {
		this.idSpace = new SimpleIntegerProperty(idSpace);
	}
	public IntegerProperty IdSpaceProperty() {
		return idSpace;
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
	//Metodos atributo: description
	public String getDescription() {
		return description.get();
	}
	public void setDescription(String description) {
		this.description = new SimpleStringProperty(description);
	}
	public StringProperty DescriptionProperty() {
		return description;
	}
        public static void loadList(Connection connection, 
            ObservableList<Space> listSpaces){
        String sql = "SELECT id_space, "
                        + "label, "
                        + "description "
                + "FROM amuyana.tbl_space";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){

                listSpaces.add(
                        new Space(
                                result.getInt("id_space"), 
                                result.getString("label"),
                                result.getString("description")
                        )
                );

            }
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_space (id_space, label, description)"
                    + "VALUES (?,?,?)";
        try {
            // Cual es la instruction sql para insertar datos?
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,this.getIdSpace());
            instruction.setString(2,this.getLabel());
            instruction.setString(3, this.getDescription());
            
            int result = instruction.executeUpdate();
            
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                Space.currentAutoIncrement = rs.getInt(1);
            }
            
            return result;
            
        } catch (SQLException ex) {
            return 0;
        }
                
    }
    
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_space SET label = ?,  "+
            " description = ? WHERE id_space = ?";
        try {
            PreparedStatement instruccion =
                            connection.prepareStatement(sql);
            instruccion.setString(1, this.getLabel());
            instruccion.setString(2, this.getDescription());
            instruccion.setInt(3, this.getIdSpace());
            
            return instruccion.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_space "+
                    "WHERE id_space = ?";
        try {
                PreparedStatement instruccion = connection.prepareStatement(sql);
                instruccion.setInt(1, this.getIdSpace());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                return 0;
        }
    }

    @Override
    public String toString(){
        return "Space: " + this.getLabel();
    }
}