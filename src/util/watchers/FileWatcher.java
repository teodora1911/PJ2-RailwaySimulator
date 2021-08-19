package util.watchers;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import engine.Simulation;

public abstract class FileWatcher implements Runnable {
    
    protected Path directory;
    private WatchService watcher;
    public static final String EXTENSION = ".txt";

    protected String lastAddedTrainFile = "";

    protected static Handler handler;

    static {
        try {
            handler = new FileHandler(Simulation.loggerDirectoryPath + File.separator + "watcher.log", true);
            Logger.getLogger(FileWatcher.class.getName()).addHandler(handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public FileWatcher(String directory) throws IOException {
        this.directory = Paths.get(directory);
        this.watcher = FileSystems.getDefault().newWatchService();
        this.directory.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
    }

    public abstract void modifyAction(Path filename);
    public abstract void creationAction(Path filename);

    @Override
    public void run(){
        while(true){

            WatchKey key;

            try {
                key = watcher.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(FileWatcher.class.getName()).log(Level.SEVERE, "Watcher cannot take watch key!", ex);
                return;
            }

            for(WatchEvent<?> event : key.pollEvents()){
                WatchEvent.Kind<?> kind = event.kind();
                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>)event;
                Path filename = ev.context();

                if(filename.toString().trim().endsWith(EXTENSION)){
                    if(kind.equals(ENTRY_MODIFY)){
                        modifyAction(filename);
                    } else {
                        creationAction(filename);
                    }
                }
            }

            boolean valid = key.reset();
            if(!valid){
                break;
            }
        }
    }

    protected void sleep(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(FileWatcher.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
