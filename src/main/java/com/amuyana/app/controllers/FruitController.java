package com.amuyana.app.controllers;

import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.menu.FccMenu;
import com.amuyana.app.node.tod.*;
import com.amuyana.app.node.tod.expression.Expression;
import com.amuyana.app.node.tod.expression.FccExp;
import com.amuyana.app.node.tod.expression.ImplicationExp;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

public class FruitController implements Initializable {
    private static DataInterface dataInterface = NodeHandler.getDataInterface();
    private static final double FONT_SIZE_BASE = 13;

    //@FXML private Group fruitGroup;
    @FXML private BorderPane fruitBorderPane;
    @FXML private ImageView bracketImageView;
    @FXML private Circle leftKnobCircle;
    @FXML private StackPane fccNameStackPane;
    @FXML private VBox expressionsVBox;
    //@FXML private MenuButton bracketMenuButton;
    @FXML private MenuButton fruitMenuButton;
    @FXML private MenuButton positiveDescendantsMenuButton;
    @FXML private MenuButton negativeDescendantsMenuButton;
    @FXML private MenuButton symmetricDescendantsMenuButton;
    @FXML private Label knob1Label;
    @FXML private Label knob2Label;
    @FXML private Label knob3Label;

    private SimpleBooleanProperty updateKnobs;

    private ContextMenu mainContextMenu;
    private ContextMenu positiveContextMenu;
    private ContextMenu negativeContextMenu;
    private ContextMenu symmetricContextMenu;

    private FccExp fccExp;
    private ImplicationExp positiveImplicationExp;
    private ImplicationExp negativeImplicationExp;
    private ImplicationExp symmetricImplicationExp;

    public boolean isUpdateKnobs() {
        return updateKnobs.get();
    }

    public SimpleBooleanProperty updateKnobsProperty() {
        return updateKnobs;
    }

    public boolean getUpdateKnobs() {
        return updateKnobs.get();
    }

    public void setUpdateKnobs(boolean updateKnobs) {
        updateKnobsProperty().set(updateKnobs);
    }

    private Tree tree;
    private Fruit fruit;
    private DoubleProperty fruitScale;
    private DoubleProperty factorScale;

    private StringProperty styleProperty;
    private StringProperty fontSize;

