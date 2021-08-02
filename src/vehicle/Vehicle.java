package vehicle;

import element.Element;
import element.ElementColor;

public abstract class Vehicle extends Element implements Runnable {
    
    protected String brand;
    protected String model;
    protected int year;
    protected boolean direction;
    protected boolean active;

    public Vehicle(int x, int y, int speed, ElementColor mapColor, String brand, String model, int year){
        super(x, y, speed, mapColor);
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.active = true;
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

    public boolean isActive(){
        return this.active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    @Override
    public void run(){
        
    }

    @Override
    public String toString(){
        return "Vozilo " + super.toString();
    }
}
