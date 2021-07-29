package wagon;

import element.ElementColor;

public abstract class PassengerWagon extends Wagon {
    
    public PassengerWagon(int x, int y, int length, String label){
        super(x, y, new ElementColor(156, 238, 166), label, length);
    }

    @Override
    public String toString(){
        return "P " + super.toString();
    }
}