    //private StringProperty firstPart;
    //private StringProperty lastPart;
    private ObjectBinding<Bounds> knob0BoundsInTreeBinding;
    private ObjectBinding<Bounds> knob1BoundsInTreeBinding;
    private ObjectBinding<Bounds> knob2BoundsInTreeBinding;
    private ObjectBinding<Bounds> knob3BoundsInTreeBinding;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fruitScale = new SimpleDoubleProperty();
        factorScale = new SimpleDoubleProperty(1); // Could be set by user
        styleProperty = new SimpleStringProperty();
        //firstPart = new SimpleStringProperty("-fx-font-size: ");
        //lastPart = new SimpleStringProperty(";");
        fontSize = new SimpleStringProperty();
        updateKnobs = new SimpleBooleanProperty();
    }

    public void initialize(Fruit fruit) {
        this.fruit = fruit;
        this.tree = this.fruit.getTree();
        if (fruit.getTrunk().getTrunkType().equals(Trunk.TrunkType.TREE)) {
            fruitBorderPane.setStyle("-fx-border-width:2.5;");
        }
        manageBindings();
        buildExpressions();
        fruitBorderPane.setOnMouseClicked(event -> {
            /*for (Fruit fruit1 : fruit.getAscendantFruits()) {
                System.out.println("1Descendant of " +fruit.getFcc()+ " is " + fruit1.getFcc());
            }for (Fruit fruit1 : fruit.getAscendantFruitsOnlyFcc()) {
                System.out.println("2Descendant of " +fruit.getFcc()+ " is " + fruit1.getFcc());
            }for (Fruit fruit1 : fruit.getAscendantFruitsConjunction()) {
                System.out.println("3Descendant of " +fruit.getFcc()+ " is " + fruit1.getFcc());
            }*/
            /*event.consume();
            mainContextMenu.hide();
            positiveContextMenu.hide();
            negativeContextMenu.hide();
            symmetricContextMenu.hide();*/
        });
    }

    private void manageBindings() {
        // Scalings
        //fruitScale.bind(Bindings.divide(factorScale,fruit.getTrunk().levelProperty()));
        //fruitGroup.scaleXProperty().bind(fruitScale);
        //fruitGroup.scaleYProperty().bind(fruitScale);
        bracketImageView.fitHeightProperty().bind(expressionsVBox.heightProperty());
        bracketImageView.setPreserveRatio(true);

        // Lines
        knob0BoundsInTreeBinding = Bindings.createObjectBinding(
                () -> {
                    Bounds nodeLocal = leftKnobCircle.getBoundsInLocal();
                    Bounds nodeScene = leftKnobCircle.localToScene(nodeLocal);
                    Bounds nodeTree = tree.sceneToLocal(nodeScene);
                    return nodeTree;
                },
                updateKnobsProperty());

        knob1BoundsInTreeBinding = Bindings.createObjectBinding(() -> {
                    Bounds nodeLocal = knob1Label.getBoundsInLocal();
                    Bounds nodeScene = knob1Label.localToScene(nodeLocal);
                    Bounds nodeTree = tree.sceneToLocal(nodeScene);
                    return nodeTree;
                },
                updateKnobsProperty());

        knob2BoundsInTreeBinding = Bindings.createObjectBinding(() -> {
                    Bounds nodeLocal = knob2Label.getBoundsInLocal();
                    Bounds nodeScene = knob2Label.localToScene(nodeLocal);
                    Bounds nodeTree = tree.sceneToLocal(nodeScene);
                    return nodeTree;
                },
                updateKnobsProperty());

        knob3BoundsInTreeBinding = Bindings.createObjectBinding(() -> {
                    Bounds nodeLocal = knob3Label.getBoundsInLocal();
                    Bounds nodeScene = knob3Label.localToScene(nodeLocal);
                    Bounds nodeTree = tree.sceneToLocal(nodeScene);
                    return nodeTree;
                },
                updateKnobsProperty());

    }

    public FccExp getFccExp() {
        return fccExp;
    }

    private void buildExpressions() {
        this.fccExp = new FccExp(fruit.getFcc(), Expression.ExpressionType.PROPOSITION);

        this.fccExp.setOnMouseEntered(mouseEvent -> {
            this.fccExp.setHoverStyle();
            for (Fruit fruit1 : tree.getObservableFruits()) {
                if (fruit1.getFcc().equals(this.fruit.getFcc())) {
                    fruit1.setHoverStyleFccExpression();
                }
            }
        });

        this.fccExp.setOnMouseExited(mouseEvent -> {
            this.fccExp.setNormalStyle();
            for (Fruit fruit1 : tree.getObservableFruits()) {
                if (fruit1.getFcc().equals(this.fruit.getFcc())) {
                    fruit1.setNormalStyleFccExpression();
                }
            }
        });

        this.positiveImplicationExp =
                new ImplicationExp(fruit.getTree().getTodController(), dataInterface.getDynamism(fruit.getFcc(),0),
                        Expression.ExpressionType.PROPOSITION, Pos.CENTER);
        this.negativeImplicationExp =
                new ImplicationExp(fruit.getTree().getTodController(), dataInterface.getDynamism(fruit.getFcc(),1),
                        Expression.ExpressionType.PROPOSITION, Pos.CENTER);
        this.symmetricImplicationExp =
                new ImplicationExp(fruit.getTree().getTodController(), dataInterface.getDynamism(fruit.getFcc(),2),
                        Expression.ExpressionType.PROPOSITION, Pos.CENTER);
        //this.positiveImplicationExp.effectProperty().bind(this.positiveImplicationExp.onMouseEnteredProperty());

        //Binding<Effect> binding = Bindings.createObjectBinding(() -> {return new Glow();},this.positiveImplicationExp.onMouseEnteredProperty());
        //System.out.println("binding.getValue() = " + binding.getValue());

        // ON HOVER EFFECT
        this.positiveImplicationExp.setOnMouseEntered(mouseEvent -> addHoverEffect(fruit,dataInterface.getDynamism(fruit.getFcc(),0)));
        this.positiveImplicationExp.setOnMouseExited(mouseEvent -> removeHoverEffect());
        this.negativeImplicationExp.setOnMouseEntered(mouseEvent -> addHoverEffect(fruit,dataInterface.getDynamism(fruit.getFcc(),1)));
        this.negativeImplicationExp.setOnMouseExited(mouseEvent -> removeHoverEffect());
        this.symmetricImplicationExp.setOnMouseEntered(mouseEvent -> addHoverEffect(fruit,dataInterface.getDynamism(fruit.getFcc(),2)));
        this.symmetricImplicationExp.setOnMouseExited(mouseEvent -> removeHoverEffect());

        expressionsVBox.getChildren().setAll(positiveImplicationExp,negativeImplicationExp,symmetricImplicationExp);
        expressionsVBox.setAlignment(Pos.CENTER);
        expressionsVBox.setFillWidth(true);
        fccNameStackPane.setAlignment(Pos.CENTER);
        //fccNameStackPane.setMinHeight(40);
        fccNameStackPane.getChildren().setAll(fccExp);
    }

    private void addHoverEffect(Fruit fruit, Dynamism dynamism) {
        Tod tod  =tree.getTodController().getTod();
        // If algebraic

        // If Propositional
        // The implication itself
        switch (dynamism.getOrientation()) {
            case 0: {
                this.positiveImplicationExp.setHoverStyle();

            } break;
            case 1: this.negativeImplicationExp.setHoverStyle(); break;
            case 2: this.symmetricImplicationExp.setHoverStyle(); break;
            default:
                throw new IllegalStateException("Unexpected value: " + dynamism.getOrientation());
        }
        // In the rest of the TOD
        for (Tie tie : tree.getTies()) {
            // For descendants
            if (tie.getAscendantFruit().equals(fruit)) {
                switch (dynamism.getOrientation()) {
                    case 0: tie.setHoverStyleLine1(); break;
                    case 1: tie.setHoverStyleLine2(); break;
                    case 2: tie.setHoverStyleLine3(); break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + dynamism.getOrientation());
                }
                Fruit descendantFruit = tie.getDescendantFruit();
                Dynamism positiveDescendant = dataInterface.getDynamism(descendantFruit.getFcc(), 0);
                Dynamism negativeDescendant = dataInterface.getDynamism(descendantFruit.getFcc(), 1);
                Dynamism symmetricDescendant = dataInterface.getDynamism(descendantFruit.getFcc(), 2);

                if (dataInterface.isInclusion(positiveDescendant, dynamism, tod)) {
                    descendantFruit.setHoverStylePositiveImplication();
                }
                if (dataInterface.isInclusion(negativeDescendant, dynamism, tod)) {
                    descendantFruit.setHoverStyleNegativeImplication();
                }
                if (dataInterface.isInclusion(symmetricDescendant, dynamism, tod)) {
                    descendantFruit.setHoverStyleSymmetricImplication();
                }
            }
            // For ascendants
            if (tie.getDescendantFruit().equals(fruit)) {
                Fruit ascendantFruit = tie.getAscendantFruit();
                Dynamism positiveAscendant = dataInterface.getDynamism(ascendantFruit.getFcc(), 0);
                Dynamism negativeAscendant = dataInterface.getDynamism(ascendantFruit.getFcc(), 1);
                Dynamism symmetricAscendant = dataInterface.getDynamism(ascendantFruit.getFcc(), 2);

                if (dataInterface.isInclusion(dynamism, positiveAscendant, tod)) {
                    ascendantFruit.setHoverStylePositiveImplication();
                    tie.setHoverStyleLine1();
                }
                if (dataInterface.isInclusion(dynamism,negativeAscendant, tod)) {
                    ascendantFruit.setHoverStyleNegativeImplication();
                    tie.setHoverStyleLine2();
                }
                if (dataInterface.isInclusion(dynamism,symmetricAscendant, tod)) {
                    ascendantFruit.setHoverStyleSymmetricImplication();
                    tie.setHoverStyleLine3();
                }
            }

        }
    }

    private void removeHoverEffect() {
        for (Fruit fruit : tree.getObservableFruits()) {
            fruit.setNormalStylePositiveImplication();
            fruit.setNormalStyleNegativeImplication();
            fruit.setNormalStyleSymmetricImplication();
        }
        for (Tie tie : fruit.getTree().getTies()) {
            tie.setNormalStyleLine1();
            tie.setNormalStyleLine2();
            tie.setNormalStyleLine3();
        }
    }

    public void setHoverStylePositiveImplication() {
        positiveImplicationExp.setHoverStyle();
    }

    public void setHoverStyleNegativeImplication() {
        negativeImplicationExp.setHoverStyle();
    }

    public void setHoverStyleSymmetricImplication() {
        symmetricImplicationExp.setHoverStyle();
    }

    public void setNormalStylePositiveImplication() {
        positiveImplicationExp.setNormalStyle();
    }

    public void setNormalStyleNegativeImplication() {
        negativeImplicationExp.setNormalStyle();
    }

    public void setNormalStyleSymmetricImplication() {
        symmetricImplicationExp.setNormalStyle();
    }

    public void buildMenus() {
        // MAIN MENU
        this.mainContextMenu = new ContextMenu();
        fccExp.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                this.mainContextMenu.show(fruitBorderPane, event.getScreenX(), event.getScreenY());
                this.positiveContextMenu.hide();
                this.negativeContextMenu.hide();
                this.symmetricContextMenu.hide();
            }
            if (event.isPrimaryButtonDown()) {
                this.mainContextMenu.hide();
                this.positiveContextMenu.hide();
                this.negativeContextMenu.hide();
                this.symmetricContextMenu.hide();

            }

        });
        //EDIT
        MenuItem editMenuItem = new MenuItem("Edit \"" + fruit.getFcc() + "\"");
        editMenuItem.setOnAction(this.tree.getTodController().openFccEditorEventHandler(fruit.getFcc()));

        // ADD NEW FCC AS CONJUNCTION
        mainContextMenu.getItems().addAll(editMenuItem, addFccMenu(),new SeparatorMenuItem());
        // MOVE UP / DOWN (the subbranches the fruits are in)
        MenuItem moveUpConMenuItem = new MenuItem("Move up the conjunction");
        moveUpConMenuItem.setOnAction(moveUp(true));
        MenuItem moveDownConMenuItem = new MenuItem("Move down the conjunction");
        moveDownConMenuItem.setOnAction(moveDown(true));
        MenuItem moveUpFccMenuItem = new MenuItem("Move up the FCC ");
        moveUpFccMenuItem.setOnAction(moveUp(false));
        MenuItem moveDownFccMenuItem = new MenuItem("Move down the Fcc");
        moveDownFccMenuItem.setOnAction(moveDown(false));
        mainContextMenu.getItems().addAll(moveUpFccMenuItem, moveDownFccMenuItem,moveUpConMenuItem,moveDownConMenuItem, new SeparatorMenuItem());
        // SHOW PROPOSITIONAL / ALGEBRAIC FORMULATIONS
        mainContextMenu.getItems().addAll(changeFccExpressionTypeMenuItem(), changeDynamismsExpressionTypeMenuItem(),new SeparatorMenuItem());

        // REMOVE FROM CONJUNCTION
        MenuItem removeFromConjunctionMenuItem = new MenuItem("Remove from conjunction");
        removeFromConjunctionMenuItem.setOnAction(event -> {
            boolean remove=true;
            // Check if there are no ascendants or descendants
            for (Tie tie : tree.getTies()) {
                if (tie.getAscendantFruit().equals(fruit)) {
                    remove=false;
                }
                if (tie.getDescendantFruit().equals(fruit)) {
                    remove=false;
                }
            }

            if (!fruit.getBranch().getLeftTrunk().getBranches().isEmpty()) {
                remove = false;
            }
            if (!fruit.getBranch().getRightTrunk().getBranches().isEmpty()) {
                remove=false;
            }

            if (remove) {
                dataInterface.connect();
                fruit.getBranch().remove(fruit.getSubBranch());
                dataInterface.disconnect();
                // Remove branch if it is the last fruit
                if (fruit.getBranch().getSubBranches().size() == 0) {
                    dataInterface.connect();
                    fruit.getBranch().getTrunk().remove(fruit.getBranch());
                    dataInterface.disconnect();
                }
                //tree.updateKnobsBounds();
                tree.update();
            } else {
                tree.getTodController().getNodeInterface().log("Cannot remove FCC because it has dependencies");
            }

        });
        mainContextMenu.getItems().addAll(removeFromConjunctionMenuItem);

        // DYNAMISMS MENU
        Dynamism positiveDynamism = dataInterface.getDynamism(fruit.getFcc(),0);
        Dynamism negativeDynamism = dataInterface.getDynamism(fruit.getFcc(),1);
        Dynamism symmetricDynamism = dataInterface.getDynamism(fruit.getFcc(),2);

        // positive
        this.positiveContextMenu = new ContextMenu();
        this.positiveImplicationExp.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                this.positiveContextMenu.show(fruitBorderPane, event.getScreenX(), event.getScreenY());
                this.mainContextMenu.hide();
                this.negativeContextMenu.hide();
                this.symmetricContextMenu.hide();
            }
            if (event.isPrimaryButtonDown()) {
                this.mainContextMenu.hide();
                this.positiveContextMenu.hide();
                this.negativeContextMenu.hide();
                this.symmetricContextMenu.hide();
            }
        });

        Menu onlyThisFccMenuItemPositive = new Menu("Deploy for this FCC only");
        onlyThisFccMenuItemPositive.getItems().addAll(descendantsMenuOnlyThisFcc(positiveDynamism), ascendantsMenuOnlyThisFcc(positiveDynamism));

        Menu allConjunctionFccMenuItemPositive = new Menu("Deploy for the FCCs conjunction");
        allConjunctionFccMenuItemPositive.getItems().addAll(descendantsMenuAllConjunctionFccs(positiveDynamism), ascendantsMenuAllConjunctionFccs(positiveDynamism));

        this.positiveContextMenu.getItems().addAll(onlyThisFccMenuItemPositive,allConjunctionFccMenuItemPositive);

        // negative
        this.negativeContextMenu = new ContextMenu();
        this.negativeImplicationExp.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                this.negativeContextMenu.show(fruitBorderPane, event.getScreenX(), event.getScreenY());
                this.mainContextMenu.hide();
                this.positiveContextMenu.hide();
                this.symmetricContextMenu.hide();
            }
            if (event.isPrimaryButtonDown()) {
                this.mainContextMenu.hide();
                this.positiveContextMenu.hide();
                this.negativeContextMenu.hide();
                this.symmetricContextMenu.hide();
            }
        });

        Menu onlyThisFccMenuItemNegative = new Menu("Deploy for this FCC only");
        onlyThisFccMenuItemNegative.getItems().addAll(descendantsMenuOnlyThisFcc(negativeDynamism), ascendantsMenuOnlyThisFcc(negativeDynamism));

        Menu allConjunctionFccMenuItemNegative = new Menu("Deploy for the FCCs conjunction");
        allConjunctionFccMenuItemNegative.getItems().addAll(descendantsMenuAllConjunctionFccs(negativeDynamism), ascendantsMenuAllConjunctionFccs(negativeDynamism));

        this.negativeContextMenu.getItems().addAll(onlyThisFccMenuItemNegative,allConjunctionFccMenuItemNegative);

        // symmetric
        this.symmetricContextMenu = new ContextMenu();
        this.symmetricImplicationExp.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                this.symmetricContextMenu.show(fruitBorderPane, event.getScreenX(), event.getScreenY());
                this.mainContextMenu.hide();
                this.positiveContextMenu.hide();
                this.negativeContextMenu.hide();
            }
            if (event.isPrimaryButtonDown()) {
                this.mainContextMenu.hide();
                this.positiveContextMenu.hide();
                this.negativeContextMenu.hide();
                this.symmetricContextMenu.hide();
            }
        });

        Menu onlyThisFccMenuSymmetric = new Menu("Deploy for this FCC only");
        onlyThisFccMenuSymmetric.getItems().addAll(descendantsMenuOnlyThisFcc(symmetricDynamism), ascendantsMenuOnlyThisFcc(symmetricDynamism));

        Menu allConjunctionFccMenuSymmetric = new Menu("Deploy for the FCCs conjunction");
        allConjunctionFccMenuSymmetric.getItems().addAll(descendantsMenuAllConjunctionFccs(symmetricDynamism), ascendantsMenuAllConjunctionFccs(symmetricDynamism));

        this.symmetricContextMenu.getItems().addAll(onlyThisFccMenuSymmetric,allConjunctionFccMenuSymmetric);

    }

    private Menu descendantsMenuAllConjunctionFccs(Dynamism thisDynamism) {
        Menu descendantsMenu = new Menu("Deploy descendants (effects)");

        // New FCC
        MenuItem toNewFccMenuItem = new MenuItem("To a new FCC");
        toNewFccMenuItem.setOnAction(deployNewOrientationDescendantAction(thisDynamism,false));
        descendantsMenu.getItems().addAll(toNewFccMenuItem, new SeparatorMenuItem());

        // Descendant Fruits directly from this fruit
        ObservableList<Integer> descendantFccs = FXCollections.observableArrayList();
        for (Fruit fruit1:fruit.getDescendantFruitsConjunction()) {
            descendantFccs.add(fruit1.getFcc().getIdFcc());
            FccMenu fccMenu = new FccMenu(this, thisDynamism, fruit1.getFcc(), FccMenu.FccMenuType.FOR_DESCENDANTS, false);
            descendantsMenu.getItems().addAll(fccMenu);
        }
        descendantsMenu.getItems().add(new SeparatorMenuItem());
        // Descendant Fruits from other fruits in conjunction
        for (SubBranch subBranch : fruit.getBranch().getSubBranches()) {
            if(subBranch.getFruit().equals(fruit)) continue;
            boolean breakloop = false;
            for (Fruit fruit2 : subBranch.getFruit().getDescendantFruitsConjunction()) {
                if(descendantFccs.contains(fruit2.getFcc().getIdFcc())) breakloop=true;
            }
            if(breakloop) continue;
            for (Fruit fruit1:subBranch.getFruit().getDescendantFruitsConjunction()) {
                descendantFccs.add(fruit1.getFcc().getIdFcc());
                FccMenu fccMenu = new FccMenu(this, thisDynamism, fruit1.getFcc(), FccMenu.FccMenuType.FOR_DESCENDANTS, false);
                descendantsMenu.getItems().addAll(fccMenu);
            }
        }
        descendantsMenu.getItems().addAll(new SeparatorMenuItem());

        // Descendant already done
        // If there's not a fruit elsewhere with same fcc
        ObservableList<Integer> isDescendantFccIdsElsewhere = FXCollections.observableArrayList();
        ObservableList<Integer> deployedFromOnlyFcc = FXCollections.observableArrayList();

        for (Fruit fruit1 : tree.getObservableFruits()) {
            //if(fruit1.equals(this.fruit)) continue;
            if (fruit1.getFcc().getIdFcc() == this.fruit.getFcc().getIdFcc()) {
                for (Fruit fruit2 : fruit1.getDescendantFruits()) {
                    isDescendantFccIdsElsewhere.addAll(fruit2.getFcc().getIdFcc());
                }
            }
            // Exclude if its a descendant of another fruit of conjunction
            for (SubBranch subBranch : fruit.getBranch().getSubBranches()) {
                //if (fruit.getSubBranch().equals(subBranch))continue;
                if (subBranch.getFruit().getDescendantFruitsOnlyFcc().contains(fruit1)) {
                    deployedFromOnlyFcc.add(fruit1.getFcc().getIdFcc());
                }
            }
        }
        // If its not in descendant from subBranch
        ObservableList<Fcc> isInOnlyFcc = FXCollections.observableArrayList();
        for (Fruit fruit1 : fruit.getDescendantFruitsOnlyFcc()) {
            isInOnlyFcc.add(fruit1.getFcc());
        }
        // Other fccs in conjunction
        ObservableList<Fcc> inConjunction = FXCollections.observableArrayList();
        for (SubBranch subBranch : fruit.getBranch().getSubBranches()) {
            inConjunction.add(subBranch.getFruit().getFcc());
        }

        // Add the rest of menus
        ObservableList<Fcc> fccs = dataInterface.getFccs(
                tree.getTodController().getTod().getLogicSystem());
        ObservableList<FccMenu> fccSortedMenus = FXCollections.observableArrayList();
        for (Fcc fcc : fccs){
            // Ignore itself in the deployment list
            if (fcc.getIdFcc()==getFruit().getFcc().getIdFcc()) continue;
            // Ignore fccs already done
            if (descendantFccs.contains(fcc.getIdFcc())) continue;
            // Ignore descendant fccs elsewhere
            if(isDescendantFccIdsElsewhere.contains(fcc.getIdFcc())) continue;
            // It is descendant in subBranch
            if(isInOnlyFcc.contains(fcc)) continue;
            // If not a onlyFcc deployment in another fruit of conjunction
            if(deployedFromOnlyFcc.contains(fcc.getIdFcc())) continue;
            // In conjunction
            if(inConjunction.contains(fcc)) continue;
            // For all the rest create a menu
            FccMenu fccMenu = new FccMenu(this, thisDynamism, fcc, FccMenu.FccMenuType.FOR_DESCENDANTS, false);
            fccSortedMenus.add(fccMenu);
        }
        // Sort remaining FccMenus alphabetically before adding them
        Comparator<FccMenu> comparator = Comparator.comparing(FccMenu::getFccName);
        FXCollections.sort(fccSortedMenus,comparator);
        descendantsMenu.getItems().addAll(fccSortedMenus);//

        return descendantsMenu;
    }

    private Menu ascendantsMenuAllConjunctionFccs(Dynamism thisDynamism) {
        Menu ascendantsMenu = new Menu("Deploy ascendants (causes)");

        // Add MenuItem: New fcc
        MenuItem deployToNewFccMenuItem = new MenuItem("New FCC");
        deployToNewFccMenuItem.setOnAction(deployNewAscendantAction(thisDynamism,false));
        ascendantsMenu.getItems().addAll(deployToNewFccMenuItem, new SeparatorMenuItem());

        // Add MenuItems: Fruits already as ascendant
        ObservableList<Integer> ascendantFCCs = FXCollections.observableArrayList();
        for (Fruit fruit1 : fruit.getAscendantFruitsConjunction()) {
            ascendantFCCs.add(fruit1.getFcc().getIdFcc());
            FccMenu fccMenu = new FccMenu(this, thisDynamism,fruit1.getFcc(), FccMenu.FccMenuType.FOR_ASCENDANTS, false);
            ascendantsMenu.getItems().addAll(fccMenu);
        }
        ascendantsMenu.getItems().addAll(new SeparatorMenuItem());

        // Descendant Fruits from conjunction
        // If there's not a fruit elsewhere with same fcc
        //ObservableList<Integer> isAscendantFccIds = FXCollections.observableArrayList();
        for (SubBranch subBranch : fruit.getBranch().getSubBranches()) {
            if(subBranch.getFruit().equals(fruit)) continue;
            boolean breakloop = false;
            for (Fruit fruit2 : subBranch.getFruit().getAscendantFruitsConjunction()) {
                if(ascendantFCCs.contains(fruit2.getFcc().getIdFcc())) breakloop=true;
            }
            if(breakloop) continue;
            for (Fruit fruit1:subBranch.getFruit().getAscendantFruitsConjunction()) {
                ascendantFCCs.add(fruit1.getFcc().getIdFcc());
                FccMenu fccMenu = new FccMenu(this, thisDynamism, fruit1.getFcc(), FccMenu.FccMenuType.FOR_ASCENDANTS, false);
                ascendantsMenu.getItems().addAll(fccMenu);
            }
        }
        ascendantsMenu.getItems().addAll(new SeparatorMenuItem());

        // 2) find those that are already deployed in another fruit of the same fcc
        ObservableList<Integer> isAscendantFccIdsElsewhere = FXCollections.observableArrayList();
        ObservableList<Integer> deployedFromOnlyFcc = FXCollections.observableArrayList();

        for (Fruit fruit1 : tree.getObservableFruits()) {
            //if(fruit1.equals(this.fruit)) continue;
            if (fruit1.getFcc().getIdFcc() == this.fruit.getFcc().getIdFcc()) {
                for (Fruit fruit2 : fruit1.getAscendantFruits()) {
                    isAscendantFccIdsElsewhere.addAll(fruit2.getFcc().getIdFcc());
                }
            }
            // exclude ascendants of another fruit of conjunction
            for (SubBranch subBranch : fruit.getBranch().getSubBranches()) {
                if (subBranch.getFruit().getAscendantFruitsOnlyFcc().contains(fruit1)) {
                    deployedFromOnlyFcc.add(fruit1.getFcc().getIdFcc());
                }
            }
        }
        // If its not in ascendant from Branch
        ObservableList<Fcc> isInConjunction = FXCollections.observableArrayList();
        for (Fruit fruit1 : fruit.getAscendantFruitsConjunction()) {
            isInConjunction.add(fruit1.getFcc());
        }

        // Other fccs in conjunction
        ObservableList<Fcc> inConjunction = FXCollections.observableArrayList();
        for (SubBranch subBranch : fruit.getBranch().getSubBranches()) {
            inConjunction.add(subBranch.getFruit().getFcc());
        }

        // Add MenuItems : for all fccs in LS except those in 1 and 2
        ObservableList<FccMenu> fccSortedMenus = FXCollections.observableArrayList();
        ObservableList<Fcc> fccs = dataInterface.getFccs(tree.getTodController().getTod().getLogicSystem());
        for (Fcc fcc : fccs){
            // Ignore itself in the deployment list
            if (fcc.getIdFcc()==getFruit().getFcc().getIdFcc()) continue;
            // Ignore fccs already done previously
            if (ascendantFCCs.contains(fcc.getIdFcc())) continue;
            // Ignore ascendants elsewhere
            if(isAscendantFccIdsElsewhere.contains(fcc.getIdFcc())) continue;
            // It is ascendant in branch
            if(isInConjunction.contains(fcc)) continue;
            // If it is not a onlyFcc ascendant of another fruit of conjunction
            if(deployedFromOnlyFcc.contains(fcc.getIdFcc())) continue;
            // In conjunction
            if(inConjunction.contains(fcc)) continue;
            // For all the rest create a menu
            FccMenu fccMenu = new FccMenu(this, thisDynamism,fcc, FccMenu.FccMenuType.FOR_ASCENDANTS, false);
            fccSortedMenus.add(fccMenu);
            //thisDynamismMenu.getItems().addAll(fccMenu);
        }

        // Sort remaining FccMenus alphabetically before adding them
        Comparator<FccMenu> comparator = Comparator.comparing(FccMenu::getFccName);
        FXCollections.sort(fccSortedMenus,comparator);
        ascendantsMenu.getItems().addAll(fccSortedMenus);//

        return ascendantsMenu;
    }

    private Menu addFccMenu() {
        Menu addFccMenu = new Menu("Add a FCC in conjunction");
        // MENU ADDNEW FCC
        MenuItem addNewFccMenuItem = new MenuItem("New Fcc");
        addNewFccMenuItem.setOnAction(event -> {
            dataInterface.connect();
            Fcc fcc = dataInterface.newFcc(tree.getTodController().getTod().getLogicSystem());
            fruit.getBranch().newSubBranch(fcc);
            dataInterface.disconnect();
            tree.getTodController().openFccEditor(fcc);
            tree.update();
        });
        addFccMenu.getItems().addAll(addNewFccMenuItem,new SeparatorMenuItem());

        // MENU ADD EXISTING FCC
        ObservableList<Fcc> fccToExclude = FXCollections.observableArrayList();
        ObservableList<Fruit> deployedFruits = FXCollections.observableArrayList();
        // exclusions
        for (SubBranch subBranch : fruit.getBranch().getSubBranches()) {
            // Exclude fccs of conjunction
            fccToExclude.add(subBranch.getFruit().getFcc());
            // Exclude ascendants and descendants
            deployedFruits.addAll(subBranch.getFruit().getAscendantFruits());
            deployedFruits.addAll(subBranch.getFruit().getDescendantFruits());
        }
        for (Fruit fruit : tree.getObservableFruits()) {
            if(deployedFruits.contains(fruit)) fccToExclude.add(fruit.getFcc());
        }
        ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();
        for (Fcc fcc : dataInterface.getFccs(tree.getTodController().getTod().getLogicSystem())) {
            if (fccToExclude.contains(fcc)) {
                continue;
            } else {
                MenuItem addFruit = new MenuItem(fcc.getName());
                addFruit.setOnAction(event -> {
                    NodeHandler.getDataInterface().connect();
                    fruit.getBranch().newSubBranch(fcc);
                    NodeHandler.getDataInterface().disconnect();
                    tree.update();
                });
                menuItems.add(addFruit);
            }
        }
        Comparator<MenuItem> comparator = Comparator.comparing(MenuItem::getText);
        FXCollections.sort(menuItems,comparator);
        addFccMenu.getItems().addAll(menuItems);
        return  addFccMenu;
    }

    private EventHandler<ActionEvent> moveUp(boolean moveConjunction) {
        return actionEvent -> {
            // Move conjunction (branch)
            if (moveConjunction) {
                Branch branch = getFruit().getBranch();
                ObservableList<Branch> branches = getFruit().getTrunk().getBranches();
                // Check it is not in position 0
                if (!(branches.indexOf(branch) == 0)) {
                    Branch upperBranch = branches.get(branches.indexOf(branch) - 1);
                    // Changes in FX
                    int thisBranchIndex = fruit.getTrunk().getChildren().indexOf(branch);
                    int upperBranchIndex = fruit.getTrunk().getChildren().indexOf(upperBranch);

                    ObservableList<Branch> workingCollection = FXCollections.observableArrayList(fruit.getTrunk().getBranches());
                    Collections.swap(workingCollection, thisBranchIndex, upperBranchIndex);
                    fruit.getTrunk().getChildren().setAll(workingCollection);

                    // Change in MYSQL
                    dataInterface.connect();
                    int orderOfThisBranch = branch.getBranchOrder();
                    int orderOfUpperBranch = upperBranch.getBranchOrder();
                    branch.setBranchOrder(orderOfUpperBranch);
                    upperBranch.setBranchOrder(orderOfThisBranch);
                    dataInterface.disconnect();

                    // Changes in ObservableList of Container1
                    int upperContainer1Index = dataInterface.getListContainer1s().indexOf(upperBranch.getContainer1());
                    int thisContainer1Index = dataInterface.getListContainer1s().indexOf(branch.getContainer1());
                    Collections.swap(dataInterface.getListContainer1s(), upperContainer1Index, thisContainer1Index);
                }
            } else {
                // Move subBranch
                SubBranch subBranch = getFruit().getSubBranch();
                ObservableList<SubBranch> subBranches = getFruit().getBranch().getSubBranches();
                // Check it is not in position 0
                if (!(subBranches.indexOf(subBranch) == 0)) {
                    System.out.println(1);
                    SubBranch upperSubBranch = subBranches.get(subBranches.indexOf(subBranch)-1);
                    //FX
                    int thisSubBranchIndex = fruit.getBranch().getSubBranches().indexOf(subBranch);
                    int upperSubBranchIndex = fruit.getBranch().getSubBranches().indexOf(upperSubBranch);

                    ObservableList<SubBranch> workingCollection = FXCollections.observableArrayList(subBranches);
                    Collections.swap(workingCollection, thisSubBranchIndex, upperSubBranchIndex);
                    //fruit.getBranch().setSubBranches(workingCollection);
                    fruit.getBranch().getSubBranchesNodes().setAll(workingCollection);

                    // Change in MYSQL
                    dataInterface.connect();
                    int orderOfThisSubBranch = subBranch.getSubBranchOrder();
                    int orderOfUpperSubBranch = upperSubBranch.getSubBranchOrder();
                    subBranch.setSubBranchOrder(orderOfUpperSubBranch);
                    upperSubBranch.setSubBranchOrder(orderOfThisSubBranch);
                    dataInterface.disconnect();

                    // Changes in ObservableList of Container1
                    int upperContainer2Index = dataInterface.getListContainer2s().indexOf(upperSubBranch.getContainer2());
                    int thisContainer2Index = dataInterface.getListContainer2s().indexOf(subBranch.getContainer2());
                    Collections.swap(dataInterface.getListContainer2s(), upperContainer2Index, thisContainer2Index);
                }
            }
            Platform.runLater(() -> tree.updateKnobsBounds());
        };
    }

    private EventHandler<ActionEvent> moveDown(boolean moveConjunction) {
        return actionEvent -> {
            if (moveConjunction) {
                Branch branch= getFruit().getBranch();
                ObservableList<Branch> branches = getFruit().getTrunk().getBranches();
                // Check it is not in last position
                int lastPosition = branches.size()-1;
                if (!(branches.indexOf(branch) ==lastPosition)) {
                    Branch lowerBranch = branches.get(branches.indexOf(branch)+1);
                    //changes in FX
                    int thisBranchIndex = fruit.getTrunk().getChildren().indexOf(branch);
                    int lowerBranchIndex = fruit.getTrunk().getChildren().indexOf(lowerBranch);

                    ObservableList<Branch> workingCollection = FXCollections.observableArrayList(fruit.getTrunk().getBranches());
                    Collections.swap(workingCollection, thisBranchIndex, lowerBranchIndex);
                    fruit.getTrunk().getChildren().setAll(workingCollection);

                    //Change in MYSQL
                    dataInterface.connect();
                    int orderOfThisBranch = branch.getBranchOrder();
                    int orderOfLowerBranch = lowerBranch.getBranchOrder();
                    branch.setBranchOrder(orderOfLowerBranch);
                    lowerBranch.setBranchOrder(orderOfThisBranch);
                    dataInterface.disconnect();

                    // Changes in ObservableList
                    int thisContainer1Index = dataInterface.getListContainer1s().indexOf(branch.getContainer1());
                    int lowerContainer1Index = dataInterface.getListContainer1s().indexOf(lowerBranch.getContainer1());
                    Collections.swap(dataInterface.getListContainer1s(),lowerContainer1Index,thisContainer1Index);
                }
            } else {
                // Move subBranch
                SubBranch subBranch = getFruit().getSubBranch();
                ObservableList<SubBranch> subBranches = getFruit().getBranch().getSubBranches();
                // Check it is not in position 0
                if (!(subBranches.indexOf(subBranch) == subBranches.size()-1)) {
                    SubBranch lowerSubBranch = subBranches.get(subBranches.indexOf(subBranch)+1);
                    //FX
                    int thisSubBranchIndex = fruit.getBranch().getSubBranches().indexOf(subBranch);
                    int lowerSubBranchIndex = fruit.getBranch().getSubBranches().indexOf(lowerSubBranch);

                    ObservableList<SubBranch> workingCollection = FXCollections.observableArrayList(fruit.getBranch().getSubBranches());
                    Collections.swap(workingCollection, thisSubBranchIndex, lowerSubBranchIndex);
                    //fruit.getBranch().getSubBranches().setAll(workingCollection);
                    fruit.getBranch().getSubBranchesNodes().setAll(workingCollection);

                    // Change in MYSQL
                    dataInterface.connect();
                    int orderOfThisSubBranch = subBranch.getSubBranchOrder();
                    int orderOfLowerSubBranch = lowerSubBranch.getSubBranchOrder();
                    subBranch.setSubBranchOrder(orderOfLowerSubBranch);
                    lowerSubBranch.setSubBranchOrder(orderOfThisSubBranch);
                    dataInterface.disconnect();

                    // Changes in ObservableList of Container1
                    int upperContainer2Index = dataInterface.getListContainer2s().indexOf(lowerSubBranch.getContainer2());
                    int thisContainer2Index = dataInterface.getListContainer2s().indexOf(subBranch.getContainer2());
                    Collections.swap(dataInterface.getListContainer2s(), upperContainer2Index, thisContainer2Index);
                }
            }
            Platform.runLater(() -> tree.updateKnobsBounds());
        };
    }

    private MenuItem changeFccExpressionTypeMenuItem() {
        MenuItem changeFccExpressionTypeMenuItem = new MenuItem("Show algebraic FCC");
        changeFccExpressionTypeMenuItem.setOnAction(actionEvent -> {
            if (fccExp.getExpressionType().equals(Expression.ExpressionType.PROPOSITION)) {
                setFccExpressionType(Expression.ExpressionType.ALGEBRA);
                changeFccExpressionTypeMenuItem.setText("Show propositional FCC");
            }
            else if (fccExp.getExpressionType().equals(Expression.ExpressionType.ALGEBRA)){
                setFccExpressionType(Expression.ExpressionType.PROPOSITION);
                changeFccExpressionTypeMenuItem.setText("Show algebraic FCC");
            }
        });
        return changeFccExpressionTypeMenuItem;
    }

    public void setFccExpressionType(Expression.ExpressionType expressionType) {
        fccExp.changeExpressionType(expressionType);
    }

    public void setDynamismsExpressionType(Expression.ExpressionType expressionType) {
        positiveImplicationExp.changeExpressionType(expressionType);
        negativeImplicationExp.changeExpressionType(expressionType);
        symmetricImplicationExp.changeExpressionType(expressionType);
    }

    private MenuItem changeDynamismsExpressionTypeMenuItem() {
        MenuItem changeDynamismExpressionTypeMenuItem = new MenuItem("Show algebraic Dynamisms");
        changeDynamismExpressionTypeMenuItem.setOnAction(actionEvent -> {
            // I'm evaluating condition in only one of the three dynamisms, cause that's enough (for now), maybe
            // user will want specific conversions
            if (positiveImplicationExp.getExpressionType().equals(Expression.ExpressionType.PROPOSITION)) {
                setDynamismsExpressionType(Expression.ExpressionType.ALGEBRA);
                changeDynamismExpressionTypeMenuItem.setText("Show propositional Dynamisms");
            }
            else if (positiveImplicationExp.getExpressionType().equals(Expression.ExpressionType.ALGEBRA)) {
                setDynamismsExpressionType(Expression.ExpressionType.PROPOSITION);
                changeDynamismExpressionTypeMenuItem.setText("Show algebraic Dynamisms");
            }
        });
        return changeDynamismExpressionTypeMenuItem;
    }

    private Menu descendantsMenuOnlyThisFcc(Dynamism orientation) {
        Menu descendantsMenu = new Menu("Deploy descendants (effects)");
        Dynamism thisDynamism = dataInterface.getDynamism(getFruit().getFcc(),orientation.getOrientation());

        // New FCC
        MenuItem toNewFccMenuItem = new MenuItem("To a new FCC");
        toNewFccMenuItem.setOnAction(deployNewOrientationDescendantAction(thisDynamism,true));
        descendantsMenu.getItems().addAll(toNewFccMenuItem, new SeparatorMenuItem());

        // Descendant Fruits
        ObservableList<Integer> isDescendantFccIds = FXCollections.observableArrayList();
        for (Fruit fruit1 : fruit.getDescendantFruitsOnlyFcc()) {
            isDescendantFccIds.add(fruit1.getFcc().getIdFcc());
            FccMenu fccMenu = new FccMenu(this, thisDynamism, fruit1.getFcc(), FccMenu.FccMenuType.FOR_DESCENDANTS, true);
            descendantsMenu.getItems().addAll(fccMenu);
        }
        descendantsMenu.getItems().addAll(new SeparatorMenuItem());
        // 2 types of exclusion inside the for
        ObservableList<Integer> isDescendantFccIdsElsewhere = FXCollections.observableArrayList();
        ObservableList<Integer> deployedFromConjunction = FXCollections.observableArrayList();

        for (Fruit fruit1 : tree.getObservableFruits()) {
            if(fruit.equals(fruit1)) continue;
            // Exclude descendants of another fruit with same fcc
            if (fruit1.getFcc().getIdFcc() == this.fruit.getFcc().getIdFcc()) {
                for (Fruit fruit2 : fruit1.getDescendantFruits()) {
                    isDescendantFccIdsElsewhere.add(fruit2.getFcc().getIdFcc());
                }
            }
            // Exclude if its a descendant of another fruit of conjunction
            for (SubBranch subBranch : fruit.getBranch().getSubBranches()) {
                //if (fruit.getSubBranch().equals(subBranch))continue;
                if (subBranch.getFruit().getDescendantFruitsConjunction().contains(fruit1)) {
                    deployedFromConjunction.add(fruit1.getFcc().getIdFcc());
                }
            }
        }

        // Exclude from menu the fruits in same conjunction
        ObservableList<Integer> isInConjunction = FXCollections.observableArrayList();
        for (SubBranch subBranch : fruit.getBranch().getSubBranches()) {
            if(fruit.getSubBranch().equals(subBranch)) continue;
            isInConjunction.add(subBranch.getFruit().getFcc().getIdFcc());
        }
        // Add the rest of menus
        ObservableList<Fcc> fccs = dataInterface.getFccs(tree.getTodController().getTod().getLogicSystem());
        ObservableList<FccMenu> fccSortedMenus = FXCollections.observableArrayList();
        for (Fcc fcc : fccs){
            // Ignore itself in the deployment list
            if (fcc.getIdFcc()==getFruit().getFcc().getIdFcc()) continue;
            // Ignore fccs already done
            if (isDescendantFccIds.contains(fcc.getIdFcc())) continue;
            // Ignore descendant fccs elsewhere
            if(isDescendantFccIdsElsewhere.contains(fcc.getIdFcc())) continue;
            // If its in conjunction
            if(deployedFromConjunction.contains(fcc.getIdFcc())) continue;
            // Ignore fruits in same conjunction
            if(isInConjunction.contains(fcc.getIdFcc())) continue;
            // For all the rest create a menu
            FccMenu fccMenu = new FccMenu(this, thisDynamism, fcc, FccMenu.FccMenuType.FOR_DESCENDANTS, true);
            fccSortedMenus.add(fccMenu);
        }
        // Sort remaining FccMenus alphabetically before adding them
        Comparator<FccMenu> comparator = Comparator.comparing(FccMenu::getFccName);
        FXCollections.sort(fccSortedMenus,comparator);
        descendantsMenu.getItems().addAll(fccSortedMenus);//

        return descendantsMenu;
    }

    private Menu ascendantsMenuOnlyThisFcc(Dynamism orientation) {
        Menu ascendantsMenu = new Menu("Deploy ascendants (causes)");
        Dynamism thisDynamism = dataInterface.getDynamism(fruit.getFcc(),orientation.getOrientation());

        // Add MenuItem: New fcc
        MenuItem deployToNewFccMenuItem = new MenuItem("To a new FCC");
        deployToNewFccMenuItem.setOnAction(deployNewAscendantAction(thisDynamism,true));
        ascendantsMenu.getItems().addAll(deployToNewFccMenuItem, new SeparatorMenuItem());

        // Add MenuItems: Fruits already as descendants
        ObservableList<Integer> ascendantFCCs = FXCollections.observableArrayList();
        for (Fruit fruit1 : fruit.getAscendantFruitsOnlyFcc()) {
            ascendantFCCs.add(fruit1.getFcc().getIdFcc());
            FccMenu fccMenu = new FccMenu(this, thisDynamism,fruit1.getFcc(), FccMenu.FccMenuType.FOR_ASCENDANTS, true);
            ascendantsMenu.getItems().addAll(fccMenu);
        }
        ascendantsMenu.getItems().addAll(new SeparatorMenuItem());
        // Find fruits to exclude
        ObservableList<Integer> isAscendantFccIdsElsewhere = FXCollections.observableArrayList();
        ObservableList<Integer> deployedFromConjunction = FXCollections.observableArrayList();

        for (Fruit fruit1 : tree.getObservableFruits()) {
            if(fruit1.equals(this.fruit)) continue;
            // 2) find those that are already deployed in another fruit of the same fcc
            if (fruit1.getFcc().getIdFcc() == this.fruit.getFcc().getIdFcc()) {
                for (Fruit fruit2 : fruit1.getAscendantFruits()) {
                    isAscendantFccIdsElsewhere.addAll(fruit2.getFcc().getIdFcc());
                }
            }
            // exclude ascendants of another fruit of conjunction
            for (SubBranch subBranch : fruit.getBranch().getSubBranches()) {
                if (subBranch.getFruit().getAscendantFruitsConjunction().contains(fruit1)) {
                    deployedFromConjunction.add(fruit1.getFcc().getIdFcc());
                }
            }
        }

        // Exclude from menu the fruits in same conjunction
        ObservableList<Integer> isInConjunction = FXCollections.observableArrayList();
        for (SubBranch subBranch : fruit.getBranch().getSubBranches()) {
            if(fruit.getSubBranch().equals(subBranch)) continue;
            isInConjunction.add(subBranch.getFruit().getFcc().getIdFcc());
        }

        // Add MenuItems : for all fccs in LS except those in 1 and 2
        ObservableList<FccMenu> fccSortedMenus = FXCollections.observableArrayList();
        ObservableList<Fcc> fccs = dataInterface.getFccs(tree.getTodController().getTod().getLogicSystem());
        for (Fcc fcc : fccs){
            // Ignore itself in the deployment list
            if (fcc.getIdFcc()==getFruit().getFcc().getIdFcc()) continue;
            // Ignore fccs already done previously
            if (ascendantFCCs.contains(fcc.getIdFcc())) continue;
            // Ignore ascendants elsewhere
            if(isAscendantFccIdsElsewhere.contains(fcc.getIdFcc())) continue;
            // If its in conjunction
            if(deployedFromConjunction.contains(fcc.getIdFcc())) continue;
            // Ignore fruits in same conjunction
            if(isInConjunction.contains(fcc.getIdFcc())) continue;
            // For all the rest create a menu
            FccMenu fccMenu = new FccMenu(this, thisDynamism,fcc, FccMenu.FccMenuType.FOR_ASCENDANTS, true);
            fccSortedMenus.add(fccMenu);
            //thisDynamismMenu.getItems().addAll(fccMenu);
        }

        // Sort remaining FccMenus alphabetically before adding them
        Comparator<FccMenu> comparator = Comparator.comparing(FccMenu::getFccName);
        FXCollections.sort(fccSortedMenus,comparator);
        ascendantsMenu.getItems().addAll(fccSortedMenus);//

        return ascendantsMenu;
    }


    public void resetValueInScaleSlider() {
        this.tree.getTodController().getScaleSlider().setValue(1);
    }

    private EventHandler<ActionEvent>  deployNewOrientationDescendantAction(Dynamism ascendantDynamism, boolean onlyThisFcc) {
        return actionEvent -> {
            NodeHandler.getDataInterface().connect();
            TodController todController = this.tree.getTodController();
            // set scale to 1 before continuing, otherwise the binding of knobs does not work
            //resetValueInScaleSlider();
            LogicSystem logicSystem = todController.getTod().getLogicSystem();
            Fcc newFcc = NodeHandler.getDataInterface().newFcc(logicSystem);
            todController.openFccEditor(newFcc);
            Fruit newFruit= null;
            if (onlyThisFcc) {
                newFruit = fruit.getSubBranch().addToRightTrunk(newFcc);
            } else {
                newFruit = fruit.getBranch().addToRightTrunk(newFcc);
            }

            // Create inclusions
            Dynamism positiveDynamismOfDescendant = dataInterface.getDynamism(newFcc, 0);
            Dynamism negativeDynamismOfDescendant = dataInterface.getDynamism(newFcc, 1);
            Dynamism symmetricDynamismOfDescendant = dataInterface.getDynamism(newFcc, 2);
            dataInterface.newInclusion(positiveDynamismOfDescendant,ascendantDynamism, todController.getTod());
            dataInterface.newInclusion(negativeDynamismOfDescendant,ascendantDynamism, todController.getTod());
            dataInterface.newInclusion(symmetricDynamismOfDescendant,ascendantDynamism, todController.getTod());

            tieDescendant(newFruit);
            tree.updateFruitsMenus();
            NodeHandler.getDataInterface().disconnect();
        };
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

    private EventHandler<ActionEvent> deployNewAscendantAction(Dynamism descendantDynamism, boolean onlyThisFcc) {
        return actionEvent -> {
            NodeHandler.getDataInterface().connect();
            TodController todController = this.tree.getTodController();
            // set scale to 1 before continuing, otherwise the binding of knobs does not work
            resetValueInScaleSlider();
            LogicSystem logicSystem = todController.getTod().getLogicSystem();
            Fcc newFcc = NodeHandler.getDataInterface().newFcc(logicSystem);
            todController.openFccEditor(newFcc);
            Fruit newFruit = null;

            if (onlyThisFcc) {
                newFruit = fruit.getSubBranch().addToLeftTrunk(newFcc);
            } else {
                newFruit = fruit.getBranch().addToLeftTrunk(newFcc);
            }

            // Create inclusions
            Dynamism positiveDynamismOfAscendant = dataInterface.getDynamism(newFcc, 0);
            Dynamism negativeDynamismOfAscendant = dataInterface.getDynamism(newFcc, 1);
            Dynamism symmetricDynamismOfAscendant = dataInterface.getDynamism(newFcc, 2);
            dataInterface.newInclusion(descendantDynamism,positiveDynamismOfAscendant, todController.getTod());
            dataInterface.newInclusion(descendantDynamism,negativeDynamismOfAscendant, todController.getTod());
            dataInterface.newInclusion(descendantDynamism,symmetricDynamismOfAscendant, todController.getTod());

            tieAscendant(newFruit);
            tree.updateFruitsMenus();
            NodeHandler.getDataInterface().disconnect();
        };
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

    // This should be called only when opening a new TOD
    public void buildTies() {
        // Descendants in subBranch
        for (Branch branch : this.fruit.getSubBranch().getRightTrunk().getBranches()) {
            for (Fruit fruit1 : branch.getFruits()) {
                if (this.fruit.isDescendant(fruit1)) {
                    Tie tie = new Tie(fruit1, this.fruit);
                    this.tree.addTie(tie);
                    tie.setOrientations();
                }
            }
        }
        // Descendant in branch
        for (Branch branch : this.fruit.getBranch().getRightTrunk().getBranches()) {
            for (Fruit fruit1 : branch.getFruits()) {
                if (this.fruit.isDescendant(fruit1)) {
                    Tie tie = new Tie(fruit1, this.fruit);
                    this.tree.addTie(tie);
                    tie.setOrientations();
                }
            }
        }

        // Ascendants in subBranch
        for (Branch branch : this.fruit.getSubBranch().getLeftTrunk().getBranches()) {
            for (Fruit fruit1 : branch.getFruits()) {
                if (fruit1.isDescendant(this.fruit)) {
                    Tie tie = new Tie(this.fruit, fruit1);
                    this.tree.addTie(tie);
                    tie.setOrientations();
                }
            }
        }
        // Ascendants in branch
        for (Branch branch : this.fruit.getBranch().getLeftTrunk().getBranches()) {
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
/*

    public String getFirstPart() {
        return firstPart.get();
    }

    public StringProperty firstPartProperty() {
        return firstPart;
    }

    public void setFirstPart(String firstPart) {
        this.firstPart.set(firstPart);
    }
*/
/*
    public String getLastPart() {
        return lastPart.get();
    }

    public StringProperty lastPartProperty() {
        return lastPart;
    }

    public void setLastPart(String lastPart) {
        this.lastPart.set(lastPart);
    }*/

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