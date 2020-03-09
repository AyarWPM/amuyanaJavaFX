package com.amuyana.app.node.content;

import com.amuyana.app.data.Fcc;
import com.amuyana.app.data.tod.FccTableEntry;
import com.amuyana.app.node.NodeHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class FccTableTab extends Tab {
    public FccTableTab() {
        setText("FCCs viewer");
        VBox vBox = new VBox();
        TableView<FccTableEntry> tableView = new TableView<>();

        // FCC
        TableColumn<FccTableEntry,String> fccId = new TableColumn<>("ID");
        fccId.setCellValueFactory(new PropertyValueFactory<>("fccId"));

        TableColumn<FccTableEntry,String> logicSystem = new TableColumn<>("Logic System");
        logicSystem.setCellValueFactory(new PropertyValueFactory<>("logicSystem"));

        TableColumn<FccTableEntry,String> nameFcc = new TableColumn<>("FCC name");
        nameFcc.setCellValueFactory(new PropertyValueFactory<>("fccName"));

        TableColumn<FccTableEntry, String> descriptionFcc = new TableColumn<>("FCC description");
        descriptionFcc.setCellValueFactory(new PropertyValueFactory<>("fccDescription"));

        // ELEMENTS
        TableColumn<FccTableEntry, String> element = new TableColumn<>("Element");
        element.setCellValueFactory(new PropertyValueFactory<>("element"));

        TableColumn<FccTableEntry, String> antiElement = new TableColumn<>("Anti-Element");
        antiElement.setCellValueFactory(new PropertyValueFactory<>("antiElement"));

        // DYNAMISMS
        TableColumn<FccTableEntry, String> positiveName = new TableColumn<>("Positive orientation");
        positiveName.setCellValueFactory(new PropertyValueFactory<>("positiveName"));
        TableColumn<FccTableEntry, String> positiveDescription = new TableColumn<>("Positive orientation description");
        positiveDescription.setCellValueFactory(new PropertyValueFactory<>("positiveDescription"));

        TableColumn<FccTableEntry, String> negativeName = new TableColumn<>("Negative orientation");
        negativeName.setCellValueFactory(new PropertyValueFactory<>("negativeName"));
        TableColumn<FccTableEntry, String> negativeDescription = new TableColumn<>("Negative orientation description");
        negativeDescription.setCellValueFactory(new PropertyValueFactory<>("negativeDescription"));

        TableColumn<FccTableEntry, String> symmetricName = new TableColumn<>("Symmetric orientation");
        symmetricName.setCellValueFactory(new PropertyValueFactory<>("symmetricName"));
        TableColumn<FccTableEntry, String> symmetricDescription = new TableColumn<>("Symmetric orientation description");
        symmetricDescription.setCellValueFactory(new PropertyValueFactory<>("symmetricDescription"));

        // ENTRIES
        tableView.getColumns().addAll(fccId,logicSystem,element,antiElement,nameFcc,descriptionFcc,positiveName,positiveDescription,negativeName,negativeDescription,symmetricName,symmetricDescription);
        tableView.getItems().setAll(getlistFccEntries());

        // BUTTON TO REFRESH
        Button refresh = new Button("Refresh");
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableView.getItems().setAll(getlistFccEntries());
            }
        });
        vBox.getChildren().addAll(refresh,tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        setContent(vBox);
    }

    private ObservableList<FccTableEntry> getlistFccEntries() {
        ObservableList<FccTableEntry> listFccEntries = FXCollections.observableArrayList();
        for (Fcc fcc : NodeHandler.getDataInterface().getListFcc()) {
            listFccEntries.add(new FccTableEntry(fcc));
        }
        return listFccEntries;
    }
}