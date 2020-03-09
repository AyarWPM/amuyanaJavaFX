package com.amuyana.app.node;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Log extends Group {
    private ListView<String > logListView;
    private HBox container;
    private Group messageGroup;

    public Log() {
        logListView = new ListView<>();
        messageGroup = new Group(new Label("Welcome"));
        container = new HBox(messageGroup);
        container.setPadding(new Insets(2));
        container.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(1), new BorderWidths(1,0,0,0))));
    }

    public HBox getContainer() {
        return container;
    }

    public void register(String messageString) {
        // in bottom bar
        messageGroup.getChildren().setAll(new Label(messageString));
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(5),messageGroup);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();

        // in listview
        logListView.getItems().add(messageString);
    }
}
