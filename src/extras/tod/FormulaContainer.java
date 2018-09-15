package extras.tod;

import controllers.AppController;
import controllers.TodController;
import data.Dynamism;
import data.Element;
import data.Fcc;

import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import static extras.tod.FormulaContainer.Signs.*;
import static extras.tod.FormulaContainer.Styles.*;


public class FormulaContainer extends HBox {

    private static AppController appController;
    private static TodController todController;
    private Fcc fcc;
    
    private ArrayList<Dynamism> listDynamisms;

    public enum Styles {SIMPLE,FULL,PHRASES};
    public enum Signs {OPENING_PARENTHESIS, CLOSING_PARENTHESIS, ACTUAL, POTENTIAL, THIRD, IMPLICATION};

    /**
     * Attention. The order of the listDynamisms items are from most general 
     * notions to the most particular, i.e. from left to right in the Table 
     * of Deductions.
     *
     */
    public FormulaContainer(Dynamism dynamism) {
        // we don't need to instantiate this
        //this.listDynamisms = new ArrayList<>();
        this.listDynamisms = new ArrayList<>();
        this.listDynamisms.add(dynamism);
    }


    public void setDynamism(Dynamism dynamism){
        // Don't forget to "reset" the list because this method will be called several times

    }

    public void setListDynamisms(ArrayList<Dynamism> listDynamisms){
        this.listDynamisms = listDynamisms;
    }


    public static void setControllers(AppController appController, TodController todController) {
        FormulaContainer.appController = appController;
        FormulaContainer.todController = todController;
    }

    void write(Styles STYLE){
        switch (STYLE){
            case SIMPLE:{
                Element element = appController.elementOf(0, listDynamisms.get(0).getFcc());
                String e = element.getSymbol();

                Element antiElement = appController.elementOf(1, listDynamisms.get(0).getFcc());
                String aE = antiElement.getSymbol();

                this.getChildren().add(sign(OPENING_PARENTHESIS));

                switch(this.listDynamisms.get(0).getOrientation()){
                    case 0:{
                        this.getChildren().add(sign(e,false));
                        this.getChildren().add(sign(ACTUAL));
                        this.getChildren().add(sign(IMPLICATION));
                        if(e.equals(aE)){
                            this.getChildren().add(sign(aE,true));
                        } else if (!e.equals(aE)){
                            this.getChildren().add(sign(aE,false));
                        }
                        this.getChildren().add(sign(POTENTIAL));
                        break;
                    }
                    case 1:{
                        if(e.equals(aE)){
                            this.getChildren().add(sign(aE,true));
                        } else if (!e.equals(aE)){
                            this.getChildren().add(sign(aE,false));
                        }
                        this.getChildren().add(sign(ACTUAL));
                        this.getChildren().add(sign(IMPLICATION));
                        this.getChildren().add(sign(e,false));
                        this.getChildren().add(sign(POTENTIAL));
                        break;
                    }
                    case 2:{
                        this.getChildren().add(sign(e,false));
                        this.getChildren().add(sign(THIRD));
                        this.getChildren().add(sign(IMPLICATION));
                        if(e.equals(aE)){
                            this.getChildren().add(sign(aE,true));
                        } else if (!e.equals(aE)){
                            this.getChildren().add(sign(aE,false));
                        }
                        this.getChildren().add(sign(THIRD));
                        break;
                    }
                    default: break;
                }

                this.getChildren().add(sign(CLOSING_PARENTHESIS));


                break;
            }
            case FULL:{
                ArrayList<String> listSymbols = new ArrayList<>();

                listSymbols.add("");

                for(Dynamism d:this.listDynamisms){

                    Element element = appController.elementOf(0, d.getFcc());
                    String e = element.getSymbol();

                    Element antiElement = appController.elementOf(1, d.getFcc());
                    String aE = antiElement.getSymbol();

                    int middlePosition = (listSymbols.size()-1)/2;

                    ArrayList<String> left = new ArrayList<>();
                    ArrayList<String> right = new ArrayList<>();

                    left.addAll(listSymbols);
                    right.addAll(listSymbols);

                    switch (d.getOrientation()) {
                        case 0:{
                            left.set(middlePosition, e);
                            left.add(middlePosition+1,"A");
                            right.set(middlePosition, aE);
                            right.add(middlePosition+1,"P");

                            break;
                        }
                        case 1:{
                            left.set(middlePosition, aE);
                            left.add(middlePosition+1,"A");
                            right.set(middlePosition, e);
                            right.add(middlePosition+1,"P");

                            break;
                        }
                        case 2:{
                            left.set(middlePosition, e);
                            left.add(middlePosition+1,"T");
                            right.set(middlePosition, aE);
                            right.add(middlePosition+1,"T");

                            break;
                        }
                        default:
                            break;
                    }

                    listSymbols.clear();
                    listSymbols.add("(");
                    listSymbols.addAll(left);
                    listSymbols.add(")");
                    listSymbols.add("\u2283");
                    listSymbols.add("(");
                    listSymbols.addAll(right);
                    listSymbols.add(")");

                }

                for(String s:listSymbols){
                    Label label = new Label(s);

                    this.getChildren().add(label);
                }
                break;
            }
            case PHRASES:{

                break;
            }

        }

    }

    private Label sign(Signs SIGN){
        Label sign = new Label();
        switch (SIGN){
            case OPENING_PARENTHESIS:{
                sign.setText("(");
                sign.setStyle(
                      "-fx-padding: 0 2 0 2;" +
                              "-fx-font:17px \"Sans\";"
                );
                break;
            }
            case CLOSING_PARENTHESIS:{
                sign.setText(")");
                sign.setStyle(
                        "-fx-padding: 0 2 0 2;" +
                                "-fx-font:17px \"Sans\";"

                );
                break;
            }
            case ACTUAL:{
                sign.setText("A");
                sign.setStyle(
                        "-fx-padding: 0 2 0 1;" +
                                "-fx-size:75%;");
                sign.setTranslateY(6);
                break;
            }
            case POTENTIAL:{
                sign.setText("P");
                sign.setStyle(
                        "-fx-padding: 0 2 0 1;" +
                                "-fx-size:75%;");
                sign.setTranslateY(6);
                break;
            }
            case THIRD:{
                sign.setText("T");
                sign.setStyle(
                        "-fx-padding: 0 2 0 1;" +
                                "-fx-size:75%;");
                sign.setTranslateY(6);
                break;
            }
            case IMPLICATION:{
                sign.setText("\u2283");
                sign.setStyle(
                        "-fx-padding: 0 2 0 2;" +
                                "-fx-font:16px \"Sans\";");
                break;
            }

        }
        return sign;
    }

    private Label sign(String element, boolean withBar){
        Label sign = new Label();
        String style = "-fx-padding: 0;" +
                "-fx-font: bold 16px \"sans\";";
        sign.setText(element);

        if(withBar){
            style += "-fx-border-width: 1.5 0 0 0;" +
                    "-fx-border-style:solid;" +
                    "-fx-border-color:black;" +
                    "-fx-border-insets: 0 2 0 2;";
        }

        sign.setStyle(style);

        return sign;
    }
}
