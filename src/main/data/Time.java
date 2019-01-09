package main.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Time{

    private static int currentAutoIncrement;
	private IntegerProperty idTime;
	private StringProperty label;
	private StringProperty description;
	private Timestamp start;
	private Timestamp end;

	public Time(int idTime, String label, String description, Timestamp start, 
Timestamp end) { 
		this.idTime = new SimpleIntegerProperty(idTime);
		this.label = new SimpleStringProperty(label);
		this.description = new SimpleStringProperty(description);
		this.start = start;
		this.end = end;
	}

	//Metodos atributo: idTime
	public int getIdTime() {
		return idTime.get();
	}
	public void setIdTime(int idTime) {
		this.idTime = new SimpleIntegerProperty(idTime);
	}
	public IntegerProperty IdTimeProperty() {
		return idTime;
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
	//Metodos atributo: start
	public Timestamp getStart() {
		return start;
	}
	public void setStart(Timestamp start) {
		this.start = start;
	}
	//Metodos atributo: end
	public Timestamp getEnd() {
		return end;
	}
	public void setEnd(Timestamp end) {
		this.end = end;
	}
        public static void loadList(Connection connection, 
            ObservableList<Time> listTimes){
        String sql = "SELECT id_time, "
                        + "label, "
                        + "description, "
                        + "start, "
                        + "end "
                + "FROM amuyana.tbl_time";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){

                listTimes.add(
                        new Time(
                                result.getInt("id_time"), 
                                result.getString("label"),
                                result.getString("description"),
                                result.getTimestamp("start"),
                                result.getTimestamp("end")
                        )
                );

            }
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_time (id_time, label, description, start, end )"
                    + "VALUES (?,?,?, ?, ?)";
        try {
            // Cual es la instruction sql para insertar datos?
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,this.getIdTime());
            instruction.setString(2, this.getLabel());
            instruction.setString(3, this.getDescription());
            instruction.setTimestamp(4,this.getStart());
            instruction.setTimestamp(5,this.getEnd());
            
            int result = instruction.executeUpdate();
            
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                Time.currentAutoIncrement = rs.getInt(1);
            }
            
            return result;
            
        } catch (SQLException ex) {
            return 0;
        }
                
    }
    
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_time SET label = ?,  "+
            " description = ?, start = ?, end = ? WHERE id_time = ?";
        try {
            PreparedStatement instruccion =
                            connection.prepareStatement(sql);
            instruccion.setInt(1,this.getIdTime());
            instruccion.setString(2,this.getLabel());
            instruccion.setString(3,this.getDescription());
            instruccion.setTimestamp(4,this.getStart());
            instruccion.setTimestamp(5,this.getEnd());
            
            return instruccion.executeUpdate();

        } catch (SQLException e) {
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_time "+
                    "WHERE id_time = ?";
        try {
                PreparedStatement instruccion = connection.prepareStatement(sql);
                instruccion.setInt(1, this.getIdTime());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                return 0;
        }
    }

    @Override
    public String toString(){
        return "Time: " +  this.getLabel();
    }
}
