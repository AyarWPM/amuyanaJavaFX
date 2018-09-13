
package extras.tod;

import com.sun.org.apache.xpath.internal.operations.Mult;
import controllers.AppController;
import controllers.TodController;
import data.Fcc;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import static extras.tod.FccContainer.FccType.NORMAL;
import static extras.tod.FormulaContainer.Styles.SIMPLE;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.Border;
import javafx.scene.shape.Circle;

public class FccContainer extends TitledPane {
    private static AppController appController;
    private static TodController todController;
    
    private final Fcc fcc;
    private final FormulaContainer positiveFormula;
    private final FormulaContainer negativeFormula;
    private final FormulaContainer symmetricFormula;

    private FccType type;

    private ContextMenu menu;

    enum FccType{NORMAL, MIRROR}

    public static void setControllers(AppController appController, TodController todController) {
        FccContainer.appController = appController;
        FccContainer.todController = todController;
    }

    public Fcc getFcc(){
        return this.fcc;
    }

    void setType(FccType type){
        this.type = type;
    }

    FccContainer(Fcc fcc){
        this.fcc = fcc;

        this.positiveFormula = new FormulaContainer();
        this.negativeFormula = new FormulaContainer();
        this.symmetricFormula = new FormulaContainer();

        this.positiveFormula.setDynamism(appController.dynamismOf(0, fcc));
        this.negativeFormula.setDynamism(appController.dynamismOf(1, fcc));
        this.symmetricFormula.setDynamism(appController.dynamismOf(2, fcc));

        this.setText(fcc.getLabel());

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                debug();
            }
        });

        setProperties();
    }

    void debug(){
        
    }
    
    private void setMenu(FccType type){
        this.menu = new ContextMenu();

        MultiContainer parentMultiContainer = (MultiContainer)getParent().getParent();
        AnalogyContainer analogyContainer = (AnalogyContainer)parentMultiContainer.getParent();

        MenuItem muimTurnToFront = new MenuItem("Turn to this");
        
        muimTurnToFront.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                analogyContainer.turnToFront(parentMultiContainer);
            }
        });

        switch(type){
            case NORMAL:{
                Menu deployMenu = new Menu("Deploy");

                CheckMenuItem inclusion = new CheckMenuItem("Deploy inclusions");
                inclusion.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(parentMultiContainer.isInclusionDeployed()){
                            parentMultiContainer.clearInclusions();
                        } else if(!parentMultiContainer.isInclusionDeployed()){
                            parentMultiContainer.deployInclusions();
                        }
                    }
                });


                CheckMenuItem positiveDeductions= new CheckMenuItem("Deploy positive deductions");
                positiveDeductions.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(parentMultiContainer.isPositiveDeductionsDeployed()){
                            parentMultiContainer.clearPositiveDeductions();
                        } else if(!parentMultiContainer.isPositiveDeductionsDeployed()){
                            parentMultiContainer.deployPositiveDeductions();
                        }
                    }
                });

                CheckMenuItem negativeDeductions= new CheckMenuItem("Deploy negative deductions");
                negativeDeductions.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(parentMultiContainer.isNegativeDeductionsDeployed()){
                            parentMultiContainer.clearNegativeDeductions();
                        } else if(!parentMultiContainer.isNegativeDeductionsDeployed()){
                            parentMultiContainer.deployNegativeDeductions();
                        }
                    }
                });

                CheckMenuItem symmetricDeductions = new CheckMenuItem("Deploy symmetric deductions");
                symmetricDeductions.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(parentMultiContainer.isSymmetricDeductionsDeployed()){
                            parentMultiContainer.clearSymmetricDeductions();
                        } else if(!parentMultiContainer.isSymmetricDeductionsDeployed()){
                            parentMultiContainer.deploySymmetricDeductions();
                        }
                    }
                });


                deployMenu.getItems().addAll(inclusion, positiveDeductions, negativeDeductions, symmetricDeductions);
                this.menu.getItems().addAll(muimTurnToFront,deployMenu);

                break;
            }
            case MIRROR:{
                MenuItem drawHere = new MenuItem("Draw here");
                this.menu.getItems().addAll(muimTurnToFront,drawHere);
                break;
            }
            default: break;
        }

        this.setContextMenu(menu);
    }

    private void setProperties(){
        this.setCollapsible(false);
    }

    void deploy(){

        if(this.type==NORMAL){
            VBox vBox = new VBox();
            vBox.getChildren().addAll(this.positiveFormula,this.negativeFormula,this.symmetricFormula);
            vBox.setSpacing(3);
            this.setContent(vBox);
            this.positiveFormula.write(SIMPLE);
            this.negativeFormula.write(SIMPLE);
            this.symmetricFormula.write(SIMPLE);

        } else if(this.type==FccType.MIRROR){
            this.setContent(new Label("The FCC has been\ndrawn somewhere else..."));
        }
        
        DoubleProperty start = new SimpleDoubleProperty();
        //start.bind(this.);
        Circle c = new Circle(start.get(), start.get(), 10);
        VBox v = (VBox)getParent();
        v.getChildren().add(c);
        System.out.println(c.getCenterX());
        
        setMenu(this.type);
    }
}
