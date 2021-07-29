package wagon;

import element.ElementColor;
import util.Constants;

public class LoadWagon extends Wagon {
    
    protected double maxCapacity = 0.0;

    public LoadWagon(int x, int y, int length, double maxCapacity){
        super(x, y, new ElementColor(113, 157, 184), Constants.WagonLabels.LOAD.getValue(), length);
        setMaxCapacity(maxCapacity);
    }

    public double getMaxCapacity(){
        return this.maxCapacity;
    }

    public void setMaxCapacity(double maxCapacity){
        if(maxCapacity > 0.0){
            this.maxCapacity = maxCapacity;
        }
    }

    @Override
    public String toString(){
        return "LOAD " + super.toString();
    }
}
