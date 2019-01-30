package com.amuyana.app.controllers;

import com.amuyana.app.FXMLSource;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.data.tod.CClass;
import com.amuyana.app.data.tod.Conjunction;
import com.amuyana.app.data.tod.containers.Tod;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.amuyana.app.node.MainBorderPane;
import com.amuyana.app.node.NodeInterface;
import com.amuyana.app.node.content.FccEditorTab;
import com.amuyana.app.node.content.RightPanelTab;
import com.amuyana.app.node.content.TodContentTab;
import com.amuyana.app.node.tod.Tree;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;

public class TodController implements Initializable {
    private NodeInterface nodeInterface;
    private DataInterface dataInterface;
    private Tod tod;

    @FXML private SplitPane splitPane;
    @FXML private Group canvas;
    @FXML private Slider scaleSlider;

    @FXML private ScrollPane treeScrollPane;

    @FXML private Label todNameLabel;
    @FXML private TextField todNameTextField;
    @FXML private HBox nameTodHBox;
    @FXML private ListView<Fcc> fccsInTodListView;

    @FXML private TabPane rightTabPane;

    private TodContentTab todContentTab;
    private Tree tree;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.dataInterface = MainBorderPane.getDataInterface();
//        treeScrollPane.setOnScroll(e->{if(e.isShiftDown()) e.consume(); return;});
        hideRightTabPane();
        manageEvents();
    }

    private void manageEvents() {
        rightTabPane.getTabs().addListener(new ListChangeListener<Tab>() {
            @Override
            public void onChanged(Change<? extends Tab> change) {
                if (change.next()) {
                    if (change.wasRemoved()) {
                        if (change.getList().size() == 1) {
                            hideRightTabPane();
                        }
                    }
                }
            }
        });
        treeScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
                manageScrollEvent(event);
                event.consume();
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

    private TodController getThisTodController() {
        return this;
    }

    public void setNodeInterface(NodeInterface nodeInterface) {
        this.nodeInterface=nodeInterface;
    }

    private void hideRightTabPane() {
        splitPane.getItems().remove(rightTabPane);
    }

    void showRightTabPane(Tab tab) {
        if (splitPane.getItems().size() == 2) {
            splitPane.getItems().add(rightTabPane);
            rightTabPane.getTabs().add(tab);
        } else if (splitPane.getItems().size() == 3) {
            rightTabPane.getTabs().add(tab);
        }
    }

    public void fillData() {
        todNameLabel.textProperty().bind(this.tod.labelProperty());
        // If there's no fcc in the tod, then show the FccSelector.fxml
        /*if (dataInterface.getFccs(tod).isEmpty()) {
            loadFccSelector();
        } else if(!dataInterface.getFccs(tod).isEmpty()){
            showNewTree();
        }*/
    }

    public void setTab(TodContentTab todContentTab) {
        this.todContentTab = todContentTab;
    }

    public void closeTab(RightPanelTab rightPanelTab) {
        rightTabPane.getTabs().remove(rightPanelTab);
        if (rightTabPane.getTabs().isEmpty()) {
            hideRightTabPane();
        }
    }


    public EventHandler<ActionEvent> openFccEditorEventHandler(Fcc fcc) {
        EventHandler<ActionEvent> eventEventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                openFccEditor(fcc);
            }
        };
        return eventEventHandler;
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
        FccEditorTab fccEditorTab =new FccEditorTab(getThisTodController(),nodeInterface,fcc);
        showRightTabPane(fccEditorTab);
        rightTabPane.getSelectionModel().select(fccEditorTab);
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

        // As we don't know yet if the user starts by creating a new FCC or by loading an existing one (or a
        // functional o a class), we don't initialize the Tree yet, we loadExistingTree the fcc selector

        this.canvas.getChildren().setAll(loadFccSelector());
        bindProperties();
    }

    public void openTod(Tod tod) {
        this.tod = tod;
        bindProperties();

        // as we are loading this for an existing Tod, check if there are any FCCs in the tod, then decide
        // to show or not fccSelector.css
        if (dataInterface.getFccs(tod).isEmpty()) {
            Node node = loadFccSelector();
            this.canvas.getChildren().setAll(node);
        } else if (!dataInterface.getFccs(tod).isEmpty()) {
            showTree();
        }
    }

    private void showScaleSlider() {
        scaleSlider.setMin(1);
        scaleSlider.maxProperty().bind(tree.maxLevelProperty());
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
        fccSelectorController.initialize(this.nodeInterface,this.todContentTab);
        return node;
    }

    public void bindProperties() {
        this.todNameLabel.textProperty().bind(this.tod.labelProperty());
        todNameTextField.textProperty().bind(this.tod.labelProperty());
        //fccsInTodListView.setItems(dataInterface.getFruit(this.tod));
    }

    @FXML
    private void editTod() {

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
                   | || | |  __/  __/
                   |_||_|  \___|\___|

         */
    void showTree() {
        this.tree = new Tree(this);
        this.tree.loadExistingTree();
        this.tree.updateFruits();
        canvas.getChildren().setAll(this.tree);
        showScaleSlider();
    }

    void showNewTree() {
        this.tree = new Tree(this);
        this.tree.loadNewTree();
        canvas.getChildren().setAll(this.tree);
        showScaleSlider();
    }

    void showNewTree(Fcc fcc) {
        this.tree = new Tree(this, fcc);
        this.tree.loadNewTreeFromExistingFcc(fcc);
        canvas.getChildren().setAll(this.tree);
        showScaleSlider();
    }

    void showNewTree(Conjunction conjunction) {
        this.tree = new Tree(this, conjunction);
        canvas.getChildren().clear();
        canvas.getChildren().setAll(this.tree);
        showScaleSlider();
    }

    void showNewTree(CClass cClass) {
        this.tree = new Tree(this, cClass);
        canvas.getChildren().clear();
        canvas.getChildren().setAll(this.tree);
        showScaleSlider();
    }

}