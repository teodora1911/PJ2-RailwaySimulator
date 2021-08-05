package util.watchers;

import java.io.IOException;
import java.nio.file.Path;

import engine.Simulation;

public class ConfigurationFileWatcher extends FileWatcher {

    public static final String configFileDirectory = "D:\\JAVA\\PROJEKTNI ZADATAK\\ProjektniZadatak2021\\ProjektniZadatak\\config\\";
    
    public ConfigurationFileWatcher() throws IOException {
        super(configFileDirectory);
    }

    @Override
    public void modifyAction(Path filename) {
        Simulation.fileReader.readRoadInfo();
    }

    @Override
    public void creationAction(Path filename) {
        /*
         * Ne implementiramo ovu metodu 
         * jer pratimo samo modifikaciju konfigurationog fajla
         * 
         */
    }
}
