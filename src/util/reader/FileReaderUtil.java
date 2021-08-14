package util.reader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import engine.Simulation;
import exceptions.InvalidFileInformationException;
import map.Map;
import train.Movement;
import util.Constants;
import util.serialization.SerializationUtilClass;

public class FileReaderUtil {
    
    public static final String configurationFilePath = "D:\\JAVA\\PROJEKTNI ZADATAK\\ProjektniZadatak2021\\ProjektniZadatak\\config\\ConfigurationFile.txt";
    public static final String appPath = "D:\\JAVA\\PROJEKTNI ZADATAK\\ProjektniZadatak2021\\ProjektniZadatak"; // koristimo ukoliko u konfiguracionom fajlu nismo napisali sve putanje
    private static String trainDirectoryPath = null;
    private static String movementDirectoryPath = null;

    private static Handler handler;
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    static{
        try {
            handler = new FileHandler(Simulation.loggerDirectoryPath + File.separator + "reader.log");
            Logger.getLogger(FileReaderUtil.class.getName()).addHandler(handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        readFoldersPaths();
        SerializationUtilClass.setPath(movementDirectoryPath);
        SerializationUtilClass.setLock(lock);
    }
    

    public FileReaderUtil() { 
       readRoadInfo();
    }

    public String getTrainDirectoryPath(){
        return trainDirectoryPath;
    }

    public String getMovementDirectoryPath(){
        return movementDirectoryPath;
    }

    // KONFIGURACIONI FAJL - CITANJE INFORMACIJA O PUTEVIMA
    public void readRoadInfo(){
        try(Stream<String> stream = Files.lines(Paths.get(configurationFilePath))){

            List<String> roadInfoLines = stream.limit(3).collect(Collectors.toList());
            if(roadInfoLines.size() != 3){
                throw new InvalidFileInformationException("Nisu zapisane informacije za sve puteve.");
            }

            for(String line : roadInfoLines){
                String[] seg = line.split(":");

                try{
                    if(seg.length != 3){
                        throw new InvalidFileInformationException("Nema dovoljno informacija za dati put.");
                    }

                    int roadIndex = Integer.parseInt(seg[0]);
                    if(roadIndex <= 0 || roadIndex >= 4){
                        throw new InvalidFileInformationException("Unesen je nevalidan ideks puta.");
                    }

                    int roadMaxSpeed = Integer.parseInt(seg[1]);
                    int roadNumberOfVehicles = Integer.parseInt(seg[2]);

                    if(roadNumberOfVehicles < 0){
                        throw new InvalidFileInformationException("Broj vozila na putu ne moze da bude negativan broj.");
                    }

                    if(roadMaxSpeed >= Constants.MIN_SPEED){
                        Map.getRoad(roadIndex).setMaxSpeed(roadMaxSpeed);
                    }
                    Map.getRoad(roadIndex).setNumberOfVehicles(roadNumberOfVehicles);

                } catch (Exception ex){
                    Logger.getLogger(FileReaderUtil.class.getName()).log(Level.WARNING, ex.getMessage(), ex);
                }
            }
        } catch (Exception ex){
            Logger.getLogger(FileReaderUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    // KONFIGURACIONI FAJL - CITANJE PUTANJA (FOLDERA)  *citaju se samo jednom, pri ucitavanju klase*
    private static void readFoldersPaths(){
        try (Stream<String> stream = Files.lines(Paths.get(configurationFilePath))) {
            List<String> paths = stream.skip(3).collect(Collectors.toList());
            for(String path : paths){
                if(path.contains("kretanja")){
                    movementDirectoryPath = getPath(path);
                }

                if(path.contains("vozovi")){
                    trainDirectoryPath = getPath(path);
                }
            }

            if(movementDirectoryPath == null){
                movementDirectoryPath = appPath + File.separator + "kretanja";
                new File(movementDirectoryPath).mkdir();
            }

            if(trainDirectoryPath == null){
                trainDirectoryPath = appPath + File.separator + "vozovi";
                new File(trainDirectoryPath).mkdir();
            }

        } catch (Exception ex) {
            Logger.getLogger(FileReaderUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private static String getPath(String directoryPath) {
        try{
            Path path = Paths.get(directoryPath);
            if (Files.isDirectory(path)){
                return directoryPath;
            } else {
                new File(directoryPath).mkdir();
                return directoryPath;
            }
        } catch (Exception ex){
            Logger.getLogger(FileReaderUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    // CITANJE SERJIALIZOVANIH KRETANJA
    public HashMap<String, String> readMovementsDirectory(){
        lock.readLock().lock();
        File directory = new File(movementDirectoryPath);
        String[] serializedFiles = directory.list((dir, name) -> name.toLowerCase().endsWith(SerializationUtilClass.EXTENSION));
        HashMap<String, String> movements = null;
        if(serializedFiles.length > 0){
            movements = new HashMap<>();
            for(String file : serializedFiles){
                Movement m = SerializationUtilClass.deserializeMovement(file);
                if(m != null){
                    movements.put(file, m.toString());
                }
            }
        }
        lock.readLock().unlock();
        return movements;
    }
}
