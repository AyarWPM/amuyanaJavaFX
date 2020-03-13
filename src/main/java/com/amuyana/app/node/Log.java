package com.amuyana.app.node;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Log extends Group {
    private ListView<String > logListView;
    private HBox container;
    private Group messageGroup;

    public Log() {
        logListView = new ListView<>();
        messageGroup = new Group(new Label("Welcome"));
        container = new HBox(messageGroup);
        container.setPadding(new Insets(3,0,3,6));
        container.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(1), new BorderWidths(1,0,0,0))));
    }

    public HBox getContainer() {
        return container;
    }

    public void register(String messageString) {
        // in bottom bar
        Label label = new Label(messageString);
        label.setGraphic(new Circle(5,Color.web("#CC0000")));

        FadeTransition fadeTransitionOut = new FadeTransition(Duration.seconds(7),messageGroup);
        fadeTransitionOut.setFromValue(1.0);
        fadeTransitionOut.setToValue(0.0);

        FadeTransition fadeTransitionIn = new FadeTransition(Duration.seconds(0.5),messageGroup);
        fadeTransitionIn.setFromValue(0.0);
        fadeTransitionIn.setToValue(1.0);
        messageGroup.getChildren().setAll(label);
        messageGroup.setOnMouseEntered(event -> {
            fadeTransitionOut.stop();
            fadeTransitionIn.play();
        });
        messageGroup.setOnMouseExited(event -> {
            fadeTransitionIn.stop();
            fadeTransitionOut.play();
        });
        fadeTransitionOut.play();

        // in listview
        logListView.getItems().add(messageString);
    }
}
