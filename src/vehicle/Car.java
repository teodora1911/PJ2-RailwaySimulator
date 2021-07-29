package vehicle;

import element.ElementColor;

public class Car extends Vehicle {
    
    protected int doorCount = 2;

    public Car(int x, int y, int speed, String brand, String model, int year, int doorCount){
        super(x, y, speed, new ElementColor(215, 96, 192), brand, model, year);
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
