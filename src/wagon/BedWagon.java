package wagon;

import util.Constants;

public class BedWagon extends PassengerWagon {
    
    public BedWagon(int x, int y, int speed, int length){
        super(x, y, speed, length, Constants.WagonLabels.BED.getValue());
    }

    @Override
    public String toString(){
        return "BED " + super.toString();
    }
}
