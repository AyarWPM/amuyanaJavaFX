package com.amuyana.app.data.tod.containers;

import com.amuyana.app.data.DataConnection;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;

import java.sql.*;

public class Container0In2 {
	private Container0 container0;
    private Container2 container2;
    private BooleanProperty side;

	public Container0In2(Container0 container0,Container2 container2, boolean side) {
		this.container0 = container0;
		this.container2 = container2;
		this.side = new SimpleBooleanProperty(side);
	}

	//Metodos atributo: container0
	public Container0 getContainer0() {
		return container0;
	}
	public void setContainer0(Container0 container0) {
		this.container0 = container0;
	}

    //Metodos atributo: container2
    public Container2 getContainer2() {
        return container2;
    }
    public void setContainer2(Container2 container2) {
        this.container2 = container2;
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
            ObservableList<Container0In2> container0In2s,
            ObservableList<Container0> container0s,
            ObservableList<Container2> container2s){
        String sql = "SELECT id_container_0, id_container_2, side FROM " + DataConnection.DATABASE + ".tbl_container_0_in_2";
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(sql);

            while(result.next()){
                for(Container0 container0: container0s){
                    if(container0.getIdContainer0()==result.getInt("id_container_0")){
                        for(Container2 container2: container2s){
                            if(container2.getIdContainer2()==result.getInt("id_container_2")){
                                boolean side = result.getBoolean("side");
                                container0In2s.add(
                                        new Container0In2(container0,container2,side));
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }  
    
    public int saveData(Connection connection){
        String sql="INSERT INTO " + DataConnection.DATABASE + ".tbl_container_0_in_2 (id_container_0, id_container_2, side) "
                    + "VALUES (?,?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            instruction.setInt(1,this.getContainer0().getIdContainer0());
            instruction.setInt(2,this.getContainer2().getIdContainer2());
            instruction.setBoolean(3,this.side.get());
            return instruction.executeUpdate();
        } catch (SQLException ex) {
            return 0;
        }
    }
    
    public int updateData(Connection connection){
        return saveData(connection);
    }
    
    public int deleteData(Connection connection){
        String sql = "DELETE FROM " + DataConnection.DATABASE + ".tbl_container_0_in_2 "+
                    "WHERE id_container_0 = ? and id_container_2 = ?";
        try {
                PreparedStatement instruccion = connection.prepareStatement(sql);
                instruccion.setInt(1, this.getContainer0().getIdContainer0());
                instruccion.setInt(2, this.getContainer2().getIdContainer2());
                return instruccion.executeUpdate();
        } catch (SQLException e) {
                return 0;
        }
    }

}