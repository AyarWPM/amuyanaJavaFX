package main.tod;

import main.data.tod.CClass;
import main.data.Fcc;
import main.data.tod.Inclusion;
import main.data.tod.Conjunction;

import java.util.ArrayList;

public class Analogy extends ArrayList<Fcc> {
    Conjunction conjunction;
    CClass cClass;

    private Type type;

    public enum Type {
        CONJUNCTION, CLASS, NONE
    }

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

    public void setConjunction(Conjunction conjunction) {
        this.conjunction = conjunction;
    }

    public Conjunction getConjunction() {
        return this.conjunction;
    }

    public CClass getcClass() {
        return cClass;
    }

    public void setcClass(CClass cClass) {
        this.cClass = cClass;
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

    return "Analogy [" +type.toString() + "]";
    }
}
