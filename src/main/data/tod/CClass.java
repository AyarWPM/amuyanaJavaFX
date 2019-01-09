package main.data.tod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.data.Fcc;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class CClass{
    private IntegerProperty idCClass;
    private StringProperty label;
    private Fcc fcc;

    public static int currentAutoIncrement;
    
    public CClass(int idCClass, String label, Fcc fcc) {
            this.idCClass = new SimpleIntegerProperty(idCClass);
            this.label = new SimpleStringProperty(label);
            this.fcc = fcc;
    }

    //Metodos atributo: idCClass
    public int getIdCClass() {
            return idCClass.get();
    }
    public void setIdCClass(int idCClass) {
            this.idCClass = new SimpleIntegerProperty(idCClass);
            
    }
    public IntegerProperty idCClassProperty() {
        return idCClass;
    }

    //Metodos atributo: label
    public String getLabel() {
            return label.get();
    }
    public void setLabel(String label) {
            this.label = new SimpleStringProperty(label);
    }

    public StringProperty labelProperty() {
        return label;
    }

    public Fcc getFcc() {
        return fcc;
    }
    public void setFcc(Fcc fcc) {
        this.fcc = fcc;
    }

    public static void loadList(Connection connection,
                                ObservableList<CClass> cClasses,
                                ObservableList<Fcc> fccs
            ) {
        
        String sql = "SELECT id_c_class, label, id_fcc FROM amuyana.tbl_c_class";
        
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            Fcc newFcc = null;


            while(result.next()){
                for (Fcc fcc : fccs) {
                    if (fcc.getIdFcc() == result.getInt("id_fcc")) {
                        newFcc=fcc;
                    }
                }
                cClasses.add(new CClass(result.getInt("id_c_class"), result.getString("label"), newFcc));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_c_class (id_c_class, label, id_fcc) "
                    + "VALUES (?,?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql, 
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,this.idCClass.get());
            instruction.setString(2,this.label.get());
            instruction.setInt(3,this.fcc.getIdFcc());
            
            int returnInt = instruction.executeUpdate();
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                CClass.currentAutoIncrement = rs.getInt(1);
            }
            return returnInt;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_c_class SET label = ?, id_fcc = ? "
                + "WHERE id_c_class = ?";
        try {
            PreparedStatement instruccion =
                            connection.prepareStatement(sql);
            instruccion.setString(1, label.get());
            instruccion.setInt(2, fcc.getIdFcc());
            instruccion.setInt(3, idCClass.get());
            
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