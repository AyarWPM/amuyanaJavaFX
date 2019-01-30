package com.amuyana.app.controllers;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.node.MainBorderPane;
import com.amuyana.app.node.menu.FccMenu;
import com.amuyana.app.node.tod.Branch;
import com.amuyana.app.node.tod.Fruit;
import com.amuyana.app.node.tod.SubBranch;
import com.amuyana.app.node.tod.Tie;
import com.amuyana.app.node.tod.expression.Expression;
import com.amuyana.app.node.tod.expression.FccExp;
import com.amuyana.app.node.tod.expression.ImplicationExp;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class FruitController implements Initializable {
    private static DataInterface dataInterface = MainBorderPane.getDataInterface();
    private static final double FONT_SIZE_BASE = 13;

    @FXML private Group fruitGroup;
    @FXML private ImageView bracketImageView;
    @FXML private Circle leftKnob;
    @FXML private StackPane fccNameStackPane;
    @FXML private VBox expressionsVBox;
    @FXML private MenuButton bracketMenuButton;
    @FXML private MenuButton fccMenuButton;
    @FXML private MenuButton positiveDescendantsMenuButton;
    @FXML private MenuButton negativeDescendantsMenuButton;
    @FXML private MenuButton symmetricDescendantsMenuButton;

    private Fruit fruit;
    private DoubleProperty fruitScale;
    private DoubleProperty factorScale;

    private StringProperty styleProperty;
    private StringProperty fontSize;

    private DoubleProperty knob0X, knob0Y, knob1X, knob1Y, knob2X, knob2Y, knob3X, knob3Y;

    private StringProperty firstPart;
    private StringProperty lastPart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fruitScale = new SimpleDoubleProperty();
        factorScale = new SimpleDoubleProperty(0.85); // Could be set by user
        styleProperty = new SimpleStringProperty();
        firstPart = new SimpleStringProperty("-fx-font-size: ");
        lastPart = new SimpleStringProperty(";");
        fontSize = new SimpleStringProperty();

        knob0X = new SimpleDoubleProperty();
        knob0Y = new SimpleDoubleProperty();
        knob1X = new SimpleDoubleProperty();
        knob1Y = new SimpleDoubleProperty();
        knob2X = new SimpleDoubleProperty();
        knob2Y = new SimpleDoubleProperty();
        knob3X = new SimpleDoubleProperty();
        knob3Y = new SimpleDoubleProperty();
    }

    public void initialize(Fruit fruit) {
        this.fruit = fruit;
        manageEvents();
        buildExpressions();
        buildMenus();
    }

    public void buildMenus() {
        buildFruitMenu();
        buildPositiveDescendantsMenu();
        buildNegativeDescendantsMenu();
        buildSymmetricDescendantsMenu();
        buildBracketMenu();
    }

    private void manageEvents() {
        fruitScale.bind(Bindings.divide(factorScale,fruit.getTrunk().levelProperty()));
        fruitGroup.scaleXProperty().bind(fruitScale);
        fruitGroup.scaleYProperty().bind(fruitScale);
        bracketImageView.fitHeightProperty().bind(expressionsVBox.heightProperty());
        bracketImageView.setPreserveRatio(true);
    }

    public void updateKnobsPositionProperties() {
        double pX = positiveDescendantsMenuButton.getWidth()/2;
        double pY = positiveDescendantsMenuButton.getHeight()/2;
        double nX = negativeDescendantsMenuButton.getWidth()/2;
        double nY = negativeDescendantsMenuButton.getHeight()/2;
        double sX = symmetricDescendantsMenuButton.getWidth()/2;
        double sY = symmetricDescendantsMenuButton.getHeight()/2;

        Point2D pointKnob0 = getFruit().getTree().sceneToLocal(leftKnob.localToScene(leftKnob.getCenterX(),leftKnob.getCenterY()));
        Point2D pointKnob1 = getFruit().getTree().sceneToLocal(positiveDescendantsMenuButton.localToScene(pX,pY));
        Point2D pointKnob2 = getFruit().getTree().sceneToLocal(negativeDescendantsMenuButton.localToScene(nX,nY));
        Point2D pointKnob3 = getFruit().getTree().sceneToLocal(symmetricDescendantsMenuButton.localToScene(sX,sY));

        setKnob0X(pointKnob0.getX());
        setKnob0Y(pointKnob0.getY());
        setKnob1X(pointKnob1.getX());
        setKnob1Y(pointKnob1.getY());
        setKnob2X(pointKnob2.getX());
        setKnob2Y(pointKnob2.getY());
        setKnob3X(pointKnob3.getX());
        setKnob3Y(pointKnob3.getY());
    }

    private void buildExpressions() {
        FccExp fccExp = new FccExp(fruit.getFcc(), Expression.ExpressionType.PROPOSITION);
        fccExp.setTheMaxWidth(300);

        ImplicationExp positiveImplicationExp =
                new ImplicationExp(dataInterface.getDynamism(fruit.getFcc(),0));
        ImplicationExp negativeImplicationExp =
                new ImplicationExp(dataInterface.getDynamism(fruit.getFcc(),1));
        ImplicationExp symmetricImplicationExp =
                new ImplicationExp(dataInterface.getDynamism(fruit.getFcc(),2));
        expressionsVBox.getChildren().setAll(positiveImplicationExp,negativeImplicationExp,symmetricImplicationExp);
        fccNameStackPane.getChildren().setAll(fccExp);
    }

    private void buildFruitMenu() {
        MenuItem editFccMenuItem = new MenuItem("Edit");
        editFccMenuItem.setOnAction(fruit.getTree().getTodController().openFccEditorEventHandler(fruit.getFcc()));
        fccMenuButton.getItems().setAll(editFccMenuItem);
    }

    private void buildPositiveDescendantsMenu() {
        Menu deployPositiveDescendantMenu = new Menu("Deploy to a FCC");

        MenuItem deployPositiveToNewFccMenuItem = new MenuItem("New FCC");
        deployPositiveToNewFccMenuItem.setOnAction(e->{
            deployNewPositiveDescendant();
            getFruit().getTree().updateFruits();
        });

        deployPositiveDescendantMenu.getItems().addAll(deployPositiveToNewFccMenuItem, new SeparatorMenuItem());

        LogicSystem logicSystem = getFruit().getTree().getTodController().getTod().getLogicSystem();
        for (Fcc fcc : MainBorderPane.getDataInterface().getFccs(logicSystem)){
            if (fcc.equals(getFruit().getFcc())) continue;
            FccMenu fccMenu = new FccMenu(
                    fruit.getTree().getTodController(),
                    getFruit(),
                    dataInterface.getDynamism(fcc,0),
                    FccMenu.FccMenuType.FOR_DESCENDANTS);
            deployPositiveDescendantMenu.getItems().addAll(fccMenu);
        }
        positiveDescendantsMenuButton.getItems().setAll(deployPositiveDescendantMenu);
    }

    private void deployNewPositiveDescendant() {
        TodController todController = fruit.getTree().getTodController();
        LogicSystem logicSystem = todController.getTod().getLogicSystem();
        Fcc newFcc = MainBorderPane.getDataInterface().newFcc(logicSystem);
        todController.openFccEditor(newFcc);
        Fruit newFruit = fruit.getSubBranch().addToRightTrunk(newFcc);

        // Create dynamisms, inclusions and tie fruits
        Dynamism dynamismOfAscendant = dataInterface.getDynamism(fruit.getFcc(), 0);
        Dynamism positiveDynamismOfDescendant = dataInterface.getDynamism(newFruit.getFcc(), 0);
        Dynamism negativeDynamismOfDescendant = dataInterface.getDynamism(newFruit.getFcc(), 1);
        Dynamism symmetricDynamismOfDescendant = dataInterface.getDynamism(newFruit.getFcc(), 2);

        Inclusion inclusion1 = dataInterface.newInclusion(positiveDynamismOfDescendant,dynamismOfAscendant);
        Inclusion inclusion2 = dataInterface.newInclusion(negativeDynamismOfDescendant,dynamismOfAscendant);
        Inclusion inclusion3 = dataInterface.newInclusion(symmetricDynamismOfDescendant,dynamismOfAscendant);

        Tie tie = new Tie(newFruit,fruit);
        fruit.getTree().addTie(tie);
        tie.updateOrientations();
    }

    private void buildNegativeDescendantsMenu() {
        negativeDescendantsMenuButton.getItems().setAll(new MenuItem("dummy"));
    }

    private void buildSymmetricDescendantsMenu() {
        symmetricDescendantsMenuButton.getItems().setAll(new MenuItem("dummy"));
    }

    private void buildBracketMenu() {
        bracketMenuButton.getItems().setAll(new MenuItem("dummy"));
    }

    // Building ties for fruits in children nodes
    public void buildTies() {
        System.out.println("1 = " + "1");
        // Descendants
        for (Branch branch : this.fruit.getSubBranch().getRightTrunk().getBranches()) {

            for (Fruit fruit : branch.getFruits()) {
                if (isDescendant(fruit)) {
                    Tie tie = new Tie(fruit, this.fruit);
                    fruit.getTree().addTie(tie);
                    tie.updateOrientations();
                    System.out.println("2 = " + 2);
                }
            }
        }
        // Ascendants

    }

    // Is descendant if any dynamism of the fruit's fcc is particular on an
    // inclusion where the general is any dynamism of this.fruit
    private boolean isDescendant(Fruit fruit) {
        for (Inclusion inclusion : dataInterface.getListInclusions()) {
            Dynamism particlar = inclusion.getParticular();
            Dynamism general = inclusion.getGeneral();
            if (fruit.getFcc().equals(particlar.getFcc())) {
                if (this.fruit.getFcc().equals(general.getFcc())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Fruit getFruit(){
        return this.fruit;
    }

    public double getFruitScale() {
        return fruitScale.get();
    }

    public DoubleProperty fruitScaleProperty() {
        return fruitScale;
    }

    public void setFruitScale(double fruitScale) {
        this.fruitScale.set(fruitScale);
    }

    public double getKnob0X() {
        return knob0X.get();
    }

    public DoubleProperty knob0XProperty() {
        return knob0X;
    }

    private void setKnob0X(double knob0X) {
        this.knob0X.set(knob0X);
    }

    public double getKnob0Y() {
        return knob0Y.get();
    }

    public DoubleProperty knob0YProperty() {
        return knob0Y;
    }

    private void setKnob0Y(double knob0Y) {
        this.knob0Y.set(knob0Y);
    }

    public double getKnob1X() {
        return knob1X.get();
    }

    public DoubleProperty knob1XProperty() {
        return knob1X;
    }

    private void setKnob1X(double knob1X) {
        this.knob1X.set(knob1X);
    }

    public double getKnob1Y() {
        return knob1Y.get();
    }

    public DoubleProperty knob1YProperty() {
        return knob1Y;
    }

    private void setKnob1Y(double knob1Y) {
        this.knob1Y.set(knob1Y);
    }

    public double getKnob2X() {
        return knob2X.get();
    }

    public DoubleProperty knob2XProperty() {
        return knob2X;
    }

    private void setKnob2X(double knob2X) {
        this.knob2X.set(knob2X);
    }

    public double getKnob2Y() {
        return knob2Y.get();
    }

    public DoubleProperty knob2YProperty() {
        return knob2Y;
    }

    private void setKnob2Y(double knob2Y) {
        this.knob2Y.set(knob2Y);
    }

    public double getKnob3X() {
        return knob3X.get();
    }

    public DoubleProperty knob3XProperty() {
        return knob3X;
    }

    private void setKnob3X(double knob3X) {
        this.knob3X.set(knob3X);
    }

    public double getKnob3Y() {
        return knob3Y.get();
    }

    public DoubleProperty knob3YProperty() {
        return knob3Y;
    }

    private void setKnob3Y(double knob3Y) {
        this.knob3Y.set(knob3Y);
    }

    public double getFactorScale() {
        return factorScale.get();
    }

    public DoubleProperty factorScaleProperty() {
        return factorScale;
    }

    public void setFactorScale(double factorScale) {
        this.factorScale.set(factorScale);
    }

    public static double getFontSizeBase() {
        return FONT_SIZE_BASE;
    }

    public String getStyleProperty() {
        return styleProperty.get();
    }

    public StringProperty stylePropertyProperty() {
        return styleProperty;
    }

    public void setStyleProperty(String styleProperty) {
        this.styleProperty.set(styleProperty);
    }

    public String getFontSize() {
        return fontSize.get();
    }

    public StringProperty fontSizeProperty() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize.set(fontSize);
    }

    public String getFirstPart() {
        return firstPart.get();
    }

    public StringProperty firstPartProperty() {
        return firstPart;
    }

    public void setFirstPart(String firstPart) {
        this.firstPart.set(firstPart);
    }

    public String getLastPart() {
        return lastPart.get();
    }

    public StringProperty lastPartProperty() {
        return lastPart;
    }

    public void setLastPart(String lastPart) {
        this.lastPart.set(lastPart);
    }
}