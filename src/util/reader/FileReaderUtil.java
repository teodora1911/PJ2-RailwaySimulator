package util.reader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import element.RailwayElement;
import locomotive.Engine;
import locomotive.LoadLocomotive;
import locomotive.PassengerLocomotive;
import locomotive.ShuntingLocomotive;
import locomotive.UniversalLocomotive;
import map.Coordinates;
import map.Map;
import railwaystation.RailwayStation;
import train.Movement;
import train.Train;
import util.Constants;
import util.serialization.SerializationUtilClass;
import wagon.BedWagon;
import wagon.LoadWagon;
import wagon.RestaurantWagon;
import wagon.SeatWagon;
import wagon.SleepWagon;
import wagon.SpecialWagon;

// MORA SE INICIJALIZOVATI PRIJE POKRETANJA SIMULACIJE
public class FileReaderUtil {
    
    public static final String configurationFilePath = "D:\\JAVA\\PROJEKTNI ZADATAK\\ProjektniZadatak2021\\ProjektniZadatak\\config\\ConfigurationFile.txt";
    public static final String appPath = "D:\\JAVA\\PROJEKTNI ZADATAK\\ProjektniZadatak2021\\ProjektniZadatak"; // koristimo ukoliko u konfiguracionom fajlu nismo napisali sve putanje
    private static String trainDirectoryPath = null;
    private static String movementDirectoryPath = null;

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    static{
        readFoldersPaths();
        SerializationUtilClass.setPath(movementDirectoryPath);
        SerializationUtilClass.setLock(lock);
    }
    

    public FileReaderUtil() { 
       readRoadInfo();
       System.out.println("Train Directory : " + trainDirectoryPath);
       System.out.println("Movement Directory : " + movementDirectoryPath);
       readTrainDirectory();
    }

    public String getTrainDirectoryPath(){
        return trainDirectoryPath;
    }

    public String getMovementDirectoryPath(){
        return movementDirectoryPath;
    }

