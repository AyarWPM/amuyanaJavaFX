package extras;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public abstract class Formula {
    
    public static HBox implication(int orientation, String element, String antielement){
        HBox implication = new HBox();
        
        Label p0= formatedLabelParenthesis("(");
        Label p1= formatedLabelParenthesis(")");
        Label p2= formatedLabelParenthesis("(");
        Label p3= formatedLabelParenthesis(")");
        
        Label e = null;
        Label se = null;
        Label i=new Label("\u2283");
        Label ae = null;
        Label sae = null;
        
        switch(orientation){
            case(0):{
                e=formatedLabelElement(element);
                se=formatedLabelSign("A");
                ae=formatedLabelElement(antielement);
                sae=formatedLabelSign("P");
                break;
            }
            case(1):{
                e=formatedLabelElement(antielement);
                se=formatedLabelSign("A");
                ae=formatedLabelElement(element);
                sae=formatedLabelSign("P");
                break;
            }
            case(2):{
                e=formatedLabelElement(element);
                se=formatedLabelSign("T");
                ae=formatedLabelElement(antielement);
                sae=formatedLabelSign("T");
                break;
            }
        }
        
        
        i.setFont(Font.font("Arial", FontPosture.REGULAR, 17));
        
        
        implication.getChildren().addAll(p0,e,se,p1,i,p2,ae,sae,p3);
        return implication;
    }
    
    private static Label formatedLabelParenthesis(String string){
        Label formatedLabel = new Label(string);
        
        formatedLabel.setFont(Font.font("Arial", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 17));
        
        return formatedLabel;
    }
    
    private static Label formatedLabelElement(String string){
        Label formatedLabel = new Label(string);
        
        formatedLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, FontPosture.REGULAR, 15));
        
        return formatedLabel;
    }
    private static Label formatedLabelSign(String string){
        Label formatedLabel = new Label(string);
        formatedLabel.setFont(Font.font("Arial", FontWeight.LIGHT, FontPosture.REGULAR, 15));
        //formatedLabel.setTranslateX(5);
        formatedLabel.setTranslateY(5);
        return formatedLabel;
    }
    
    public static HBox conjunction(int orientation, String element, String antielement){
        return null;
    }
}
