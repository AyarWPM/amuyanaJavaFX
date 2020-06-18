package com.amuyana.app.node.content;

import com.amuyana.app.data.Register;
import com.amuyana.app.data.Syllogism;
import com.amuyana.app.data.tod.RegisterTableEntry;
import com.amuyana.app.data.tod.SyllogismTableEntry;
import com.amuyana.app.node.NodeHandler;
import com.amuyana.app.node.tod.expression.ConjunctionExp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class StatisticsTab extends Tab {
    public StatisticsTab() {
        setText("Statistics");
        VBox vBox = new VBox();
        TableView<RegisterTableEntry> tableView = new TableView<>();
        // id
        TableColumn<RegisterTableEntry, String> id = new TableColumn<>("id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        // date
        TableColumn<RegisterTableEntry, String> date = new TableColumn<>("Date of registration");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        // tods
        TableColumn<RegisterTableEntry, String> tods = new TableColumn<>("Table of deductions");
        tods.setCellValueFactory(new PropertyValueFactory<>("tods"));
        // propositional dynamism
        TableColumn<RegisterTableEntry, String> dynamismName = new TableColumn<>("Dynamism name");
        dynamismName.setCellValueFactory(new PropertyValueFactory<>("dynamismName"));
        // conjunction expression
        TableColumn<RegisterTableEntry, ConjunctionExp> conjunctionExp = new TableColumn<>("Contradictional conjunction");
        conjunctionExp.setCellValueFactory(new PropertyValueFactory<>("conjunctionExp"));
        // start
        TableColumn<RegisterTableEntry, String> start = new TableColumn<>("Starting time");
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        // end
        TableColumn<RegisterTableEntry, String> end = new TableColumn<>("Ending time");
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        // interval  / elapsed time
        TableColumn<RegisterTableEntry, String> interval = new TableColumn<>("Elapsed time");
        interval.setCellValueFactory(new PropertyValueFactory<>("interval"));
        // ENTRIES
        tableView.getColumns().addAll(id, date, tods, dynamismName, conjunctionExp, start, end, interval);

        tableView.getItems().setAll(getListRegisterEntries());

        // BUTTON TO REFRESH
        Button refresh = new Button("Refresh");
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableView.getItems().setAll(getListRegisterEntries());
            }
        });
        vBox.getChildren().addAll(refresh,tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        setContent(vBox);
    }

    private ObservableList<RegisterTableEntry> getListRegisterEntries() {
        ObservableList<RegisterTableEntry> listRegisterEntries = FXCollections.observableArrayList();
        for (Register register : NodeHandler.getDataInterface().getListRegisters()) {
            listRegisterEntries.add(new RegisterTableEntry(register));
        }
        return listRegisterEntries;
    }
}