
package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Dynamism;
import data.Fcc;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class FccContainer extends TitledPane {
    private static AppController appController;
    private static TodController todController;
    
    private final Fcc fcc;
    private final FormulaContainer positiveFormula;
    private final FormulaContainer negativeFormula;
    private final FormulaContainer symmetricFormula;
    
    public FccContainer(Fcc fcc){
        this.fcc = fcc;
        
        ArrayList<Dynamism> tempListDynamisms = new ArrayList<>();
        
        tempListDynamisms.add(appController.dynamismOf(0, fcc));
        this.positiveFormula = new FormulaContainer(tempListDynamisms);
        
        tempListDynamisms.clear();
        
        tempListDynamisms.add(appController.dynamismOf(1, fcc));
        this.negativeFormula = new FormulaContainer(tempListDynamisms);
        
        tempListDynamisms.clear();
        
        tempListDynamisms.add(appController.dynamismOf(2, fcc));
        this.symmetricFormula = new FormulaContainer(tempListDynamisms);
        
        this.setText(fcc.getLabel());
        
        setStyle();
    }
    
    private void setStyle(){
        this.setCollapsible(false);
    }

    void deploy(){
        this.setContent(new VBox(this.positiveFormula,this.negativeFormula,this.symmetricFormula));
        this.positiveFormula.deploy();
        this.negativeFormula.deploy();
        this.symmetricFormula.deploy();
    }
    
    public static void setControllers(AppController appController, TodController todController) {
        FccContainer.appController = appController;
        FccContainer.todController = todController;
    }
}
