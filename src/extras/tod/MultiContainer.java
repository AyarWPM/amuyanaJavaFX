package extras.tod;

import controllers.AppController;
import data.Fcc;
import javafx.event.EventHandler;
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
    
    public Fcc fcc;
    
    public VBox positionLeft;
    public VBox positionCenter;
    
    public VBox positionRight;
    public VBox positionTop;
    public VBox positionMiddle;
    public VBox positionBottom;
    
    
    private int xMove=0;
    private int yMove=0;
    private int zMove=-10000;
    
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
    */
            
    public MultiContainer(Fcc fcc) {
        this.fcc = fcc;
        
        this.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        
        positionLeft = new VBox();
        positionCenter = new VBox();
        positionTop = new VBox();
        positionMiddle = new VBox();
        positionBottom=  new VBox();
        positionRight = new VBox();
        
        FccContainer fccContainer = new FccContainer(fcc);
        
        fccContainer.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("getLayoutBounds(): " + getLayoutBounds());
                    System.out.println("getLayoutX(): " + getLayoutX());
                    System.out.println("getLocalToParentTransform(): " + getLocalToParentTransform());
                    System.out.println("getLocalToSceneTransform(): " + getLocalToSceneTransform());
                }
            });
        
        positionCenter.getChildren().add(fccContainer);
        
        positionRight.getChildren().addAll(positionTop,positionMiddle,positionBottom);
        
        this.getChildren().addAll(positionLeft,positionCenter,positionRight);
        
        this.setTranslateX(xMove);
        this.setTranslateY(yMove);
        //this.setTranslateZ(zMove);
        
        xMove-=20;
        yMove+=25;
        //zMove+=10;
        manageEvents();
    }
    
    public static void setAppController(AppController appController) {
        MultiContainer.appController = appController;
    }
    
    
    private void manageEvents() {
        
    }
    
    public Fcc getFcc() {
        return fcc;
    }

    public void setFcc(Fcc fcc) {
        this.fcc = fcc;
    }

    public void deployInclusions(){
        positionLeft.getChildren().addAll(new LevelContainer(appController.getListAnalogyForInitial(fcc)));
        
    }
    
//    public void deployPositionCenter(){
//        // I add one FccContainer only, the one that belong to the 
//        // MultiContainer . This position should be deployed automatically...
//        
//    }
    
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
