package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import static extras.tod.FccContainer.FccType.NORMAL;

public class MultiContainer extends HBox {
    private static AppController appController;
    private static TodController todController;
    private final Fcc centralFcc;

    private FccContainer fccContainer;

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

        positionRight = new VBox();
        positionRight.setAlignment(Pos.CENTER_LEFT);

        positionTop = new VBox();
        positionCenter = new VBox();
        positionBottom=  new VBox();

        setStyle();
        manageEvents();
    }

    public static void setControllers(AppController appController, TodController todController) {
        MultiContainer.appController = appController;
        MultiContainer.todController = todController;
    }

    void setStyle(){
        this.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        this.setSpacing(30);
        //this.setPadding(new Insets(0,20,0,20));
        //this.setMargin(positionFccContainer,new Insets(0,20,0,20));
        //this.setAlignment(Pos.BOTTOM_RIGHT);
    }

    void manageEvents() {
    }

    void deploy(){
        this.fccContainer = new FccContainer(this.centralFcc);

        if(todController.isInTod(centralFcc)){
            fccContainer.setType(FccContainer.FccType.MIRROR);
        } else if(!todController.isInTod(centralFcc)){
            fccContainer.setType(NORMAL);
        }

        //TodController.getListFccContainers().add(this.fccContainer);

        this.positionFccContainer.getChildren().add(this.fccContainer);
        this.positionRight.getChildren().addAll(this.positionTop,this.positionCenter,this.positionBottom);

        this.getChildren().addAll(this.positionLeft,this.positionFccContainer,this.positionRight);
        this.fccContainer.deploy();
    }

    public void deployAntecedents() {
        LevelContainer antecedentLevel = new LevelContainer(
                appController.getListAnalogyForInclusion(this.fccContainer.getFcc()), LevelContainer.LevelType.INCLUSION);

        getPositionLeft().getChildren().add(antecedentLevel);

        antecedentLevel.deploy();
        setAntecedentDeployed(true);
    }

    @Override
    public String toString(){
        return "[\"" + fccContainer.getFcc().toString() + "\"" + " multiContainer]";
    }

    @Override
    public void toBack() {
        super.toBack();
    }

    @Override
    public void toFront() {
        super.toFront();
        new Thread(todController.getTaskPositionMultiContainers(todController.getLevelContainerOf(fccContainer))).start();
    }

    public FccContainer getFccContainer(){
        return fccContainer;
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
