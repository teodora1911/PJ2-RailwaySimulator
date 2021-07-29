package vehicle;

import element.Element;
import element.ElementColor;
import map.Coordinates;
import map.Map;
import util.Constants;

public abstract class Vehicle extends Element implements Runnable {
    
    protected String brand;
    protected String model;
    protected int year;
    protected boolean direction;
    protected int speed;

    public Vehicle(int x, int y, int speed, ElementColor mapColor, String brand, String model, int year){
        super(x, y, mapColor);
        this.brand = brand;
        this.model = model;
        this.year = year;
        setSpeed(speed);
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

    public int getSpeed(){
        return speed;
    }

    public void setSpeed(int speed){
        if(speed < Constants.MIN_SPEED){
            this.speed = Constants.MIN_SPEED;
        } else {
            this.speed = speed;
        }
    }

    @Override
    public void run(){
        // wait - za cekanje voza da prodje i ispunjavanje uslova - mozda busy waiting cak
        // uzima mapu, segment da vidi da li ima vozova koji se krecu prema njemu

        while(!Map.isFieldFree(this.coordinates)){
            // ceka
        }

        Map.putElementInField(this, this.coordinates);

        while(active){
            try {
                Thread.sleep(speed);
            } catch (InterruptedException ex) {
                // Logger 
                System.out.println("Interrupted ex - vehicle");
            }

            Coordinates nextCoordinates = Map.nextCoordinates(this.coordinates);

            if(nextCoordinates != null){
                while(!Map.isFieldFree(nextCoordinates)){
                    // ceka
                }
                Map.putElementInField(this, nextCoordinates);
            } else {
                this.active = false;
                Map.putElementInField(this, new Coordinates(-1, -1));
            }
        }
    }

    @Override
    public String toString(){
        return "Vozilo " + super.toString();
    }
}
