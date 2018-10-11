package controllers;

import com.sun.org.apache.xpath.internal.operations.Mult;
import data.Dynamism;
import data.Fcc;
import extras.tod.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static extras.tod.FccContainer.FccType.MIRROR;
import static extras.tod.FccContainer.FccType.NORMAL;

public class TodController implements Initializable {

    private AppController appController;
    private TodContainer todContainer;
    
    @FXML private HBox todContent;
    @FXML private ComboBox<Fcc> cobxFcc;

    private static ArrayList<FccContainer> listFccContainers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listFccContainers = new ArrayList<>();
        manageEvents();
    }

    public void fillData() {
        cobxFcc.setItems(appController.getListFcc());
        //cobxFcc.getSelectionModel().selectFirst();
    }

    private Task<Void> getTaskDeployTodContainer(Fcc initialFcc){
        Task<Void> deployTodContainer = new Task<Void>(){
            @Override
            protected Void call() {

                Platform.runLater(()-> deployTod(initialFcc));
                return null;
            }

            @Override
            protected void succeeded() {
                // Because this is the first deployment, all fccContainers are new
                // Or we could use the mainLevelContainer
                //new Thread(getTaskSetBracketAndKnobs(getListFccContainers())).start();
                new Thread(getTaskSetBracketAndKnobs(getTodContainer().getMainLevelContainer())).start();
                super.succeeded();
            }
        };
        return deployTodContainer;
    }

    //private Task<Void> getTaskSetBracketAndKnobs(ArrayList<FccContainer> fccContainers){
    private Task<Void> getTaskSetBracketAndKnobs(LevelContainer levelContainer){
        Task<Void> setBracketAndKnobs = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(50);
                Platform.runLater(() -> {
                    for(FccContainer fc:getFccContainersOf(levelContainer)){
                        fc.setBracketAndKnobs();
                    }
                });
                return null;
            }

            @Override
            protected void succeeded() {
                // Every time we must reposition all multiContainers...
                new Thread(getTaskPositionMultiContainers()).start();
                super.succeeded();
            }
        };
        return setBracketAndKnobs;
    }

    private Task<Void> getTaskPositionMultiContainers(){
        Task<Void> positionMultiContainers = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                Thread.sleep(100);
                Platform.runLater(()->positionMultiContainers());
                return null;
            }

            @Override
            protected void succeeded() {
                if(cobxFcc.isDisable())
                    cobxFcc.setDisable(false);
                super.succeeded();
            }
        };

        return positionMultiContainers;
    }


    public void manageEvents(){
        cobxFcc.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Fcc>() {
            @Override
            public void changed(ObservableValue<? extends Fcc> observable, Fcc oldValue, Fcc newValue) {
                if(newValue!=null){
                    listFccContainers.clear();
                    new Thread(getTaskDeployTodContainer(newValue)).start();
                    cobxFcc.setDisable(true);
                }
            }
        });
    }

    public void setAppController(AppController aThis) {
        this.appController=aThis;
        setControllers(this);
    }

    private void setControllers(TodController todController) {
        TodContainer.setControllers(this.appController, todController);
        LevelContainer.setControllers(this.appController, todController);
        AnalogyContainer.setControllers(this.appController, todController);
        MultiContainer.setControllers(this.appController, todController);
        FccContainer.setControllers(this.appController, todController);
        FormulaContainer.setControllers(this.appController, todController);
        Tier.setControllers(this.appController,todController);
    }

    public static boolean isInTod(Fcc fcc) {
        //boolean itIs = false;
        for(FccContainer fc : listFccContainers){
            if(fc.getFcc().equals(fcc)){
                return true;
            }
        }
        return false;
    }

    public static ArrayList<FccContainer> getListFccContainers() {
        return listFccContainers;
    }

    public TodContainer getTodContainer(){
        return todContainer;
    }

    public MultiContainer getMultiContainer(FccContainer fccContainer){
        return (MultiContainer)fccContainer.getParent().getParent();
    }

    public FccContainer getFccContainerOf(MultiContainer multiContainer){
        VBox vBox = (VBox)multiContainer.getChildren().get(1);
        return (FccContainer)vBox.getChildren().get(0);
    }

    public AnalogyContainer getAnalogyContainerOf(MultiContainer multiContainer){
        return (AnalogyContainer)multiContainer.getParent();
    }

    public LevelContainer getLevelContainerOf(FccContainer fccContainer) {
        return (LevelContainer)fccContainer.getParent().getParent().getParent().getParent();
    }

    public ArrayList<FccContainer> getFccContainersOf(LevelContainer levelContainer) {
        ArrayList<FccContainer> listFccContainers = new ArrayList<>();

        for (Node analogyNode : levelContainer.getChildren()) {
            AnalogyContainer analogyContainer = (AnalogyContainer)analogyNode;
            for (Node multiNode : analogyContainer.getChildren()) {
                MultiContainer multiContainer = (MultiContainer)multiNode;
                listFccContainers.add(multiContainer.getFccContainer());
            }
        }

        return listFccContainers;
    }

    private void deployTod(Fcc newValue) {
        this.todContainer = new TodContainer(newValue);
        todContent.getChildren().clear();
        todContent.getChildren().add(this.todContainer);

        this.todContainer.deploy();
    }

    @FXML
    public void debug(){

    }

    /**
     * This is the method that switches the mirror fccContainer with the normal fccContainer as required by the user
     * @param mirrorMultiContainer The multiContainer that has the mirror fccContainer
     */
    public void switchMultiContainers(MultiContainer mirrorMultiContainer) {
        AnalogyContainer mirrorAnalogyContainer = getAnalogyContainerOf(mirrorMultiContainer);
        int mirrorIndex = mirrorAnalogyContainer.getChildren().indexOf(mirrorMultiContainer);

        MultiContainer normalMultiContainer=null;
        int normalIndex=0;

        for(FccContainer fc:listFccContainers){
            if(fc.getFcc().equals(getFccContainerOf(mirrorMultiContainer).getFcc())){
                if(fc.getType().equals(NORMAL)){

                    normalMultiContainer=getMultiContainer(fc);
                    normalIndex = getAnalogyContainerOf(normalMultiContainer).getChildren().indexOf(normalMultiContainer);
                }
            }
        }

        AnalogyContainer normalAnalogyContainer = getAnalogyContainerOf(normalMultiContainer);

        mirrorAnalogyContainer.getChildren().remove(mirrorMultiContainer);
        normalAnalogyContainer.getChildren().remove(normalMultiContainer);

        mirrorAnalogyContainer.getChildren().add(mirrorIndex,normalMultiContainer);
        normalAnalogyContainer.getChildren().add(normalIndex,mirrorMultiContainer);
    }

    /**
     * This method creates a Tier for each pair of Knobs, one in each Fcc's that have a relation in the
     * sense of the Inclusion operation.
     *
     */
    public void tie(FccContainer newFccContainer) {
        Dynamism newPositiveDynamism = appController.dynamismOf(0,newFccContainer.getFcc());
        Dynamism newNegativeDynamism = appController.dynamismOf(1,newFccContainer.getFcc());
        Dynamism newSymmetricDynamism = appController.dynamismOf(2,newFccContainer.getFcc());

        /*
         * NewFccContainer as Inclusion of any existingDynamism
        */
        for(FccContainer existingFccContainer:listFccContainers){
            Fcc existingFcc = existingFccContainer.getFcc();

            // check if its positive, negative or symmetric orientation
            if(appController.isDeduction(existingFcc, newPositiveDynamism)){
                // If the newFccContainer is a MIRROR or a NORMAL we care about knobs 4 or 1,2,3 respectively
                // For the existingFccContainer we care only about knob 0
                if(newFccContainer.getType()== NORMAL){
                    // TODO if there exists already a Tier linking these two don't add it...
                    tie(newFccContainer,1,existingFccContainer);
                } else if(newFccContainer.getType()== MIRROR){
                    tie(newFccContainer,4,existingFccContainer);
                }
            }
            if(appController.isDeduction(existingFcc,newNegativeDynamism)){
                if(newFccContainer.getType()== NORMAL){
                    tie(newFccContainer,2,existingFccContainer);
                } else if(newFccContainer.getType()== MIRROR){
                    tie(newFccContainer,4,existingFccContainer);
                }
            }
            if(appController.isDeduction(existingFcc,newSymmetricDynamism)){
                if(newFccContainer.getType()== NORMAL){
                    tie(newFccContainer,3,existingFccContainer);
                } else if(newFccContainer.getType()== MIRROR){
                    tie(newFccContainer,4,existingFccContainer);
                }
            }

            /*
             * NewFccContainer as positive Deduction of any existingDynamism
             * */
            Fcc newFcc = newFccContainer.getFcc();

            Dynamism existingPositiveDynamism = appController.dynamismOf(0,existingFcc);
            Dynamism existingNegativeDynamism = appController.dynamismOf(1,existingFcc);
            Dynamism existingSymmetricDynamism = appController.dynamismOf(2,existingFcc);

            if(appController.isDeduction(newFcc,existingPositiveDynamism)){
                if(existingFccContainer.getType()== NORMAL){
                    tie(existingFccContainer,1,newFccContainer);
                } else if(existingFccContainer.getType()==MIRROR){
                    tie(existingFccContainer,4,newFccContainer);
                }
            }

            /*
             * NewFccContainer as negative Deduction of any existingDynamism
             * */
            if(appController.isDeduction(newFcc,existingNegativeDynamism)){
                if(existingFccContainer.getType()== NORMAL){
                    tie(existingFccContainer,2,newFccContainer);
                } else if(existingFccContainer.getType()==MIRROR){
                    tie(existingFccContainer,4,newFccContainer);
                }
            }

            /*
             * NewFccContainer as symmetric Deduction of any existingDynamism
             * */
            if(appController.isDeduction(newFcc,existingSymmetricDynamism)){
                if(existingFccContainer.getType()== NORMAL){
                    tie(existingFccContainer,3,newFccContainer);
                } else if(existingFccContainer.getType()==MIRROR){
                    tie(existingFccContainer,4,newFccContainer);
                }
            }

        }
        // is it a deduction?

        // Don't forget to check that there's not another tie already...

        // Maybe implement a method in the Tier class to update ties everytime there's a new fccContainer added,
        // because that will change positions of containers
    }

    private void tie(FccContainer fccContainer1, int i, FccContainer fccContainer2){
        Tier tier = new Tier();
        switch (i){
            case(1):{
                tier.setKnobStart(fccContainer1.knob1);
                tier.startXProperty().bind(fccContainer1.knob1XProperty());
                tier.startYProperty().bind(fccContainer1.knob1YProperty());
                break;
            }
            case(2):{
                tier.setKnobStart(fccContainer1.knob2);
                tier.startXProperty().bind(fccContainer1.knob2XProperty());
                tier.startYProperty().bind(fccContainer1.knob2YProperty());
                break;
            }
            case(3):{
                tier.setKnobStart(fccContainer1.knob3);
                tier.startXProperty().bind(fccContainer1.knob3XProperty());
                tier.startYProperty().bind(fccContainer1.knob3YProperty());
                break;
            }
            case(4):{
                tier.setKnobStart(fccContainer1.knob4);
                tier.startXProperty().bind(fccContainer1.knob4XProperty());
                tier.startYProperty().bind(fccContainer1.knob4YProperty());
                break;
            }
            default: break;
        }
        tier.setKnobFinal(fccContainer2.knob0);
        tier.endXProperty().bind(fccContainer2.knob0XProperty());
        tier.endYProperty().bind(fccContainer2.knob0YProperty());
        todContainer.getChildren().add(tier);
    }

    public void setKnobsPositions(){
        for(FccContainer fc:listFccContainers){
            fc.setKnobsPositions();
        }
    }

    /* FROM HERE
    USED IN MULTICONTAINER
     */
    private void deployInclusions(FccContainer fccContainer){
        LevelContainer inclusionLevel = new LevelContainer(appController.getListAnalogyForInclusion(fccContainer.getFcc()), LevelContainer.LevelType.INCLUSION);
        getMultiContainer(fccContainer).getPositionLeft().getChildren().add(inclusionLevel);
        inclusionLevel.deploy();
        fccContainer.setInclusionDeployed(true);
    }

    public Task<Void> getTaskDeployInclusions(FccContainer fccContainer){
        Task<Void> deployInclusions = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //Thread.sleep(100);
                Platform.runLater(()->deployInclusions(fccContainer));
                return null;
            }

            @Override
            protected void succeeded() {
                new Thread(getTaskSetBracketAndKnobs(getInclusionsLevel(fccContainer))).start();
                super.succeeded();
            }
        };
        return deployInclusions;
    }

    public void deployPositiveDeductions(FccContainer fccContainer){
        LevelContainer positiveDeductionsLevel =
                new LevelContainer(appController.getListAnalogyForDeduction(appController.dynamismOf(0,fccContainer.getFcc())), LevelContainer.LevelType.DEDUCTION);
        getMultiContainer(fccContainer).getPositionTop().getChildren().add(positiveDeductionsLevel);
        positiveDeductionsLevel.deploy();
        fccContainer.setPositiveDeductionDeployed(true);
    }

    public void deployNegativeDeductions(FccContainer fccContainer){
        LevelContainer negativeDeductionsLevel =
                new LevelContainer(appController.getListAnalogyForDeduction(appController.dynamismOf(1,fccContainer.getFcc())), LevelContainer.LevelType.DEDUCTION);
        getMultiContainer(fccContainer).getPositionCenter().getChildren().add(negativeDeductionsLevel);
        negativeDeductionsLevel.deploy();
        fccContainer.setNegativeDeductionDeployed(true);
    }

    public void deploySymmetricDeductions(FccContainer fccContainer){
        LevelContainer symmetricDeductionsLevel =
                new LevelContainer(appController.getListAnalogyForDeduction(appController.dynamismOf(2,fccContainer.getFcc())), LevelContainer.LevelType.DEDUCTION);
        getMultiContainer(fccContainer).getPositionBottom().getChildren().add(symmetricDeductionsLevel);
        symmetricDeductionsLevel.deploy();
        fccContainer.setSymmetricDeductionDeployed(true);
    }

    public void clearInclusions(FccContainer fccContainer){
        ArrayList<AnalogyContainer> tempListAnalogyContainers = new ArrayList<>();
        //MultiContainer mainParentMultiContainer = getMultiContainer(fccContainer);

        //test if there are children in the LevelContainer of Inclusions
        //if(!parentMultiContainer.getPositionLeft().getChildren().isEmpty()){
        if(fccContainer.isInclusionDeployed()){
            for(Node nodeAnalogy:getInclusionsLevel(fccContainer).getChildren()){
                AnalogyContainer analogyContainer = (AnalogyContainer)nodeAnalogy;
                // this is gonna be used after to remove all analogy containers
                //tempListAnalogyContainers.add(analogyContainer);

                for(Node nodeMulti:analogyContainer.getChildren()){

                    MultiContainer multiContainer = (MultiContainer)nodeMulti;
                    FccContainer subFccContainer = multiContainer.getFccContainer();

                    clearInclusions(subFccContainer);
                    clearPositiveDeductions(subFccContainer);
                    clearNegativeDeductions(subFccContainer);
                    clearSymmetricDeductions(subFccContainer);

                    getMultiContainer(subFccContainer).getPositionLeft().getChildren().clear();
                    getMultiContainer(subFccContainer).getPositionTop().getChildren().clear();
                    getMultiContainer(subFccContainer).getPositionCenter().getChildren().clear();
                    getMultiContainer(subFccContainer).getPositionBottom().getChildren().clear();

                    // take into account: removing the lines that either arrive (to knob0) or
                    // depart (from knob1, knob2, knob3 or knob4) from fccContainer of this multiContainer
                    // ! Remove from todContainer AND Tier.listTiers !
                    for(Tier t:Tier.listTiersOf(subFccContainer)){
                        getTodContainer().getChildren().remove(t);
                        Tier.getListTiers().remove(t);
                    }

                    // If fccContainer is of type NORMAL, find for another MIRROR of the same fcc and make it NORMAL,
                    // if there's no mirror just remove it...
                    if(subFccContainer.getType()==NORMAL){
                        for(FccContainer fc:getListFccContainers()){
                            if(fc.getFcc().equals(subFccContainer.getFcc())){
                                if(fc.getType()==MIRROR){
                                    fc.setType(NORMAL);
                                    fc.deploy();
                                    break;
                                }
                            }
                        }
                    }

                    // ... If fccContainer is of type MIRROR do nothing special, just remove it.

                    TodController.getListFccContainers().remove(subFccContainer);
                }

                getMultiContainer(fccContainer).getPositionLeft().getChildren().clear();
                getMultiContainer(fccContainer).getPositionTop().getChildren().clear();
                getMultiContainer(fccContainer).getPositionCenter().getChildren().clear();
                getMultiContainer(fccContainer).getPositionBottom().getChildren().clear();
            }
            fccContainer.setInclusionDeployed(false);
        }

    }

    public void clearPositiveDeductions(FccContainer fccContainer){
        getMultiContainer(fccContainer).getPositionTop().getChildren().clear();
        fccContainer.setPositiveDeductionDeployed(false);
    }
    public void clearNegativeDeductions(FccContainer fccContainer){
        getMultiContainer(fccContainer).getPositionCenter().getChildren().clear();
        fccContainer.setNegativeDeductionDeployed(false);
    }
    public void clearSymmetricDeductions(FccContainer fccContainer){
        getMultiContainer(fccContainer).getPositionBottom().getChildren().clear();
        fccContainer.setSymmetricDeductionDeployed(false);
    }

    public LevelContainer getInclusionsLevel(FccContainer fccContainer) {
        LevelContainer levelContainer =(LevelContainer)getMultiContainer(fccContainer).getPositionLeft().getChildren().get(0);
        //System.out.println(levelContainer.getChildren());
        return levelContainer;
    }

    /* TO HERE
    USED IN MULTICONTAINER
     */

    public void assembleTod(){
        // 1. Position multiContainers -- This seems to be a method to apply at the levelContainer level
        //positionMultiContainers();

        // 2. Set knobs positions -- this seems to be a method to apply globally at the TOD level
       // setKnobsPositions();

        // 3. Draw lines -- this seems to be a method to apply globally at the TOD level
    }

    private void positionMultiContainers(){
        // Which MultiContainer is reference for alignment? -> If it is mainLevelContainer it is the one in front,
        // if levelContainer is deployment of inclusion side then
        // the one that has the largest deduction-level-container side, if is deployment of deduction side then the
        // one that has the largest inclusion-level-container side.

        // To know the size of any multiContainer we must ask if inclusion or deduction levels are deployed, and
        // calculate sizes accordingly

        for (Node analogyContainerNode : todContainer.getMainLevelContainer().getChildren()) {
            AnalogyContainer analogyContainer = (AnalogyContainer)analogyContainerNode;


            //for (int i = analogyContainer.getChildren().size() - 1; i >=0 ; i--) {
            for(Node multiContainerNode:analogyContainer.getChildren()){
                int size = analogyContainer.getChildren().size();
                int i = analogyContainer.getChildren().indexOf(multiContainerNode);

                MultiContainer multiContainer = (MultiContainer)analogyContainer.getChildren().get(i);

                FccContainer fccContainer = getFccContainerOf(multiContainer);
                System.out.println(i + " :" + multiContainer + " of inclusion width: "+getInclusionsLevelWidth(fccContainer));
                multiContainer.setLayoutX(-getInclusionsLevelWidth(fccContainer)-i*30);
                if (analogyContainer.getChildren().indexOf(multiContainer) == 0) {
                    multiContainer.setLayoutY(i*30);
                } else if (analogyContainer.getChildren().indexOf(multiContainer) != 0) {
                    MultiContainer firstMulti = (MultiContainer)analogyContainer.getChildren().get(0);
                    double moveDown = getProfundityFccContainer(firstMulti.getFccContainer());
                    multiContainer.setLayoutY(i*30+moveDown);
                }

            }
        }

        AnalogyContainer firstAnalogyContainer = (AnalogyContainer)todContainer.getMainLevelContainer().getChildren().get(0);
        MultiContainer frontMultiContainer = (MultiContainer)firstAnalogyContainer.getChildren().get(firstAnalogyContainer.getChildren().size()-1);
        FccContainer frontFccContainer = frontMultiContainer.getFccContainer();

        Point2D originFirstFccContainer = todContainer.getMainLevelContainer().sceneToLocal(frontFccContainer.localToScene(0, 0));

        System.out.println(originFirstFccContainer);

    }

    private double getInclusionsLevelWidth(FccContainer fccContainer){
        double width = 0;
        if(fccContainer.isInclusionDeployed()){
            width = getInclusionsLevel(fccContainer).getWidth();
            System.out.println(width);
        }
        return width;
    }

    private double getProfundityFccContainer(FccContainer fccContainer){
        return fccContainer.getLayoutY();
    }

}