package wagon;

import util.Constants;

public class BedWagon extends PassengerWagon {
    
    public BedWagon(int x, int y, int length){
        super(x, y, length, Constants.WagonLabels.BED.getValue());
    }

    @Override
    public String toString(){
        return "BED " + super.toString();
    }
}
