package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class MultiContainer extends HBox {

    private static AppController appController;
    private static TodController todController;
    
    private FccContainer fccContainer;

    private VBox positionLeft;
    private VBox positionCenter;

    private VBox positionRight;
    private VBox positionTop;
    private VBox positionMiddle;
    private VBox positionBottom;

    private boolean inclusionDeployed;
    private boolean positiveDeductionDeployed;
    private boolean negativeDeductionDeployed;
    private boolean symmetricDeductionDeployed;
    
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

        this.fccContainer = new FccContainer(fcc);

        //if(TodController.getListFccContainers().contains(fcc)){
        if(TodController.isInTod(fcc)){
            fccContainer.setType(FccContainer.FccType.MIRROR);
        } else if(!TodController.isInTod(fcc)){
            fccContainer.setType(FccContainer.FccType.NORMAL);
        }

        TodController.getListFccContainers().add(this.fccContainer);

        positionLeft = new VBox();
        positionLeft.setAlignment(Pos.CENTER_RIGHT);

        positionCenter = new VBox();
        positionCenter.setAlignment(Pos.CENTER);

        positionRight = new VBox();
        positionRight.setAlignment(Pos.CENTER_LEFT);

        positionTop = new VBox();
        positionMiddle = new VBox();
        positionBottom=  new VBox();

        setProperties();
        manageEvents();
    }

    public static void setControllers(AppController appController, TodController todController) {
        MultiContainer.appController = appController;
        MultiContainer.todController = todController;
    }

    boolean isInclusionDeployed(){
        return inclusionDeployed;
    }

    boolean isPositiveDeductionsDeployed() {
        return positiveDeductionDeployed;
    }

    boolean isNegativeDeductionsDeployed() {
        return negativeDeductionDeployed;
    }

    boolean isSymmetricDeductionsDeployed() {
        return symmetricDeductionDeployed;
    }

    void setInclusionDeployed(boolean inclusionDeployed) {
        this.inclusionDeployed = inclusionDeployed;
    }

    void setPositiveDeductionDeployed(boolean positiveDeductionDeployed) {
        this.positiveDeductionDeployed = positiveDeductionDeployed;
    }

    void setNegativeDeductionDeployed(boolean negativeDeductionDeployed) {
        this.negativeDeductionDeployed = negativeDeductionDeployed;
    }

    void setSymmetricDeductionDeployed(boolean symmetricDeductionDeployed) {
        this.symmetricDeductionDeployed = symmetricDeductionDeployed;
    }

    void setProperties(){
        this.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        this.setSpacing(30);
        //this.setAlignment(Pos.BOTTOM_RIGHT);
    }
    
    void manageEvents() {
        
    }
    
    void deploy(){
        this.getChildren().addAll(this.positionLeft,this.positionCenter,this.positionRight);
        this.positionCenter.getChildren().add(this.fccContainer);
        this.fccContainer.deploy();
        this.positionRight.getChildren().addAll(this.positionTop,this.positionMiddle,this.positionBottom);
        //this.fccContainer.scale
    }
    
    void deployInclusions(){
        LevelContainer inclusionLevel = new LevelContainer(appController.getListAnalogyForInclusion(fccContainer.getFcc()));
        this.positionLeft.getChildren().add(inclusionLevel);
        inclusionLevel.deploy();
        setInclusionDeployed(true);
    }
    
    void deployPositiveDeductions(){
        LevelContainer positiveDeductionsLevel = new LevelContainer(
                (appController.getListAnalogyForDeduction(appController.dynamismOf(0,fccContainer.getFcc())))
        );
        this.positionTop.getChildren().add(positiveDeductionsLevel);
        positiveDeductionsLevel.deploy();
        setPositiveDeductionDeployed(true);
    }

    void deployNegativeDeductions(){
        LevelContainer negativeDeductionsLevel = new LevelContainer(
                (appController.getListAnalogyForDeduction(appController.dynamismOf(1,fccContainer.getFcc())))
        );
        this.positionMiddle.getChildren().add(negativeDeductionsLevel);
        negativeDeductionsLevel.deploy();
        setNegativeDeductionDeployed(true);
    }

    void deploySymmetricDeductions(){
        LevelContainer symmetricDeductionsLevel = new LevelContainer(
                (appController.getListAnalogyForDeduction(appController.dynamismOf(2,fccContainer.getFcc())))
        );
        this.positionBottom.getChildren().add(symmetricDeductionsLevel);
        symmetricDeductionsLevel.deploy();
        setSymmetricDeductionDeployed(true);
    }

    void clearInclusions(){
        this.positionLeft.getChildren().clear();
        setInclusionDeployed(false);
    }

    void clearPositiveDeductions(){
        this.positionTop.getChildren().clear();
        setPositiveDeductionDeployed(false);
    }
    void clearNegativeDeductions(){
        this.positionMiddle.getChildren().clear();
        setNegativeDeductionDeployed(false);
    }
    void clearSymmetricDeductions(){
        this.positionBottom.getChildren().clear();
        setSymmetricDeductionDeployed(false);
    }


    @Override
    public String toString(){
        return "[\"" + fccContainer.getFcc().toString() + "\"" + " multiContainer]";
    }

    
}
