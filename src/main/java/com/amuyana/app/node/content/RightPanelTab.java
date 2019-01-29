package com.amuyana.app.node.content;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

import java.io.IOException;

public class RightPanelTab extends Tab {
    private Object controller;

    public RightPanelTab() {

    }

    void setSource(Node node) {
        ScrollPane scrollPane = new ScrollPane(node);
        scrollPane.setFitToHeight(false);
        scrollPane.setFitToWidth(true);

        setContent(scrollPane);
    }

}
