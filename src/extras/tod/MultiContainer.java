package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

//import static extras.tod.FccContainer.FccType.NORMAL;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MultiContainer extends HBox {
    private static AppController appController;
    private static TodController todController;
    private final Fcc centralFcc;

    //private FccContainer fccContainer;

    private boolean antecedentDeployed, descendantDeployed;

    private VBox positionAntecedents, positionFccContainer, positionDescendants;

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

        positionAntecedents = new VBox();
        positionAntecedents.setAlignment(Pos.CENTER_RIGHT);
        
        positionFccContainer = new VBox();
        positionFccContainer.setAlignment(Pos.CENTER);
        positionFccContainer.setPickOnBounds(false);
        positionFccContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });
        
        positionDescendants = new VBox();
        positionDescendants.setAlignment(Pos.CENTER_LEFT);
        setStyle();
        
    }

    public static void setControllers(AppController appController, TodController todController) {
        MultiContainer.appController = appController;
        MultiContainer.todController = todController;
    }

    void setStyle(){
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, new CornerRadii(10), new BorderWidths(2))));
        this.setSpacing(40);
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

        this.getChildren().addAll(this.positionAntecedents,this.positionFccContainer,this.positionDescendants);

        fccContainer.deploy();
    }

    public void deployAntecedents() {
        LevelContainer antecedentLevel = new LevelContainer(
                appController.getListAnalogyForAntecedent(getFccContainer().getFcc()), LevelContainer.LevelType.ANTECEDENT);

        antecedentLevel.deploy();

        getPositionAntecedents().getChildren().add(antecedentLevel);
        setAntecedentDeployed(true);
        
        todController.startExecutor();
        todController.getExecutorService().execute(todController.getTaskSetBracketAndKnobs(antecedentLevel));
        todController.getExecutorService().execute(todController.getTaskSetTiers(antecedentLevel));
        
        boolean key=true;
        while(key){
            todController.getExecutorService().execute(todController.getTaskPositionMultiContainers(antecedentLevel));
            todController.getExecutorService().execute(todController.getTaskPositionAnalogyContainers(antecedentLevel));
            if(antecedentLevel==todController.getTodContainer().getMainLevelContainer()){
                key=false;
            } else if (antecedentLevel!=todController.getTodContainer().getMainLevelContainer()){
                antecedentLevel=antecedentLevel.getMultiContainerParent().getAnalogyContainerParent().getLevelContainerParent();
            }
        }
        
        todController.getExecutorService().execute(todController.getTaskSetKnobsPositions());
        todController.getExecutorService().execute(todController.getTaskSetBorderAnalogy());
        
        todController.endExecutor();
    }

    public void deployDescendants(){
        LevelContainer descendantLevel = new LevelContainer(
                appController.getListAnalogyForDescendant(getFccContainer().getFcc()), LevelContainer.LevelType.ANTECEDENT);

        descendantLevel.deploy();

        getPositionDescendants().getChildren().add(descendantLevel);
        setDescendantDeployed(true);
        
        todController.startExecutor();
        todController.getExecutorService().execute(todController.getTaskSetBracketAndKnobs(descendantLevel));
        todController.getExecutorService().execute(todController.getTaskSetTiers(descendantLevel));
        
        boolean key=true;
        while(key){
            todController.getExecutorService().execute(todController.getTaskPositionMultiContainers(descendantLevel));
            todController.getExecutorService().execute(todController.getTaskPositionAnalogyContainers(descendantLevel));
            if(descendantLevel==todController.getTodContainer().getMainLevelContainer()){
                key=false;
            } else if (descendantLevel!=todController.getTodContainer().getMainLevelContainer()){
                descendantLevel=descendantLevel.getMultiContainerParent().getAnalogyContainerParent().getLevelContainerParent();
            }
        }
        
        todController.getExecutorService().execute(todController.getTaskSetKnobsPositions());
        todController.getExecutorService().execute(todController.getTaskSetBorderAnalogy());
        
        todController.endExecutor();
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

    public VBox getPositionAntecedents() {
        return positionAntecedents;
    }

    public VBox getPositionDescendants() {
        return positionDescendants;
    }

    public void setPositionAntecedents(VBox positionAntecedents) {
        this.positionAntecedents = positionAntecedents;
    }

    public AnalogyContainer getAnalogyContainerParent() {
        return (AnalogyContainer)getParent();
    }

    public LevelContainer getAntecedentsLevelContainer() {
        return (LevelContainer) positionAntecedents.getChildren().get(0);
    }

    public LevelContainer getDescendantsLevelContainers() {
        return (LevelContainer)positionDescendants.getChildren().get(0);
    }

    public boolean isAntecedentDeployed(){
        return antecedentDeployed;
    }

    public boolean isDescendantDeployed() {
        return descendantDeployed;
    }

    public void setAntecedentDeployed(boolean antecedentDeployed) {
        this.antecedentDeployed = antecedentDeployed;
    }

    public void setDescendantDeployed(boolean descendantDeployed) {
        this.descendantDeployed=descendantDeployed;
    }

}
