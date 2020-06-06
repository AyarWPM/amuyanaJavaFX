package com.amuyana.app.controllers;

import com.amuyana.app.data.*;
import com.amuyana.app.data.tod.CClass;
import com.amuyana.app.data.tod.Conjunction;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.data.tod.containers.Tod;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.NodeInterface;
import com.amuyana.app.node.content.TodScrollPane;
import com.amuyana.app.node.content.FccEditorTab;
import com.amuyana.app.node.content.RightPanelTab;
import com.amuyana.app.node.content.TodTab;
import com.amuyana.app.node.tod.Fruit;
import com.amuyana.app.node.tod.Tree;

import com.amuyana.app.node.tod.expression.Expression;
import com.amuyana.app.node.tod.expression.ImplicationExp;
import com.amuyana.app.node.tod.expression.SyllogismExp;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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

    private Slider scaleSlider;
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

    @FXML private HBox selectedSyllogismHBox;
    @FXML private ListView<Syllogism> syllogismsListView;
    @FXML private VBox selectedSyllogismVBox;
    @FXML private Label syllogismLabel;
    @FXML private TextField syllogismTextField;
    @FXML private Button syllogismButton;
    @FXML private Label syllogismTitle;

    private NodeInterface nodeInterface;
    private DataInterface dataInterface;
    private Tod tod;

    private TodTab todTab;
    private Tree tree;
    private BooleanProperty editName;
    private BooleanProperty leftPanelOpen;
    private BooleanProperty rightPanelOpen;
    private Syllogism selectedSyllogism;

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
        scaleSlider = new Slider(1,1,1);
        /*scaleSlider.setManaged(false);*/
        this.selectedSyllogismVBox.setManaged(false);
        this.syllogismTitle.setManaged(false);
        this.syllogismLabel.setManaged(false);
        this.syllogismTextField.setManaged(false);
        this.syllogismButton.setManaged(false);
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
        syllogismsListView.setItems(dataInterface.getSyllogisms(tod));
    }

    public void updateListViews() {
        fccsInTodListView.setItems(dataInterface.getFccs(this.tod));
        inclusionsInTodListView.setItems(dataInterface.getInclusions(tod));
    }

    public void updateSyllogismListView() {
        tree.setChangeOfSelectionFromTree(true);
        syllogismsListView.getItems().clear();
        syllogismsListView.setItems(dataInterface.getSyllogisms(tod));
        syllogismsListView.getSelectionModel().select(selectedSyllogism);
        tree.setChangeOfSelectionFromTree(false);
    }

    private void manageEvents() {
        syllogismsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Syllogism>() {
            @Override
            public void changed(ObservableValue<? extends Syllogism> observable, Syllogism oldValue, Syllogism newValue) {
                // Change from TableView to a null value (selecting non syllogism)
                if (newValue == null && !tree.getChangeOfSelectionFromTree()) {
                    //System.out.println("null from TableView");
                    clearSelectionOfSyllogism();
                }
                // Change from tree to a null value
                if (newValue == null && tree.getChangeOfSelectionFromTree()) {
                    //System.out.println("null from tree");
                    // do nothing
                }
                // Change from TableView to a non-null value
                if (newValue!=null && !tree.getChangeOfSelectionFromTree()) {
                    //System.out.println("non null from tableview");
                    clearSelectionOfSyllogism();
                    selectSyllogism(newValue);
                    // for all ties
                }
                // Change from tree to a non-null value
                if (newValue!=null && tree.getChangeOfSelectionFromTree()) {
                    //System.out.println("non null from tree");
                    // do nothing
                }
            }

            private void selectSyllogism(Syllogism syllogism) {
                List<Inclusion> listOfInclusions = new ArrayList<>(dataInterface.getInclusions(syllogism));
                Collections.reverse(listOfInclusions);

                ObservableList<List<ImplicationExp>> listOfLists = FXCollections.observableArrayList();

                for (Fruit fruit : tree.getObservableFruits()) {
                    ObservableList<ImplicationExp> anImplicationExps = FXCollections.observableArrayList();
                    int count = 0;
                    List<ImplicationExp> tempListOfImplicationExps = new ArrayList<>(findImplicationExpsOfSyllogism(count, fruit, listOfInclusions, anImplicationExps));

                    if (tempListOfImplicationExps.size()-1 == listOfInclusions.size()) {
                        listOfLists.add(tempListOfImplicationExps);
                    }
                }

                // There may be more than one list of implicationsExps, in the case where is repetition of the exact syllogism in two or more places
                // in the TOD. Here we only consider the first one... but later we may take account of the fact that these repetitions are only possible when
                // the general term of an inclusion is in one case non part of a conjunction ("Deploy from this fcc" menu) and in other cases part of a conjunction
                // or more generally the conjunctions are different.

                for (ImplicationExp implicationExp : listOfLists.get(0)) {
                    implicationExp.setSelection(true);
                }

                if (!listOfLists.isEmpty()) {
                    tree.setSelection(true);
                    tree.setSelectedListInclusions(listOfInclusions);
                    tree.setImplicationExps(listOfLists.get(0));
                    showSelectedSyllogismExpression(listOfInclusions);
                }
            }

            private List<ImplicationExp> findImplicationExpsOfSyllogism(int count, Fruit fruit, List<Inclusion> listOfInclusions, ObservableList<ImplicationExp> anImplicationExps) {
                boolean foundImplicationExp=false;
                for (ImplicationExp implicationExp : fruit.getFruitController().getImplicationExps()) {
                    if(foundImplicationExp) break;
                    if (listOfInclusions.get(count).getGeneral().equals(implicationExp.getDynamism())) {
                        for (Fruit fruit1 : fruit.getDescendantFruits()) {
                            if(foundImplicationExp) break;
                            for (ImplicationExp implicationExp1 : fruit1.getFruitController().getImplicationExps()) {
                                if (listOfInclusions.get(count).getParticular().equals(implicationExp1.getDynamism())) {
                                    if(!anImplicationExps.contains(implicationExp)){
                                        anImplicationExps.add(implicationExp);
                                    }
                                    if(!anImplicationExps.contains(implicationExp1)){
                                        anImplicationExps.add(implicationExp1);
                                    }

                                    if (count < listOfInclusions.size()-1) {
                                        findImplicationExpsOfSyllogism(++count,fruit1,listOfInclusions,anImplicationExps);
                                    }
                                    if (count == listOfInclusions.size() - 1) {
                                        foundImplicationExp=true;
                                    }
                                    if(foundImplicationExp) break;
                                }
                            }
                            //
                        }
                    }
                }
                return anImplicationExps;
            }

            private void clearSelectionOfSyllogism() {
                tree.setSelection(false);
                tree.getSelectedListInclusions().clear();
                tree.getImplicationExps().clear();
                selectedSyllogismVBox.setManaged(false);
                for (Fruit fruit : getTree().getObservableFruits()) {
                    fruit.getFruitController().unselectAllImplicationExp();
                }
            }
        });
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
            nodeInterface.log("Enter integers in \"amount\" and \"index\" fields");
            return;
        }
        if (fccsInTodListView.getSelectionModel().isEmpty()) {
            nodeInterface.log("Select an FCC from the list");
            return;
        }
        if (amount <= 0) {
            nodeInterface.log("Amount must be positive");
            return;
        }
        if (amount > 10) {
            nodeInterface.log("Maximum amount is 10");
            return;
        }
        Fcc fccToDuplicate = fccsInTodListView.getSelectionModel().getSelectedItem();

        nodeInterface.log("Duplicating " + fccToDuplicate + ", please wait...");
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> {
            dataInterface.connect();
            if (!fccsInTodListView.getSelectionModel().isEmpty()) {
                int i=index;
                while (i < index + amount) {
                    if (index == 0) {
                        Fcc newFcc = dataInterface.duplicateFcc(fccToDuplicate,0,tod.getLogicSystem());
                    } else if (index > 0) {
                        Fcc newFcc = dataInterface.duplicateFcc(fccToDuplicate,i,tod.getLogicSystem());
                    }
                    i++;
                }
                tree.updateFruitsMenus();
                nodeInterface.log(fccToDuplicate.getName() + " has been duplicated " + amount + " times");
            }
            dataInterface.disconnect();
        });
        new Thread(sleeper).start();



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

    public void showFccSelector() {
        setContent(loadFccSelector());
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

    public NodeInterface getNodeInterface() {
        return nodeInterface;
    }

    public HBox getSelectedSyllogismHBox() {
        return selectedSyllogismHBox;
    }

    public VBox getSelectedSyllogismVBox() {
        return selectedSyllogismVBox;
    }

    /**
     *
     * @param selectedListInclusions The list is in reverse order as that of the syllogism, it is in the order of
     *                              the table of deductions from left to right
     */
    public void showSelectedSyllogismExpression(List<Inclusion> selectedListInclusions) {
        // 1. reverse the list
        List<Inclusion> listInclusions = new ArrayList<>(selectedListInclusions);
        Collections.reverse(listInclusions);
        // 2. Determine if there is already a syllogism with those inclusions
        syllogismTitle.setManaged(true);
        syllogismButton.setManaged(true);
        if (dataInterface.isSyllogism(listInclusions,getTod())) {
            this.selectedSyllogism=dataInterface.getSyllogism(listInclusions,getTod());
            syllogismLabel.setText(selectedSyllogism.getLabel());
            syllogismLabel.setManaged(true);
            syllogismLabel.setVisible(true);
            syllogismTextField.setManaged(false);
            syllogismTextField.setVisible(false);
            syllogismButton.setText("Edit");
            updateSyllogismListView();

        } else {
            tree.setChangeOfSelectionFromTree(true);
            syllogismsListView.getSelectionModel().clearSelection();
            tree.setChangeOfSelectionFromTree(false);
            syllogismLabel.setManaged(false);
            syllogismLabel.setVisible(false);
            syllogismTextField.setManaged(true);
            syllogismTextField.setVisible(true);
            syllogismTextField.setText("Syllogism label");
            syllogismButton.setText("Create");
            selectedSyllogism=null;
        }

        if (!getSelectedSyllogismVBox().isManaged()) {
            getSelectedSyllogismVBox().setManaged(true);
        }
        SyllogismExp syllogismExp = new SyllogismExp(selectedListInclusions);
        getSelectedSyllogismHBox().getChildren().setAll(syllogismExp.getExpression());
    }

    @FXML
    public void syllogismButtonOnAction() {
        switch (syllogismButton.getText()) {
            // It will save a new Syllogism
            case "Create":{
                List<Inclusion> listInclusions = new ArrayList<>(tree.getSelectedListInclusions());
                Collections.reverse(listInclusions);
                NodeHandler.getDataInterface().connect();
                selectedSyllogism = dataInterface.newSyllogism(syllogismTextField.getText(),listInclusions, tod);
                NodeHandler.getDataInterface().disconnect();
                syllogismLabel.setText(syllogismTextField.getText());
                syllogismLabel.setManaged(true);
                syllogismLabel.setVisible(true);
                syllogismTextField.setManaged(false);
                syllogismTextField.setVisible(false);
                syllogismButton.setText("Edit");
                updateSyllogismListView();
                break;
            }
            case "Edit":{
                syllogismLabel.setManaged(false);
                syllogismLabel.setVisible(false);
                syllogismTextField.setManaged(true);
                syllogismTextField.setVisible(true);
                syllogismTextField.setText(selectedSyllogism.getLabel());
                syllogismButton.setText("Save");
                break;
            }
                //it will save an existing syllogism
            case "Save":{
                selectedSyllogism.setLabel(syllogismTextField.getText());
                NodeHandler.getDataInterface().connect();
                dataInterface.update(selectedSyllogism);
                NodeHandler.getDataInterface().disconnect();
                syllogismLabel.setText(syllogismTextField.getText());
                syllogismLabel.setManaged(true);
                syllogismLabel.setVisible(true);
                syllogismTextField.setManaged(false);
                syllogismTextField.setVisible(false);
                syllogismButton.setText("Edit");
                updateSyllogismListView();
                break;
            }
        }
    }

    public void setSelectedSyllogism(Syllogism syllogism) {
        selectedSyllogism = syllogism;
    }
}
