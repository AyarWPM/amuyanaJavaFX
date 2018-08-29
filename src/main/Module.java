package main;

import javafx.scene.Node;

/**
 *
 * @author Ayar Portugal <ayar.portugal@amuyaña.com>
 */
public enum Module {
    LOGIC_SYSTEM("/resources/fxml/LogicSystem.fxml"),
    DUALITIES("/resources/fxml/Dualities.fxml"),
    INCLUSIONS("/resources/fxml/Inclusion.fxml"),
    TOD("/resources/fxml/Tod.fxml"),
    DIALECTIC("/resources/fxml/Dialectic.fxml"),
    CLASSES("/resources/fxml/CClass.fxml"),
    STC("/resources/fxml/Stc.fxml"),
    SYLLOGISM("/resources/fxml/Syllogism.fxml"),
    STATS("/resources/fxml/Stats.fxml"),
    SETTINGS("/resources/fxml/Settings.fxml");
    
    private final String url;
    
    private Node node;
    
    private Module(String url) {
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
