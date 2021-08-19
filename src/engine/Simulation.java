package engine;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.MainWindowViewController;
import util.reader.FileReaderUtil;
import util.watchers.ConfigurationFileWatcher;
import util.watchers.TrainFileWatcher;

public class Simulation {

    public static final String loggerDirectoryPath = "D:\\JAVA\\PROJEKTNI ZADATAK\\ProjektniZadatak2021\\ProjektniZadatak\\loggeri";
    
    public static MainWindowViewController mwvc;
    public static FileReaderUtil fileReader;
    public static TrainCreationClass trainCreation;

    private static Handler handler;

    static {
        try{
            File loggerDirectory = new File(loggerDirectoryPath);
            if(!loggerDirectory.exists()){
                loggerDirectory.mkdir();
            }
        } catch (Exception ex){
            System.out.println("Logger direktorijum se ne moze da kreira.");
        }
        try {
            handler = new FileHandler(loggerDirectoryPath + File.separator + "simulation.log", true);
            Logger.getLogger(Simulation.class.getName()).addHandler(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        
        VehicleCreationThread thread = new VehicleCreationThread();
        thread.setDaemon(true);
        thread.start();
    }
}
