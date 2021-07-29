package locomotive;

import element.ElementColor;
import util.Constants;

public class ShuntingLocomotive extends Locomotive {
    
    public ShuntingLocomotive(int x, int y, double power, Engine engine){
        super(x, y, new ElementColor(165, 116, 30), Constants.LocomotiveLabels.SHUNTING.getValue(), power, engine);
    }

    @Override
    public String toString(){
        return "SHUNTING " + super.toString();
    }
}
