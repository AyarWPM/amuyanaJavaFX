package extras.tod;

import data.Fcc;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author ayar
 */
public class Analogy extends ArrayList<Fcc> {

    public enum Type {
        CCLASS, INCLUSION, MIXED, NONE
    };
    
    private Type type;
    
    public Analogy() {

    }
    
    public void setType(Type type){
        this.type=type;
    }
    
}
