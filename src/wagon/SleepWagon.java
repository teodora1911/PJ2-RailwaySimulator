package wagon;

import util.Constants;

public class SleepWagon extends PassengerWagon {
    
    public SleepWagon(int x, int y, int speed, int length){
        super(x, y, speed, length, Constants.WagonLabels.SLEEP.getValue());
    }

    @Override
    public String toString(){
        return "SLEEP " + super.toString();
    }
}
