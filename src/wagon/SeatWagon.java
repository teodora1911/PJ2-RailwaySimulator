package wagon;

import util.Constants;

public class SeatWagon extends PassengerWagon {
    
    protected int numberOfSeats = 0;

    public SeatWagon(int x, int y, int length, int numberOfSeats){
        super(x, y, length, Constants.WagonLabels.SEAT.getValue());
        setNumberOfSeats(numberOfSeats);
    }

    public int getNumberOfSeats(){
        return this.numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats){
        if(numberOfSeats > 0){
            this.numberOfSeats = numberOfSeats;
        }
    }

    @Override
    public String toString(){
        return "SEAT " + super.toString();
    }
}
