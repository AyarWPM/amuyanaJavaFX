package com.amuyana.app;

/**
 *
 * @author Ayar Portugal <ayar.portugal@amuyaña.com>
 */
public enum FXMLSource {
    CONNEXION("/fxml/Connection.fxml"),
    LOGIC_SYSTEM("/fxml/LogicSystem.fxml"),
    TOD("/fxml/Tod.fxml"),
    FCCSELECTOR("/fxml/FccSelector.fxml"),
    FCCEDITOR("/fxml/FccEditor.fxml"),
    FCCS("/fxml/Fccs.fxml"),
    INCLUSIONS("/fxml/Inclusion.fxml"),
    SYLLOGISM("/fxml/Syllogism.fxml"),
    CCLASS("/com/amuyana/app/controllers/CClass.fxml"),
    CONJUNCTION("/com/amuyana/app/controllers/Conjunction.fxml"),
    DIALECTIC("/fxml/Dialectic.fxml"),
    STC("/fxml/Stc.fxml"),
    STATS("/fxml/Stats.fxml"),
    FRUIT("/fxml/Fruit.fxml");

    private final String url;

    FXMLSource(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

}
