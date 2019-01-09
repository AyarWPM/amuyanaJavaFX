package main.data.tod.containers;

import main.controllers.AppController;
import main.controllers.TodController;
import main.data.Fcc;
import main.tod.AnalogyContainer;
import main.tod.FccContainer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Container2 extends HBox {
    public static int currentAutoIncrement;
    private IntegerProperty idContainer2;
    private Fcc fcc;
    private Container1 container1;

    // non-fundamental
    private Container0 leftContainer0;
    private Container0 rightContainer0;
    private FccContainer fccContainer;

    private static AppController appController;
    private static TodController todController;

    //private FccContainer fccContainer;

    private boolean antecedentDeployed, descendantDeployed;

    private VBox positionAntecedents, positionFccContainer, positionDescendants;

    ObservableList<Container0> permanentListContainer0s;

    public Container2(int idContainer2, Fcc fcc, Container1 container1) {
        this.idContainer2 = new SimpleIntegerProperty(idContainer2);
        this.fcc = fcc;
        this.container1 = container1;

        this.leftContainer0 = new Container0(0);
        this.rightContainer0 = new Container0(0);
        this.fccContainer = new FccContainer(fcc);

        this.getChildren().add(leftContainer0);
        this.getChildren().add(fccContainer);
        this.getChildren().add(rightContainer0);
    }

    /** This is the central container.
     * It has 3 columns. 1st is for all the
     * previous dynamisms. 2dn is for
     * the central Fcc, 3d is for all other Fccs that are developments
     * of the orientations' dynamisms
     * However there are 5 positions, each holding not Fcc but ClassContainer
     * - 1st is the 1st column which is a VBox
     * - 2nd is the 2nd column which is also a VBox
     * - 3d is first position of VBox occupying the 3d column
     * - 4th is center
     * - 5th is bottom
     *
     * @param fcc
    */
    public Container2(Fcc fcc) {
        this.fcc = fcc;

        positionAntecedents = new VBox();
        positionAntecedents.setAlignment(Pos.CENTER_RIGHT);
        
        positionFccContainer = new VBox();
        positionFccContainer.setAlignment(Pos.CENTER);
        positionFccContainer.setPickOnBounds(false);
        positionFccContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        });
        
        positionDescendants = new VBox();
        positionDescendants.setAlignment(Pos.CENTER_LEFT);
        setStyle();
        
    }

    public static void setControllers(AppController appController, TodController todController) {
        Container2.appController = appController;
        Container2.todController = todController;
    }
    //Metodos atributo: idContainer2
    public int getIdContainer2() {
        return idContainer2.get();
    }
    public void setIdContainer2(int idContainer2) {
        this.idContainer2 = new SimpleIntegerProperty(idContainer2);
    }
    public IntegerProperty idContainer2Property() {
        return idContainer2;
    }

    public Fcc getFcc() {
        return fcc;
    }
    public void setFcc(Fcc fcc) {
        this.fcc = fcc;
    }

    public Container1 getContainer1() {
        return container1;
    }

    public void setContainer1(Container1 container1) {
        this.container1 = container1;
    }

    public int saveData(Connection connection){
        String sql = "INSERT INTO amuyana.tbl_container_2 (id_container_2, id_fcc, id_container_1) "
                + "VALUES (?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, this.getIdContainer2());
            statement.setInt(2, this.fcc.getIdFcc());
            statement.setInt(3, this.container1.getIdContainer1());
            int result = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                Container2.currentAutoIncrement = rs.getInt(1);
            }
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(Fcc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    // Maybe not going to be used...
    public int updateData(Connection connection){
        String sql = "UPDATE amuyana.tbl_container_2 SET  _blank_ = ? WHERE id_container_2 = ?";
        try {
            PreparedStatement instruccion =
                    connection.prepareStatement(sql);
            instruccion.setInt(1, idContainer2.get()); // change these
            instruccion.setInt(2, idContainer2.get()); // change these

            return instruccion.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteData(Connection connection){
        try {
            PreparedStatement instruccion = connection.prepareStatement(
                    "DELETE FROM amuyana.tbl_container_2 "+
                            "WHERE id_container_2 = ?"
            );
            instruccion.setInt(1, this.idContainer2.get());
            return instruccion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void loadList(Connection connection,
                                ObservableList<Container2> container2s,
                                ObservableList<Fcc> fccs,
                                ObservableList<Container1> container1s){
        try {
            Statement instruction = connection.createStatement();
            ResultSet result = instruction.executeQuery(
                    "SELECT id_container_2, id_fcc, id_container_1 "
                            + "FROM amuyana.tbl_container_2"
            );

            while(result.next()){
                for (Fcc fcc : fccs) {
                    if (fcc.getIdFcc() == result.getInt("id_fcc")) {
                        for (Container1 container1 : container1s) {
                            if (container1.getIdContainer1() == result.getInt("id_container_1")) {
                                container2s.add(
                                        new Container2(result.getInt("id_container_2"),fcc,container1)
                                );
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void setStyle(){
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, new CornerRadii(10), new BorderWidths(2))));
        this.setSpacing(40);
        //setStyle("-fx-background-color:transparent;");
        //setBackground(Background.EMPTY);

        this.setPickOnBounds(false);

        //System.out.println(pickOnBoundsProperty());
        //this.setPadding(new Insets(0,20,0,20));
        //this.setMargin(positionFccContainer,new Insets(0,20,0,20));
        //this.setAlignment(Pos.BOTTOM_RIGHT);
    }


    public void deploy(){
        FccContainer fccContainer = new FccContainer(this.fcc);

        this.positionFccContainer.getChildren().add(fccContainer);

        this.getChildren().addAll(this.positionAntecedents,this.positionFccContainer,this.positionDescendants);

        fccContainer.deploy();
    }


    public void deployAntecedents() {
        //Container0 antecedentLevel =
        //        new Container0(appController.getDataInterface().getListAnalogyForAntecedent(getFccContainer().getInclusion()));
        Container0 antecedentLevel = new Container0(0); // TODO

        antecedentLevel.deploy();

        getPositionAntecedents().getChildren().add(antecedentLevel);
        setAntecedentDeployed(true);
        
        todController.startExecutor();
        todController.getExecutorService().execute(todController.getTaskSetBracketAndKnobs(antecedentLevel));
        todController.getExecutorService().execute(todController.getTaskSetTiers(antecedentLevel));
        
        boolean key=true;
        while(key){
            todController.getExecutorService().execute(todController.getTaskPositionMultiContainers(antecedentLevel));
            todController.getExecutorService().execute(todController.getTaskPositionAnalogyContainers(antecedentLevel));
            if(antecedentLevel==todController.getTod().getContainer0()){
                key=false;
            } else if (antecedentLevel!=todController.getTod().getContainer0()){
                antecedentLevel=antecedentLevel.getParentContainer3().getAnalogyContainerParent().getLevelContainerParent();
            }
        }
        
        todController.getExecutorService().execute(todController.getTaskSetKnobsPositions());
        todController.getExecutorService().execute(todController.getTaskSetBorderAnalogy());
        
        todController.endExecutor();
    }

    public void deployDescendants(){
        Container0 descendantLevel = new Container0(0); //TODO
                //appController.getDataInterface().getListAnalogyForDescendant(getFccContainer().getInclusion()));

        descendantLevel.deploy();

        getPositionDescendants().getChildren().add(descendantLevel);
        setDescendantDeployed(true);
        
        todController.startExecutor();
        todController.getExecutorService().execute(todController.getTaskSetBracketAndKnobs(descendantLevel));
        todController.getExecutorService().execute(todController.getTaskSetTiers(descendantLevel));
        
        boolean key=true;
        while(key){
            todController.getExecutorService().execute(todController.getTaskPositionMultiContainers(descendantLevel));
            todController.getExecutorService().execute(todController.getTaskPositionAnalogyContainers(descendantLevel));
            if(descendantLevel==todController.getTod().getContainer0()){
                key=false;
            } else if (descendantLevel!=todController.getTod().getContainer0()){
                descendantLevel=descendantLevel.getParentContainer3().getAnalogyContainerParent().getLevelContainerParent();
            }
        }
        
        todController.getExecutorService().execute(todController.getTaskSetKnobsPositions());
        todController.getExecutorService().execute(todController.getTaskSetBorderAnalogy());
        
        todController.endExecutor();
    }
    
    @Override
    public String toString(){
        return "[\"" + getFccContainer().getFcc().toString() + "\"" + " multiContainer]";
    }

    @Override
    public void toBack() {
        super.toBack();
    }

    @Override
    public void toFront() {
        super.toFront();
        //new Thread(todController.getTaskPositionMultiContainers(todController.getLevelContainerOf(getFccContainer()))).start();
    }

    /**
     * Get the central fccContainer, i.e. the one uniquely associated to the multiContainer
     * @return The fccContainer in the center of the AnalogyContainer
     */
    public FccContainer getFccContainer(){
        return (FccContainer)positionFccContainer.getChildren().get(0);
    }

    public VBox getPositionAntecedents() {
        return positionAntecedents;
    }

    public VBox getPositionDescendants() {
        return positionDescendants;
    }

    public void setPositionAntecedents(VBox positionAntecedents) {
        this.positionAntecedents = positionAntecedents;
    }

    public AnalogyContainer getAnalogyContainerParent() {
        return (AnalogyContainer)getParent();
    }

    public Container0 getAntecedentsLevelContainer() {
        return (Container0) positionAntecedents.getChildren().get(0);
    }

    public Container0 getDescendantsLevelContainers() {
        return (Container0)positionDescendants.getChildren().get(0);
    }

    public boolean isAntecedentDeployed(){
        return antecedentDeployed;
    }

    public boolean isDescendantDeployed() {
        return descendantDeployed;
    }

    public void setAntecedentDeployed(boolean antecedentDeployed) {
        this.antecedentDeployed = antecedentDeployed;
    }

    public void setDescendantDeployed(boolean descendantDeployed) {
        this.descendantDeployed=descendantDeployed;
    }

}
