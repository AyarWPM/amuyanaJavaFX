package com.amuyana.app.node.menu;
import com.amuyana.app.controllers.TodController;
import com.amuyana.app.data.Dynamism;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.node.NodeInterface;
import com.amuyana.app.node.tod.Fruit;
import com.amuyana.app.node.tod.Tie;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;

public class FccMenu extends Menu {
    private TodController todController; // needed?

    private Fcc fcc;
    private NodeInterface nodeInterface;
    private FccMenuType fccMenuType;

    public enum FccMenuType{FOR_ASCENDANTS,FOR_DESCENDANTS}

    /**
     * Menu that can be used in any of the two locations (bracketMenuButton or the descendantMenuButton) where
     * user orders deployments of the Table of Deductions
     * @param todController The Controller of the Tod which is the platform for this menu, needed ???
     * @param fruit The fruit where this menu is going to be
     * @param dynamism Dynamism of the FCC of the fruit in param, we need this to then create the Inclusion in Tie
     * @param fccMenuType The type of menu depends on its location: the bracket at left hand side of ImplicationExps
     *                    is FOR_ASCENDANTS and each of the buttons at the right hand side are FOR_DESCENDANTS
     */
    public FccMenu(TodController todController, Fruit fruit, Dynamism dynamism, FccMenuType fccMenuType) {
        super(dynamism.getFcc().toString());
        this.fccMenuType = fccMenuType;
        this.fcc = dynamism.getFcc();
        this.todController = todController;

        if (fccMenuType.equals(FccMenuType.FOR_ASCENDANTS)) {
            CheckMenuItem toPositiveCheckMenuItem = new CheckMenuItem("From the positive");
            CheckMenuItem toNegativeCheckMenuItem = new CheckMenuItem("From the negative");
            CheckMenuItem toSymmetricCheckMenuItem = new CheckMenuItem("From the symmetric");
        } else if (fccMenuType.equals(FccMenuType.FOR_DESCENDANTS)) {
            CheckMenuItem toPositiveCheckMenuItem = new CheckMenuItem("To the positive");
            CheckMenuItem toNegativeCheckMenuItem = new CheckMenuItem("To the negative");
            CheckMenuItem toSymmetricCheckMenuItem = new CheckMenuItem("To the symmetric");

            //toPositiveCheckMenuItem.selectedProperty().bindBidirectional(fruit.getTree().ti);
// todo
            getItems().setAll(toPositiveCheckMenuItem,toNegativeCheckMenuItem,toSymmetricCheckMenuItem);
        }
    }

    public Fcc getFcc() {
        return this.fcc;
    }

}
