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
        } catch (IOException ex){
            System.out.println("File Watcher failed!");
        }
        CreationThread thread = new CreationThread();
        thread.setDaemon(true);
        thread.start();
    }
}
