package engine;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
import map.RailwayStationsGraph;
import railwaystation.RailwayStation;
import train.Train;
import util.Constants;
import wagon.BedWagon;
import wagon.LoadWagon;
import wagon.RestaurantWagon;
import wagon.SeatWagon;
import wagon.SleepWagon;
import wagon.SpecialWagon;

public class TrainCreationClass {
    private String path;

    public TrainCreationClass(String path){
        this.path = path;
        readFiles();
    }

    private void readFiles(){
        File directory = new File(path);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        for(File filename : files){
            try{
                createNewTrain(filename.toPath());
            } catch (Exception ex){
                Logger.getLogger(TrainCreationClass.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

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
            Logger.getLogger(TrainCreationClass.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            // ako dodje do bilo kakvog izuzetka, obrisati dati fajl
        }
    }

    private boolean validTrainConfiguration(String[] railwayElements) {
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
}
