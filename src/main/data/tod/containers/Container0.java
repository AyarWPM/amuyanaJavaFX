package main.data.tod.containers;

import main.controllers.AppController;
import main.controllers.TodController;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.tod.AnalogyContainer;
import main.tod.FccContainer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;

public class Container0 extends Group {
    private IntegerProperty idContainer0;
    public static int currentAutoIncrement;

    private static TodController todController;
    private static AppController appController;
    private double scale = 1;

    public Container0(int idContainer0) {
        this.idContainer0 = new SimpleIntegerProperty(idContainer0);
    }

    //Metodos atributo: idContainer0
    public int getIdContainer0() {
        return idContainer0.get();
    }
    public void setIdContainer0(int idContainer0) {
        this.idContainer0 = new SimpleIntegerProperty(idContainer0);
    }
    public IntegerProperty idContainer0Property() {
        return idContainer0;
    }

    public ObservableList<Container1> getContainer1s() {
        return FXCollections.observableArrayList();
    }

    public int saveData(Connection connection){
        String sql = "INSERT INTO amuyana.tbl_container_0 (id_container_0) "
                + "VALUES (?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, this.getIdContainer0());

            int result = statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                Container0.currentAutoIncrement = rs.getInt(1);
            }

            return result;

        } catch (SQLException ex) {
            Logger.getLogger(Container0.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_container_0 SET _blank_ = ? WHERE id_container_0 = ?";
        try {
            PreparedStatement instruccion =
                    connection.prepareStatement(sql);
            instruccion.setInt(1, idContainer0.get()); // change these
            instruccion.setInt(2, idContainer0.get()); // change these

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "DELETE FROM amuyana.tbl_container_0 "+
                            "WHERE id_container_0 = ?"
            );
            instruccion.setInt(1, this.idContainer0.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void loadList(Connection connection,
                                ObservableList<Container0> container0s){
        try {

            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(
                    "SELECT id_container_0 "
                            + "FROM amuyana.tbl_container_0"
            );

            while(result.next()){
                container0s.add(new Container0(result.getInt("id_container_0")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setControllers(AppController appController, TodController todController) {
        Container0.appController = appController;
        Container0.todController = todController;
    }

    private void setStyle() {
        //this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        //this.setSpacing(20);
        //this.setAlignment(Pos.CENTER);
        //this.setPickOnBounds(true);
    }


    public void deploy() {

    }

    /**
     *        .-""""-.        .-""""-.
     *       /        \      /        \
     *      /_        _\    /_        _\
     *     // \      / \\  // \      / \\
     *     |\__\    /__/|  |\__\    /__/|
     *      \    ||    /    \    ||    /
     *       \        /      \        /
     *        \  __  /        \  __  /
     *         '.__.'          '.__.'
     *          |  |            |  |
     *          |  |            |  |
     */

    /**
     * This method is called by the user,
     * @param analogyContainer
     */
    public void addAnalogyContainer(AnalogyContainer analogyContainer) {
        getChildren().add(analogyContainer);
        // Update positions
        todController.updatePositions(this);
    }

    /**
     * This method is called by the user,
     * @param analogyContainer
     */
    public void removeAnalogyContainer(AnalogyContainer analogyContainer) {

    }

    /**
     * This method is called from getTaskPositionMultiContainers task in TodController
     */
    public void positionContainer3s() {
        for (AnalogyContainer analogyContainer : getAnalogyContainers()) {
            analogyContainer.positionMultiContainers();
        }
    }

    public void positionContainer2s() {
        for (AnalogyContainer analogyContainer : getAnalogyContainers()) {

            Point2D refAnalogyContainer = analogyContainer.localToScene(0,0);
            Point2D refPointLevelContainer = this.localToScene(0,0);

            double diffX = refAnalogyContainer.getX()-refPointLevelContainer.getX();
            double diffY = refAnalogyContainer.getY()-refPointLevelContainer.getY();


            double cumulativeHeight=0;
            for(AnalogyContainer previousAnalogyContainer :getAnalogyContainers()){
                if(previousAnalogyContainer.equals(analogyContainer)){
                    break;
                } else if (!previousAnalogyContainer.equals(analogyContainer)){
                    cumulativeHeight+= previousAnalogyContainer.prefHeight(-1);

                }
            }



            analogyContainer.setLayoutX(-diffX);
            analogyContainer.setLayoutY(cumulativeHeight);

            /* ONE APPROACH
            int index = getAnalogyContainers().indexOf(analogyContainer);
            double previousAnalogyContainerBoundaryMaxY = 0;

            if(index!=0){
                previousAnalogyContainerBoundaryMaxY = getAnalogyContainers().get(index-1).getBoundsInParent().getMaxY();
            }
            */

            /* SECOND APPROACH
            double heightForPrevious=0;

            for(AnalogyContainer previousAnalogyContainer:getAnalogyContainers()){
                if(previousAnalogyContainer.equals(analogyContainer)){
                    break;
                } else if (!previousAnalogyContainer.equals(analogyContainer)){
                    heightForPrevious+=previousAnalogyContainer.prefHeight(-1);
                }
            }
            */
        }
    }

    public ObservableList<AnalogyContainer> getAnalogyContainers() {
        ObservableList<AnalogyContainer> listAnalogies = FXCollections.observableArrayList();
        for (Node node : this.getChildren()) {
            listAnalogies.add((AnalogyContainer) node);
        }
        return listAnalogies;

    }

    public ObservableList<FccContainer> getFccContainers() {
        ObservableList<FccContainer> listFccContainers = FXCollections.observableArrayList();
        for (AnalogyContainer analogyContainer : getAnalogyContainers()) {
            listFccContainers.addAll(analogyContainer.getFccContainers());
        }
        return listFccContainers;
    }

    /**
     * It assumes this method is called for a levelContainer that has a multiContainerParent. This condition should
     * be tested before calling this method.
     * @return
     */
    public Container2 getParentContainer3() {
        return (Container2)getParent().getParent();
    }

    //public Container1 getParentContainer3() {
    //    return (Container1)getParent().getParent();
    //}

}