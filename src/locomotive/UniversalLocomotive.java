package locomotive;

import element.ElementColor;
import util.Constants;

public class UniversalLocomotive extends Locomotive {
    
    public UniversalLocomotive(int x, int y, int speed, double power, Engine engine){
        super(x, y, speed, new ElementColor(144, 227, 119), Constants.LocomotiveLabels.UNIVERSAL.getValue(), power, engine);
    }

    @Override
    public String toString(){
        return "UNIVERSAL " + super.toString();
    }
}
