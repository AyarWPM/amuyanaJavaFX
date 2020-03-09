package com.amuyana.app.controllers;

import com.amuyana.app.data.*;
import com.amuyana.app.data.tod.CClass;
import com.amuyana.app.data.tod.Conjunction;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.data.tod.containers.Tod;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import com.amuyana.app.node.Message;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.NodeInterface;
import com.amuyana.app.node.content.TodScrollPane;
import com.amuyana.app.node.content.FccEditorTab;
import com.amuyana.app.node.content.RightPanelTab;
import com.amuyana.app.node.content.TodTab;
import com.amuyana.app.node.tod.Fruit;
import com.amuyana.app.node.tod.Tree;

import com.amuyana.app.node.tod.expression.Expression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;

public class TodController implements Initializable {
    @FXML private SplitPane splitPane;
    //@FXML private HBox canvas;

    @FXML private Slider scaleSlider;
    @FXML private Button changeFCCsNotationButton;
    @FXML private Button changeDynamismsNotationButton;

    @FXML private ScrollPane treeScrollPane;
    @FXML private BorderPane todBorderPane;
    @FXML private Label todNameLabel;
    @FXML private Label logicSystemNameLabel;
    @FXML private TextField todNameTextField;
    @FXML private ListView<Fcc> fccsInTodListView;
    @FXML private TextField amountCopyTextField;
    @FXML private TextField indexCopyTextField;
    @FXML private ListView<Inclusion> inclusionsInTodListView;
    @FXML private ScrollPane leftPanel;
    @FXML private Button toggleTodNameButton;

    @FXML private Button toggleLeftPanelButton;
    @FXML private TabPane rightTabPane;

    private NodeInterface nodeInterface;
    private DataInterface dataInterface;
    private Tod tod;

