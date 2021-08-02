package train;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import element.RailwayElement;
import engine.Simulation;
import locomotive.Engine;
import locomotive.Locomotive;
import map.Coordinates;
import map.Field;
import map.FieldType;
import map.Map;
import railwaystation.RailwayStation;
import util.serialization.SerializationUtilClass;

public class Train implements Runnable {
    
    private ArrayList<RailwayElement> configuration;
    private LinkedList<RailwayStation> stations;
    private int id;
    private int initialSpeed; // in miliseconds
    private int currentSpeed; // in miliseconds
    private Movement historyOfMovement = new Movement();
    private RailwayStation currentStation;

    private Coordinates startingPosition; // postavlja stanica kada pusta voz da se krece
    private Coordinates lookaheadPosition; // polje ispred voza
    private Coordinates lookbehindPosition; // polje iza voza

    private boolean hasElectricEngine = false;

    private Object speedLock = new Object();
    private Object movementLock = new Object();

    public Train(int id, int speed, ArrayList<RailwayElement> configuration, LinkedList<RailwayStation> stations){
        this.id = id;
        this.initialSpeed = this.currentSpeed = speed;
        this.configuration = configuration;
        this.stations = stations;
        historyOfMovement.setTrainId(this.id);
        hasElectricEngine = (configuration.stream().filter(
            e -> e instanceof Locomotive).map(e -> (Locomotive)e).filter(l -> l.getEngine() == Engine.ELECTRIC).count() != 0);
        System.out.println("Has electric engine : " + hasElectricEngine);
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getSpeed(){
        synchronized(speedLock){
            return this.currentSpeed;
        }
    }

    public void changeSpeed(int speed){
        synchronized(speedLock){
            this.currentSpeed = speed;
        }
    }

    public void resetSpeed(){
        synchronized(speedLock){
            currentSpeed = initialSpeed;
        }
    }

    public Object getMovementLock(){
        return this.movementLock;
    }

    public RailwayStation getNextStation(){
        return stations.peek();
    }

    public void setStartingPosition(Coordinates position){ // postavlja stanica kada voz treba da pocne sa kretanjem
        this.startingPosition = position;
    }

    private Coordinates lookForNextPosition(Coordinates currentCoordinates) {
        // ako se vec nalazi na stanici, ne treba vise da trazi sljedece koordinate
        if(Map.isStationOnField(currentCoordinates)){
            return currentCoordinates;
        }

        Coordinates northC = new Coordinates(currentCoordinates.getX(), currentCoordinates.getY() - 1);
        Coordinates westC = new Coordinates(currentCoordinates.getX() - 1, currentCoordinates.getY());
        Coordinates southC = new Coordinates(currentCoordinates.getX(), currentCoordinates.getY() + 1);
        Coordinates eastC = new Coordinates(currentCoordinates.getX() + 1, currentCoordinates.getY());

        List<Coordinates> possibleDirections = new ArrayList<>();
        possibleDirections.add(northC);
        possibleDirections.add(westC);
        possibleDirections.add(southC);
        possibleDirections.add(eastC);
        Field[][] map = Map.getMap();

        // filtiriramo one koordinate koje su VALIDNE, koje su PRUGE ili PRUZNI PRELAZI ili STANICA
        Predicate<Coordinates> filterForRailway = (c) -> c.isValid() && map[c.getX()][c.getY()] != null && (map[c.getX()][c.getY()].getFieldType() == FieldType.RAILWAY ||
        map[c.getX()][c.getY()].getFieldType() == FieldType.CROSSING || map[c.getX()][c.getY()].getStation() != null);

        // i te koordinate smjestimo u listu mogucih koordinata
        possibleDirections = possibleDirections.stream().filter(filterForRailway).collect(Collectors.toList());

        if(possibleDirections.isEmpty()){
            System.out.println("PRAZNA LISTA MOGUCIH PRAVACA?");
            return null;
        }

        // uklanjamo sve koordinate po kojima se voz vec kretao i sve koordinate prethodne stanice, ako ih ima
        possibleDirections.removeAll(currentStation.getCoordinates());
        possibleDirections.removeAll(historyOfMovement.getPath());

        if(possibleDirections.size() != 1){
            System.out.println("IMA VISE MOGUCIH PRAVACA?");
            return null;
        } else {
            return possibleDirections.get(0);
        }
    }

    private void setFieldUnderVoltage(Coordinates coordinates){
        if(!Map.isStationOnField(coordinates)){
            Map.getMap()[coordinates.getX()][coordinates.getY()].setUnderVoltage(true);
        }
    }

    // ne treba da sinhronizuju (mozda) Map.updateLock jer u trenutku kada se pozivaju, voz vec sinhronizuje

    private void resetFieldUnderVoltage(Coordinates coordinates){
        if(!Map.isStationOnField(coordinates)){
            Map.getMap()[coordinates.getX()][coordinates.getY()].setUnderVoltage(false);
        }
    }

    private void executeStep(){
        Field[][] map = Map.getMap();
        synchronized(Map.updateLock){
            if(hasElectricEngine){
                setFieldUnderVoltage(lookaheadPosition);
            }
            Coordinates nextPosition = lookaheadPosition;
            System.out.println(nextPosition);
            for(RailwayElement element : configuration){
                Coordinates currentPosition = element.getCoordinates();
                element.setCoordinates(nextPosition);
                map[element.getX()][element.getY()].setElement(element);
                map[currentPosition.getX()][currentPosition.getY()].setElement(null);
                if(!Map.isStationOnField(nextPosition)){
                    historyOfMovement.addNewCoordinates(nextPosition);
                }
                nextPosition = currentPosition;
            }

            if(hasElectricEngine){
                resetFieldUnderVoltage(lookbehindPosition);
                lookbehindPosition = nextPosition;
                setFieldUnderVoltage(lookbehindPosition);
            } else {
                lookbehindPosition = nextPosition;
            }

            lookaheadPosition = lookForNextPosition(lookaheadPosition);
            if(lookaheadPosition != null){
                setFieldUnderVoltage(lookaheadPosition);
            } else {
                System.out.println("NE MOZE DA NADJE SLJEDECE KOORDINATE.");
            }

            Simulation.mwvc.makeChange();
        }

        try{
            Thread.sleep(currentSpeed);
        } catch (InterruptedException ex){
            Logger.getLogger(Train.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public void run(){
        System.out.println("VOZ JE KRENUO SA RADOM.");
        historyOfMovement.setMovementTime(new Date().getTime());

        currentStation = stations.peek();
        stations.poll();
        System.out.println("TRENUTNA STANICA : " + currentStation.getName());

        while(!stations.isEmpty()){ // sve dok ne prodje sve stanice, krece se

            // ceka na trenutnoj stanici
            historyOfMovement.setStationRetentionTime(currentStation.getName(), new Date().getTime());
            synchronized(movementLock){
                try{
                    currentStation.addTrainToWaitingQueue(this);
                    movementLock.wait();
                } catch (InterruptedException ex){
                    Logger.getLogger(Train.class.getName()).log(Level.SEVERE, "Neuspjesno cekanje na stanici.", ex);
                    // break;
                }
            }
            historyOfMovement.updateStationRetentionTime(currentStation.getName(), new Date().getTime());

            System.out.println("Trenutna brzina voza " + id + " : " + currentSpeed);
            // poziciju prije voza inicijalizujemo sa startingPosition koju smo dobili od stanice kada je pustala voz
            lookaheadPosition = startingPosition;
            // poziciju voza inicijalizujemo da koordinatama posljednjeg elementa u konfiguraciji - jer je svakako taj element u stanici (i dalje)
            lookbehindPosition = configuration.get(configuration.size() - 1).getCoordinates();

            // sve dok svi elementi nisu izasli iz stanice
            while(Map.isStationOnField(lookbehindPosition)){
                executeStep();
            }

            // kad su svi elementi izasli sa mape, tada salje iducoj stanici signal da moze da pusti druga vozila koja zele da idu u istom smjeru
            getNextStation().outOfStation(this);

            // sve dok svi elementi nisu usli u stanicu
            while(!configuration.stream().allMatch(element -> Map.isStationOnField(element.getCoordinates()))){
                executeStep();
            }

            currentStation = stations.peek();
            stations.poll();
            currentStation.inTheStation(this);
        }

        historyOfMovement.updateMovementTime(new Date().getTime());
        SerializationUtilClass.serializeMovement(historyOfMovement, "movement" + id);

        System.out.println(historyOfMovement.getPath());

        System.out.println("VOZ JE ZAVRSIO SA RADOM.");
    }

    //@Override
    //public boolean equals(Object object){
    //    if(this == object){
    //        return true;
    //    }
//
    //    if(object == null || getClass() != object.getClass()){
    //        return false;
    //    }
//
    //    Train other = (Train)object;
    //    return (this.id == other.getId());
    //}
//
    //@Override
    //public int hashCode(){
    //    int prime = 31;
    //    return prime + (int)(id ^ (id >>> 32));
    //}
}
