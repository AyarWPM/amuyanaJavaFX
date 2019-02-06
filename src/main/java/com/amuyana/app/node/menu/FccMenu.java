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
     * Constructor for descendants' menu
     * Menu that can be used in any of the two locations (bracketMenuButton or the descendantMenuButton) where
     * user orders deployments of the Table of Deductions
     * @param fruitController the controller of the fruit.fxml instance representing fruit
     * @param ascendantDynamism The dynamism of the fruit from which the other dynamisms of the descendant fruit will
     *                 be deployed, ie. the other dynamisms will be 'included' (in the sens of the logic) in this one
     * @param descendantFcc The fcc that descends from ascendantDynamism
     */
    public FccMenu(FruitController fruitController, Dynamism ascendantDynamism, Fcc descendantFcc) {
        super(descendantFcc.toString());
        this.fruitController = fruitController;
        this.fruit = fruitController.getFruit();
        this.deployFcc = descendantFcc;
        this.tree = fruit.getTree();
        this.fccMenuType = FccMenuType.FOR_DESCENDANTS;
        this.dataInterface = MainBorderPane.getDataInterface();
        buildForDescendant(ascendantDynamism,descendantFcc);
    }

    /**
     * Constructor for ascendants' menu
     * @param fruitController Controller
     * @param ascendantFcc The ascendant Fcc
     */
    public FccMenu(FruitController fruitController, Fcc ascendantFcc) {
        super(ascendantFcc.toString());
        this.fruitController=fruitController;
        this.fruit=fruitController.getFruit();
        this.deployFcc=ascendantFcc;
        this.tree = fruit.getTree();
        this.fccMenuType = FccMenuType.FOR_ASCENDANTS;
        this.dataInterface = MainBorderPane.getDataInterface();
        buildForAscendant();
    }

    private void buildForAscendant() {

    }

    private void buildForDescendant(Dynamism ascendantDynamism, Fcc descendantFcc) {

        // If there are no fruits method did not returned
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
                boolean condition = false;
                // If there's a fruit as descendant with fcc of descendantDynamism don't add a new Fruit
                for (Fruit fruit1 : fruit.getDescendantFruits()) {
                    // if(this.equals(fruit1)) continue;
                    if (fruit1.getFcc().getIdFcc() == descendantDynamism.getFcc().getIdFcc()) {
                        dataInterface.newInclusion(descendantDynamism, ascendantDynamism);
                        condition=true;
                    }
                }
                // If there's not a fruit add one
                if (!condition) {
                    fruitController.resetValueInScaleSlider();
                    Fruit newFruit = fruit.getSubBranch().addToRightTrunk(deployFcc);
                    fruitController.tie(newFruit);
                    dataInterface.newInclusion(descendantDynamism, ascendantDynamism);
                }
                fruitController.buildMenus();
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
