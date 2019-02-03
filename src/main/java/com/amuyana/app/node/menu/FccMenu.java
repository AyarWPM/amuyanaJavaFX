package com.amuyana.app.node.menu;
import com.amuyana.app.controllers.FruitController;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.Inclusion;
import com.amuyana.app.node.MainBorderPane;
import com.amuyana.app.node.tod.Fruit;
import com.amuyana.app.node.tod.Tree;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;


public class FccMenu extends Menu {
    private final DataInterface dataInterface;
    private final Tree tree;
    private final Fruit fruit;
    private final Fcc deployFcc;
    private final FruitController fruitController;

    private FccMenuType fccMenuType;

    public enum FccMenuType{FOR_ASCENDANTS,FOR_DESCENDANTS}

    /**
     * Menu that can be used in any of the two locations (bracketMenuButton or the descendantMenuButton) where
     * user orders deployments of the Table of Deductions
     * @param fruit
     * @param dynamism The dynamism of the fruit from which the other dynamisms of the descendant fruit will
     *                 be deployed, ie. the other dynamisms will be 'included' (in the sens of the logic) in this one
     * @param deployFcc The fcc that descends from ascendantDynamism
     * @param fccMenuType The type of menu depends on its location: the bracket at left hand side of ImplicationExps
     */
    public FccMenu(FruitController fruitController, Fruit fruit, Dynamism dynamism, Fcc deployFcc, FccMenuType fccMenuType) {
        super(deployFcc.toString());
        this.fruitController = fruitController;
        this.fruit = fruit;
        this.deployFcc = deployFcc;
        this.tree = fruit.getTree();
        this.fccMenuType = fccMenuType;
        this.dataInterface = MainBorderPane.getDataInterface();

        if (fccMenuType.equals(FccMenuType.FOR_DESCENDANTS)) {
            buildForDescendant(dynamism, deployFcc);
        } else if (fccMenuType.equals(FccMenuType.FOR_ASCENDANTS)) {
            buildForAscendant();
        }

    }

    private void buildForAscendant() {

    }

    private void buildForDescendant(Dynamism ascendantDynamism, Fcc descendantFcc) {
        Dynamism positiveDescendant = dataInterface.getDynamism(descendantFcc,0);
        Dynamism negativeDescendant = dataInterface.getDynamism(descendantFcc,1);
        Dynamism symmetricDescendant = dataInterface.getDynamism(descendantFcc,2);

        CheckMenuItem toPositiveCheckMenuItem = new CheckMenuItem();
        CheckMenuItem toNegativeCheckMenuItem = new CheckMenuItem();
        CheckMenuItem toSymmetricCheckMenuItem = new CheckMenuItem();
        toPositiveCheckMenuItem.textProperty().bind(positiveDescendant.propositionProperty());
        toNegativeCheckMenuItem.textProperty().bind(negativeDescendant.propositionProperty());
        toSymmetricCheckMenuItem.textProperty().bind(symmetricDescendant.propositionProperty());

        if (dataInterface.isInclusion(positiveDescendant,ascendantDynamism)) {
            toPositiveCheckMenuItem.setSelected(true);
        }

        if (dataInterface.isInclusion(negativeDescendant,ascendantDynamism)) {
            toNegativeCheckMenuItem.setSelected(true);
        }

        if (dataInterface.isInclusion(symmetricDescendant,ascendantDynamism)) {
            toSymmetricCheckMenuItem.setSelected(true);
        }

        toPositiveCheckMenuItem.setOnAction(toOrientationCheckMenuAction(ascendantDynamism,
                dataInterface.getDynamism(descendantFcc,0),
                toPositiveCheckMenuItem));
        toNegativeCheckMenuItem.setOnAction(toOrientationCheckMenuAction(ascendantDynamism,
                dataInterface.getDynamism(descendantFcc,1),
                toNegativeCheckMenuItem));
        toSymmetricCheckMenuItem.setOnAction(toOrientationCheckMenuAction(ascendantDynamism,
                dataInterface.getDynamism(descendantFcc,2),
                toSymmetricCheckMenuItem));

        getItems().setAll(toPositiveCheckMenuItem,toNegativeCheckMenuItem,toSymmetricCheckMenuItem);
    }

    private EventHandler<ActionEvent> toOrientationCheckMenuAction(Dynamism ascendantDynamism,
                                                                   Dynamism descendantDynamism,
                                                                   CheckMenuItem checkMenuItem) {
        return actionEvent -> {
            // if user selects
            if (checkMenuItem.selectedProperty().get()) {
                // If there's a fruit just add Inclusion and Tie will know what to do when Inclusions updates
                boolean thereIsFruit = false;
                for (Fruit fruit : tree.getObservableFruits()) {
                    if (fruit.getFcc().getIdFcc() == descendantDynamism.getFcc().getIdFcc()) {
                        thereIsFruit=true;
                        dataInterface.newInclusion(descendantDynamism, ascendantDynamism);
                    }
                }
                // If there's not a fruit add one, again don't check if there are branches or not (yet)
                if (!thereIsFruit) {
                    fruitController.resetValueInScaleSlider();
                    Fruit newFruit = fruit.getSubBranch().addToRightTrunk(deployFcc);
                    fruitController.tie(newFruit);
                    dataInterface.newInclusion(descendantDynamism, ascendantDynamism);
                    fruitController.buildMenus();
                }
            }
            // if user deselects
            else if (!checkMenuItem.selectedProperty().get()){
                Inclusion inclusionToRemove = null;

                for (Inclusion inclusion : dataInterface.getListInclusions()) {
                    if (inclusion.getParticular().getIdDynamism()==descendantDynamism.getIdDynamism()) {
                        if (inclusion.getGeneral().getIdDynamism()==ascendantDynamism.getIdDynamism()) {
                            inclusionToRemove=inclusion;
                        }
                    }
                }
                dataInterface.delete(inclusionToRemove);
            }
        };
    }

}
