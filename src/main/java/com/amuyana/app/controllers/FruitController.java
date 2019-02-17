package com.amuyana.app.controllers;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.node.MainBorderPane;
import com.amuyana.app.node.menu.FccMenu;
import com.amuyana.app.node.tod.Branch;
import com.amuyana.app.node.tod.Fruit;
import com.amuyana.app.node.tod.Tie;
import com.amuyana.app.node.tod.Tree;
import com.amuyana.app.node.tod.expression.Expression;
import com.amuyana.app.node.tod.expression.FccExp;
import com.amuyana.app.node.tod.expression.ImplicationExp;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FruitController implements Initializable {
    private static DataInterface dataInterface = MainBorderPane.getDataInterface();
    private static final double FONT_SIZE_BASE = 13;

    @FXML private Group fruitGroup;
    @FXML private ImageView bracketImageView;
    @FXML private Circle leftKnobCircle;
    @FXML private StackPane fccNameStackPane;
    @FXML private VBox expressionsVBox;
    @FXML private MenuButton bracketMenuButton;
    @FXML private MenuButton fccMenuButton;
    @FXML private MenuButton positiveDescendantsMenuButton;
    @FXML private MenuButton negativeDescendantsMenuButton;
    @FXML private MenuButton symmetricDescendantsMenuButton;
    @FXML private Label knob1Label;
    @FXML private Label knob2Label;
    @FXML private Label knob3Label;

    private Tree tree;
    private Fruit fruit;
    private DoubleProperty fruitScale;
    private DoubleProperty factorScale;

    private StringProperty styleProperty;
    private StringProperty fontSize;

    private StringProperty firstPart;
    private StringProperty lastPart;
    private ObjectBinding<Bounds> knob1BoundsInTreeBinding;
    private ObjectBinding<Bounds> knob0BoundsInTreeBinding;
    private ObjectBinding<Bounds> knob2BoundsInTreeBinding;
    private ObjectBinding<Bounds> knob3BoundsInTreeBinding;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fruitScale = new SimpleDoubleProperty();
        factorScale = new SimpleDoubleProperty(0.85); // Could be set by user
        styleProperty = new SimpleStringProperty();
        firstPart = new SimpleStringProperty("-fx-font-size: ");
        lastPart = new SimpleStringProperty(";");
        fontSize = new SimpleStringProperty();

    }

    public void initialize(Fruit fruit) {
        this.fruit = fruit;
        this.tree = this.fruit.getTree();
        manageBindings();
        buildExpressions();
        //buildMenus();
    }

    private void manageBindings() {
        // Scalings
        fruitScale.bind(Bindings.divide(factorScale,fruit.getTrunk().levelProperty()));
        fruitGroup.scaleXProperty().bind(fruitScale);
        fruitGroup.scaleYProperty().bind(fruitScale);
        bracketImageView.fitHeightProperty().bind(expressionsVBox.heightProperty());
        bracketImageView.setPreserveRatio(true);

        Group canvas = tree.getTodController().getCanvas();

        // Lines
        knob0BoundsInTreeBinding = Bindings.createObjectBinding(() -> {
                    Bounds nodeLocal = leftKnobCircle.getBoundsInLocal();
                    Bounds nodeScene = leftKnobCircle.localToScene(nodeLocal);
                    Bounds nodeTree = canvas.sceneToLocal(nodeScene);
                    return nodeTree;
                }, leftKnobCircle.boundsInLocalProperty(), leftKnobCircle.localToSceneTransformProperty(),
                canvas.localToSceneTransformProperty());

        knob1BoundsInTreeBinding = Bindings.createObjectBinding(() -> {
                    Bounds nodeLocal = knob1Label.getBoundsInLocal();
                    Bounds nodeScene = knob1Label.localToScene(nodeLocal);
                    Bounds nodeTree = canvas.sceneToLocal(nodeScene);
                    return nodeTree;
                }, knob1Label.boundsInLocalProperty(), knob1Label.localToSceneTransformProperty(),
                canvas.localToSceneTransformProperty());

        knob2BoundsInTreeBinding = Bindings.createObjectBinding(() -> {
                    Bounds nodeLocal = knob2Label.getBoundsInLocal();
                    Bounds nodeScene = knob2Label.localToScene(nodeLocal);
                    Bounds nodeTree = canvas.sceneToLocal(nodeScene);
                    return nodeTree;
                }, knob2Label.boundsInLocalProperty(), knob2Label.localToSceneTransformProperty(),
                canvas.localToSceneTransformProperty());

        knob3BoundsInTreeBinding = Bindings.createObjectBinding(() -> {
                    Bounds nodeLocal = knob3Label.getBoundsInLocal();
                    Bounds nodeScene = knob3Label.localToScene(nodeLocal);
                    Bounds nodeTree = canvas.sceneToLocal(nodeScene);
                    return nodeTree;
                }, knob3Label.boundsInLocalProperty(), knob3Label.localToSceneTransformProperty(),
                canvas.localToSceneTransformProperty());
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

    public void buildMenus() {
        System.out.println("building menu for" + this.fruit.getFcc());

        buildFruitMenu();
        buildAscendantsMenu();
        buildDescendantsMenu();
    }

    private void buildFruitMenu() {
        MenuItem editFccMenuItem = new MenuItem("Edit");
        editFccMenuItem.setOnAction(this.tree.getTodController().openFccEditorEventHandler(fruit.getFcc()));
        fccMenuButton.getItems().setAll(editFccMenuItem);
        //fccMenuButton.setOnMouseClicked(e-> System.out.println("e = " + e));
    }

    private void buildAscendantsMenu() {
        Menu deployAscendantMenu = new Menu("Deploy an ascendant FCC");
        for (int i=0; i <= 2; i++) {
            Dynamism thisDynamism = dataInterface.getDynamism(fruit.getFcc(),i);
            Menu thisDynamismMenu = new Menu(thisDynamism.toString());

            deployAscendantMenu.getItems().addAll(thisDynamismMenu);
            // Add MenuItem: New fcc
            MenuItem deployToNewFccMenuItem = new MenuItem("New FCC");
            deployToNewFccMenuItem.setOnAction(deployNewAscendantAction(thisDynamism));
            thisDynamismMenu.getItems().addAll(deployToNewFccMenuItem, new SeparatorMenuItem());

            // Add MenuItems: Fruits already as descendants
            List<Fcc> ascendantFCCs = FXCollections.observableArrayList();
            for (Fruit fruit1 : fruit.getAscendantFruits()) {
                ascendantFCCs.add(fruit1.getFcc());
                FccMenu fccMenu = new FccMenu(this, thisDynamism,fruit1.getFcc(), FccMenu.FccMenuType.FOR_ASCENDANTS);
                thisDynamismMenu.getItems().addAll(fccMenu);
            }
            // Add separator
            thisDynamismMenu.getItems().addAll(new SeparatorMenuItem());

            // 1) Find those done above, and
            ObservableList<Fcc> fccs = dataInterface.getFccs(
                    tree.getTodController().getTod().getLogicSystem());
            List<Integer> isAscendantFccIds = FXCollections.observableArrayList();
            // Convert list of ascendantFCCs to list of Integers of their ids
            for (Fcc fcc1 : ascendantFCCs) {
                isAscendantFccIds.add(fcc1.getIdFcc());
            }

            // 2) find those that are already deployed in another fruit of the same fcc
            ObservableList<Integer> isAscendantFccIdsElsewhere = FXCollections.observableArrayList();
            for (Fruit fruit1 : tree.getObservableFruits()) {
                if(fruit1.equals(this.fruit)) continue;
                if (fruit1.getFcc().getIdFcc() == this.fruit.getFcc().getIdFcc()) {
                    if (!fruit1.getAscendantFruits().isEmpty()) {
                        for (Fruit fruit2 : fruit1.getAscendantFruits()) {
                            isAscendantFccIdsElsewhere.addAll(fruit2.getFcc().getIdFcc());
                        }
                    }
                }
            }

            // Add MenuItems : for all fccs in LS except those in 1 and 2
            for (Fcc fcc : fccs){
                // Ignore itself in the deployment list
                if (fcc.getIdFcc()==getFruit().getFcc().getIdFcc()) continue;
                // Ignore fccs already done previously
                if (isAscendantFccIds.contains(fcc.getIdFcc())) continue;
                // Ignore ascendants elsewhere
                if(isAscendantFccIdsElsewhere.contains(fcc.getIdFcc())) continue;
                // For all the rest create a menu
                FccMenu fccMenu = new FccMenu(this, thisDynamism,fcc, FccMenu.FccMenuType.FOR_ASCENDANTS);
                thisDynamismMenu.getItems().addAll(fccMenu);
            }
        }
        bracketMenuButton.getItems().setAll(deployAscendantMenu);
    }

    private void buildDescendantsMenu() {
        for (int i = 0; i <= 2; i++) {
            Menu deployOrientationDescendantMenu = new Menu("Deploy a descendant FCC");
            // New FCC
            Dynamism ascendantDynamism = dataInterface.getDynamism(getFruit().getFcc(),i);
            MenuItem deployToNewFccMenuItem = new MenuItem("New FCC");
            deployToNewFccMenuItem.setOnAction(deployNewOrientationDescendantAction(ascendantDynamism));
            deployOrientationDescendantMenu.getItems().addAll(deployToNewFccMenuItem, new SeparatorMenuItem());

            // Descendant Fruits
            ObservableList<Fcc> descendantFccs = FXCollections.observableArrayList();
            for (Fruit fruit1:fruit.getDescendantFruits()) {
                descendantFccs.add(fruit1.getFcc());
                FccMenu fccMenu = new FccMenu(this, ascendantDynamism, fruit1.getFcc(), FccMenu.FccMenuType.FOR_DESCENDANTS);
                deployOrientationDescendantMenu.getItems().addAll(fccMenu);
            }

            deployOrientationDescendantMenu.getItems().addAll(new SeparatorMenuItem());

            // Descendant fruits elsewhere
            ObservableList<Integer> isDescendantFccIds = FXCollections.observableArrayList();
            for (Fcc fcc : descendantFccs) {
                isDescendantFccIds.addAll(fcc.getIdFcc());
            }
            // If there's not a fruit deployed from this.fruit
            ObservableList<Integer> isDescendantFccIdsElsewhere = FXCollections.observableArrayList();
            for (Fruit fruit1 : tree.getObservableFruits()) {
                if(fruit1.equals(this.fruit)) continue;
                if (fruit1.getFcc().getIdFcc() == this.fruit.getFcc().getIdFcc()) {
                    for (Fruit fruit2 : fruit1.getDescendantFruits()) {
                        isDescendantFccIdsElsewhere.addAll(fruit2.getFcc().getIdFcc());
                    }
                }
            }

            // Add the rest of menus
            ObservableList<Fcc> fccs = dataInterface.getFccs(
                    tree.getTodController().getTod().getLogicSystem());
            for (Fcc fcc : fccs){
                // Ignore itself in the deployment list
                if (fcc.getIdFcc()==getFruit().getFcc().getIdFcc()) continue;
                // Ignore fccs already done
                if (isDescendantFccIds.contains(fcc.getIdFcc())) continue;
                // Ignore descendant fccs elsewhere
                if(isDescendantFccIdsElsewhere.contains(fcc.getIdFcc())) continue;
                // For all the rest create a menu
                FccMenu fccMenu = new FccMenu(this, ascendantDynamism, fcc, FccMenu.FccMenuType.FOR_DESCENDANTS);
                deployOrientationDescendantMenu.getItems().addAll(fccMenu);
            }

            switch (i) {
                case 0:
                    positiveDescendantsMenuButton.getItems().setAll(deployOrientationDescendantMenu);
                    break;
                case 1:
                    negativeDescendantsMenuButton.getItems().setAll(deployOrientationDescendantMenu);
                    break;
                case 2:
                    symmetricDescendantsMenuButton.getItems().setAll(deployOrientationDescendantMenu);
                    break;
            }
        }
    }

    private EventHandler<ActionEvent>  deployNewOrientationDescendantAction(Dynamism ascendantDynamism) {
        return actionEvent -> {
            TodController todController = this.tree.getTodController();

            // set scale to 1 before continuing, otherwise the binding of knobs does not work
            resetValueInScaleSlider();
            LogicSystem logicSystem = todController.getTod().getLogicSystem();
            Fcc newFcc = MainBorderPane.getDataInterface().newFcc(logicSystem);
            todController.openFccEditor(newFcc);
            Fruit newFruit = fruit.getSubBranch().addToRightTrunk(newFcc);
            // Create inclusions
            Dynamism positiveDynamismOfDescendant = dataInterface.getDynamism(newFcc, 0);
            Dynamism negativeDynamismOfDescendant = dataInterface.getDynamism(newFcc, 1);
            Dynamism symmetricDynamismOfDescendant = dataInterface.getDynamism(newFcc, 2);
            dataInterface.newInclusion(positiveDynamismOfDescendant,ascendantDynamism, todController.getTod());
            dataInterface.newInclusion(negativeDynamismOfDescendant,ascendantDynamism, todController.getTod());
            dataInterface.newInclusion(symmetricDynamismOfDescendant,ascendantDynamism, todController.getTod());

            tieDescendant(newFruit);
            tree.updateFruitsMenus();
            // Update orientation of all ties as well
            tree.updateOrientationTies();
        };
    }

    private EventHandler<ActionEvent> deployNewAscendantAction(Dynamism descendantDynamism) {
        return actionEvent -> {
            TodController todController = this.tree.getTodController();
            // set scale to 1 before continuing, otherwise the binding of knobs does not work
            resetValueInScaleSlider();
            LogicSystem logicSystem = todController.getTod().getLogicSystem();
            Fcc newFcc = MainBorderPane.getDataInterface().newFcc(logicSystem);
            todController.openFccEditor(newFcc);
            Fruit newFruit = fruit.getSubBranch().addToLeftTrunk(newFcc);

            // Create inclusions
            Dynamism positiveDynamismOfAscendant = dataInterface.getDynamism(newFcc, 0);
            Dynamism negativeDynamismOfAscendant = dataInterface.getDynamism(newFcc, 1);
            Dynamism symmetricDynamismOfAscendant = dataInterface.getDynamism(newFcc, 2);
            dataInterface.newInclusion(descendantDynamism,positiveDynamismOfAscendant, todController.getTod());
            dataInterface.newInclusion(descendantDynamism,negativeDynamismOfAscendant, todController.getTod());
            dataInterface.newInclusion(descendantDynamism,symmetricDynamismOfAscendant, todController.getTod());

            tieAscendant(newFruit);
            tree.updateFruitsMenus();
            // Update orientation of all ties as well
            tree.updateOrientationTies();
        };
    }

    public void resetValueInScaleSlider() {
        this.tree.getTodController().getScaleSlider().setValue(1);
    }

    public void tieAscendant(Fruit newFruit) {
        // for ascendants
        boolean thereIsAscendantTie=false;
        for (Tie tie : tree.getTies()) {
            if (tie.getAscendantFruit().equals(newFruit)) {
                if (tie.getDescendantFruit().equals(fruit)) {
                    thereIsAscendantTie=true;
                    break;
                }
            }
        }
        if (!thereIsAscendantTie) {
            Tie tie = new Tie(fruit,newFruit);
            this.tree.addTie(tie);
        }
    }

    public void tieDescendant(Fruit newFruit) {
        // For descendants
        boolean thereIsDescendantTie=false;
        for (Tie tie : tree.getTies()) {
            if (tie.getAscendantFruit().equals(fruit)) {
                if (tie.getDescendantFruit().equals(newFruit)) {
                    thereIsDescendantTie=true;
                    break;
                }
            }
        }
        if (!thereIsDescendantTie) {
            Tie tie = new Tie(newFruit,fruit);
            this.tree.addTie(tie);
        }
    }


    // Building ties for fruits in children nodes
    public void buildTies() {
        // Descendants
        for (Branch branch : this.fruit.getSubBranch().getRightTrunk().getBranches()) {
            for (Fruit fruit1 : branch.getFruits()) {
                if (this.fruit.isDescendant(fruit1)) {
                    Tie tie = new Tie(fruit1, this.fruit);
                    this.tree.addTie(tie);
                    tie.setOrientations();
                }
            }
        }
        // Ascendants
        for (Branch branch : this.fruit.getSubBranch().getLeftTrunk().getBranches()) {
            for (Fruit fruit1 : branch.getFruits()) {
                if (fruit1.isDescendant(this.fruit)) {
                    Tie tie = new Tie(this.fruit, fruit1);
                    this.tree.addTie(tie);
                    tie.setOrientations();
                }
            }
        }
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

    public Bounds getKnob1BoundsInTreeBinding() {
        return knob1BoundsInTreeBinding.get();
    }

    public ObjectBinding<Bounds> knob1BoundsInTreeBindingProperty() {
        return knob1BoundsInTreeBinding;
    }

    public Bounds getKnob0BoundsInTreeBinding() {
        return knob0BoundsInTreeBinding.get();
    }

    public ObjectBinding<Bounds> knob0BoundsInTreeBindingProperty() {
        return knob0BoundsInTreeBinding;
    }

    public Bounds getKnob2BoundsInTreeBinding() {
        return knob2BoundsInTreeBinding.get();
    }

    public ObjectBinding<Bounds> knob2BoundsInTreeBindingProperty() {
        return knob2BoundsInTreeBinding;
    }

    public Bounds getKnob3BoundsInTreeBinding() {
        return knob3BoundsInTreeBinding.get();
    }

    public ObjectBinding<Bounds> knob3BoundsInTreeBindingProperty() {
        return knob3BoundsInTreeBinding;
    }

}