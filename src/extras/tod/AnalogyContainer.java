package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Fcc;
import javafx.scene.Group;
import javafx.scene.Node;


public class AnalogyContainer extends Group {

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
            int size=this.getChildren().size();
            double factor = size*0.15;
            MultiContainer multiContainer = new MultiContainer(f);
            this.getChildren().add(multiContainer);
            multiContainer.setTranslateX(this.getChildren().indexOf(multiContainer)*20);
            multiContainer.setTranslateY(-this.getChildren().indexOf(multiContainer)*28);
            multiContainer.toBack();

            multiContainer.setOpacity(1-factor);

            multiContainer.deploy();
        }
    }

    public void turnToFront(FccContainer fccContainer){

        for(Node n:getChildren()){
            //MultiContainer multi = (MultiContainer)n;
            if(((MultiContainer) n).getChildren().get(1).equals(fccContainer)){
                System.out.println("yes");
            } else if(!((MultiContainer) n).getChildren().get(1).equals(fccContainer)){
                System.out.println("no");
            }
        }
    }

    private void setStyle() {
        //this.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(4))));
        //this.setAlignment(Pos.CENTER);
    }
}
