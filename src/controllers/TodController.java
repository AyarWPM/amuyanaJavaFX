package controllers;

import data.Dynamism;
import data.Fcc;
import extras.tod.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import static extras.tod.FccContainer.FccType.MIRROR;
import static extras.tod.FccContainer.FccType.NORMAL;

public class TodController implements Initializable {

    private AppController appController;
    private TodContainer todContainer;
    
    @FXML private HBox todContent;
    @FXML private ComboBox<Fcc> cobxFcc;

    //private static ArrayList<FccContainer> listFccContainers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //listFccContainers = new ArrayList<>();
        manageEvents();
    }

    public void fillData() {
        cobxFcc.setItems(appController.getListFcc());
        //cobxFcc.getSelectionModel().selectFirst();
    }
    
    public void manageEvents(){
        cobxFcc.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Fcc>() {
            @Override
            public void changed(ObservableValue<? extends Fcc> observable, Fcc oldValue, Fcc newValue) {
                if(newValue!=null){

                    //ObservableList<FccContainer> emptyList = FXCollections.observableArrayList();
                    //getTodContainer().getFccContainers(emptyList,todContainer.getMainLevelContainer()).clear();

                    new Thread(getTaskDeployTod(newValue)).start();
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

    /*
    START OF GETTERS AND SETTERS
     */

    public boolean isInTod(Fcc fcc) {
        //boolean itIs = false;
        int i = 0;
        for(FccContainer fc : getFccContainers()){
            System.out.println(i);
            if(fc.getFcc().equals(fcc)){
                System.out.println("yep!");
                return true;
            }
            i++;
        }
        return false;
    }

    private ObservableList<FccContainer> getFccContainers() {
        ObservableList<FccContainer> listFccContainers = FXCollections.observableArrayList();
        return todContainer.getFccContainers(listFccContainers,todContainer.getMainLevelContainer());
    }

    public TodContainer getTodContainer(){
        return todContainer;
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

    /*
    END OF GETTERS AND SETTERS
     */

    /*
    START OF TODCONTAINER METHODS
     */

    private void deployTod(Fcc newValue) {
        this.todContainer = new TodContainer(newValue);
        //todContent.getChildren().clear();
        todContent.getChildren().setAll(this.todContainer);
        this.todContainer.deploy();
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

        ObservableList<FccContainer> listFccContainers = FXCollections.observableArrayList();

        for(FccContainer fc:todContainer.getFccContainers(FXCollections.observableArrayList(),todContainer.getMainLevelContainer())){
            if(fc.getFcc().equals(getFccContainerOf(mirrorMultiContainer).getFcc())){
                if(fc.getType().equals(NORMAL)){
                    normalMultiContainer=fc.getMultiContainerParent();
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
     * After all multi containers have been positioned we know the position of the first one, hence we can
     *         move analogyContainers so that the fists fccContainers are aligned. REMEMBER you're in one levelContainer
     *         although for now it is for mainLevelContainer, but later you'll have to do three iterations for the
     *         deductions side.
     *
     // frontFccContainer is the reference container. If fccContainers were aligned with a drift (like going
     // on a diagonal) it would matter, if they're just aligned on top of each other it wouldn't.
     * @param levelContainer
     */
    private void positionAnalogyContainers(LevelContainer levelContainer) {
        for(AnalogyContainer analogyContainer:levelContainer.getAnalogyContainers()){
            int index = levelContainer.getAnalogyContainers().indexOf(analogyContainer);

            if(index==0) continue;
            AnalogyContainer previousAnalogyContainer = levelContainer.getAnalogyContainers().get(index-1);

            double maxYinPrevious = previousAnalogyContainer.getBoundsInParent().getMaxY();

            analogyContainer.setLayoutY(maxYinPrevious+30);
        }
    }


    /*
    END OF TODCONTAINER METHODS
     */

    /*
    START OF LEVELCONTAINER METHODS
     */

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
        for(FccContainer existingFccContainer:getFccContainers()){
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
        tier.toBack();
    }

    /*
    END OF LEVELCONTAINER METHODS
     */

    /*
    START OF MULTICONTAINER METHODS
     */

    public void clearAntecedents(FccContainer fccContainer){
        ArrayList<AnalogyContainer> tempListAnalogyContainers = new ArrayList<>();
        //MultiContainer mainParentMultiContainer = getMultiContainer(fccContainer);

        //test if there are children in the LevelContainer of Inclusions
        //if(!parentMultiContainer.getPositionLeft().getChildren().isEmpty()){
        if(fccContainer.getMultiContainerParent().isAntecedentDeployed()){
            for(Node nodeAnalogy:fccContainer.getMultiContainerParent().getAntecedentsLevelContainer().getChildren()){
                AnalogyContainer analogyContainer = (AnalogyContainer)nodeAnalogy;
                // this is gonna be used after to remove all analogy containers
                //tempListAnalogyContainers.add(analogyContainer);

                for(Node nodeMulti:analogyContainer.getChildren()){

                    MultiContainer multiContainer = (MultiContainer)nodeMulti;
                    FccContainer subFccContainer = multiContainer.getFccContainer();

                    clearAntecedents(subFccContainer);
                    clearPositiveDeductions(subFccContainer);
                    clearNegativeDeductions(subFccContainer);
                    clearSymmetricDeductions(subFccContainer);

                    //subFccContainer.getMultiContainerParent().

                    subFccContainer.getMultiContainerParent().getPositionLeft().getChildren().clear();
                    subFccContainer.getMultiContainerParent().getPositionTop().getChildren().clear();
                    subFccContainer.getMultiContainerParent().getPositionCenter().getChildren().clear();
                    subFccContainer.getMultiContainerParent().getPositionBottom().getChildren().clear();

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
                        for(FccContainer fc:getFccContainers()){
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

                    getFccContainers().remove(subFccContainer);

                }

                fccContainer.getMultiContainerParent().getPositionLeft().getChildren().clear();
                fccContainer.getMultiContainerParent().getPositionTop().getChildren().clear();
                fccContainer.getMultiContainerParent().getPositionCenter().getChildren().clear();
                fccContainer.getMultiContainerParent().getPositionBottom().getChildren().clear();
            }
            fccContainer.getMultiContainerParent().setAntecedentDeployed(false);
        }

    }

    public void clearPositiveDeductions(FccContainer fccContainer){
        fccContainer.getMultiContainerParent().getPositionTop().getChildren().clear();
        fccContainer.getMultiContainerParent().setPositiveDeductionDeployed(false);
    }
    public void clearNegativeDeductions(FccContainer fccContainer){
        fccContainer.getMultiContainerParent().getPositionCenter().getChildren().clear();
        fccContainer.getMultiContainerParent().setNegativeDeductionDeployed(false);
    }
    public void clearSymmetricDeductions(FccContainer fccContainer){
        fccContainer.getMultiContainerParent().getPositionBottom().getChildren().clear();
        fccContainer.getMultiContainerParent().setSymmetricDeductionDeployed(false);
    }
    /*
    END OF MULTICONTAINER METHODS
     */

    /*
    START OF FCCCONTAINER METHODS
     */

    /*
    END OF FCCCONTAINER METHODS
     */

    /*
    START OF TASKS
     */
    private Task<Void> getTaskDeployTod(Fcc initialFcc){
        Task<Void> deployTod = new Task<Void>(){
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
        return deployTod;
    }

    public Task<Void> getTaskDeployDeduction() {
        Task<Void> deployDeduction = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //Thread.sleep(100);
                //Platform.runLater(()->deployNegativeDeductions());
                return null;
            }

            @Override
            protected void succeeded() {
                //new Thread().start();
                super.succeeded();
            }
        };
        return deployDeduction;
    }

    public Task<Void> getTaskDeployAntecedents(MultiContainer multiContainer){
        Task<Void> deployAntecedents = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //Platform.runLater(()-> deployAntecedents(fccContainer));
                Platform.runLater(multiContainer::deployAntecedents);
                return null;
            }

            @Override
            protected void succeeded() {
                new Thread(getTaskSetBracketAndKnobs(multiContainer.getAntecedentsLevelContainer())).start();
                super.succeeded();
            }
        };
        return deployAntecedents;
    }

    private Task<Void> getTaskSetBracketAndKnobs(LevelContainer levelContainer){
        Task<Void> setBracketAndKnobs = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //Thread.sleep(50);
                Platform.runLater(() -> {
                    for(FccContainer fc:levelContainer.getFccContainers()){
                        fc.setBracketAndKnobs();
                    }
                });
                return null;
            }

            @Override
            protected void succeeded() {
                // Only if it is not the mainLevelContainer proceed to adding tiers.
                // Else just to positionMultiContainers
                if (levelContainer.equals(todContainer.getMainLevelContainer())) {
                    new Thread(getTaskPositionMultiContainers(levelContainer)).start();
                    super.succeeded();
                } else if (!levelContainer.equals(todContainer.getMainLevelContainer())) {
                    new Thread(getTaskSetTiers(levelContainer)).start();
                    super.succeeded();
                }

                super.succeeded();
            }
        };
        return setBracketAndKnobs;
    }

    private Task<Void> getTaskSetTiers(LevelContainer levelContainer) {
        Task<Void> setTiers = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(()->{
                    for (FccContainer fc : levelContainer.getFccContainers()) {
                        tie(fc);
                    }
                });
                return null;
            }

            @Override
            protected void succeeded() {
                new Thread(getTaskPositionMultiContainers(levelContainer)).start();
                super.succeeded();
            }
        };

        return setTiers;
    }

    public Task<Void> getTaskPositionMultiContainers(LevelContainer levelContainer){
        Task<Void> positionMultiContainers = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                Thread.sleep(100);
                // Platform.runLater(()->positionMultiContainers(levelContainer));
                Platform.runLater(levelContainer::positionMultiContainers);
                return null;
            }

            @Override
            protected void succeeded() {
                new Thread(getTaskPositionAnalogyContainers(levelContainer)).start();
                super.succeeded();
            }
        };

        return positionMultiContainers;
    }

    public Task<Void> getTaskPositionAnalogyContainers(LevelContainer levelContainer){
        Task<Void> positionAnalogyContainers = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                Thread.sleep(200);
                Platform.runLater(levelContainer::positionAnalogyContainers);
                return null;
            }

            @Override
            protected void succeeded() {
                if (levelContainer.equals((todContainer.getMainLevelContainer()))) {
                    new Thread(getTaskSetKnobsPositions()).start();
                } else if (!levelContainer.equals((todContainer.getMainLevelContainer()))) {
                    //if it is not mainLevelContainer we know it is inside a multi, and that one inside an analogy
                    // and that last inside another level
                    new Thread(getTaskPositionMultiContainers(
                        levelContainer.getMultiContainerParent().getAnalogyContainerParent().getLevelContainerParent())
                    ).start();
                }
                super.succeeded();
            }
        };
        return positionAnalogyContainers;
    }

    private Task<Void> getTaskSetKnobsPositions() {
        Task<Void> setKnobsPositions = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                Thread.sleep(300);
                Platform.runLater(()->{
                    for(FccContainer fc:getFccContainers()){
                        fc.setKnobsPositions();
                    }
                });
                return null;
            }

            @Override
            protected void succeeded() {
                if(cobxFcc.isDisable())
                    cobxFcc.setDisable(false);
                super.succeeded();
            }
        };

        return setKnobsPositions;
    }

    private Task<Void> getTaskSetBorderAnalogy(LevelContainer levelContainer) {
        Task<Void> setBorderAnalogy = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {

                Platform.runLater(()->{
                    setBorderAnalogy(levelContainer);
                });
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
            }
        };

        return setBorderAnalogy;
    }

    private void setBorderAnalogy(LevelContainer levelContainer) {
        ArrayList<AnalogyContainer> listAnalogyContainers = new ArrayList<>();

        String style = "-fx-stroke:blue;-fx-stroke-type:outside;-fx-stroke-width:2;";

        for(Node analogyNode:levelContainer.getChildren()){

            AnalogyContainer analogyContainer = (AnalogyContainer) analogyNode;

            double width = analogyContainer.prefWidth(-1);
            double height = analogyContainer.prefHeight(-1);

            Line topLine = new Line(0,0,width,0);
            Line rightLine = new Line(width,0,width,height);
            Line bottomLine = new Line(width,height,0,height);
            Line leftLine = new Line(0,height,0,0);

            topLine.setStyle(style);
            rightLine.setStyle(style);
            bottomLine.setStyle(style);
            leftLine.setStyle(style);

            analogyContainer.getChildren().addAll(topLine, rightLine, bottomLine, leftLine);
        }
    }

    private void setBorderAnalogy(AnalogyContainer analogyContainer) {

        String style = "-fx-stroke:blue;-fx-stroke-type:outside;-fx-stroke-width:3;";

        double width = analogyContainer.prefWidth(-1);
        double height = analogyContainer.prefHeight(-1);

        Line topLine = new Line(0,0,width,0);
        Line rightLine = new Line(width,0,width,height);
        Line bottomLine = new Line(width,height,0,height);
        Line leftLine = new Line(0,height,0,0);
        topLine.setStyle(style);
        rightLine.setStyle(style);
        bottomLine.setStyle(style);
        leftLine.setStyle(style);
        analogyContainer.getChildren().addAll(topLine, rightLine, bottomLine, leftLine);


    }

    /*
    END OF TASKS
     */
}