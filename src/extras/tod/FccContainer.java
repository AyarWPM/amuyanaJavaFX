package extras.tod;

import controllers.AppController;
import data.Dynamism;
import data.Fcc;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;


/**
 *
 * @author ayar
 */
public class FccContainer extends TitledPane {
    
    private Fcc fcc;
    private ArrayList<FormulaContainer> listFormulaContainers;
    private static AppController appController;
    
    
    public FccContainer(Fcc fcc){
        this.fcc = fcc;
        super.setCollapsible(false);
        
        VBox formulasContainer = new VBox();
        
        ArrayList<Dynamism> listDynamisms = new ArrayList<>();
        
        listDynamisms.add(appController.dynamismOf(0, fcc));
        FormulaContainer positiveFormula = new FormulaContainer(listDynamisms);
        
        listDynamisms.clear();
        listDynamisms.add(appController.dynamismOf(1, fcc));
        FormulaContainer negativeFormula = new FormulaContainer(listDynamisms);
        
        listDynamisms.clear();
        listDynamisms.add(appController.dynamismOf(2, fcc));
        FormulaContainer symmetricFormula = new FormulaContainer(listDynamisms);
        
        formulasContainer.getChildren().addAll(positiveFormula,negativeFormula,symmetricFormula);
        
        super.setContent(formulasContainer);
        
        super.setText(fcc.getLabel());
    }
    
    
    /**
     * 
     * @param fcc The fcc associated to this container
     * @param listDynamisms The dynamisms from the most general to the most
     * particular excluding the dynamisms of the Fcc fcc introduced as param
     */
    public FccContainer(Fcc fcc, ArrayList<Dynamism> listDynamisms){
        
        this.fcc=fcc;
        
        VBox formulasContainer = new VBox();
        FormulaContainer newFormula;
        
        Dynamism tempDynamism;
        
        tempDynamism = appController.dynamismOf(0, fcc);
        listDynamisms.add(tempDynamism);
        
        newFormula = new FormulaContainer(listDynamisms);
        
        formulasContainer.getChildren().add(newFormula);
        
        listDynamisms.remove(listDynamisms.size()-1);
        
        tempDynamism = appController.dynamismOf(1, fcc);
        listDynamisms.add(tempDynamism);
        
        newFormula = new FormulaContainer(listDynamisms);
        
        formulasContainer.getChildren().add(newFormula);
        listDynamisms.remove(listDynamisms.size()-1);
        
        tempDynamism = appController.dynamismOf(2, fcc);
        listDynamisms.add(tempDynamism);
        newFormula = new FormulaContainer(listDynamisms);
        formulasContainer.getChildren().add(newFormula);
        
        
        super.setContent(formulasContainer);
        
        super.setText(fcc.getLabel());
        
        
    }
    
    public static void setAppController(AppController appController){
        FccContainer.appController = appController;
    }
    
    
    
}
