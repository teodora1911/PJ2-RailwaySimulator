package wagon;

import util.Constants;

public class SleepWagon extends PassengerWagon {
    
    public SleepWagon(int x, int y, int length){
        super(x, y, length, Constants.WagonLabels.SLEEP.getValue());
    }

    @Override
    public String toString(){
        return "SLEEP " + super.toString();
    }
}
