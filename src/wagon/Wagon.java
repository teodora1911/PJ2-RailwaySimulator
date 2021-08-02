package wagon;

import element.ElementColor;
import element.RailwayElement;

public abstract class Wagon extends RailwayElement {
    
    private int length;

    public Wagon(int x, int y, int speed, ElementColor mapColor, String label, int length){
        super(x, y, speed, mapColor, label);
    }

    public int getLength(){
        return this.length;
    }

    public void setLength(int length){
        this.length = length;
    }

    @Override
    public String toString(){
        return "W " + label;
    }
}