    // KONFIGURACIONI FAJL - CITANJE INFORMACIJA O PUTEVIMA
    public void readRoadInfo(){
        System.out.println("Reading road info ....");
        try(Stream<String> stream = Files.lines(Paths.get(configurationFilePath))){

            List<String> roadInfoLines = stream.limit(3).collect(Collectors.toList());
            if(roadInfoLines.size() != 3){
                throw new IllegalArgumentException("Configuration file is not valid.");
            }

            for(String line : roadInfoLines){
                String[] seg = line.split(":");

                try{
                    if(seg.length != 3){
                        throw new IllegalArgumentException("Road info is not valid.");
                    }

                    int roadIndex = Integer.parseInt(seg[0]);
                    if(roadIndex <= 0 || roadIndex >= 4){
                        throw new IllegalArgumentException("Road info is not valid.");
                    }

                    int roadMaxSpeed = Integer.parseInt(seg[1]);
                    int roadNumberOfVehicles = Integer.parseInt(seg[2]);

                    if(roadNumberOfVehicles < 0){
                        throw new IllegalArgumentException("Road info is not valid.");
                    }

                    if(roadMaxSpeed >= Constants.MIN_SPEED){
                        Map.getRoad(roadIndex).setMaxSpeed(roadMaxSpeed);
                    }
                    Map.getRoad(roadIndex).setNumberOfVehicles(roadNumberOfVehicles);

                } catch (IllegalArgumentException ex){
                    Logger.getLogger(FileReaderUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
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


    // VOZOVI - VALIDACIJA KONFIGURACIJE VOZA
    public static boolean validTrainConfiguration(String[] railwayElements){

        // ako je duzina voza 0 ili prvi element voza nije lokomotiva, tada konfiguracija nije validna
        if(railwayElements.length < 1 || Arrays.stream(Constants.LocomotiveLabels.values()).noneMatch(e -> e.getValue().equals(railwayElements[0]))){
            return false;
        }

        // izdvajamo sve lokomotive koje su definisane, radimo distinct da ne bismo imali duplikata
        List<String> locomotives = Arrays.stream(railwayElements).filter(e -> e.endsWith("L")).distinct().collect(Collectors.toList());

        // provjeravamo da li se ti svi stringovi poklapaju sa oznakama za lokomotive
        for(String label : locomotives){
            // i ovdje, ako se oznake lokomotiva ne poklapaju sa definisanim oznakama, tada nije validna konfiguracija
            if(Arrays.stream(Constants.LocomotiveLabels.values()).noneMatch(e -> e.getValue().equals(label))){
                return false;
            }
        }

        // sve lokomotive su validne i izbacujemo iz liste univerzalnu lokomotivu
        locomotives.remove(Constants.LocomotiveLabels.UNIVERSAL.getValue());

        // ako je velicina liste veca od 1, to znaci da imamo vise razlicitih vrsta lokomotiva koje se ne mogu da sklapaju zajedno
        // pa konfiguracija voza u ovom slucaju nije validna
        if(locomotives.size() > 1){
            return false;
        }

        // ukoliko je velicina liste 0, onda su definisane samo univerzalne lokomotive


        // skoro analogno radimo i sa vagonima
        List<String> wagons = Arrays.stream(railwayElements).filter(e -> e.endsWith("V")).distinct().collect(Collectors.toList());
        for(String label : wagons){
            if(Arrays.stream(Constants.WagonLabels.values()).noneMatch(e -> e.getValue().equals(label))){
                return false;
            }
        }

        
        List<String> passengerWagonLabels = Constants.WagonLabels.getPassegnerWagonLabels();
        // ukoliko lista vagona sadrzi bilo koju oznaku za PUTNICKI vagon
        if(wagons.stream().anyMatch(e -> passengerWagonLabels.contains(e))){
            // i ako sadrzi labele ili za teretni ili za specijalni, konfiguracija nije validna
            if(wagons.contains(Constants.WagonLabels.LOAD.getValue()) || wagons.contains(Constants.WagonLabels.SPECIAL.getValue())){
                return false;
            }
        } else { // ako ne sadrzi labele za putnicke vagone
            // provjeravamo da li sadzi obe labele, i ako sadrzi onda nije validno
            if(wagons.contains(Constants.WagonLabels.LOAD.getValue()) && wagons.contains(Constants.WagonLabels.SPECIAL.getValue())){
                return false;
            }
        }

        // sada bi i konfiguracija i za vagone trebala da bude validna 
        // prvo provjeravamo da li je velicina liste lokomotiva prazna - sto znaci da imamo samo univerzalne
        // pa posto svaki tip vagona moze da ide sa univerzalnim, vracamo true
        if(locomotives.isEmpty() || wagons.isEmpty()){
            return true;
        }

        Constants.LocomotiveLabels type = Constants.LocomotiveLabels.fromString(locomotives.get(0));

        switch (type) {
            case PASSENGER:
                if(wagons.stream().anyMatch(e -> passengerWagonLabels.contains(e))){
                    return true;
                } else {
                    return false;
                }

            case LOAD:
                if(wagons.contains(Constants.WagonLabels.LOAD.getValue())){
                    return true;
                } else {
                    return false;
                }

            case SHUNTING:
                if(wagons.contains(Constants.WagonLabels.SPECIAL.getValue())){
                    return true;
                } else {
                    return false;
                }

            case UNIVERSAL:
                return true;
        }
        return false;
    }

    // VOZOVI - KREIRANJE KONFIGURACIJE VOZA - lokomotiva i vagona
    private ArrayList<RailwayElement> getConfiguration(String[] configurationString, int x, int y){
        Random rand = new Random();
        ArrayList<RailwayElement> configuration = new ArrayList<>();

        Engine engine = Engine.fromId((rand.nextInt() % 3) + 1);

        for(String element : configurationString){
            if(element.endsWith("L")){ // znaci da je lokomotiva
                Constants.LocomotiveLabels type = Constants.LocomotiveLabels.fromString(element);
                switch (type) {
                    case PASSENGER:
                        configuration.add(new PassengerLocomotive(x, y, rand.nextDouble(), engine));
                        break;
                
                    case LOAD:
                        configuration.add(new LoadLocomotive(x, y, rand.nextDouble(), engine));
                        break;

                    case SHUNTING:
                        configuration.add(new ShuntingLocomotive(x, y, rand.nextDouble(), engine));
                        break;

                    case UNIVERSAL:
                        configuration.add(new UniversalLocomotive(x, y, rand.nextDouble(), engine));
                        break;
                }
            } else { // inace je vagon
                Constants.WagonLabels type = Constants.WagonLabels.fromString(element);
                switch (type) {
                    case SEAT:
                        configuration.add(new SeatWagon(x, y, rand.nextInt(50) + 25, rand.nextInt(100)));
                        break;
                
                    case BED:
                        configuration.add(new BedWagon(x, y, rand.nextInt(50) + 25));
                        break;

                    case SLEEP:
                        configuration.add(new SleepWagon(x, y, rand.nextInt(50) + 25));
                        break;

                    case RESTAURANT:
                        configuration.add(new RestaurantWagon(x, y, rand.nextInt(50) + 25, "Restaurant" + rand.nextInt()));
                        break;

                    case LOAD:
                        configuration.add(new LoadWagon(x, y, rand.nextInt(50) + 25, rand.nextDouble()));
                        break;

                    case SPECIAL:
                        configuration.add(new SpecialWagon(x, y, rand.nextInt(50) + 25));
                        break;
                }
            }
        }

        return configuration;
    }

    /*
     * Jedan voz u txt fajlu je predstavljen u obliku : 
     *     ID (identifikator voza)
     *     BRZINA_KRETANJA
     *     LOKOMOTIVE-VAGONI
     *     POLAZNA_STANICA
     *     ODREDISNA_STANICA
     */
    public void createNewTrain(Path filename){
        try (Stream<String> stream = Files.lines(filename)){
            List<String> lines = stream.collect(Collectors.toList());
            if(lines.size() != 5){
                throw new IllegalArgumentException("Train configuration is not valid.");
            }

            int id = Integer.parseInt(lines.get(0));
            int speed = Integer.parseInt(lines.get(1));

            if(speed < Constants.MIN_SPEED){
                throw new IllegalArgumentException("Train configuration is not valid.");
            }

            if(!validTrainConfiguration(lines.get(2).split("-"))){
                throw new IllegalArgumentException("Train configuration is not valid.");
            }
            String src = lines.get(3);
            String dest = lines.get(4);

            if(!Constants.STATION_NAMES.contains(src) || !Constants.STATION_NAMES.contains(dest) || src.equals(dest)){
                throw new IllegalArgumentException("Train configuration is not valid.");
            }

            ArrayList<String> stationsRoute = RailwayStationsGraph.findRoute(src, dest);
            LinkedList<RailwayStation> stations = new LinkedList<>();
            for(String s : stationsRoute){
                RailwayStation station = Map.getStation(s);
                if(station == null){
                    throw new IllegalArgumentException("Problem u stanicama.");
                }
                stations.offer(station);
            }

            if(stations.isEmpty()){
                throw new IllegalArgumentException("Lista stanica je prazna.");
            }

            Coordinates coordinates = stations.peek().getCoordinates().stream().findAny().get(); // biramo bilo koju koorinatu na kojoj se nalazi prva stanica voza
            ArrayList<RailwayElement> configuration = getConfiguration(lines.get(2).split("-"), coordinates.getX(), coordinates.getY());

            Train newTrain = new Train(id, speed, configuration, stations);
            new Thread(newTrain).start();
        } catch (Exception ex) {
            Logger.getLogger(FileReaderUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            // ako dodje do bilo kakvog izuzetka, obrisati dati fajl
        }
    }

    // CITANJE VOZOVA CIJE SU KONFIGURACIJE VEC DEFINISANE U DIREKTORUJUMU VOZOVI
    private void readTrainDirectory(){
        File directory = new File(trainDirectoryPath);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        for(File filename : files){
            try{
                createNewTrain(filename.toPath());
            } catch (Exception ex){
                Logger.getLogger(FileReaderUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
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

// Klasa koja predstavlja graf stanica i sluzi da bismo dobili putanju od pocetne do odredisne stanice
class RailwayStationsGraph {

    public final static HashMap<String, LinkedList<String>> graph = new HashMap<>();

    static {
        LinkedList<String> neighboursA = new LinkedList<>();
        neighboursA.offer("B");
        graph.put("A", neighboursA);

        LinkedList<String> neighboursB = new LinkedList<>();
		neighboursB.offer("A");
		neighboursB.offer("C");
		graph.put("B", neighboursB);
		
		LinkedList<String> neighboursC = new LinkedList<>();
		neighboursC.offer("B");
		neighboursC.offer("D");
		neighboursC.offer("E");
		graph.put("C", neighboursC);
		
		LinkedList<String> neighboursD = new LinkedList<>();
		neighboursD.offer("C");
		graph.put("D", neighboursD);
		
		LinkedList<String> neighboursE = new LinkedList<>();
		neighboursE.offer("C");
		graph.put("E", neighboursE);
    }

    private RailwayStationsGraph() { }

    private static void travelGraph(String src, String dest, HashMap<String, String> pred){
        LinkedList<String> queue = new LinkedList<>();
        ArrayList<String> visited = new ArrayList<>();

        visited.add(src);
        queue.add(src);

        while(!queue.isEmpty()){
            String node = queue.poll();
            if(node != null){
                for(String neigh :graph.get(node)){
                    if(!visited.contains(neigh)){
                        visited.add(neigh);
                        pred.put(neigh, node);
                        queue.add(neigh);

                        if(neigh.equals(dest)){
                            return;
                        }
                    }
                }
            }
        }
    }

    public static ArrayList<String> findRoute(String src, String dest){

        if(src.equals(dest)){
            return null;
        }

        HashMap<String, String> pred = new HashMap<>();
        ArrayList<String> path = new ArrayList<>();
        travelGraph(src, dest, pred);

        String crawl = dest;
        path.add(crawl);

        while(pred.get(crawl) != null){
            path.add(pred.get(crawl));
            crawl = pred.get(crawl);
        }

        Collections.reverse(path);
        return path;
    }
}
