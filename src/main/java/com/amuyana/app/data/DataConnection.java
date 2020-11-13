package com.amuyana.app.data;

import com.amuyana.app.node.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataConnection {
    private Connection connection;
    private static String URL="amuyana.net";
    private static String USERNAME="coxpueqo_client";
    private static String PASSWORD="coxpueqo_password";
    public static String DATABASE="coxpueqo_amuyana";

/*    public static void setValues(String url, String username, String password) {
        DataConnection.URL = url;
        DataConnection.USERNAME = username;
        DataConnection.PASSWORD = password;
    }*/

    public static String getUsername() {
        return USERNAME;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public boolean connect(){
        boolean response = false;
        String fullUrl = "jdbc:mysql://"+URL+"/"+DATABASE;
        try {
            connection = DriverManager.getConnection(fullUrl,USERNAME,PASSWORD);
            response = true;
        } catch (SQLException ex) {
            System.err.println("SQLException");
            System.err.println("Message: " + ex.getMessage());
            System.err.println("Cause: " + ex.getCause());
        }
        return response;
    }
    
    // metodo para cerrar conexión
    public void disconnect(){

        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
