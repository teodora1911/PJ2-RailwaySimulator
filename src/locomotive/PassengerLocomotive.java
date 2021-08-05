package locomotive;

import element.ElementColor;
import util.Constants;

public class PassengerLocomotive extends Locomotive {
    
    public PassengerLocomotive(int x, int y, double power, Engine engine){
        super(x, y, new ElementColor(117, 95, 95), Constants.LocomotiveLabels.PASSENGER.getValue(), power, engine);
    }

    @Override
    public String toString(){
        return "PASSENGER " + super.toString();
    }
}
