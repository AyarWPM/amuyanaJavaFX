package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

//import static extras.tod.FccContainer.FccType.MIRROR;
//import static extras.tod.FccContainer.FccType.NORMAL;
import static extras.tod.FormulaContainer.Styles.SIMPLE;

public class FccContainer extends VBox {
    private static AppController appController;
    private static TodController todController;
    
    private final Fcc fcc;

    private HBox header;
    private VBox formulasHolder, knobsHolder;
    private BorderPane content;

    private Label title;

    private final Label bracket = new Label("{");

    public Knob knob0, knob1, knob2, knob3;

    private FormulaContainer positiveFormula, negativeFormula, symmetricFormula;

    //private FccType type;

    private final Label MIRROR_MESSAGE = new Label("The FCC has been\ndrawn somewhere else...");

    //public enum FccType{NORMAL, MIRROR}

    private DoubleProperty knob0X = new SimpleDoubleProperty();
    private DoubleProperty knob0Y = new SimpleDoubleProperty();
    private DoubleProperty knob1X = new SimpleDoubleProperty();
    private DoubleProperty knob1Y = new SimpleDoubleProperty();
    private DoubleProperty knob2X = new SimpleDoubleProperty();
    private DoubleProperty knob2Y = new SimpleDoubleProperty();
    private DoubleProperty knob3X = new SimpleDoubleProperty();
    private DoubleProperty knob3Y = new SimpleDoubleProperty();

    FccContainer(Fcc fcc){
        this.fcc = fcc;
        manageEvents();
    }

    public static void setControllers(AppController appController, TodController todController) {
        FccContainer.appController = appController;
        FccContainer.todController = todController;
    }

