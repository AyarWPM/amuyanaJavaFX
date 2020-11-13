package com.amuyana.app.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Element{

    public static int currentAutoIncrement;
    private IntegerProperty idElement;
    private StringProperty symbol;
    private IntegerProperty polarity;
    private Fcc fcc;

    public Element(int idElement, String symbol, 
int polarity, Fcc fcc) { 
        this.idElement = new SimpleIntegerProperty(idElement);
        this.symbol = new SimpleStringProperty(symbol);
        this.polarity = new SimpleIntegerProperty(polarity);
        this.fcc = fcc;
    }

    //Metodos atributo: idElement
    public int getIdElement() {
            return idElement.get();
    }
    public void setIdElement(int idElement) {
            this.idElement = new SimpleIntegerProperty(idElement);
    }
    public IntegerProperty IdElementProperty() {
            return idElement;
    }
    //Metodos atributo: symbol
    public String getSymbol() {
            return symbol.get();
    }
    public void setSymbol(String symbol) {
            this.symbol = new SimpleStringProperty(symbol);
    }
    public StringProperty symbolProperty() {
            return symbol;
    }

    //Metodos atributo: polarity
    public int getPolarity() {
            return polarity.get();
    }
    public void setPolarity(int polarity) {
            this.polarity = new SimpleIntegerProperty(polarity);
    }
    public IntegerProperty PolarityProperty(){
        return polarity;
    }
    
    //Metodos atributo: idFcc
    public Fcc getFcc() {
            return this.fcc;
    }
    public void setFcc(Fcc fcc) {
            this.fcc = fcc;
    }
    
    public static void loadList(Connection connection, 
            ObservableList<Element> listElement, 
            ObservableList<Fcc> listFcc) {
        try {
                Statement statement = connection.createStatement();

                ResultSet resultado = statement.executeQuery(
                        "SELECT id_element, "
                                + "symbol, "
                                + "polarity, "
                                + "id_fcc "
                                + "FROM " + DataConnection.DATABASE + ".tbl_element"
                );

                while(resultado.next()){
                    for(Fcc f:listFcc){
                        if(resultado.getInt("id_fcc")==f.getIdFcc()){
                            listElement.add(new Element(
                                    resultado.getInt("id_element"), 
                                    resultado.getString("symbol"), 
                                    resultado.getInt("polarity"), 
                                    f)
                            );
                        }
                    }
                        
                }
                
                
            } catch (SQLException ex) {
                Logger.getLogger(Element.class.getName()).log(Level.SEVERE, null, ex);
            }
    }       
    
    public int saveData(Connection connection){
        String sql="INSERT INTO " + DataConnection.DATABASE + ".tbl_element (id_element, symbol, polarity, id_fcc) "
                    + "VALUES (?,?,?,?)";
        try {
            // Cual es la instruction sql para insertar datos?
            PreparedStatement instruction = connection.prepareStatement(sql, 
                    Statement.RETURN_GENERATED_KEYS);
            
            instruction.setInt(1,this.getIdElement());
            instruction.setString(2,this.getSymbol());
            instruction.setInt(3,this.getPolarity());
            instruction.setInt(4,this.getFcc().getIdFcc());
            
            int result = instruction.executeUpdate();
            
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                Element.currentAutoIncrement = rs.getInt(1);
            }
            return result;
            
        } catch (SQLException ex) {
            Logger.getLogger(Element.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
                
    }
    
    public int updateData(Connection connection){
        String sql = "UPDATE " + DataConnection.DATABASE + ".tbl_element SET symbol = ?,  "+
            " polarity = ? WHERE id_element = ?";
        try {
            PreparedStatement instruccion =
                            connection.prepareStatement(sql);
            instruccion.setString(1, symbol.get());
            instruccion.setInt(2, polarity.get());
            instruccion.setInt(3, idElement.get());
            
            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql="DELETE FROM " + DataConnection.DATABASE + ".tbl_element WHERE id_element = ?";
        try {
            PreparedStatement instruccion = connection.prepareStatement(sql);
            instruccion.setInt(1, this.getIdElement());
            return instruccion.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Dynamism.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}