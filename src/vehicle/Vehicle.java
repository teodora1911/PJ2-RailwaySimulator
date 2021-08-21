package vehicle;

import static util.Constants.MIN_SPEED;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import element.Element;
import element.ElementColor;
import engine.Simulation;
import map.Field;
import map.FieldType;
import map.Map;
import map.Segment;
import util.LoggerUtil;

public abstract class Vehicle extends Element implements Runnable {
    
    protected String brand;
    protected String model;
    protected int year;
    protected int speed;
    protected Segment road;

    protected static FileHandler handler;
    protected static Logger logger = Logger.getLogger(Vehicle.class.getName());

    static {
        LoggerUtil.setLogger(logger, handler, "vehicle.log");
    }

    // za sva vozila pravac je true, jer sadrze odgovarajuce segmente
    private static final boolean DIRECTION = true;

    public Vehicle(Segment road, int speed, ElementColor mapColor, String brand, String model, int year, String label){
        super(road.getStartingPoint(DIRECTION).getX(), road.getStartingPoint(DIRECTION).getY(), mapColor, label);
        setSpeed(speed);
        this.road = road;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public String getBrand(){
        return this.brand;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public String getModel(){
        return this.model;
    }

    public void setModel(String model){
        this.model = model;
    }

    public int getYear(){
        return this.year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public Segment getRoad(){
        return this.road;
    }

    public void setRoad(Segment road){
        this.road = road;
    }

    public int getSpeed(){
        return this.speed;
    }

    public void setSpeed(int speed){
        if(speed < MIN_SPEED){
            this.speed = MIN_SPEED;
        } else {
            this.speed = speed;
        }
    }

    @Override
    public void run(){
        Field currentField = null;
        Field nextField = road.getStartingPoint(DIRECTION);
        while(nextField != null){
            if(nextField.getFieldType() == FieldType.CROSSING){
                while(!Map.isFieldEmpty(nextField.getCoordinates()) || Map.isFieldUnderVoltage(nextField.getCoordinates()) || !Map.freeForCrossing(nextField)){
                    // busy waiting
                }
            } else {
                while(!Map.isFieldEmpty(nextField.getCoordinates())){
                    // busy waiting
                }
            }

            synchronized(Map.updateLock){
                this.coordinates = nextField.getCoordinates();
                Map.getMap()[coordinates.getX()][coordinates.getY()].setElement(this);
                if(currentField != null){
                    Map.getMap()[currentField.getX()][currentField.getY()].setElement(null);
                }
                Simulation.mwvc.makeChange();
            }

            currentField = nextField;
            nextField = road.getNextField(currentField, DIRECTION);

            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        // dosli smo do kraja (gdje su sljedece koordinate null) i vozilo treba da izadje sa mape (tj oslobodi polje)
        synchronized(Map.updateLock){
            Map.getMap()[getX()][getY()].setElement(null);
            Simulation.mwvc.makeChange();
        }
    }

    @Override
    public String toString(){
        return "Vozilo " + super.toString();
    }
}
