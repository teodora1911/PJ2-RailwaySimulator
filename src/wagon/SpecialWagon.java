package wagon;

import element.ElementColor;
import util.Constants;

public class SpecialWagon extends Wagon {
    
    public SpecialWagon(int x, int y, int length){
        super(x, y, new ElementColor(163, 163, 11), Constants.WagonLabels.SPECIAL.getValue(), length);
    }

    @Override
    public String toString(){
        return "SPECIAL " + super.toString();
    }
}
