package engine;

import java.io.IOException;

import application.MainWindowViewController;
import util.reader.FileReaderUtil;
import util.watchers.ConfigurationFileWatcher;
import util.watchers.TrainFileWatcher;

public class Simulation {
    
    public static MainWindowViewController mwvc;
    public static FileReaderUtil fileReader;
    public static TrainCreationClass trainCreation;

    public static void setMainWindowViewController(MainWindowViewController mwvc){
        Simulation.mwvc = mwvc;
    }

    public Simulation(){
        fileReader = new FileReaderUtil();
        trainCreation = new TrainCreationClass(fileReader.getTrainDirectoryPath());
    }

    public void start(){
        try{
            new Thread(new ConfigurationFileWatcher()).start();
            new Thread(new TrainFileWatcher(fileReader.getTrainDirectoryPath())).start();
        } catch (IOException ex){
            System.out.println("File Watcher failed!");
        }
        VehicleCreationThread thread = new VehicleCreationThread();
        thread.setDaemon(true);
        thread.start();
    }
}
