package vehicle;

import element.ElementColor;
import map.Segment;

public class Car extends Vehicle {
    
    protected int doorCount = 2;

    public Car(Segment road, int speed, String brand, String model, int year, int doorCount){
        super(road, speed, new ElementColor(215, 96, 192), brand, model, year);
        setDoorCount(doorCount);
    }

    public int getDoorCount(){
        return this.doorCount;
    }

    public void setDoorCount(int doorCount) {
        if(doorCount >= 2 && doorCount <= 6){
            this.doorCount = doorCount;
        }
    }
}
