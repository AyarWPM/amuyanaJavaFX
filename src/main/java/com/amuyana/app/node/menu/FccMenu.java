package com.amuyana.app.node.menu;
import com.amuyana.app.controllers.FruitController;
import com.amuyana.app.data.*;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.tod.Fruit;
import com.amuyana.app.node.tod.Tree;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.layout.Region;

import java.util.Optional;


public class FccMenu extends Menu {
    private final DataInterface dataInterface;
    private final Tree tree;
    private final Fruit fruit;
    private final Fcc fcc;
    private final FruitController fruitController;
    Tod tod;
    private FccMenuType fccMenuType;

    public enum FccMenuType{FOR_ASCENDANTS,FOR_DESCENDANTS}

    /**
     * Constructor for descendants' menu
     * Menu that can be used in any of the two locations (bracketMenuButton or the descendantMenuButton) where
     * user orders deployments of the Table of Deductions
     * @param fruitController the controller of the fruit.fxml instance representing fruit
     * @param thisDynamism The dynamism of the fruit from which the other dynamisms of the deployFcc fruit will be deployed
     * @param fcc The fcc that descends from ascendantDynamism
     * @param inFccOnly True means the fccMenu is for deployments of the fcc only, not the conjunction, therefore we deploy
     *                  to subBranch. If its false we deploy for conjunction, ie in Branch's trunk
     */
    public FccMenu(FruitController fruitController, Dynamism thisDynamism, Fcc fcc, FccMenuType fccMenuType, boolean inFccOnly) {
        super(fcc.toString());
        this.fruitController = fruitController;
        this.fruit = fruitController.getFruit();
        this.fcc = fcc;
        this.fccMenuType=fccMenuType;
        this.tree = fruit.getTree();
        this.tod = tree.getTodController().getTod();
        this.fccMenuType = FccMenuType.FOR_DESCENDANTS;
        this.dataInterface = NodeHandler.getDataInterface();

        if (fccMenuType.equals(FccMenuType.FOR_ASCENDANTS)) {
            buildForAscendant(thisDynamism,inFccOnly);
        } else if (fccMenuType.equals(FccMenuType.FOR_DESCENDANTS)) {
            buildForDescendant(thisDynamism,inFccOnly);
        }
    }

    public String getFccName() {
        return fcc.getName();
    }

    private void buildForAscendant(Dynamism descendantDynamism, boolean inFccOnly) {
        Dynamism positiveAscendant = dataInterface.getDynamism(fcc,0);
        Dynamism negativeAscendant = dataInterface.getDynamism(fcc,1);
        Dynamism symmetricAscendant = dataInterface.getDynamism(fcc,2);

        CheckMenuItem fromPositiveCheckMenuItem = new CheckMenuItem();
        CheckMenuItem fromNegativeCheckMenuItem = new CheckMenuItem();
        CheckMenuItem fromSymmetricCheckMenuItem = new CheckMenuItem();
        fromPositiveCheckMenuItem.textProperty().bind(positiveAscendant.propositionProperty());
        fromNegativeCheckMenuItem.textProperty().bind(negativeAscendant.propositionProperty());
        fromSymmetricCheckMenuItem.textProperty().bind(symmetricAscendant.propositionProperty());

        if (dataInterface.isInclusion(descendantDynamism,positiveAscendant,tod)) {
            fromPositiveCheckMenuItem.setSelected(true);
        }
        if (dataInterface.isInclusion(descendantDynamism,negativeAscendant,tod)) {
            fromNegativeCheckMenuItem.setSelected(true);
        }
        if (dataInterface.isInclusion(descendantDynamism,symmetricAscendant,tod)) {
            fromSymmetricCheckMenuItem.setSelected(true);
        }

        fromPositiveCheckMenuItem.setOnAction(fromAscendantCheckMenuAction(descendantDynamism, positiveAscendant, fromPositiveCheckMenuItem, inFccOnly));
        fromNegativeCheckMenuItem.setOnAction(fromAscendantCheckMenuAction(descendantDynamism, negativeAscendant, fromNegativeCheckMenuItem,inFccOnly));
        fromSymmetricCheckMenuItem.setOnAction(fromAscendantCheckMenuAction(descendantDynamism, symmetricAscendant, fromSymmetricCheckMenuItem,inFccOnly));

        getItems().setAll(fromPositiveCheckMenuItem,fromNegativeCheckMenuItem,fromSymmetricCheckMenuItem);
    }

