package util.watchers;

import java.io.IOException;
import java.nio.file.Path;

public class TrainFileWatcher extends FileWatcher {
    
    public TrainFileWatcher(Path directory) throws IOException {
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
        // pozivamo metodu u FileReader klasi
        // da parsira fajl i kreira novi voz
        // i postavi u red cekanja na pocetnoj stanici
    }
}
