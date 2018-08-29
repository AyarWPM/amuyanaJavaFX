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

public class CClass{
    private IntegerProperty idCClass;
    private StringProperty label;

    public static int currentAutoIncrement;
    
    public CClass(int idCClass, String label) { 
            this.idCClass = new SimpleIntegerProperty(idCClass);
            this.label = new SimpleStringProperty(label);
    }

    //Metodos atributo: idCClass
    public int getIdCClass() {
            return idCClass.get();
    }
    public void setIdCClass(int idCClass) {
            this.idCClass = new SimpleIntegerProperty(idCClass);
            
    }
    public IntegerProperty IdCClassProperty() {
            return idCClass;
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
            ObservableList<CClass> listCClass
            ) {
        
        String sql = "SELECT id_c_class, label FROM amuyana.tbl_c_class";
        
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()){
                listCClass.add(new CClass(result.getInt("id_c_class"), result.getString("label")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Inclusion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_c_class (id_c_class, label) "
                    + "VALUES (?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql, 
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,this.idCClass.get());
            instruction.setString(2,this.label.get());
            
            int returnInt = instruction.executeUpdate();
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                CClass.currentAutoIncrement = rs.getInt(1);
            }
            return returnInt;
            
        } catch (SQLException ex) {
            Logger.getLogger(FccHasLogicSystem.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_c_class SET label = ? "
                + "WHERE id_c_class = ?";
        try {
            PreparedStatement instruccion =
                            connection.prepareStatement(sql);
            instruccion.setString(1, label.get());
            instruccion.setInt(2, idCClass.get());
            
            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_c_class " +
                        "WHERE id_c_class = ? ";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            instruction.setInt(1, this.idCClass.get());
            return instruction.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CClass.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        
    }
    
    @Override
    public String toString(){
        return this.getLabel();
    }
}