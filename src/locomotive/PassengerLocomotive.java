package locomotive;

import element.ElementColor;
import util.Constants;

public class PassengerLocomotive extends Locomotive {
    
    public PassengerLocomotive(int x, int y, int speed, double power, Engine engine){
        super(x, y, speed, new ElementColor(60, 184, 75), Constants.LocomotiveLabels.PASSENGER.getValue(), power, engine);
    }

    @Override
    public String toString(){
        return "PASSENGER " + super.toString();
    }
}
