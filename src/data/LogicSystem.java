package data;

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

public class LogicSystem{

    private IntegerProperty idLogicSystem;
    private StringProperty label;
    private StringProperty description;
    private Timestamp creationDate;
    public static int currentAutoIncrement;

    public LogicSystem(int idLogicSystem, String label, String description, 
            Timestamp creationDate) { 
        this.idLogicSystem = new SimpleIntegerProperty(idLogicSystem);
        this.label = new SimpleStringProperty(label);
        this.description = new SimpleStringProperty(description);
        this.creationDate = creationDate;
    }

    //Metodos atributo: idSystem
    public int getIdLogicSystem() {
            return idLogicSystem.get();
    }
    public void setIdLogicSystem(int idLogicSystem) {
            this.idLogicSystem = new SimpleIntegerProperty(idLogicSystem);
    }
    public IntegerProperty IdLogicSystemProperty() {
            return idLogicSystem;
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
    //Metodos atributo: creationDate
    public Timestamp getCreationDate() {
            return creationDate;
    }
    public void setCreationDate(Timestamp creationDate) {
            this.creationDate = creationDate;
    }

    // Desde aqui nos conectamos a la base de datos, extraemos toda la 
    // información de las tablas y las enviamos ahí donde se necesiten.
    public static void loadList(Connection connection, ObservableList<LogicSystem> listLogicSystems){

        try {
            // MYSQL: vamos a llenar el cobx con los sistemas lógicos

            // esta  no se puede instanciar, para usarla creamos un método especial
            Statement statement = connection.createStatement();

            // clase para almacenar el resultado del query
            // retorna filas y columnas. 
            ResultSet resultado = statement.executeQuery(
                    "SELECT id_logic_system, label, description, creation_date FROM amuyana.tbl_logic_system");

            // el método next hace que se seleccione un registro del 
            // 'resultado', 
            while(resultado.next()){
                listLogicSystems.add(new LogicSystem(
                        resultado.getInt("id_logic_system"), 
                        resultado.getString("label"), 
                        resultado.getString("description"),
                        resultado.getTimestamp("creation_date")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LogicSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString(){
        return this.idLogicSystem.get() + ". " + this.label.get();
    }
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_logic_system (label, description, creation_date)"
                    + "VALUES (?,?,?)";
        try {
            // Cual es la instruction sql para insertar datos?
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setString(1,this.label.get());
            instruction.setString(2,this.description.get());
            instruction.setTimestamp(3,this.creationDate);
            
            
            int result = instruction.executeUpdate();
            
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                LogicSystem.currentAutoIncrement = rs.getInt(1);
            }
            
            return result;
            
        } catch (SQLException ex) {
            Logger.getLogger(LogicSystem.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
                
    }
    
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_logic_system SET label = ?,  "+
            " description = ? WHERE id_logic_system = ?";
        try {
            PreparedStatement instruccion =
                            connection.prepareStatement(sql);
            instruccion.setString(1, label.get());
            instruccion.setString(2, description.get());
            instruccion.setInt(3, idLogicSystem.get());
            
            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        try {
                PreparedStatement instruccion = connection.prepareStatement(
                                                "DELETE FROM amuyana.tbl_logic_system "+
                                                "WHERE id_logic_system = ?"
                );
                instruccion.setInt(1, this.idLogicSystem.get());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                e.printStackTrace();
                return 0;
        }
    }

}