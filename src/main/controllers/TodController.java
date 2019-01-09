
package main.controllers;

import main.data.DataInterface;
import main.data.Dynamism;
import main.data.Fcc;
import main.data.tod.containers.Container0;
import main.data.tod.containers.Container2;
import main.data.tod.containers.Tod;
import main.extras.AmuyanaTab;
import main.tod.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.geometry.Bounds;

public class TodController implements Initializable {

    private AppController appController;
    private Tod tod;
    
    @FXML private HBox canvas;

    @FXML private ScrollPane leftScrollPane;
    //@FXML private ScrollPane todRightPanelScrollPane;
    //@FXML private VBox todTopPanelVBox;

    @FXML private Label nameLabel;
    @FXML private TextField nameTextField;
    @FXML private ToggleButton nameToggleButton;
    @FXML private ListView<Fcc> fccsInTodListView;

    ExecutorService executorService;

    private DataInterface dataInterface;
    private AmuyanaTab amuyanaTab;

    //private static ArrayList<FccContainer> listFccContainers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //this.tod = new Tod();
        //this.tod = new Tod(0,"name", logicSystem, container0);
        canvas.getChildren().setAll(this.tod);
        manageEvents();
    }

    @FXML
    private void nameToggle() {

    }

    public void setAppController(AppController aThis) {
        this.appController=aThis;
        setControllers(this);
    }

    private void setControllers(TodController todController) {
        Tod.setControllers(this.appController, todController);
        Container0.setControllers(this.appController, todController);
        AnalogyContainer.setControllers(this.appController, todController);
        Container2.setControllers(this.appController, todController);
        FccContainer.setControllers(this.appController, todController);
        FormulaContainer.setControllers(this.appController, todController);
        Tier.setControllers(this.appController,todController);

        //Conjunction.setControllers(this.appController,todController);
        this.dataInterface = appController.getDataInterface();
    }

    public void setTab(AmuyanaTab amuyanaTab) {
        this.amuyanaTab = amuyanaTab;
    }

    public void setLeftPanel(ScrollPane leftPanel) {
        leftScrollPane.setContent(leftPanel);
    }

    public void manageEvents(){

    }

    public void deployTod(Analogy initialAnalogy) {
        this.tod.deploy(initialAnalogy);
        Container0 mainContainer0 = tod.getContainer0();
        startExecutor();
        executorService.execute(getTaskSetBracketAndKnobs(mainContainer0));
        executorService.execute(getTaskPositionMultiContainers(mainContainer0));
        executorService.execute(getTaskPositionAnalogyContainers(mainContainer0));
        executorService.execute(getTaskSetKnobsPositions());
        endExecutor();
    }

    public void updatePositions(Container0 container0) {
        startExecutor();
        executorService.execute(getTaskPositionMultiContainers(container0));
        executorService.execute(getTaskPositionAnalogyContainers(container0));
        executorService.execute(getTaskSetKnobsPositions());
        endExecutor();
    }

    public Tod getTod(){
        return tod;
    }

    private ObservableList<FccContainer> getFccContainers() {
        ObservableList<FccContainer> listFccContainers = FXCollections.observableArrayList();
        return tod.getFccContainers(listFccContainers, tod.getContainer0());
    }
    
    public ObservableList<AnalogyContainer> getAnalogyContainers(){
        ObservableList<AnalogyContainer> listAnalogies = FXCollections.observableArrayList();
        return tod.getAnalogyContainers(listAnalogies, tod.getContainer0());
    }

    public FccContainer getFccContainerOf(Container2 container2){
        VBox vBox = (VBox) container2.getChildren().get(1);
        return (FccContainer)vBox.getChildren().get(0);
    }

    public AnalogyContainer getAnalogyContainerOf(Container2 container2){
        return (AnalogyContainer) container2.getParent();
    }

    public Container0 getLevelContainerOf(FccContainer fccContainer) {
        return (Container0)fccContainer.getParent().getParent().getParent().getParent();
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public void startExecutor(){
        executorService = Executors.newSingleThreadExecutor();
    }
    
    public void endExecutor(){
        executorService.shutdown();
    }
    
    /**
     * This is the method that switches the mirror fccContainer with the normal fccContainer as required by the user
     * @param mirrorMultiContainer The multiContainer that has the mirror fccContainer
     */
    /*
    public void switchMultiContainers(AnalogyContainer mirrorMultiContainer) {
        AnalogyContainer mirrorAnalogyContainer = getAnalogyContainerOf(mirrorMultiContainer);
        int mirrorIndex = mirrorAnalogyContainer.getChildren().indexOf(mirrorMultiContainer);

        AnalogyContainer normalMultiContainer=null;
        int normalIndex=0;

        ObservableList<FccContainer> listFccContainers = FXCollections.observableArrayList();

        for(FccContainer fc:tod.getFccContainers(FXCollections.observableArrayList(),tod.getContainer0())){
            if(fc.getInclusion().equals(getFccContainerOf(mirrorMultiContainer).getInclusion())){
                if(fc.getType().equals(NORMAL)){
                    normalMultiContainer=fc.getParentContainer3();
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
*/


    /**
     * After all multi containers have been positioned we know the position of the first one, hence we can
     *         move analogyContainers so that the fists fccContainers are aligned. REMEMBER you're in one container0
     *         although for now it is for mainLevelContainer, but later you'll have to do three iterations for the
     *         deductions side.
     *
     // frontFccContainer is the reference container. If fccContainers were aligned with a drift (like going
     // on a diagonal) it would matter, if they're just aligned on top of each other it wouldn't.
     * @param container0
     */
    private void positionAnalogyContainers(Container0 container0) {
        for(AnalogyContainer analogyContainer : container0.getAnalogyContainers()){
            int index = container0.getAnalogyContainers().indexOf(analogyContainer);

            if(index==0) continue;
            AnalogyContainer previousAnalogyContainer = container0.getAnalogyContainers().get(index-1);

            double maxYinPrevious = previousAnalogyContainer.getBoundsInParent().getMaxY();

            analogyContainer.setLayoutY(maxYinPrevious+30);
        }
    }

    /**
     * This method creates a Tier for each pair of Knobs, one in each Fcc's that have a relation in the
     * sense of the Inclusion operation.
     *
     */
    public void tie(FccContainer newFccContainer) {
        Dynamism newPositiveDynamism = this.dataInterface.dynamismOf(0,newFccContainer.getFcc());
        Dynamism newNegativeDynamism = this.dataInterface.dynamismOf(1,newFccContainer.getFcc());
        Dynamism newSymmetricDynamism = this.dataInterface.dynamismOf(2,newFccContainer.getFcc());

        /*
         * NewFccContainer as Inclusion of any existingDynamism
         */
        for(FccContainer existingFccContainer:getFccContainers()){
            Fcc existingFcc = existingFccContainer.getFcc();

            // check if its positive, negative or symmetric orientation
            if(this.dataInterface.isDeduction(existingFcc, newPositiveDynamism)){
                // TODO if there exists already a Tier linking these two don't add it...
                tie(newFccContainer,1,existingFccContainer);
            }
            if(this.dataInterface.isDeduction(existingFcc,newNegativeDynamism)){
                tie(newFccContainer,2,existingFccContainer);
            }
            if(this.dataInterface.isDeduction(existingFcc,newSymmetricDynamism)){
                tie(newFccContainer,3,existingFccContainer);
            }

            /*
             * NewFccContainer as positive Deduction of any existingDynamism
             * */
            Fcc newFcc = newFccContainer.getFcc();

            Dynamism existingPositiveDynamism = this.dataInterface.dynamismOf(0,existingFcc);
            Dynamism existingNegativeDynamism = this.dataInterface.dynamismOf(1,existingFcc);
            Dynamism existingSymmetricDynamism = this.dataInterface.dynamismOf(2,existingFcc);

            if(this.dataInterface.isDeduction(newFcc,existingPositiveDynamism)){
                tie(existingFccContainer,1,newFccContainer);
            }

            /*
             * NewFccContainer as negative Deduction of any existingDynamism
             * */
            if(this.dataInterface.isDeduction(newFcc,existingNegativeDynamism)){
                tie(existingFccContainer,2,newFccContainer);
            }

            /*
             * NewFccContainer as symmetric Deduction of any existingDynamism
             * */
            if(this.dataInterface.isDeduction(newFcc,existingSymmetricDynamism)){
                tie(existingFccContainer,3,newFccContainer);
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
            default: break;
        }
        tier.setKnobFinal(fccContainer2.knob0);
        tier.endXProperty().bind(fccContainer2.knob0XProperty());
        tier.endYProperty().bind(fccContainer2.knob0YProperty());
        tod.getChildren().add(tier);
        tier.toBack();
    }

    public void clearAntecedents(FccContainer fccContainer){
        ArrayList<AnalogyContainer> tempListAnalogyContainers = new ArrayList<>();
        //AnalogyContainer mainParentMultiContainer = getMultiContainer(fccContainer);

        //test if there are children in the Container0 of Inclusions
        if(fccContainer.getMultiContainerParent().isAntecedentDeployed()){
            for(Node nodeAnalogy:fccContainer.getMultiContainerParent().getAntecedentsLevelContainer().getChildren()){
                AnalogyContainer analogyContainer = (AnalogyContainer)nodeAnalogy;
                // this is gonna be used after to remove all analogy containers
                //tempListAnalogyContainers.add(analogyContainer);

                for(Node nodeMulti: analogyContainer.getChildren()){

                    Container2 container3 = (Container2)nodeMulti;
                    FccContainer subFccContainer = container3.getFccContainer();

                    clearAntecedents(subFccContainer);

                    subFccContainer.getMultiContainerParent().getPositionAntecedents().getChildren().clear();
                    subFccContainer.getMultiContainerParent().getPositionDescendants().getChildren().clear();

                    // take into account: removing the lines that either arrive (to knob0) or
                    // depart (from knob1, knob2, knob3 or knob4) from fccContainer of this container3
                    // ! Remove from tod AND Tier.listTiers !
                    for(Tier t:Tier.listTiersOf(subFccContainer)){
                        getTod().getChildren().remove(t);
                        Tier.getListTiers().remove(t);
                    }

                    // ... If fccContainer is of type MIRROR do nothing special, just remove it.

                    getFccContainers().remove(subFccContainer);

                }

                fccContainer.getMultiContainerParent().getPositionAntecedents().getChildren().clear();
                fccContainer.getMultiContainerParent().getPositionDescendants().getChildren().clear();
            }
            fccContainer.getMultiContainerParent().setAntecedentDeployed(false);
        }

    }

    /*
    START OF TASKS
     */
    private Task<Void> getTaskDeployTod(Fcc initialFcc){
        Task<Void> deployTod = new Task<Void>(){
            @Override
            protected Void call() {
                //Platform.runLater(()-> deployTod());
                return null;
            }

            @Override
            protected void succeeded() {
                // Because this is the first deployment, all fccContainers are new
                // Or we could use the mainLevelContainer
//                new Thread(getTaskSetBracketAndKnobs(getTod().getContainer0())).start();
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

    @Deprecated
    public Task<Void> getTaskDeployAntecedents(Container2 container2){
        Task<Void> deployAntecedents = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //Platform.runLater(()-> deployAntecedents(fccContainer));
                Platform.runLater(container2::deployAntecedents);
                return null;
            }

            @Override
            protected void succeeded() {
                //new Thread(getTaskSetBracketAndKnobs(container2.getAntecedentsLevelContainer())).start();
                super.succeeded();
            }
        };
        return deployAntecedents;
    }

    public Task<Void> getTaskSetBracketAndKnobs(Container0 container0){
        Task<Void> setBracketAndKnobs = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(50);
                Platform.runLater(() -> {
                    for(FccContainer fc: container0.getFccContainers()){
                        fc.setBracketAndKnobs();
                    }
                });
                return null;
            }

            @Override
            protected void succeeded() {
                // Only if it is not the mainLevelContainer proceed to adding tiers.
                // Else just to positionContainer3s
                
//                if (container0.equals(tod.getContainer0())) {
//                    new Thread(getTaskPositionMultiContainers(container0)).start();
//                    super.succeeded();
//                } else if (!container0.equals(tod.getContainer0())) {
//                    new Thread(getTaskSetTiers(container0)).start();
//                    super.succeeded();
//                }

                super.succeeded();
            }
        };
        return setBracketAndKnobs;
    }

    public Task<Void> getTaskSetTiers(Container0 container0) {
        Task<Void> setTiers = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(()->{
                    for (FccContainer fc : container0.getFccContainers()) {
                        tie(fc);
                    }
                });
                return null;
            }

            @Override
            protected void succeeded() {
                new Thread(getTaskPositionMultiContainers(container0)).start();
                super.succeeded();
            }
        };

        return setTiers;
    }

    public Task<Void> getTaskPositionMultiContainers(Container0 container0){
        Task<Void> positionMultiContainers = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                Thread.sleep(100);
                Platform.runLater(container0::positionContainer3s);
                return null;
            }

            @Override
            protected void succeeded() {
//                new Thread(getTaskPositionAnalogyContainers(container0)).start();
                super.succeeded();
            }
        };

        return positionMultiContainers;
    }

    public Task<Void> getTaskPositionAnalogyContainers(Container0 container0){
        Task<Void> positionAnalogyContainers = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
//                Thread.sleep(100);
                Platform.runLater(container0::positionContainer2s);
                return null;
            }

            @Override
            protected void succeeded() {
//                if (container0.equals((tod.getContainer0()))) {
//                    new Thread(getTaskSetKnobsPositions()).start();
//                } else if (!container0.equals((tod.getContainer0()))) {
//                    //if it is not mainLevelContainer we know it is inside a multi, and that one inside an analogy
//                    // and that last inside another level
//                    new Thread(getTaskPositionMultiContainers(
//                        container0.getParentContainer3().getAnalogyContainerParent().getLevelContainerParent())
//                    ).start();
//                }
                super.succeeded();
            }
        };
        return positionAnalogyContainers;
    }

    public Task<Void> getTaskSetKnobsPositions() {
        Task<Void> setKnobsPositions = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                //Thread.sleep(300);
                Platform.runLater(()->{
                    for(FccContainer fc:getFccContainers()){
                        fc.setKnobsPositions();
                    }
                });
                return null;
            }

            @Override
            protected void succeeded() {
//                new Thread(getTaskSetBorderAnalogy()).start();
                //new Thread(getTaskPutFccContainersInFront()).start();
                super.succeeded();
            }
        };

        return setKnobsPositions;
    }
    
    private Task<Void> getTaskPutFccContainersInFront() {
        Task<Void> setBorderAnalogy = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                //Thread.sleep(400);
                Platform.runLater(()->putFccContainersInFront());
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
            }
        };

        return setBorderAnalogy;
    }

    public Task<Void> getTaskSetBorderAnalogy() {
        Task<Void> setBorderAnalogy = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
//                Thread.sleep(400);
                Platform.runLater(()->setBorderAnalogy());
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
            }
        };

        return setBorderAnalogy;
    }

    private void setBorderAnalogy() {
        ArrayList<AnalogyContainer> listAnalogyContainers = new ArrayList<>();

        Random randomizer = new Random();
        ArrayList<String> listOfcolors = new ArrayList<>();
        
        listOfcolors.add("red");
        listOfcolors.add("green");
        listOfcolors.add("blue");
        listOfcolors.add("yellow");
        listOfcolors.add("black");
        listOfcolors.add("brown");
        listOfcolors.add("darkorange");
        listOfcolors.add("darkviolet");
        listOfcolors.add("forestgreen");
        listOfcolors.add("hotpink");
        listOfcolors.add("lawngreen");
        listOfcolors.add("lightgray");
        listOfcolors.add("maroon");
        listOfcolors.add("navy");
        listOfcolors.add("orange");
        
        for(AnalogyContainer analogyContainer :getAnalogyContainers()){
            
            Bounds bounds = tod.sceneToLocal(analogyContainer.localToScene(analogyContainer.getBoundsInLocal()));
            
            double minX, maxX, minY, maxY;
            
            minX = bounds.getMinX();
            maxX = bounds.getMaxX();
            minY = bounds.getMinY();
            maxY = bounds.getMaxY();
            
            Line topLine = new Line(minX,minY,maxX,minY);
            Line rightLine = new Line(maxX,minY,maxX,maxY);
            Line bottomLine = new Line(maxX,maxY,minX,maxY);
            Line leftLine = new Line(minX,maxY,minX,minY);
            
            String style = "-fx-stroke:"+ listOfcolors.get(randomizer.nextInt(listOfcolors.size())) + ";-fx-stroke-type:outside;-fx-stroke-width:1;";
            topLine.setStyle(style);
            rightLine.setStyle(style);
            bottomLine.setStyle(style);
            leftLine.setStyle(style);

            tod.getChildren().addAll(topLine, rightLine, bottomLine, leftLine);
        }
    }

    public void putFccContainersInFront(){
        for(FccContainer fc:getFccContainers()){
            fc.toFront();
        }
    }

    // for testing only
    public void setScale(double i) {
        canvas.setScaleX(canvas.getScaleX()+i);
        canvas.setScaleY(canvas.getScaleY()+i);
    }


    /*
    END OF TASKS
     */
}