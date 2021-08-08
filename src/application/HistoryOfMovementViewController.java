package application;

import java.util.HashMap;
import java.util.Map;

import engine.Simulation;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class HistoryOfMovementViewController {
    
    private  Stage stage = new Stage();

    private  Scene scene;

    private TableView<Map.Entry<String, String>> table;

    public HistoryOfMovementViewController(){
        setTableView();
     }

    private void setTableView(){
        HashMap<String, String> map = Simulation.fileReader.readMovementsDirectory();

        TableColumn<Map.Entry<String, String>, String> column1 = new TableColumn<>("Ime fajla");
        column1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>(){
            
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> f){
                return new SimpleStringProperty(f.getValue().getKey());
            }
        });
        
        TableColumn<Map.Entry<String, String>, String> column2 = new TableColumn<>("Opis");
        column2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String,String>,String>, ObservableValue<String>>(){
            
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> f){
                return new SimpleStringProperty(f.getValue().getValue());
            }
        });


        ObservableList<Map.Entry<String, String>> items;
        if(map != null && !map.isEmpty()){
            items = FXCollections.observableArrayList(map.entrySet());
        } else {
            items = FXCollections.observableArrayList();
        }
        table = new TableView<>(items);
        table.setEditable(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().setAll(column1, column2);
    }

    public void showHistory(){
        scene = new Scene(table, 500, 500);
        stage.setScene(scene);
        stage.setTitle("Zavrsene dionice");
        stage.show();
    }
}