    void manageEvents(){
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                debug();
            }
        });

        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getThis().getMultiContainerParent().setTranslateZ(-1);
            }
        });

        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getThis().getMultiContainerParent().setTranslateZ(0);
            }
        });
    }

    void debug(){
    }

    public void deploy(){
        // method initialize() is here because when we clear positions of multicontainers and if fcc is normal, we are looking
        // for mirrors (supposedly this) and we deploy it, but if initialize() was called in the constructor then
        // we would have to "reset" attributes one by one, so we rather initialize it here...
        // Same for setStyle(); which is called at the end of this method
        initialize();
        addChildren();
        setStyle();
    }

    /*
    public void setType(){
        if(todController.isMirrorFccContainer(getThis())){
            setType(FccContainer.FccType.MIRROR);
        } else if(!todController.isMirrorFccContainer(getThis())){
            setType(NORMAL);
        }
    }
    */


    private void initialize(){

        this.header = new HBox();

        this.content = new BorderPane();
        this.title = new Label();
        this.title.setContextMenu(new ContextMenu(getMenu()));

        this.knob0 = new Knob(getThis());
        this.knob1 = new Knob(getThis());
        this.knob2 = new Knob(getThis());
        this.knob3 = new Knob(getThis());
    }

    private void addChildren() {
        setTitle(fcc.getLabel());
        this.header.getChildren().add(this.title);

        Pane bracketHolder = getBracketHolder();
        formulasHolder = getFormulasHolder();
        VBox knobsHolder = getKnobsHolder();

        this.content.setLeft(bracketHolder);
        this.content.setCenter(formulasHolder);
        this.content.setRight(knobsHolder);

        formulasHolder.setAlignment(Pos.CENTER_LEFT);
        knobsHolder.setAlignment(Pos.CENTER_RIGHT);
        knobsHolder.setTranslateX(5);
        this.getChildren().add(header);
        this.getChildren().add(content);
    }

    private void setTitle(String label) {
        this.title.setText(label);
    }

    private void setStyle(){
        this.header.setAlignment(Pos.CENTER_LEFT);
        this.header.setSpacing(5);
        this.setPickOnBounds(true);
        this.setStyle("-fx-background-color:white;-fx-border-color:black;");
        this.setSpacing(5);

    }

    public Fcc getFcc(){
        return this.fcc;
    }

    /*
    public void setType(FccType type){
        this.type = type;
    }

    public FccType getType(){
        return this.type;
    }
    */

    private FccContainer getThis(){
        return this;
    }

    public MultiContainer getMultiContainerParent(){
        return (MultiContainer)getParent().getParent();
    }

    private AnalogyContainer getParentAnalogyContainer(){
        return todController.getAnalogyContainerOf(getMultiContainerParent());
    }

    public void setBracketAndKnobs(){
        // Size of bracket is proportional to content size

        double i = formulasHolder.getHeight();

        bracket.setStyle("-fx-font-size:" + i + ";");

        bracket.setLayoutY(-i*0.17);
        knob0.setLayoutY(i/2.0);

        double spacing = i/5;

        knobsHolder.setSpacing(spacing);
    }

    public void setKnobsPositions(){
        Point2D pointKnob0 = todController.getTodContainer().sceneToLocal(knob0.localToScene(0,0));
        Point2D pointKnob1 = todController.getTodContainer().sceneToLocal(knob1.localToScene(0,0));
        Point2D pointKnob2 = todController.getTodContainer().sceneToLocal(knob2.localToScene(0,0));
        Point2D pointKnob3 = todController.getTodContainer().sceneToLocal(knob3.localToScene(0,0));

        setKnob0X(pointKnob0.getX());
        setKnob0Y(pointKnob0.getY());
        setKnob1X(pointKnob1.getX());
        setKnob1Y(pointKnob1.getY());
        setKnob2X(pointKnob2.getX());
        setKnob2Y(pointKnob2.getY());
        setKnob3X(pointKnob3.getX());
        setKnob3Y(pointKnob3.getY());
    }

    private Pane getBracketHolder(){
        Pane bracketHolder = new Pane();

        bracketHolder.getChildren().add(knob0);
        bracketHolder.getChildren().add(bracket);

        // Style
        //bracketHolder.setStyle("-fx-border-color:blue;-fx-padding:-10 -5 -10 0;");
        bracketHolder.setStyle("-fx-padding:-10 -5 -10 0;");

        return bracketHolder;
    }

    private VBox getFormulasHolder(){
        VBox formulasHolder = new VBox();

        this.positiveFormula = new FormulaContainer(appController.getDataInterface().dynamismOf(0, fcc));
        this.negativeFormula = new FormulaContainer(appController.getDataInterface().dynamismOf(1, fcc));
        this.symmetricFormula = new FormulaContainer(appController.getDataInterface().dynamismOf(2, fcc));

        this.positiveFormula.write(SIMPLE);
        this.negativeFormula.write(SIMPLE);
        this.symmetricFormula.write(SIMPLE);

        formulasHolder.getChildren().addAll(positiveFormula,negativeFormula,symmetricFormula);

        //this.header.getChildren().set(1,getMenu());
        //this.content.getChildren().set(2,this.formulas);

        return formulasHolder;
    }

    private VBox getKnobsHolder(){
        this.knobsHolder = new VBox();
        this.knobsHolder.setStyle("-fx-padding:0 0 0 5");
        this.knobsHolder.getChildren().addAll(knob1,knob2,knob3);
        return knobsHolder;
    }

    private Menu getMenu(){
        Menu menu = new Menu();
        Menu deployMenu = new Menu("Deploy");

        Menu deployInThisLevelMenu = new Menu("In this level");
        Menu conjunctionsWithFccMenu = new Menu("Conjunctions with FCC-" + getFcc().getIdFcc());
        //conjunctionsWithFccMenu.getItems().addAll(appController.getListConjunctionsForMenu(getFcc()));
        Menu classesWithFccMenu = new Menu("Conjunctions with FCC-" + getFcc().getIdFcc());
        deployInThisLevelMenu.getItems().addAll(conjunctionsWithFccMenu,classesWithFccMenu);


        Menu deployAntecedent = new Menu("Antecedent");
        Menu deployDescendant = new Menu("Descendant");




/*
        MenuItem muimTurnToFront = new MenuItem("Show in front");

        muimTurnToFront.setOnAction(event -> getThis().getMultiContainerParent().toFront());

        Menu deployMenu = new Menu("Deploy");

        CheckMenuItem cmiDeployAntecedents = new CheckMenuItem("Deploy all antecedents");
        cmiDeployAntecedents.setOnAction(event -> getThis().getMultiContainerParent().deployAntecedents());

        CheckMenuItem cmiDeployDescendants = new CheckMenuItem("Deploy all descendants");
        cmiDeployDescendants.setOnAction(event -> getThis().getMultiContainerParent().deployDescendants());
        
        deployMenu.getItems().addAll(cmiDeployAntecedents, cmiDeployDescendants);

        menu.getItems().addAll(muimTurnToFront,deployMenu);*/

        return menu;
    }

    @Override
    public String toString(){
        return "[\"" + getFcc().toString() + "\"" + " fccContainer]";
    }

    public double getKnob0X() {
        return knob0X.get();
    }

    public DoubleProperty knob0XProperty() {
        return knob0X;
    }

    public void setKnob0X(double knob0X) {
        this.knob0X.set(knob0X);
    }

    public double getKnob0Y() {
        return knob0Y.get();
    }

    public DoubleProperty knob0YProperty() {
        return knob0Y;
    }

    public void setKnob0Y(double knob0Y) {
        this.knob0Y.set(knob0Y);
    }

    public double getKnob1X() {
        return knob1X.get();
    }

    public DoubleProperty knob1XProperty() {
        return knob1X;
    }

    public void setKnob1X(double knob1X) {
        this.knob1X.set(knob1X);
    }

    public double getKnob1Y() {
        return knob1Y.get();
    }

    public DoubleProperty knob1YProperty() {
        return knob1Y;
    }

    public void setKnob1Y(double knob1Y) {
        this.knob1Y.set(knob1Y);
    }

    public double getKnob2X() {
        return knob2X.get();
    }

    public DoubleProperty knob2XProperty() {
        return knob2X;
    }

    public void setKnob2X(double knob2X) {
        this.knob2X.set(knob2X);
    }

    public double getKnob2Y() {
        return knob2Y.get();
    }

    public DoubleProperty knob2YProperty() {
        return knob2Y;
    }

    public void setKnob2Y(double knob2Y) {
        this.knob2Y.set(knob2Y);
    }

    public double getKnob3X() {
        return knob3X.get();
    }

    public DoubleProperty knob3XProperty() {
        return knob3X;
    }

    public void setKnob3X(double knob3X) {
        this.knob3X.set(knob3X);
    }

    public double getKnob3Y() {
        return knob3Y.get();
    }

    public DoubleProperty knob3YProperty() {
        return knob3Y;
    }

    public void setKnob3Y(double knob3Y) {
        this.knob3Y.set(knob3Y);
    }
}