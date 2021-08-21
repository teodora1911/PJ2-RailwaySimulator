package engine;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.MainWindowViewController;
import util.LoggerUtil;
import util.reader.FileReaderUtil;
import util.watchers.ConfigurationFileWatcher;
import util.watchers.TrainFileWatcher;

public class Simulation {
    
    public static MainWindowViewController mwvc;
    public static FileReaderUtil fileReader;
    public static TrainCreationClass trainCreation;

    private static FileHandler handler;
    private static Logger logger = Logger.getLogger(Simulation.class.getName());

    static {
        LoggerUtil.setLogger(logger, handler, "simulation.log");
    }

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
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        VehicleCreationThread thread = new VehicleCreationThread();
        thread.setDaemon(true);
        thread.start();
    }
}
