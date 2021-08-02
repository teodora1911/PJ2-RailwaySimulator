package locomotive;

import element.ElementColor;
import element.RailwayElement;

public abstract class Locomotive extends RailwayElement {
    
    protected Engine engine;
    protected double power;

    public Locomotive(int x, int y, int speed, ElementColor mapColor, String label, double power, Engine engine){
        super(x, y, speed, mapColor, label);
        this.power = power;
        this.engine = engine;
    }

    public Engine getEngine(){
        return this.engine;
    }

    public void setEngine(Engine engine){
        this.engine = engine;
    }

    public double getPower(){
        return this.power;
    }

    public void setPower(double power){
        this.power = power;
    }

    @Override
    public String toString(){
        return "L " + label;
    }
}
