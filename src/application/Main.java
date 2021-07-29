package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage){
        MainWindowViewController mwv = new MainWindowViewController(primaryStage);
        mwv.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
