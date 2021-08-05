package vehicle;

import element.ElementColor;
import map.Segment;

public class Truck extends Vehicle {
    
    protected double capacity;

    public Truck(Segment road, int speed, String brand, String model, int year, double capacity){
        super(road, speed, new ElementColor(0, 231, 163), brand, model, year, "K");
        setCapacity(capacity);
    }

    public double getCapacity(){
        return this.capacity;
    }

    public void setCapacity(double capacity){
        if(capacity > 0){
            this.capacity = capacity;
        }
    }
}
