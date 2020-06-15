package com.amuyana.app.node.content;

import com.amuyana.app.data.Syllogism;
import com.amuyana.app.data.tod.FccTableEntry;
import com.amuyana.app.data.tod.SyllogismTableEntry;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.tod.expression.SyllogismExp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SyllogismsTab extends Tab {
    public SyllogismsTab() {
        setText("Syllogisms");
        VBox vBox = new VBox();
        TableView<SyllogismTableEntry> tableView = new TableView<>();
        // id
        TableColumn<SyllogismTableEntry, String> id = new TableColumn<>("id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        // label
        TableColumn<SyllogismTableEntry, String> label = new TableColumn<>("Label");
        label.setCellValueFactory(new PropertyValueFactory<>("label"));
        // tod
        TableColumn<SyllogismTableEntry, String> tod = new TableColumn<>("Table of deductions");
        tod.setCellValueFactory(new PropertyValueFactory<>("tod"));
        // Number of dynamisms
        TableColumn<SyllogismTableEntry, String> nDynamisms = new TableColumn<>("D");
        nDynamisms.setCellValueFactory(new PropertyValueFactory<>("numDynamisms"));
        // number of inclusions (#dyn-1)
        TableColumn<SyllogismTableEntry, String> nInclusions = new TableColumn<>("I");
        nInclusions.setCellValueFactory(new PropertyValueFactory<>("numInclusions"));
        // The inclusions
        TableColumn<SyllogismTableEntry, SyllogismExp> syllogismExp = new TableColumn<>("Syllogism");
        syllogismExp.setCellValueFactory(new PropertyValueFactory<>("syllogismExp"));
        // Number of registers
        TableColumn<SyllogismTableEntry, String> nRegisters = new TableColumn<>("R");
        nRegisters.setCellValueFactory(new PropertyValueFactory<>("numRegisters"));

        // ENTRIES
        try {
            tableView.getColumns().addAll(id, label, tod, nDynamisms, nInclusions, syllogismExp, nRegisters);
        } catch (Exception e) {
            System.err.println(e);
        }

        tableView.getItems().setAll(getListSyllogismEntries());

        // BUTTON TO REFRESH
        Button refresh = new Button("Refresh");
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableView.getItems().setAll(getListSyllogismEntries());
            }
        });
        vBox.getChildren().addAll(refresh,tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        setContent(vBox);
    }

    private ObservableList<SyllogismTableEntry> getListSyllogismEntries() {
        ObservableList<SyllogismTableEntry> listSyllogismEntries = FXCollections.observableArrayList();
        for (Syllogism syllogism : NodeHandler.getDataInterface().getListSyllogisms()) {
            listSyllogismEntries.add(new SyllogismTableEntry(syllogism));
        }
        return listSyllogismEntries;
    }
}