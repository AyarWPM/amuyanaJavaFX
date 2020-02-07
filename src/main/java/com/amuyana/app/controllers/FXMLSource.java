package com.amuyana.app.controllers;

/**
 *
 * @author Ayar Portugal <ayar.portugal@amuyaña.com>
 */
public enum FXMLSource {
    CONNEXION("/view/Connection.fxml"),
    LOGIC_SYSTEM("/view/LogicSystem.fxml"),
    TOD("/view/Tod.fxml"),
    FCCSELECTOR("/view/FccSelector.fxml"),
    FCCEDITOR("/view/FccEditor.fxml"),
    FCCS("/view/Fccs.fxml"),
    INCLUSIONS("/view/Inclusion.fxml"),
    SYLLOGISM("/view/Syllogism.fxml"),
    CCLASS("/view/CClass.fxml"),
    CONJUNCTION("/view/Conjunction.fxml"),
    DIALECTIC("/view/Dialectic.fxml"),
    STC("/view/Stc.fxml"),
    STATS("/view/Stats.fxml"),
    FRUIT("/view/Fruit.fxml"),
    ABOUT("/view/About.fxml");

    private final String url;

    FXMLSource(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

}
