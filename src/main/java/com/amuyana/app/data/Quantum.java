package com.amuyana.app.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Quantum{

    private static int currentAutoIncrement;
	private IntegerProperty idQuantum;
	private StringProperty label;
	private StringProperty description;
	private StringProperty unit;

	public Quantum(int idQuantum, String label, String description, 
String unit) { 
		this.idQuantum = new SimpleIntegerProperty(idQuantum);
		this.label = new SimpleStringProperty(label);
		this.description = new SimpleStringProperty(description);
		this.unit = new SimpleStringProperty(unit);
	}

	//Metodos atributo: idQuantum
	public int getIdQuantum() {
		return idQuantum.get();
	}
	public void setIdQuantum(int idQuantum) {
		this.idQuantum = new SimpleIntegerProperty(idQuantum);
	}
	public IntegerProperty IdQuantumProperty() {
		return idQuantum;
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
	//Metodos atributo: unit
	public String getUnit() {
		return unit.get();
	}
	public void setUnit(String unit) {
		this.unit = new SimpleStringProperty(unit);
	}
	public StringProperty UnitProperty() {
		return unit;
	}
        
        public static void loadList(Connection connection, 
            ObservableList<Quantum> listQuantums){
        String sql = "SELECT id_quantum, "
                    + "label, "
                    + "description, "
                    + "unit "
                    + "FROM " + DataConnection.DATABASE + ".tbl_quantum";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){

                listQuantums.add(
                    new Quantum(
                        result.getInt("id_quantum"), 
                        result.getString("label"),
                        result.getString("description"),
                        result.getString("unit")
                    )
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public int saveData(Connection connection){
        String sql="INSERT INTO " + DataConnection.DATABASE + ".tbl_quantum (id_quantum, label, description, unit) "
                    + "VALUES (?,?,?, ?)";
        try {
            // Cual es la instruction sql para insertar datos?
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,this.getIdQuantum());
            instruction.setString(2,this.getLabel());
            instruction.setString(3,this.getDescription());
            instruction.setString(4,this.getUnit());
            
            int result = instruction.executeUpdate();
            
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                Quantum.currentAutoIncrement = rs.getInt(1);
            }
            
            return result;
            
        } catch (SQLException ex) {
            return 0;
        }
                
    }
    
    public int updateData(Connection connection){
        String sql = "UPDATE " + DataConnection.DATABASE + ".tbl_quantum SET label = ?, "+
            "description = ? ,  unit = ? WHERE id_quantum = ?";
        try {
            PreparedStatement instruccion = connection.prepareStatement(sql);
            
            instruccion.setString(1, this.getLabel());
            instruccion.setString(2, this.getDescription());
            instruccion.setString(3, this.getUnit());
            instruccion.setInt(4, this.getIdQuantum());

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_quantum "+
                    "WHERE id_quantum = ?";
        try {
                PreparedStatement instruccion = connection.prepareStatement(sql);
                instruccion.setInt(1, this.getIdQuantum());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                return 0;
        }
    }

    @Override
    public String toString(){
        return "Quantum: " + this.getLabel();
    }
}