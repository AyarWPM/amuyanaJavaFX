package main.data.tod.containers;

import main.controllers.AppController;
import main.controllers.TodController;

import java.sql.*;
import main.data.LogicSystem;
import main.tod.Analogy;
import main.tod.AnalogyContainer;
import main.tod.FccContainer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Tod extends Group {
    private IntegerProperty idTod;
    private StringProperty label;
    private LogicSystem logicSystem;
    private Container0 container0;

    public static int currentAutoIncrement;

    private static AppController appController;
    private static TodController todController;

    public Tod(int idTod, String label, LogicSystem logicSystem, Container0 container0) {
        this.idTod = new SimpleIntegerProperty(idTod);
        this.label = new SimpleStringProperty(label);
        this.logicSystem = logicSystem;
        //this.container0 = container0;
        setContainer0(container0);
    }

    public static void setControllers(AppController appController, TodController todController) {
        Tod.appController = appController;
        Tod.todController = todController;
    }

    //Metodos atributo: idTod
    public int getIdTod() {
        return idTod.get();
    }
    public void setIdTod(int idTod) {
        this.idTod = new SimpleIntegerProperty(idTod);
    }
    public IntegerProperty idTodProperty() {
        return idTod;
    }

    //Metodos atributo: label
    public String getLabel() {
        return label.get();
    }
    public void setLabel(String label) {
        this.label = new SimpleStringProperty(label);
    }
    public StringProperty labelProperty() {
        return label;
    }
    // Methods attribute: logicSystem
    public LogicSystem getLogicSystem() {
        return logicSystem;
    }
    public void setLogicSystem(LogicSystem logicSystem) {
        this.logicSystem = logicSystem;
    }
    // Methods attribute: logicSystem
    public Container0 getContainer0() {
        return container0;
    }

    public void setContainer0(Container0 container0) {
        this.container0 = container0;
        //TODO go through all children and remove only the Container0 if it is there
        this.getChildren().add(container0);
    }

    /**
     *
     * @return All the Container0's in the tod, starting by the main one (attribute of tod) and all the others...
     */
    public ObservableList<Container0> getContainer0s() {
        ObservableList<Container0> container0s = FXCollections.observableArrayList();
        for (Container1 container1 : getContainer0().getContainer1s()) {

        }
        return container0s;
    }

    public ObservableList<Container0> getContainer0s(ObservableList<Container0> container0s) {

            return container0s;
    }

    public static void loadList(Connection connection,
                                ObservableList<Tod> tods,
                                ObservableList<LogicSystem> logicSystems,
                                ObservableList<Container0> container0s) {
        String sql = "SELECT id_tod, label, id_logic_system, id_container_0 FROM amuyana.tbl_tod";

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while(result.next()){
                int idTod = result.getInt("id_tod");
                String label = result.getString("label");
                LogicSystem logicSystem = null;
                Container0 container0 = null;
                for (LogicSystem ls : logicSystems) {
                    if (ls.getIdLogicSystem() == result.getInt("id_logic_system")) {
                        logicSystem = ls;
                    }
                }
                for (Container0 c0 : container0s) {
                    if (c0.getIdContainer0() == result.getInt("id_container_0")) {
                        container0 = c0;
                    }
                }
                tods.add(new Tod(idTod, label, logicSystem,container0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int saveData(Connection connection){
        String sql="INSERT INTO amuyana.tbl_data (id_data, label, id_logic_system, id_container_0) "
                + "VALUES (?,?,?,?)";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            instruction.setInt(1,this.idTod.get());
            instruction.setString(2,this.label.get());
            instruction.setInt(3,this.logicSystem.getIdLogicSystem());
            instruction.setInt(4,this.container0.getIdContainer0());

            int returnInt = instruction.executeUpdate();
            ResultSet rs = instruction.getGeneratedKeys();
            if(rs.next()){
                Tod.currentAutoIncrement = rs.getInt(1);
            }
            return returnInt;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_tod SET label = ? "
                + "WHERE id_tod = ?";
        try {
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            statement.setString(1, label.get());
            statement.setInt(2, idTod.get());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteData(Connection connection){
        String sql = "DELETE FROM amuyana.tbl_tod " +
                "WHERE id_tod = ? ";
        try {
            PreparedStatement instruction = connection.prepareStatement(sql);
            instruction.setInt(1, this.idTod.get());
            return instruction.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString(){
        return this.getLabel();
    }

    private void setStyle() {
        //this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(6))));
    }

    public void deploy(Analogy initialAnalogy) {
        addRules();
        this.container0 = new Container0(0);//TODO
        this.getChildren().setAll(this.container0);
        this.container0.deploy();
    }

    private void addRules() {
        Line xAxis = new Line(0,0,400,0);
        Line yAxis = new Line(0,0,0,400);

        xAxis.setStrokeWidth(2);
        yAxis.setStrokeWidth(2);

        this.getChildren().addAll(xAxis,yAxis);

        for(int i=0;i<=400;i+=10){
            Line markX = new Line();
            Line markY = new Line();

            markX.setStartX(i);
            markX.setEndX(i);
            markX.setStartY(-3);
            markX.setEndY(3);

            markY.setStartX(-3);
            markY.setEndX(3);
            markY.setStartY(i);
            markY.setEndY(i);

            markX.setStrokeWidth(1);

            if(i==50||i==100||i==150||i==200||i==250||i==300||i==350||i==400){
                markX.setStrokeWidth(2);
                markX.setStartY(-6);
                markX.setEndY(10);
                markY.setStrokeWidth(2);
                markY.setStartX(-6);
                markY.setEndX(10);
            }

            this.getChildren().addAll(markX,markY);
        }
    }

    private void addBorder(){
        //Line top = new Line()
    }

    public ObservableList<FccContainer> getFccContainers(ObservableList<FccContainer> listFccContainers, Container0 container0) {
        ObservableList<FccContainer> tempListFccContainers = FXCollections.observableArrayList();
        tempListFccContainers.addAll(listFccContainers);

        for (AnalogyContainer analogyContainer : container0.getAnalogyContainers()) {
            tempListFccContainers.addAll(analogyContainer.getFccContainers());
            for (Container2 container3 : analogyContainer.getMultiContainers()) {
                if (container3.isAntecedentDeployed()) {
                    tempListFccContainers.addAll(getFccContainers(tempListFccContainers, container3.getAntecedentsLevelContainer()));
                }
                if (container3.isDescendantDeployed()) {
                    tempListFccContainers.addAll(getFccContainers(tempListFccContainers, container3.getDescendantsLevelContainers()));
                }
            }
        }
        return tempListFccContainers;
    }
    
    public ObservableList<AnalogyContainer> getAnalogyContainers(ObservableList<AnalogyContainer> listAnalogyContainers, Container0 container0){
        ObservableList<AnalogyContainer> tempListAnalogyContainers = FXCollections.observableArrayList();
        
        for(AnalogyContainer analogyContainer : container0.getAnalogyContainers()){
            tempListAnalogyContainers.add(analogyContainer);
            
            for(Container2 container3 : analogyContainer.getMultiContainers()){
                if(container3.isAntecedentDeployed()){
                    tempListAnalogyContainers.addAll(getAnalogyContainers(tempListAnalogyContainers, container3.getAntecedentsLevelContainer()));
                }
                
                // TODO
                // if deductions are deployed...
                
            }
        }
        return tempListAnalogyContainers;
    }

    public ObservableList<Container0> getLevelContainers(ObservableList<Container0> listContainer0, Container0 container0) {
        ObservableList<Container0> tempListContainer0s = FXCollections.observableArrayList();

        tempListContainer0s.addAll(listContainer0);
        for (AnalogyContainer analogyContainer : container0.getAnalogyContainers()) {

            for (Container2 container3 : analogyContainer.getMultiContainers()) {
                // for the Antecedent level and Descendant level
                tempListContainer0s.addAll(container3.getAntecedentsLevelContainer());

                tempListContainer0s.addAll(container3.getDescendantsLevelContainers());

            }
        }

        return tempListContainer0s;
    }

}
