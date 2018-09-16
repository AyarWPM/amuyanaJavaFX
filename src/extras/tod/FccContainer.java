
package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static extras.tod.FccContainer.FccType.NORMAL;
import static extras.tod.FormulaContainer.Styles.SIMPLE;
import javafx.beans.property.DoubleProperty;
import javafx.scene.text.Text;

public class FccContainer extends VBox {
    private static AppController appController;
    private static TodController todController;
    
    private final Fcc fcc;

    Text bracket;
    HBox content;
    HBox header;
    Label title;
    GridPane formulas;

    private FormulaContainer positiveFormula;
    private FormulaContainer negativeFormula;
    private FormulaContainer symmetricFormula;

    private FccType type;

    private final Label MIRROR_MESSAGE = new Label("The FCC has been\ndrawn somewhere else...");

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

    public FccType getType(){
        return this.type;
    }

    FccContainer(Fcc fcc){
        this.fcc = fcc;

        //initContainer();
        this.title = new Label();

        this.formulas = new GridPane();

        // methods that could be accessed later
        setTitle(fcc.getLabel());
        setFormulas();
        setStyle();

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                debug();
            }
        });
    }

    private void setStyle(){
        //this.setAlignment(Pos.CENTER_RIGHT);
        setStyle("-fx-background-color:ivory");
    }

    private void setTitle(String label) {
        this.title.setText(label);
    }

    private void setFormulas(){
        this.positiveFormula = new FormulaContainer(appController.dynamismOf(0, fcc));
        this.negativeFormula = new FormulaContainer(appController.dynamismOf(1, fcc));
        this.symmetricFormula = new FormulaContainer(appController.dynamismOf(2, fcc));

        this.formulas.add(this.positiveFormula,0,0);
        this.formulas.add(this.negativeFormula,0,1);
        this.formulas.add(this.symmetricFormula,0,2);

        this.positiveFormula.write(SIMPLE);
        this.negativeFormula.write(SIMPLE);
        this.symmetricFormula.write(SIMPLE);
    }

    void debug(){
        System.out.println(bracket.prefHeight(-1));
        System.out.println(content.getHeight());
    }
    
    private MenuButton getMenu(){
        MenuButton menu = new MenuButton();

        MultiContainer parentMultiContainer = (MultiContainer)getParent().getParent();
        AnalogyContainer analogyContainer = (AnalogyContainer)parentMultiContainer.getParent();

        MenuItem muimTurnToFront = new MenuItem("Show in front");
        
        muimTurnToFront.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                analogyContainer.turnToFront(parentMultiContainer);
            }
        });

        switch(this.type){
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

                menu.getItems().addAll(muimTurnToFront,deployMenu);

                break;
            }
            case MIRROR:{
                MenuItem drawHere = new MenuItem("Draw here");

                drawHere.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        drawHere();
                    }
                });
                
                menu.getItems().addAll(muimTurnToFront,drawHere);
                break;
            }
            default: break;
        }

        return menu;
    }

    void deploy(){
        header = new HBox();
        content = new HBox();

        header.getChildren().add(this.title);
        header.setAlignment(Pos.CENTER);

        header.getChildren().add(getMenu());

        bracket = new Text("{");

        //scaleBracket(bracket);

        content.getChildren().add(bracket);
        content.getChildren().add(new HBox()); // this will be replace right away

        if(this.type==NORMAL){
            setNormal();
            //content.getChildren().add(this.formulas);
        } else if(this.type == FccType.MIRROR){
            setMirror();
        }

        this.getChildren().add(header);
        this.getChildren().add(content);

    }

    private void drawHere() {
        // Find the MultiContainer that contains the FccContainer of the 
        // fcc of type NORMAL, then switch it...
        
        for(FccContainer fc:TodController.getListFccContainers()){
            if(fc.getType().equals(FccType.NORMAL)){
                MultiContainer multiContainer = (MultiContainer)fc.getParent().getParent();
                
            }
        }
    }

    private void setNormal(){
        this.setType(FccType.NORMAL);
        this.header.getChildren().set(1,getMenu());
        this.content.getChildren().set(1,this.formulas);
    }

    private void setMirror(){
        this.setType(FccType.MIRROR);
        this.header.getChildren().set(1,getMenu());
        this.content.getChildren().set(1,this.MIRROR_MESSAGE);

    }
}
