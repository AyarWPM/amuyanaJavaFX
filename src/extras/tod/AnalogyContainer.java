package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.VBox;


public class AnalogyContainer extends Group {
    private final static double TRANSLATE_X=20;
    private final static double TRANSLATE_Y=28;
    private final static double TRANSPARENCY=0.2;
    
    private static AppController appController;
    private static TodController todController;

    Analogy analogy;
    
    private static int xMove;
    private static int yMove;

    // List of MultiContainers of the analogous FCC's
    // private ArrayList<MultiContainer> listMultiContainers;

    AnalogyContainer(Analogy analogy){
        this.analogy = analogy;
        
        setStyle();
        
    }

    public static void setControllers(AppController appController, TodController todController) {
        AnalogyContainer.appController = appController;
        AnalogyContainer.todController = todController;
        
    }
    
    void deploy(){
        for(Fcc f:this.analogy){
            MultiContainer multiContainer = new MultiContainer(f);
            
            this.getChildren().add(multiContainer);
            int index = this.getChildren().indexOf(multiContainer);
            
            
            
            multiContainer.setLayoutX(index*TRANSLATE_X);
            multiContainer.setLayoutY(-index*TRANSLATE_Y);
            multiContainer.setOpacity(1-index*TRANSPARENCY);
            multiContainer.toBack();
            
            multiContainer.deploy();
        }
    }

    public void turnToFront(MultiContainer multiContainer){
        
        int POS = getChildren().indexOf(multiContainer);
        
        
        
    }
    
//    public void turnToFront(MultiContainer multiContainer){
//        int POS = getChildren().indexOf(multiContainer);
//        
//        int index = 0;
//        
//        for(int i=POS;i<getChildren().size();i++){
//            MultiContainer multi = (MultiContainer)getChildren().get(i);
//            
//            multi.setLayoutX(index*TRANSLATE_X);
//            multi.setLayoutY(-index*TRANSLATE_Y);
//            multi.toBack();
//            
//            System.out.println("Multi " + multi + " in x " + multi.getLayoutX() + " and in y " + multi.getLayoutY());
//            
//            index++;
//        }
//        
//        for(int i = 0;i<POS;i++){
//            MultiContainer multi = (MultiContainer)getChildren().get(i);
//            
//            multi.setLayoutX(index*TRANSLATE_X);
//            multi.setLayoutY(-index*TRANSLATE_Y);
//            multi.toBack();
//            
//            System.out.println("Multi " + multi + " in x " + multi.getLayoutX() + " and in y " + multi.getLayoutY());
//            
//            index++;
//        }
//        
//    }

    private void setStyle() {
        //this.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(4))));
        //this.setAlignment(Pos.CENTER);
    }
}
