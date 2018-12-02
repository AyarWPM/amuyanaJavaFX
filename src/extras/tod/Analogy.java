package extras.tod;

import data.Fcc;
import data.Inclusion;

import java.util.ArrayList;

public class Analogy extends ArrayList<Fcc> {

    public enum Type {
         CONJUNCTION, CLASS, NONE
    }
    
    private Type type;

    public Analogy() {

    }

    public Analogy(Fcc fcc) {
        add(fcc);
    }

    public Analogy(Type type) {
        this.type = type;
    }

    public Analogy(Fcc fcc, Type type) {
        this.type = type;
        add(fcc);
    }
    
    public void setType(Type type){
        this.type=type;
    }

    /*
    UTILITY METHODS
     */
    private Analogy convertDynamismIntoFccLists(ArrayList<Inclusion> listDynamism) {
        Analogy listFcc = new Analogy();



        return listFcc;
    }

    @Override
    public String toString() {
        String string = "";
        if (type.equals(Type.CONJUNCTION)) {
            string = "Conjunction";
        }
        if (type.equals(Type.CLASS)) {
            string = "Class";
        }

    return type.toString();
    }
}
