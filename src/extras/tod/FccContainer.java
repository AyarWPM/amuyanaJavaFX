
package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Dynamism;
import data.Fcc;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import static extras.tod.FormulaContainer.Styles.SIMPLE;

public class FccContainer extends TitledPane {
    private static AppController appController;
    private static TodController todController;
    
    private final Fcc fcc;
    private final FormulaContainer positiveFormula;
    private final FormulaContainer negativeFormula;
    private final FormulaContainer symmetricFormula;
    
    public FccContainer(Fcc fcc){
        this.fcc = fcc;

        this.positiveFormula = new FormulaContainer();
        this.positiveFormula.setDynamism(appController.dynamismOf(0, fcc));

        this.negativeFormula = new FormulaContainer();
        this.negativeFormula.setDynamism(appController.dynamismOf(1, fcc));

        this.symmetricFormula = new FormulaContainer();
        this.symmetricFormula.setDynamism(appController.dynamismOf(2, fcc));

        this.setText(fcc.getLabel());
        
        setStyle();
    }

    public static void setControllers(AppController appController, TodController todController) {
        FccContainer.appController = appController;
        FccContainer.todController = todController;
    }

    private void setStyle(){
        this.setCollapsible(false);
    }

    void deploy(){
        VBox vBox = new VBox();

        vBox.getChildren().addAll(this.positiveFormula,this.negativeFormula,this.symmetricFormula);

        vBox.setSpacing(3);

        this.setContent(vBox);

        this.positiveFormula.write(SIMPLE);
        this.negativeFormula.write(SIMPLE);
        this.symmetricFormula.write(SIMPLE);
    }
}
