package util.watchers;

import java.io.IOException;
import java.nio.file.Path;

import engine.Simulation;

public class TrainFileWatcher extends FileWatcher {
    
    public TrainFileWatcher(String directory) throws IOException {
        super(directory);
    }

    @Override
    public void modifyAction(Path filename) {
        /*
         * Ne implementiramo ovu metodu 
         * jer pratimo samo kreiranje novih fajlova u folderu vozovi
         */
    }

    @Override
    public void creationAction(Path filename) {
        if(!lastAddedTrainFile.equals(filename.toString())){ // FileWatcherExplanation
            lastAddedTrainFile = filename.toString();
            this.sleep();
            System.out.println("Naredba da se kreira novi voz ...");
            Simulation.trainCreation.createNewTrain(this.directory.resolve(filename));
        }
    }
}