    private void buildForDescendant(Dynamism ascendantDynamism, boolean inFccOnly) {
        Dynamism positiveDescendant = dataInterface.getDynamism(fcc,0);
        Dynamism negativeDescendant = dataInterface.getDynamism(fcc,1);
        Dynamism symmetricDescendant = dataInterface.getDynamism(fcc,2);

        CheckMenuItem toPositiveCheckMenuItem = new CheckMenuItem();
        CheckMenuItem toNegativeCheckMenuItem = new CheckMenuItem();
        CheckMenuItem toSymmetricCheckMenuItem = new CheckMenuItem();
        toPositiveCheckMenuItem.textProperty().bind(positiveDescendant.propositionProperty());
        toNegativeCheckMenuItem.textProperty().bind(negativeDescendant.propositionProperty());
        toSymmetricCheckMenuItem.textProperty().bind(symmetricDescendant.propositionProperty());

        if (dataInterface.isInclusion(positiveDescendant,ascendantDynamism,tod)) {
            toPositiveCheckMenuItem.setSelected(true);
        }
        if (dataInterface.isInclusion(negativeDescendant,ascendantDynamism,tod)) {
            toNegativeCheckMenuItem.setSelected(true);
        }
        if (dataInterface.isInclusion(symmetricDescendant,ascendantDynamism,tod)) {
            toSymmetricCheckMenuItem.setSelected(true);
        }

        toPositiveCheckMenuItem.setOnAction(toOrientationCheckMenuAction(ascendantDynamism,positiveDescendant,toPositiveCheckMenuItem,inFccOnly));
        toNegativeCheckMenuItem.setOnAction(toOrientationCheckMenuAction(ascendantDynamism,negativeDescendant,toNegativeCheckMenuItem,inFccOnly));
        toSymmetricCheckMenuItem.setOnAction(toOrientationCheckMenuAction(ascendantDynamism,symmetricDescendant,toSymmetricCheckMenuItem,inFccOnly));

        getItems().setAll(toPositiveCheckMenuItem,toNegativeCheckMenuItem,toSymmetricCheckMenuItem);
    }

