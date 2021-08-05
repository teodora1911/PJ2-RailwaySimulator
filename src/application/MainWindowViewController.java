package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar.ButtonData;
//import javafx.scene.layout.Background;
//import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
//import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import map.Field;
import map.FieldType;
import map.Map;
//import javafx.scene.paint.Color;
import util.Constants;

import static util.Constants.MAP_SIZE;

import element.ElementColor;
import engine.Simulation;;

public class MainWindowViewController {

    private Stage stage;
    private Scene scene;

    private BorderPane pane = new BorderPane();

    private ButtonBar buttonBar = new ButtonBar();

    private GridPane gridMap = new GridPane();

    private Label[][] gridLabels = new Label[MAP_SIZE][MAP_SIZE];

    private Button movementButton = new Button("Istorija kretanja");

    private Button startButton = new Button("Pokreni simulaciju");

    private Button cancelButton = new Button("Zaustavi simulaciju");

    public MainWindowViewController(Stage stage){
        this.stage = stage;
        this.scene = new Scene(pane, 800, 800);
        Simulation.setMainWindowViewController(this);
        setButtonPane();
        setGridPane();
        setBorderPane();
        this.stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            @Override
            public void handle(WindowEvent event){
                Platform.exit();
                System.exit(1);
            }
        });
    }

    private void setButtonPane(){
        System.out.println("... setting button pane...");
        startButton.setFont(new Font("Georgia", 16));
        startButton.setPadding(new Insets(10, 10, 10, 10));
        startButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                System.out.println("Starting simulation.");
                Simulation simulation = new Simulation();
                simulation.start();
                startButton.setDisable(true);
            }
        });

        movementButton.setFont(new Font("Georgia", 16));
        movementButton.setPadding(new Insets(10, 10, 10, 10));
        movementButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                HistoryOfMovementViewController history = new HistoryOfMovementViewController();
                history.showHistory();
            }
        });

        cancelButton.setFont(new Font("Georgia", 16));
        cancelButton.setPadding(new Insets(10, 10, 10, 10));
        cancelButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                System.exit(1);
            }
        });

        buttonBar.setPadding(new Insets(10));
        ButtonBar.setButtonData(startButton, ButtonData.LEFT);
        ButtonBar.setButtonData(cancelButton, ButtonData.LEFT);
        ButtonBar.setButtonData(movementButton, ButtonData.RIGHT);
        buttonBar.getButtons().addAll(startButton, cancelButton, movementButton);
        System.out.println("... button pane done ...");
    }

    private void setGridPane(){
        System.out.println("... setting grid pane...");
        gridMap.setPadding(new Insets(10, 10, 10, 10));
        gridMap.setAlignment(Pos.CENTER);
        gridMap.getRowConstraints().clear();
        gridMap.getColumnConstraints().clear();

        for(int i = 0; i < MAP_SIZE; ++i){
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setMinWidth(Constants.MIN_SIZE_LABEL);
            columnConstraints.setPrefWidth(Constants.PREF_SIZE_LABEL);
            columnConstraints.setHalignment(HPos.CENTER);
            gridMap.getColumnConstraints().add(columnConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(Constants.MIN_SIZE_LABEL);
            rowConstraints.setPrefHeight(Constants.PREF_SIZE_LABEL);
            rowConstraints.setValignment(VPos.CENTER);
            gridMap.getRowConstraints().add(rowConstraints);
        }
        
        for(int i = 0; i < MAP_SIZE; ++i){
           for(int j  = 0; j < MAP_SIZE; ++j){
               gridLabels[i][j] = new Label("");
               gridLabels[i][j].setPrefSize(Constants.PREF_SIZE_LABEL, Constants.PREF_SIZE_LABEL);
               gridLabels[i][j].setMinSize(Constants.MIN_SIZE_LABEL, Constants.MIN_SIZE_LABEL);
               gridLabels[i][j].setStyle("-fx-background-color: white; -fx-text-fill: black;");
               gridMap.add(gridLabels[i][j], i, j);
           }
        }

       updateMap();
       gridMap.setGridLinesVisible(true);
       System.out.println("... grid pane done ...");
    }

    private void setBorderPane(){
        System.out.println("... setting border pane...");
        BorderPane.setAlignment(gridMap, Pos.CENTER);
        BorderPane.setAlignment(buttonBar, Pos.BOTTOM_CENTER);
        pane.setCenter(gridMap);
        pane.setBottom(buttonBar);
        System.out.println("... border pane done ...");
    }

    public void show(){
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Simulacija zeljeznickog saobracaja");
        stage.show();
    }

    // poziva se kada bilo koje vozilo i/ili kompozicija promijene svoje koordinate na mapi
    public void makeChange(){
       Platform.runLater(new Runnable(){
           @Override
           public void run(){
            //System.out.println("... izmjena mape ...");
            updateMap();
           }
       });
    }

    private void updateMap(){
        Field[][] map = Map.getMap();

        for(int i = 0; i < MAP_SIZE; ++i){
            for(int j = 0; j < MAP_SIZE; ++j){
                if(map[i][j] != null){
                    updateMapCell(map[i][j]);
                }
            }
        }
    }

    private void updateMapCell(Field field){ // field nije nikad null
        if(field.getStation() != null){
            gridLabels[field.getX()][field.getY()].setStyle(
                "-fx-background-color: rgb(" +
                 Constants.RAILWAY_STATION_COLOR.r + "," +
                  Constants.RAILWAY_STATION_COLOR.g + "," +
                   Constants.RAILWAY_STATION_COLOR.b + ");"
                );
            gridLabels[field.getX()][field.getY()].setText("");
        } else {
            if(field.getElement() != null){
                ElementColor color = field.getElement().getMapColor();
                gridLabels[field.getX()][field.getY()].setStyle(
                "-fx-background-color: rgb(" +
                 color.r + "," +
                  color.g + "," +
                   color.b + "); "
                );
                gridLabels[field.getX()][field.getY()].setText(field.getElement().getLabel());
            } else if (field.isUnderVoltage()){
                gridLabels[field.getX()][field.getY()].setStyle(
                "-fx-background-color: rgb(" +
                 240 + "," +
                  255 + "," +
                   0 + ");"
                );
                gridLabels[field.getX()][field.getY()].setText("");
            } else {
                FieldType type = field.getFieldType();
                
                switch (type) {
                    case ROAD:
                        gridLabels[field.getX()][field.getY()].setStyle(
                            "-fx-background-color: rgb(" +
                             Constants.ROAD_COLOR.r + "," +
                              Constants.ROAD_COLOR.g + "," +
                               Constants.ROAD_COLOR.b + ");"
                            );
                        gridLabels[field.getX()][field.getY()].setText("");
                        break;
                
                    case RAILWAY:
                        gridLabels[field.getX()][field.getY()].setStyle(
                            "-fx-background-color: rgb(" +
                             Constants.RAILWAY_COLOR.r + "," +
                              Constants.RAILWAY_COLOR.g + "," +
                               Constants.RAILWAY_COLOR.b + ");"
                            );
                        gridLabels[field.getX()][field.getY()].setText("");
                        break;

                    case CROSSING:
                        gridLabels[field.getX()][field.getY()].setStyle(
                            "-fx-background-color: rgb(" +
                             Constants.CROSSING_COLOR.r + "," +
                              Constants.CROSSING_COLOR.g + "," +
                               Constants.CROSSING_COLOR.b + ");"
                            );
                        gridLabels[field.getX()][field.getY()].setText("");
                        break;
                }
            }
        }
    }
}