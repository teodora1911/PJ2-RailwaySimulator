package wagon;

import util.Constants;

public class RestaurantWagon extends PassengerWagon {
    
    protected String description = "";

    public RestaurantWagon(int x, int y, int length, String description){
        super(x, y, length, Constants.WagonLabels.RESTAURANT.getValue());
        setDescpription(description);
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescpription(String description){
        if(description != null){
            this.description = description;
        }
    }

    @Override
    public String toString(){
        return "RESTAURANT " + super.toString();
    }
}
