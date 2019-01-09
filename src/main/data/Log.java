package main.data;

import java.sql.Timestamp;


public class Log{

    private Timestamp date;
    private String type;
    private String message;
    
    private static int id;
    
    public Log(Timestamp date, String type, String message) {
        this.date = date;
        this.type = type;
        this.message = message;

    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Log.id = id;
    }
    
}