package extras.tod;

import data.Fcc;
import java.util.ArrayList;

public class Analogy extends ArrayList<Fcc> {

    public enum Type {
         CONJUNCTION, CLASS, MIXED
    }
    
    private Type type;
    
    public Analogy(Type type) {

    }

    public Analogy(Fcc fcc) {
        add(fcc);
    }
    
    public Analogy(Fcc fcc, Type type) {
        add(fcc);
    }
    
    public void setType(Type type){
        this.type=type;
    }
    
}
