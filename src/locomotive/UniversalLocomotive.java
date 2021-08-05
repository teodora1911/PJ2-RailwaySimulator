package locomotive;

import element.ElementColor;
import util.Constants;

public class UniversalLocomotive extends Locomotive {
    
    public UniversalLocomotive(int x, int y, double power, Engine engine){
        super(x, y, new ElementColor(254, 227, 226), Constants.LocomotiveLabels.UNIVERSAL.getValue(), power, engine);
    }

    @Override
    public String toString(){
        return "UNIVERSAL " + super.toString();
    }
}
