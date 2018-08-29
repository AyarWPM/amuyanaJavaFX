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

public class Fcc{
    private IntegerProperty idFcc;
    private StringProperty label;
    private StringProperty description;
    public static int currentAutoIncrement;

    public Fcc(int idFcc, String label, String description) { 
            this.idFcc = new SimpleIntegerProperty(idFcc);
            this.label = new SimpleStringProperty(label);
            this.description = new SimpleStringProperty(description);
    }

    //Metodos atributo: idFcc
    public int getIdFcc() {
            return idFcc.get();
    }
    public void setIdFcc(int idFcc) {
            this.idFcc = new SimpleIntegerProperty(idFcc);
    }
    public IntegerProperty IdFccProperty() {
            return idFcc;
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


    public int saveData(Connection connection){
        String sql = "INSERT INTO amuyana.tbl_fcc (id_fcc, label, description)"
                    + "VALUES (?,?,?)";
        try {            
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            
            statement.setInt(1, this.getIdFcc());
            statement.setString(2, this.getLabel());
            statement.setString(3, this.getDescription());
            
            int result = statement.executeUpdate();
            
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                Fcc.currentAutoIncrement = rs.getInt(1);
            }
            
            return result;
            
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    }

    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_fcc SET label = ?,  "+
            " description = ? WHERE id_fcc = ?";
        try {
            PreparedStatement instruccion =
                            connection.prepareStatement(sql);
            instruccion.setString(1, label.get());
            instruccion.setString(2, description.get());
            instruccion.setInt(3, idFcc.get());
            
            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteData(Connection connection){
        try {
                PreparedStatement instruccion = connection.prepareStatement(
                                                "DELETE FROM amuyana.tbl_fcc "+
                                                "WHERE id_fcc = ?"
                );
                instruccion.setInt(1, this.idFcc.get());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                e.printStackTrace();
                return 0;
        }
    }

    public static void loadList(Connection connection, 
            ObservableList<Fcc> listFcc){
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(
                "SELECT id_fcc, "
                        + "label, "
                        + "description "
                + "FROM amuyana.tbl_fcc"
            );

            while(result.next()){

                listFcc.add(
                        new Fcc(
                                result.getInt("id_fcc"), 
                                result.getString("label"),
                                result.getString("description")
                        )
                );

            }
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    @Override
    public String toString(){
        return "(" + this.idFcc.get() + ") " + this.getLabel();
    }
}
