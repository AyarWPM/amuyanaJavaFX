package main;

import javafx.scene.Node;

/**
 *
 * @author Ayar Portugal <ayar.portugal@amuyaña.com>
 */
public enum FXMLSource {
    LOGIC_SYSTEM("/resources/fxml/LogicSystem.fxml"),

    TOD("/resources/fxml/Tod.fxml"),
    FCC("/resources/fxml/Fcc.fxml"),
    INCLUSIONS("/resources/fxml/Inclusion.fxml"),
    SYLLOGISM("/resources/fxml/Syllogism.fxml"),
    CLASSES("/resources/fxml/CClass.fxml"),

    DIALECTIC("/resources/fxml/Dialectic.fxml"),
    STC("/resources/fxml/Stc.fxml"),

    STATS("/resources/fxml/Stats.fxml");

    private final String url;
    
    private Node node;
    
    private FXMLSource(String url) {
        this.url = url;
    }
    
    public void setNode(Node node){
        this.node=node;
    }
    
    public Node getNode() {
        return this.node;
    }
    
    public String getUrl() {
        return this.url;
    }

}
