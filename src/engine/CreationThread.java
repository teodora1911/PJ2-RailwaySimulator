package engine;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import map.Coordinates;
import map.Map;
import util.Constants;
import vehicle.Car;
import vehicle.Truck;

public class CreationThread extends Thread {

    private Random rand = new Random();

    public CreationThread(){
        super();
    }
    
    @Override
    public void run(){
        int numberOfVehicles[] = new int[Map.numberOfRoads()];

        while(true){
            for(int i = 0; i < Map.numberOfRoads(); ++i){
                numberOfVehicles[i] = Map.getRoad(i + 1).getAdditionalNumberOfVehicles();
            }

            while(!Arrays.stream(numberOfVehicles).boxed().allMatch(i -> i.equals(0))){
                for(int i = 0; i < Map.numberOfRoads(); ++i){
                    if(numberOfVehicles[i] != 0){
                        boolean direction = (rand.nextInt() % 2 == 0);
                        Coordinates c = Map.getRoad(i + 1).getStartingCoordinates(direction);
                        if(rand.nextInt() % 2 == 0){
                            new Thread(new Car(c.getX(), c.getY(), rand.nextInt(Map.getRoad(i + 1).getMaxSpeed()), "", "", 1, 4)).start();
                        } else {
                            new Thread(new Truck(c.getX(), c.getY(), rand.nextInt(Map.getRoad(i + 1).getMaxSpeed()), "brand", "model", 1, 4)).start();
                        }

                        --numberOfVehicles[i];

                        try {
                            Thread.sleep(Constants.CREATION_TIME_GAP);
                        } catch (Exception ex) {
                            Logger.getLogger(CreationThread.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                }
            }
        }  
    }
}