    private EventHandler<ActionEvent> fromAscendantCheckMenuAction(Dynamism descendantDynamism,
                                                                   Dynamism ascendantDynamism,
                                                                   CheckMenuItem checkMenuItem, boolean inFccOnly) {
        return actionEvent -> {
            tree.getTodController().getNodeInterface().log("Applying changes, please wait...");
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
                // If user selects
                if (checkMenuItem.selectedProperty().get()) {
                    dataInterface.newInclusion(descendantDynamism,ascendantDynamism, tree.getTodController().getTod());
                    // If there's a fruit already just add inclusion (fruits are tied already)
                    boolean thereIsAFruit = false;
                    for (Fruit fruit1 : fruit.getAscendantFruits()) {
                        if (fruit1.getFcc().getIdFcc() == ascendantDynamism.getFcc().getIdFcc()) {
                            thereIsAFruit = true;
                            fruitController.tieAscendant(fruit1);
                            break;
                        }
                    }
                    if (!thereIsAFruit) {
                        Fruit newFruit;
                        if (inFccOnly) {
                            newFruit = fruit.getSubBranch().addToLeftTrunk(fcc);
                        } else {
                            newFruit = fruit.getBranch().addToLeftTrunk(fcc);
                        }
                        fruitController.resetValueInScaleSlider();

                        fruitController.tieAscendant(newFruit);
                    }
                }
                // If user deselects
                else if (!checkMenuItem.selectedProperty().get()) {
                    Inclusion inclusionToRemove = null;
                    for (Inclusion inclusion : dataInterface.getListInclusions()) {
                        if (inclusion.getTod().equals(tod)) {
                            if (inclusion.getParticular().getIdDynamism()==descendantDynamism.getIdDynamism()) {
                                if (inclusion.getGeneral().getIdDynamism()==ascendantDynamism.getIdDynamism()) {
                                    inclusionToRemove=inclusion;
                                }
                            }
                        }
                    }
                    // if there are syllogisms with the inclusion ask if user wants to remove them, otherwise cancel
                    ObservableList<Syllogism> syllogismsWithInclusionToRemove = FXCollections.observableArrayList();
                    for (InclusionHasSyllogism inclusionHasSyllogism : dataInterface.getListInclusionHasSyllogisms()) {
                        if (inclusionHasSyllogism.getInclusion().equals(inclusionToRemove)) {
                            syllogismsWithInclusionToRemove.add(inclusionHasSyllogism.getSyllogism());
                        }
                    }

                    // check if by removing the inclusion we also remove the fruit, if so then check if there are
                    // child syllogisms that would be removed
                    if (dataInterface.getInclusions(fruit.getTree().getTodController().getTod(), inclusionToRemove.getParticular().getFcc(), inclusionToRemove.getGeneral().getFcc()).size() == 1) {
                        for (Syllogism syllogism : dataInterface.getListSyllogisms(tod)) {
                            ObservableList<Inclusion> listAllChildInclusions = FXCollections.observableArrayList();
                            for (Fruit fruit1 : fruit.getAscendantChildFruits()) {
                                if (fruit1.getFcc().equals(inclusionToRemove.getGeneral().getFcc())) {
                                    if (fruit.getChildAscendantInclusions(listAllChildInclusions).containsAll(dataInterface.getInclusions(syllogism))) {
                                        if (!syllogismsWithInclusionToRemove.contains(syllogism)) {
                                            syllogismsWithInclusionToRemove.add(syllogism);
                                        }
                                    }
                                } else {
                                    if (fruit.getChildInclusions(listAllChildInclusions).containsAll(dataInterface.getInclusions(syllogism))) {
                                        if (!syllogismsWithInclusionToRemove.contains(syllogism)) {
                                            syllogismsWithInclusionToRemove.add(syllogism);
                                        }
                                    }
                                }
                            }

                        }
                    }

                    if (syllogismsWithInclusionToRemove.isEmpty()) {
                        dataInterface.delete(inclusionToRemove);
                    } else {
                        // ask
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        alert.setResizable(true);
                        alert.setTitle("Confirm deletion of dependent syllogisms");
                        alert.setHeaderText("If you continue the following syllogisms will also be removed, do you want to continue?");
                        String contentText="";
                        for (Syllogism syllogism : syllogismsWithInclusionToRemove) {
                            contentText=contentText+syllogism.getLabel() + "\n";
                        }
                        alert.setContentText(contentText);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent()) {
                            if (result.get() == ButtonType.OK) {
                                // delete the syllogisms and all dependencies
                                //dataInterface.getDataConnection().connect();
                                for (Syllogism syllogism : syllogismsWithInclusionToRemove) {
                                    dataInterface.delete(syllogism);
                                }
                                dataInterface.delete(inclusionToRemove);
                                //dataInterface.getDataConnection().disconnect();
                            }
                        }
                    }
                }
                tree.updateOrientationTies();
                tree.checkFruitsRemoval();
                tree.updateFruitsMenus();
                tree.getTodController().updateListViews();
                tree.getTodController().updateSyllogismListView();
                dataInterface.disconnect();
            });
            new Thread(sleeper).start();


        };
    }

    private EventHandler<ActionEvent> toOrientationCheckMenuAction(Dynamism ascendantDynamism,
                                                                   Dynamism descendantDynamism,
                                                                   CheckMenuItem checkMenuItem,
                                                                   boolean inFccOnly) {
        return actionEvent -> {
            tree.getTodController().getNodeInterface().log("Applying changes, please wait...");
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
                // if user selects
                if (checkMenuItem.selectedProperty().get()) {
                    // Create inclusion
                    dataInterface.newInclusion(descendantDynamism, ascendantDynamism, tree.getTodController().getTod());

                    // If there's a fruit as descendant with fcc of descendantDynamism don't add a new Fruit
                    boolean thereIsAFruit = false;
                    for (Fruit fruit1 : fruit.getDescendantFruits()) {
                        if (fruit1.getFcc().getIdFcc() == descendantDynamism.getFcc().getIdFcc()) {
                            thereIsAFruit=true;
                            fruitController.tieDescendant(fruit1);
                            break;
                        }
                    }
                    // If there's not a fruit add one
                    if (!thereIsAFruit) {
                        Fruit newFruit;
                        fruitController.resetValueInScaleSlider();
                        // in subBranch
                        if (inFccOnly) {
                            newFruit = fruit.getSubBranch().addToRightTrunk(fcc);
                        } else {
                            newFruit = fruit.getBranch().addToRightTrunk(fcc);
                        }
                        fruitController.tieDescendant(newFruit);
                    }
                }
                // if user deselects
                else if (!checkMenuItem.selectedProperty().get()){
                    Inclusion inclusionToRemove = null;
                    for (Inclusion inclusion : dataInterface.getListInclusions()) {
                        if (inclusion.getTod().equals(tod)) {
                            if (inclusion.getParticular().getIdDynamism()==descendantDynamism.getIdDynamism()) {
                                if (inclusion.getGeneral().getIdDynamism()==ascendantDynamism.getIdDynamism()) {
                                    inclusionToRemove=inclusion;
                                }
                            }
                        }
                    }
                    // if there are syllogisms with the inclusion ask if user wants to remove them, otherwise cancel
                    ObservableList<Syllogism> syllogismsWithInclusionToRemove = FXCollections.observableArrayList();
                    for (InclusionHasSyllogism inclusionHasSyllogism : dataInterface.getListInclusionHasSyllogisms()) {
                        if (inclusionHasSyllogism.getInclusion().equals(inclusionToRemove)) {
                            syllogismsWithInclusionToRemove.add(inclusionHasSyllogism.getSyllogism());
                        }
                    }

                    // check if by removing the inclusion we also remove the fruit, if so then check if there are
                    // child syllogisms that would be removed
                    if (dataInterface.getInclusions(fruit.getTree().getTodController().getTod(), inclusionToRemove.getParticular().getFcc(), inclusionToRemove.getGeneral().getFcc()).size() == 1) {
                        for (Syllogism syllogism : dataInterface.getListSyllogisms(tod)) {
                            ObservableList<Inclusion> listAllChildInclusions = FXCollections.observableArrayList();
                            // If this.fruit is the child in the inclusion, then getChildInclusions, otherwise getChildDescendantInclusions.
                            for (Fruit fruit1 : fruit.getDescendantChildFruits()) {
                                if (fruit1.getFcc().equals(inclusionToRemove.getParticular().getFcc())) {
                                    if (fruit.getChildDescendantInclusions(listAllChildInclusions).containsAll(dataInterface.getInclusions(syllogism))) {
                                        if (!syllogismsWithInclusionToRemove.contains(syllogism)) {
                                            syllogismsWithInclusionToRemove.add(syllogism);
                                        }
                                    }
                                } else {
                                    if (fruit.getChildInclusions(listAllChildInclusions).containsAll(dataInterface.getInclusions(syllogism))) {
                                        if (!syllogismsWithInclusionToRemove.contains(syllogism)) {
                                            syllogismsWithInclusionToRemove.add(syllogism);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (syllogismsWithInclusionToRemove.isEmpty()) {
                        dataInterface.delete(inclusionToRemove);
                    } else {
                        // ask
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        alert.setResizable(true);
                        alert.setTitle("Confirm deletion of dependent syllogisms");
                        alert.setHeaderText("If you continue the following syllogisms will also be removed, do you want to continue?");
                        String contentText="";
                        for (Syllogism syllogism : syllogismsWithInclusionToRemove) {
                            contentText=contentText+syllogism.getLabel() + "\n";
                        }
                        alert.setContentText(contentText);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent()) {
                            if (result.get() == ButtonType.OK) {
                                // delete the syllogisms and all dependencies
                                //dataInterface.getDataConnection().connect();
                                for (Syllogism syllogism : syllogismsWithInclusionToRemove) {
                                    dataInterface.delete(syllogism);
                                }
                                dataInterface.delete(inclusionToRemove);
                            }
                        }
                    }
                }
                tree.updateOrientationTies();
                tree.checkFruitsRemoval();
                tree.updateFruitsMenus();
                tree.getTodController().updateListViews();
                tree.getTodController().updateSyllogismListView();
                dataInterface.disconnect();
            });
            new Thread(sleeper).start();

        };
    }
}
