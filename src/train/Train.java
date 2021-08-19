package train;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import element.RailwayElement;
import engine.Simulation;
import exceptions.TrainPathNotFoundException;
import locomotive.Engine;
import locomotive.Locomotive;
import map.Coordinates;
import map.Field;
import map.FieldType;
import map.Map;
import railwaystation.RailwayStation;
import util.LoggerUtilClass;
import util.serialization.SerializationUtilClass;

public class Train implements Runnable {
    
    private ArrayList<RailwayElement> configuration;
    private LinkedList<RailwayStation> stations;
    private int id;
    private int initialSpeed;
    private int currentSpeed;
    private Movement historyOfMovement = new Movement();
    private RailwayStation currentStation;

    private Coordinates startingPosition; 
    private Coordinates lookaheadPosition; 
    private Coordinates lookbehindPosition;

    private boolean hasElectricEngine = false;

    private Object speedLock = new Object();
    private Object movementLock = new Object();

    protected static FileHandler handler;
    protected static Logger logger = Logger.getLogger(Train.class.getName());

    static {
        LoggerUtilClass.setLogger(logger, handler, "train.log");
    }

    public Train(int id, int speed, ArrayList<RailwayElement> configuration, LinkedList<RailwayStation> stations){
        this.id = id;
        this.initialSpeed = this.currentSpeed = speed;
        this.configuration = configuration;
        this.stations = stations;
        historyOfMovement.setTrainId(this.id);
        historyOfMovement.setStationPath(this.stations);
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

    public ArrayList<RailwayElement> getConfiguration(){
        return this.configuration;
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

    public void setStartingPosition(Coordinates position){
        this.startingPosition = position;
    }

    private Coordinates lookForNextPosition(Coordinates currentCoordinates) throws TrainPathNotFoundException {
        // ukoliko je jedan zeljeznicki element u konfiguraciji u stanici, ne treba vise da trazi sljedece koordinate
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

        Predicate<Coordinates> filterForRailway = (c) -> c.isValid() && map[c.getX()][c.getY()] != null && (map[c.getX()][c.getY()].getFieldType() == FieldType.RAILWAY ||
        map[c.getX()][c.getY()].getFieldType() == FieldType.CROSSING || map[c.getX()][c.getY()].getStation() != null);

        possibleDirections = possibleDirections.stream().filter(filterForRailway).collect(Collectors.toList());

        if(possibleDirections.isEmpty()){
            throw new TrainPathNotFoundException();
        }

        // uklanjamo sve koordinate po kojima se voz vec kretao i sve koordinate prethodne stanice
        possibleDirections.removeAll(currentStation.getCoordinates());
        possibleDirections.removeAll(historyOfMovement.getPath());

        if(possibleDirections.size() != 1){
            throw new TrainPathNotFoundException();
        } else {
            return possibleDirections.get(0);
        }
    }

    private void setFieldUnderVoltage(Coordinates coordinates){
        if(!Map.isStationOnField(coordinates)){
            Map.getMap()[coordinates.getX()][coordinates.getY()].setUnderVoltage(true);
        }
    }

    private void resetFieldUnderVoltage(Coordinates coordinates){
        if(!Map.isStationOnField(coordinates)){
            Map.getMap()[coordinates.getX()][coordinates.getY()].setUnderVoltage(false);
        }
    }

    private void executeStep() throws TrainPathNotFoundException {
        Field[][] map = Map.getMap();
        synchronized(Map.updateLock){
            if(hasElectricEngine){
                setFieldUnderVoltage(lookaheadPosition);
            }
            Coordinates nextPosition = lookaheadPosition;

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
            if(hasElectricEngine){
                setFieldUnderVoltage(lookaheadPosition);
            }
            Simulation.mwvc.makeChange();
        }

        try{
            Thread.sleep(currentSpeed);
        } catch (InterruptedException ex){
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void removeTrainFromMap(){
        System.out.println("Removing train from map ...");
        synchronized(Map.updateLock){
            configuration.stream().forEach(element -> Map.getMap()[element.getX()][element.getY()].setElement(null));
            Simulation.mwvc.makeChange();
        }
    }

    @Override
    public void run(){
        System.out.println("Train " + this.id + " is created ...");
        try{
            historyOfMovement.setMovementTime(new Date().getTime());

            currentStation = stations.peek();
            stations.poll();

            while(!stations.isEmpty()){
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

                lookaheadPosition = startingPosition;
                lookbehindPosition = configuration.get(configuration.size() - 1).getCoordinates();

                while(Map.isStationOnField(lookbehindPosition)){
                    executeStep();
                }

                /**
                 * Kada u potpunosti izadje iz stanice, voz daje signal (sljedecoj stanici)
                 * da moze da se pustaju vozovi koji idu u istom smjeru.
                 */
                getNextStation().outOfStation(this);

                while(!configuration.stream().allMatch(element -> Map.isStationOnField(element.getCoordinates()))){
                    executeStep();
                }

                if(hasElectricEngine){
                    synchronized(Map.updateLock){
                        resetFieldUnderVoltage(lookbehindPosition);
                        Simulation.mwvc.makeChange();
                    }
                }

                currentStation = stations.peek();
                stations.poll();
                currentStation.inTheStation(this);
            }

            historyOfMovement.updateMovementTime(new Date().getTime());
            SerializationUtilClass.serializeMovement(historyOfMovement, "movement" + id);
        } catch (TrainPathNotFoundException ex){
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            /**
             * Ako voz ne moze da nadje sljedece koordinate, 
             * uklanjamo ga sa mape.
             */
            removeTrainFromMap();
        }
    }
}
