package application;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HistoryOfMovementViewController {
    
    private static Stage stage = new Stage();

    private static Scene scene;

    private static BorderPane root = new BorderPane(new TextField("HistoryOfMovement"));

    static {
        scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.setTitle("HISTORY OF TRAINS MOVEMENT");
    }

    private HistoryOfMovementViewController(){ }

    public static void showHistory(){
        stage.show();
    }
}
