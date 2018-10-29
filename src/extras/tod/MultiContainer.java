package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static extras.tod.FccContainer.FccType.NORMAL;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MultiContainer extends HBox {
    private static AppController appController;
    private static TodController todController;
    private final Fcc centralFcc;

    //private FccContainer fccContainer;

    private boolean antecedentDeployed, positiveDeductionDeployed, negativeDeductionDeployed, symmetricDeductionDeployed;

    private VBox positionLeft, positionFccContainer, positionRight, positionTop, positionCenter, positionBottom;

    /** This is the central container.
     * It has 3 columns. 1st is for all the
     * previous dynamisms. 2dn is for
     * the central Fcc, 3d is for all other Fccs that are developments
     * of the orientations' dynamisms
     * However there are 5 positions, each holding not Fcc but ClassContainer
     * - 1st is the 1st column which is a VBox
     * - 2nd is the 2nd column which is also a VBox
     * - 3d is first position of VBox occupying the 3d column
     * - 4th is center
     * - 5th is bottom
     *
     * @param fcc
    */
    public MultiContainer(Fcc fcc) {
        this.centralFcc = fcc;

        positionLeft = new VBox();
        positionLeft.setAlignment(Pos.CENTER_RIGHT);
        
        positionFccContainer = new VBox();
        positionFccContainer.setAlignment(Pos.CENTER);
        positionFccContainer.setPickOnBounds(false);
        positionFccContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(positionFccContainer.toString() + " is " + positionFccContainer.isPickOnBounds());
            }
        });
        
        positionRight = new VBox();
        positionRight.setAlignment(Pos.CENTER_LEFT);
        
        positionTop = new VBox();
        
        positionCenter = new VBox();
        
        positionBottom=  new VBox();
        setStyle();
        
    }

    public static void setControllers(AppController appController, TodController todController) {
        MultiContainer.appController = appController;
        MultiContainer.todController = todController;
    }

    void setStyle(){
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, new CornerRadii(10), new BorderWidths(2))));
        this.setSpacing(30);
        //setStyle("-fx-background-color:transparent;");
        //setBackground(Background.EMPTY);

        this.setPickOnBounds(false);

        //System.out.println(pickOnBoundsProperty());
        //this.setPadding(new Insets(0,20,0,20));
        //this.setMargin(positionFccContainer,new Insets(0,20,0,20));
        //this.setAlignment(Pos.BOTTOM_RIGHT);
    }


    void deploy(){
        FccContainer fccContainer = new FccContainer(this.centralFcc);
        
        this.positionFccContainer.getChildren().add(fccContainer);
        this.positionRight.getChildren().addAll(this.positionTop,this.positionCenter,this.positionBottom);

        this.getChildren().addAll(this.positionLeft,this.positionFccContainer,this.positionRight);
        
        // setType before deploy!
        fccContainer.setType();
        
        fccContainer.deploy();
    }

    public void deployAntecedents() {
        LevelContainer antecedentLevel = new LevelContainer(
                appController.getListAnalogyForInclusion(getFccContainer().getFcc()), LevelContainer.LevelType.INCLUSION);

        getPositionLeft().getChildren().add(antecedentLevel);

        antecedentLevel.deploy();
        setAntecedentDeployed(true);
    }

    @Override
    public String toString(){
        return "[\"" + getFccContainer().getFcc().toString() + "\"" + " multiContainer]";
    }

    @Override
    public void toBack() {
        super.toBack();
    }

    @Override
    public void toFront() {
        super.toFront();
        //new Thread(todController.getTaskPositionMultiContainers(todController.getLevelContainerOf(getFccContainer()))).start();
    }

    public FccContainer getFccContainer(){
        return (FccContainer)positionFccContainer.getChildren().get(0);
    }

    public VBox getPositionLeft() {
        return positionLeft;
    }

    public void setPositionLeft(VBox positionLeft) {
        this.positionLeft = positionLeft;
    }

    public VBox getPositionTop() {
        return positionTop;
    }

    public void setPositionTop(VBox positionTop) {
        this.positionTop = positionTop;
    }

    public VBox getPositionCenter() {
        return positionCenter;
    }

    public void setPositionCenter(VBox positionCenter) {
        this.positionCenter = positionCenter;
    }

    public VBox getPositionBottom() {
        return positionBottom;
    }

    public void setPositionBottom(VBox positionBottom) {
        this.positionBottom = positionBottom;
    }

    public AnalogyContainer getAnalogyContainerParent() {
        return (AnalogyContainer)getParent();
    }

    public LevelContainer getAntecedentsLevelContainer() {
        return (LevelContainer)getPositionLeft().getChildren().get(0);
    }


    public boolean isAntecedentDeployed(){
        return antecedentDeployed;
    }

    public boolean isPositiveDeductionsDeployed() {
        return positiveDeductionDeployed;
    }

    public boolean isNegativeDeductionsDeployed() {
        return negativeDeductionDeployed;
    }

    public boolean isSymmetricDeductionsDeployed() {
        return symmetricDeductionDeployed;
    }

    public void setAntecedentDeployed(boolean antecedentDeployed) {
        this.antecedentDeployed = antecedentDeployed;
    }

    public void setPositiveDeductionDeployed(boolean positiveDeductionDeployed) {
        this.positiveDeductionDeployed = positiveDeductionDeployed;
    }

    public void setNegativeDeductionDeployed(boolean negativeDeductionDeployed) {
        this.negativeDeductionDeployed = negativeDeductionDeployed;
    }

    public void setSymmetricDeductionDeployed(boolean symmetricDeductionDeployed) {
        this.symmetricDeductionDeployed = symmetricDeductionDeployed;
    }



}
