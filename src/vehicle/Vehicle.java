package vehicle;

import java.util.logging.Level;
import java.util.logging.Logger;

import element.Element;
import element.ElementColor;
import engine.Simulation;
import map.Field;
import map.FieldType;
import map.Map;
import map.Segment;

public abstract class Vehicle extends Element implements Runnable {
    
    protected String brand;
    protected String model;
    protected int year;
    protected Segment road;

    // za sva vozila pravac je true, jer sadrze odgovarajuce segmente
    private static final boolean DIRECTION = true;

    public Vehicle(Segment road, int speed, ElementColor mapColor, String brand, String model, int year){
        super(road.getStartingPoint(DIRECTION).getX(), road.getStartingPoint(DIRECTION).getY(), speed, mapColor);
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

    @Override
    public void run(){
        Field currentField = null;
        Field nextField = road.getStartingPoint(DIRECTION);
        while(nextField != null){
            if(nextField.getFieldType() == FieldType.CROSSING){
                System.out.println("CROSSING");
                while(!Map.isFieldEmpty(nextField.getCoordinates()) || Map.isFieldUnderVoltage(nextField.getCoordinates()) || !Map.freeForCrossing(nextField)){
                    // busy waiting
                }
            } else {
                while(!Map.isFieldEmpty(nextField.getCoordinates())){
                    // busy waiting
                }
            }

            // kada je prosao sve provjere, moze da krene
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
                Logger.getLogger(Vehicle.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        // sljedeci je null, vozilo treba da izadje sa mape
        synchronized(Map.updateLock){
            Map.getMap()[getX()][getY()].setElement(null);
        }

        System.out.println("Vozilo je zavrsilo svoju trku.");
    }

    @Override
    public String toString(){
        return "Vozilo " + super.toString();
    }
}
