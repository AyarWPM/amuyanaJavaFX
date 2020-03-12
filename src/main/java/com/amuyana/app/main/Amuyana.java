package com.amuyana.app.main;

import com.amuyana.app.data.DataHandler;
import com.amuyana.app.data.DataInterface;
import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.LogicSystem;
import com.amuyana.app.data.tod.containers.Tod;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.NodeInterface;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Amuyana extends Application {

    @Override
    public void start(Stage stage) {
        DataInterface dataInterface = new DataHandler();
        NodeInterface nodeInterface = new NodeHandler(dataInterface, stage);

        Scene scene = new Scene((BorderPane) nodeInterface.getRootNode(), 900, 700);
        stage.setTitle(NodeHandler.VERSION);
        stage.setScene(scene);
        // Debug lines
        //nodeInterface.openDebug1();

        // Debug TOD
/*        dataInterface.setDataConnectionValues("localhost","amuyana","");
        dataInterface.connect();
        dataInterface.loadData();
        dataInterface.disconnect();
        LogicSystem logicSystem = dataInterface.getListLogicSystem().get(0);
        Tod tod = dataInterface.getTods(logicSystem).get(0);
        nodeInterface.load(logicSystem);
        nodeInterface.open(tod);*/

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }
}
