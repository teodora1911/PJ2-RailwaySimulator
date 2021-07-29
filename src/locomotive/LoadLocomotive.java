package locomotive;

import element.ElementColor;
import util.Constants;

public class LoadLocomotive extends Locomotive {
    
    public LoadLocomotive(int x, int y, double power, Engine engine){
        super(x, y, new ElementColor(61, 17, 137), Constants.LocomotiveLabels.LOAD.getValue(), power, engine);
    }

    @Override
    public String toString(){
        return "LOAD " + super.toString();
    }
}
