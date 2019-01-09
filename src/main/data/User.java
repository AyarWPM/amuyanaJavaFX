package main.data;

import java.sql.Connection;
import java.sql.Date;
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

public class User{
    private IntegerProperty idUser;
    private StringProperty username;
    private StringProperty email;
    private StringProperty password;
    private Date joinedDate;

    public User(int idUser, String username, String email, 
String password, Date joinedDate) {
        this.idUser = new SimpleIntegerProperty(idUser);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.joinedDate = joinedDate;
    }
    
    @Override
    public String toString(){
        return username.get();
    }

    //Metodos atributo: idUser
    public int getIdUser() {
        return idUser.get();
    }
    public void setIdUser(int idUser) {
        this.idUser = new SimpleIntegerProperty(idUser);
    }
    public IntegerProperty IdUserProperty() {
        return idUser;
    }
    //Metodos atributo: username
    public String getUsername() {
        return username.get();
    }
    public void setUsername(String username) {
        this.username = new SimpleStringProperty(username);
    }
    public StringProperty UsernameProperty() {
        return username;
    }
    //Metodos atributo: email
    public String getEmail() {
        return email.get();
    }
    public void setEmail(String email) {
        this.email = new SimpleStringProperty(email);
    }
    public StringProperty EmailProperty() {
        return email;
    }
    //Metodos atributo: password
    public String getPassword() {
        return password.get();
    }
    public void setPassword(String password) {
        this.password = new SimpleStringProperty(password);
    }
    public StringProperty PasswordProperty() {
        return password;
    }
    //Metodos atributo: date
    public Date getDate() {
        return joinedDate;
    }
    public void setDate(Date date) {
        this.joinedDate = date;
    }
    
    public static void loadList(Connection connection, ObservableList<User> listUser){
        
        try {
            Statement statement = connection.createStatement();
            ResultSet resultado = statement.executeQuery(
                    "SELECT id_user, "
                            + "username, "
                            + "email, "
                            + "password, "
                            + "joined_date "
                            + "FROM amuyana.tbl_user");
            
            while(resultado.next()){
                listUser.add(new User(
                    resultado.getInt("id_user"), 
                    resultado.getString("username"), 
                    resultado.getString("email"),
                    resultado.getString("password"),
                    resultado.getDate("joined_date")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LogicSystem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}