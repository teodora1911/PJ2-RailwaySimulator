package wagon;

import element.ElementColor;

public abstract class PassengerWagon extends Wagon {
    
    public PassengerWagon(int x, int y, int length, String label){
        super(x, y, new ElementColor(136, 69, 69), label, length);
    }

    @Override
    public String toString(){
        return "P " + super.toString();
    }
}
