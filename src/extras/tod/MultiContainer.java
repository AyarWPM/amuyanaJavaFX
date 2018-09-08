package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class MultiContainer extends HBox {

    private static AppController appController;
    private static TodController todController;
    
    public FccContainer fccContainer;
    
    public VBox positionLeft;
    public VBox positionCenter;
    
    public VBox positionRight;
    public VBox positionTop;
    public VBox positionMiddle;
    public VBox positionBottom;
    
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

        if(TodController.getListFccsInScene().contains(fcc)){
            fccContainer.setType(FccContainer.FccType.MIRROR);
        } else if(!TodController.getListFccsInScene().contains(fcc)){

            TodController.addFccInScene(fcc);
            fccContainer.setType(FccContainer.FccType.NORMAL);
        }

        positionLeft = new VBox();
        positionCenter = new VBox();
        positionTop = new VBox();
        positionMiddle = new VBox();
        positionBottom=  new VBox();
        positionRight = new VBox();
        
        setStyle();
        manageEvents();
    }

    public static void setControllers(AppController appController, TodController todController) {
        MultiContainer.appController = appController;
        MultiContainer.todController = todController;
    }
    
    private void setStyle(){
        this.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
    }
    
    private void manageEvents() {
        
    }
    
    void deploy(){
        this.getChildren().addAll(this.positionLeft,this.positionCenter,this.positionRight);
        
        this.positionCenter.getChildren().add(this.fccContainer);

        this.fccContainer.deploy();
        
        this.positionRight.getChildren().addAll(this.positionTop,this.positionMiddle,this.positionBottom);
    }
    
    public void deployInclusions(){
        //positionLeft.getChildren().addAll(new LevelContainer(appController.getListAnalogyForInitial(fcc)));
        
    }
    
    public void deployPositiveDeductions(){
        // I add as many ZContainers as there are notions that are particular 
        // in a inclusion with respect to the POSITIVE orientation of the Fcc 
        // of the MultiContainer 
        
    }
    public void deployNegativeDeductions(){
        // I add as many ZContainers as there are notions that are particular 
        // in a inclusion with respect to the NEGATIVE orientation of the Fcc 
        // of the MultiContainer 
        
    }
    public void deploySymmetricDeductions(){
        // I add as many ZContainers as there are notions that are particular 
        // in a inclusion with respect to the SYMETTRIC orientation of the Fcc 
        // of the MultiContainer 
        
    }

    
}