    private TodTab todTab;
    private Tree tree;
    private BooleanProperty editName;
    private BooleanProperty leftPanelOpen;
    private BooleanProperty rightPanelOpen;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dataInterface = NodeHandler.getDataInterface();
        editName = new SimpleBooleanProperty();
        leftPanelOpen = new SimpleBooleanProperty(true);
        rightPanelOpen = new SimpleBooleanProperty(true);
        //this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        toggleRightTabPane();
        manageEvents();
        //todo : zoom
        scaleSlider.setManaged(false);
    }

    public void setTab(TodTab todTab) {
        this.todTab = todTab;
    }

    public void setNodeInterface(NodeInterface nodeInterface) {
        this.nodeInterface=nodeInterface;
    }

    private void bindProperties() {
        todNameLabel.textProperty().bind(todNameTextField.textProperty());
        todNameLabel.visibleProperty().bind(editName.not());
        todNameLabel.managedProperty().bind(editName.not());

        todNameTextField.textProperty().bindBidirectional(tod.labelProperty());
        todNameTextField.visibleProperty().bind(editName);
        todNameTextField.managedProperty().bind(editName);
    }

    public void fillData() {
        logicSystemNameLabel.textProperty().bind(nodeInterface.getLogicSystem().labelProperty());
        updateListViews();
    }

    public void updateListViews() {
        fccsInTodListView.setItems(dataInterface.getFccs(this.tod));
        inclusionsInTodListView.setItems(dataInterface.getInclusions(tod));
    }

    private void manageEvents() {
        // Part of zoom
        /*treeScrollPane.viewportBoundsProperty().addListener(
                new ChangeListener<Bounds>() {
                    @Override public void changed(ObservableValue<? extends Bounds> observableValue, Bounds oldBounds, Bounds newBounds) {
                        System.out.println("newBounds = " + newBounds);

                        canvas.setPrefSize(
                                Math.max(tree.getBoundsInParent().getMaxX(), newBounds.getWidth()),
                                Math.max(tree.getBoundsInParent().getMaxY(), newBounds.getHeight())
                        );

                    }
                });*/

        /*rightTabPane.getTabs().addListener((ListChangeListener<Tab>) change -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    if (change.getList().size() == 1) {
                        setRightPanelOpen(false);
                    }
                }
            }
        });*/



        // old zoom stuff
        /*treeScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            event.consume();
            manageScrollEvent(event);
        });*/

    }

    private void manageScrollEvent(ScrollEvent scrollEvent) {
        if(scrollEvent.getDeltaY()==0) return;
        if(scrollEvent.isShiftDown()) return;

        int factor = (int) (scrollEvent.getDeltaY()/scrollEvent.getMultiplierY()); // 40/40 = +/-1
        if (factor == 1) {
            scaleSlider.setValue(scaleSlider.getValue()+1);
        } else if (factor == -1) {
            scaleSlider.setValue(scaleSlider.getValue()-1);
        }

        double deltaX = scrollEvent.getX()/treeScrollPane.getWidth()-0.5;
        double deltaY = scrollEvent.getY()/treeScrollPane.getHeight()-0.5;

        treeScrollPane.setHvalue((treeScrollPane.getHvalue()+1)*0.3+treeScrollPane.getHvalue()*deltaX*50);
        treeScrollPane.setVvalue((treeScrollPane.getVvalue()+1)*0.3+treeScrollPane.getVvalue()*deltaY*40);
    }

    void setContent(Node node) {
        HBox hBox = new HBox(node);
        hBox.setPadding(new Insets(300));
        TodScrollPane todScrollPane = new TodScrollPane(hBox);
        todScrollPane.setHvalue(0.5);
        todScrollPane.setVvalue(0.5);
        todBorderPane.setCenter(todScrollPane);
    }

    @FXML
    void duplicateFcc() {
        int amount;
        int index;
        try {
            amount = Integer.parseInt(amountCopyTextField.getText());
            index = Integer.parseInt(indexCopyTextField.getText());
        }catch(NumberFormatException e) {
            nodeInterface.log("Enter positive values in \"amount\" and \"index\" fields");
            return;
        }
        if (fccsInTodListView.getSelectionModel().isEmpty()) {
            nodeInterface.log("Select an FCC from the list");
        }
        if (amount == 0) {
            nodeInterface.log("0 Fccs have been duplicated");
            return;
        }

        dataInterface.connect();
        if (!fccsInTodListView.getSelectionModel().isEmpty()) {
            Fcc oldFcc = fccsInTodListView.getSelectionModel().getSelectedItem();
            int i=index;
            while (i < index + amount) {
                if (index == 0) {
                    Fcc newFcc = dataInterface.duplicateFcc(oldFcc,0,tod.getLogicSystem());
                } else if (index > 0) {
                    Fcc newFcc = dataInterface.duplicateFcc(oldFcc,i,tod.getLogicSystem());
                }
                i++;
            }
            tree.updateFruitsMenus();
            nodeInterface.log(amount + " FCCs have been duplicated");
        }
        dataInterface.disconnect();
    }

    @FXML
    private void toggleTodName() {
        if (isEditName()) {
            // Save
            setEditName(false);
            dataInterface.connect();
            dataInterface.update(tod);
            dataInterface.disconnect();

            toggleTodNameButton.setText("Edit");
        } else {
            setEditName(true);
            toggleTodNameButton.setText("Save");
        }
    }

    @FXML
    private void toggleLeftPanel() {
        if (getLeftPanelOpen()) {
            setLeftPanelOpen(false);
            toggleLeftPanelButton.setText("\u2192");
            splitPane.getItems().remove(leftPanel);
        } else {
            setLeftPanelOpen(true);
            toggleLeftPanelButton.setText("\u2190");
            splitPane.getItems().add(0, leftPanel);
        }
    }

    @FXML
    private void changeFCCsNotation() {
        String m1="Show propositional notation for all FCCs";
        String m2="Show algebraic notation for all FCCs";
        if (changeFCCsNotationButton.getText().equals(m1)) {
            for (Fruit fruit : getTree().getObservableFruits()) {
                fruit.changeFCCsNotationType(Expression.ExpressionType.PROPOSITION);
            }
            changeFCCsNotationButton.setText(m2);
        } else if (changeFCCsNotationButton.getText().equals(m2)) {
            for (Fruit fruit : getTree().getObservableFruits()) {
                fruit.changeFCCsNotationType(Expression.ExpressionType.ALGEBRA);
            }
            changeFCCsNotationButton.setText(m1);
        }
    }

    @FXML
    private void changeDynamismsNotation() {
        String m1="Show propositional notation for all dynamisms";
        String m2="Show algebraic notation for all dynamisms";

        if (changeDynamismsNotationButton.getText().equals(m1)) {
            for (Fruit fruit : getTree().getObservableFruits()) {
                fruit.changeDynamismsNotationType(Expression.ExpressionType.PROPOSITION);
            }
            changeDynamismsNotationButton.setText(m2);
        }

        else if (changeDynamismsNotationButton.getText().equals(m2)) {
            for (Fruit fruit : getTree().getObservableFruits()) {
                fruit.changeDynamismsNotationType(Expression.ExpressionType.ALGEBRA);
            }
            changeDynamismsNotationButton.setText(m1);
        }
    }


    @FXML
    private void editTod() {
        //todo
    }

    private TodController getThisTodController() {
        return this;
    }

    private void toggleRightTabPane() {
        if (getRightPanelOpen()) {
            setRightPanelOpen(false);
            splitPane.getItems().remove(rightTabPane);
        } else if (!getRightPanelOpen()){
            setRightPanelOpen(true);
            splitPane.getItems().add(rightTabPane);
        }
    }

    void closeTab(RightPanelTab rightPanelTab) {
        rightTabPane.getTabs().remove(rightPanelTab);
        if (rightTabPane.getTabs().isEmpty()) {
            toggleRightTabPane();
        }
    }


    EventHandler<ActionEvent> openFccEditorEventHandler(Fcc fcc) {
        return actionEvent -> openFccEditor(fcc);
    }

    public void openFccEditor(Fcc fcc) {
        for (Tab tab : rightTabPane.getTabs()) {
            if (tab.getClass().equals(FccEditorTab.class)) {
                FccEditorTab fccEditorTab = (FccEditorTab)tab;
                if(fccEditorTab.getFccEditorController().getFcc().equals(fcc)) {
                    rightTabPane.getSelectionModel().select(fccEditorTab);
                    return;
                }
            }
        }
        if (rightTabPane.getTabs().isEmpty()) {
            toggleRightTabPane();
        }
        FccEditorTab fccEditorTab =new FccEditorTab(getThisTodController(),nodeInterface,fcc);
        fccEditorTab.getFccEditorController().setEditMode(true);
        rightTabPane.getTabs().add(fccEditorTab);
        rightTabPane.getSelectionModel().select(fccEditorTab);
    }

    private void showScaleSlider() {
        scaleSlider.setMin(1);
        scaleSlider.maxProperty().bind(tree.maxLevelProperty());
// part of zoom
        /*tree.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override public void changed(ObservableValue<? extends Bounds> observableValue, Bounds oldBounds, Bounds newBounds) {
                canvas.setPrefSize(
                        Math.max(newBounds.getMaxX(), treeScrollPane.getViewportBounds().getWidth()),
                        Math.max(newBounds.getMaxY(), treeScrollPane.getViewportBounds().getHeight())
                );
            }
        });*/

    }

    public void newTod(LogicSystem logicSystem) {
        // get the maximum id of tables of deductions and add 1
        int currentValue=0;
        for (Tod tod : dataInterface.getTods(logicSystem)) {
            if (currentValue < tod.getIdTod()) {
                currentValue = tod.getIdTod();
            }
        }
        currentValue+=1;
        this.tod = this.dataInterface.newTod("New Table of deductions " + currentValue,logicSystem);
        setContent(loadFccSelector());
        //this.canvas.getChildren().setAll(loadFccSelector());
        bindProperties();
    }

    public void openTod(Tod tod) {
        this.tod = tod;
        bindProperties();
        if (dataInterface.getFccs(tod).isEmpty()) {
            setContent(loadFccSelector());
            //this.canvas.getChildren().setAll(node);
        } else if (!dataInterface.getFccs(tod).isEmpty()) {
            showTree();
        }
    }

    private Node loadFccSelector() {
        Node node = null;
        FccSelectorController fccSelectorController=null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXMLSource.FCCSELECTOR.getUrl()));
        try {
            node = fxmlLoader.load();
            fccSelectorController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(fccSelectorController).initialize(this.nodeInterface,this.todTab);
        return node;
    }

    public ScrollPane getTreeScrollPane() {
        return treeScrollPane;
    }


    public Tod getTod() {
        return this.tod;
    }

    public Tree getTree() {
        return this.tree;
    }

    public Slider getScaleSlider() {
        return this.scaleSlider;
    }

    /*
                  _____
                 |_   _| __ ___  ___
                   | || '__/ _ \/ _ \
                   | || | |  __/  __/    = TABLE OF DEDUCTIONS
                   |_||_|  \___|\___|

     */

    /**
     * It instantiates this.tree and loads a new tree, that is it will create a new FCC
     */
    void showNewTree() {
        this.tree = new Tree(this);
        this.tree.loadNewTree();
        setContent(this.tree);
        //canvas.getChildren().setAll(this.tree);
        this.tree.updateFruitsMenus();
        this.tree.buildTies();
        showScaleSlider();
    }

    /**
     * It instantiates this.tree and loads an existing tree, i.e. it seeks for containers in the database from which
     * we create the Table of Deductions (Tree) just like the user crated it in the first place
      */
    public void showTree() {
        this.tree = new Tree(this);
        this.tree.loadExistingTree();
        //canvas.getChildren().setAll(this.tree);
        this.tree.updateFruitsMenus();
        this.tree.buildTies();
        setContent(this.tree);
        showScaleSlider();
    }

    /**
     * It instantiates this.tree and loads a Tree departing from one FCC which is provided by the user from the
     * drop-down menu
     * @param fcc Fundamental Conjunction of Contradiction
     */
    void showNewTree(Fcc fcc) {
        this.tree = new Tree(this);
        this.tree.loadNewTreeFromExistingFcc(fcc);
        setContent(this.tree);
        //canvas.getChildren().setAll(this.tree);
        this.tree.updateFruitsMenus();
        this.tree.buildTies();
        showScaleSlider();
    }

    void showNewTree(Conjunction conjunction) {

    }

    void showNewTree(CClass cClass) {

    }

    private boolean isEditName() {
        return editName.get();
    }

    public BooleanProperty editNameProperty() {
        return editName;
    }

    private void setEditName(boolean editName) {
        this.editName.set(editName);
    }

    private boolean getLeftPanelOpen() {
        return leftPanelOpen.get();
    }

    public BooleanProperty leftPanelOpenProperty() {
        return leftPanelOpen;
    }

    private void setLeftPanelOpen(boolean leftPanelOpen) {
        this.leftPanelOpen.set(leftPanelOpen);
    }

    private boolean getRightPanelOpen() {
        return rightPanelOpen.get();
    }

    public BooleanProperty rightPanelOpenProperty() {
        return rightPanelOpen;
    }

    private void setRightPanelOpen(boolean rightPanelOpen) {
        this.rightPanelOpen.set(rightPanelOpen);
    }


}
