package main.data;

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

public class Dynamism{

    public static int currentAutoIncrement;

    private IntegerProperty idDynamism;
    private IntegerProperty orientation;
    private StringProperty propFormulation;
    private StringProperty description;
    private Fcc fcc;
    
    public Dynamism(int idDynamism, int orientation, String propFormulation, 
String description, Fcc fcc) { 
            this.idDynamism = new SimpleIntegerProperty(idDynamism);
            this.orientation = new SimpleIntegerProperty(orientation);
            this.propFormulation = new SimpleStringProperty(propFormulation);
            this.description = new SimpleStringProperty(description);
            this.fcc = fcc;
    }

    //Metodos atributo: idDynamism
    public int getIdDynamism() {
            return idDynamism.get();
    }
    public void setIdDynamism(int idDynamism) {
            this.idDynamism = new SimpleIntegerProperty(idDynamism);
    }
    public IntegerProperty IdDynamismProperty() {
            return idDynamism;
    }
    //Metodos atributo: orientation
    public int getOrientation() {
            return orientation.get();
    }
    public void setOrientation(int orientation) {
            this.orientation = new SimpleIntegerProperty(orientation);
    }
    public IntegerProperty orientation(){
        return orientation;
    }
    //Metodos atributo: propFormulation
    public String getPropFormulation() {
            return propFormulation.get();
    }
    public void setPropFormulation(String propFormulation) {
            this.propFormulation = new SimpleStringProperty(propFormulation);
    }
    public StringProperty PropFormulationProperty() {
            return propFormulation;
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
    //Metodos atributo: idFcc
    public Fcc getFcc() {
            return this.fcc;
    }
    public void setFcc(Fcc idFcc) {
            this.fcc = idFcc;
    }
    
    public static void loadList(Connection connection,
                                ObservableList<Dynamism> listDynamisms,
                                ObservableList<Fcc> listFcc) {
        String sql = "SELECT id_dynamism, "
                            + "orientation, "
                            + "prop_formulation, "
                            + "description, "
                            + "id_fcc "
                            + "FROM amuyana.tbl_dynamism";
        try {
            Statement instruction = connection.createStatement();
            ResultSet resultado = instruction.executeQuery(sql);

            while(resultado.next()){
                for(Fcc f:listFcc){
                    if(f.getIdFcc()==resultado.getInt("id_fcc")){
                        listDynamisms.add(new Dynamism(
                            resultado.getInt("id_dynamism"),
                            resultado.getInt("orientation"), 
                            resultado.getString("prop_formulation"), 
                            resultado.getString("description"), 
                            f)
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dynamism.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    @Override
    public String toString(){
        return this.getPropFormulation();
    }
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_dynamism (id_dynamism, orientation, prop_formulation, description, id_fcc)"
                    + "VALUES (?,?,?,?,?)";
        try {
            // Cual es la instruction sql para insertar datos?
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,this.getIdDynamism());
            instruction.setInt(2,this.getOrientation());
            instruction.setString(3,this.getPropFormulation());
            instruction.setString(4,this.getDescription());
            instruction.setInt(5,this.getFcc().getIdFcc());
            
            int result = instruction.executeUpdate();
            
            ResultSet rs = instruction.getGeneratedKeys();
            while(rs.next()){
                Dynamism.currentAutoIncrement = rs.getInt(1);
            }
            
            return result;
            
        } catch (SQLException ex) {
            Logger.getLogger(Dynamism.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
                
    }
    
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_dynamism SET prop_formulation = ?,  "+
            " description = ? WHERE id_dynamism = ?";
        try {
            PreparedStatement instruccion =
                            connection.prepareStatement(sql);
            instruccion.setString(1, propFormulation.get());
            instruccion.setString(2, description.get());
            instruccion.setInt(3, idDynamism.get());
            
            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql="DELETE FROM amuyana.tbl_dynamism WHERE id_dynamism = ?";
        try {
            PreparedStatement instruccion = connection.prepareStatement(sql);
            instruccion.setInt(1, this.idDynamism.get());
            int response = instruccion.executeUpdate();
            return response;
        } catch (SQLException ex) {
            Logger.getLogger(Dynamism.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
}
