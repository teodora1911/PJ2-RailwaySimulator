package engine;

import java.io.IOException;

import application.MainWindowViewController;
import util.reader.FileReaderUtil;
import util.watchers.ConfigurationFileWatcher;

public class Simulation {
    
    public static MainWindowViewController mwvc;
    public static FileReaderUtil fileReader;

    public static void setMainWindowViewController(MainWindowViewController mwvc){
        Simulation.mwvc = mwvc;
    }

    public Simulation(){
        fileReader = new FileReaderUtil();
    }

    public void start(){
        try{
            new Thread(new ConfigurationFileWatcher()).start();
            //new Thread(new TrainFileWatcher()).start();
        } catch (IOException ex){
            System.out.println("File Watcher failed!");
        }
        VehicleCreationThread thread = new VehicleCreationThread();
        thread.setDaemon(true);
        thread.start();
    }

   // public static void main(String[] args) {
   //     String[] config1 = {"UL", "UL", "SV", "PL", "ZV", "PL", "RV", "RV", "UL"}; // true
   //     String[] config2 = {"ML", "XV", "XV", "ML", "UL", "XV"}; // true
   //     String[] config3 = {"XV", "XV", "XV"}; // false
   //     String[] config4 = {"UL", "ZV", "LV", "PL", "TV", "UL", "PL", "RV"}; // false
   //     String[] config5 = {"TL", "UL", "TV", "TV", "TL", "UL"}; // true
   //     System.out.println(FileReaderUtil.validTrainConfiguration(config1));
   //     System.out.println(FileReaderUtil.validTrainConfiguration(config2));
   //     System.out.println(FileReaderUtil.validTrainConfiguration(config3));
   //     System.out.println(FileReaderUtil.validTrainConfiguration(config4));
   //     System.out.println(FileReaderUtil.validTrainConfiguration(config5));
   // }
}
