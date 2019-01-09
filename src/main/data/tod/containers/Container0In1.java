package main.data.tod.containers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Container0In1 {
	private Container0 container0;
	private Container1 container1;
	private BooleanProperty side;

	public Container0In1(Container0 container0, Container1 container1, boolean side) {
		this.container0 = container0;
        this.container1 = container1;
        this.side = new SimpleBooleanProperty(side);
	}

	//Metodos atributo: container1
	public Container1 getContainer1() {
		return container1;
	}
	public void setContainer1(Container1 container1) {
		this.container1 = container1;
	}
	//Metodos atributo: container0

    public Container0 getContainer0() {
		return container0;
	}
	public void setContainer0(Container0 container0) {
		this.container0 = container0;
	}
	// Methods attribute: side
    public boolean isSide() {
        return side.get();
    }
    public BooleanProperty sideProperty() {
        return side;
    }
    public void setSide(boolean side) {
        this.side.set(side);
    }

    public static void loadList(Connection connection,
                                ObservableList<Container0In1> container0In1s,
                                ObservableList<Container0> container0s,
                                ObservableList<Container1> container1s){
        String sql = "SELECT id_container_0, id_container_1, side FROM amuyana.tbl_container_0_in_1";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){
                for(Container1 container1: container1s){
                    if(container1.getIdContainer1()==result.getInt("id_container_1")){
                        for(Container0 container0: container0s){
                            if(container0.getIdContainer0()==result.getInt("id_container_0")){
                                boolean side = result.getBoolean("side");
                                container0In1s.add(
                                        new Container0In1(container0,container1, side));
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Container0In1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_container_0_in_1 (id_container_0, id_container_1, side) "
                    + "VALUES (?,?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            instruction.setInt(1,this.getContainer0().getIdContainer0());
            instruction.setInt(2,this.getContainer1().getIdContainer1());
            instruction.setBoolean(3,this.side.get());
            return instruction.executeUpdate();
        } catch (SQLException ex) {
            return 0;
        }
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_container_0_in_1 "+
                    "WHERE id_container_0 = ? and id_container_1 = ?";
        try {
                PreparedStatement instruccion = connection.prepareStatement(sql);
                instruccion.setInt(1, this.getContainer0().getIdContainer0());
                instruccion.setInt(2, this.getContainer1().getIdContainer1());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                return 0;
        }
    }

}