package engine;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static util.Constants.MIN_SPEED;

import map.Map;
import util.Constants;
import vehicle.Car;
import vehicle.Truck;
import map.Segment;

public class VehicleCreationThread extends Thread {

    private Random rand = new Random();
    private static Handler handler;

    static{
        try {
            handler = new FileHandler(Simulation.logDirectory + "vehiclecreationthread.log");
            Logger.getLogger(VehicleCreationThread.class.getName()).addHandler(handler);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public VehicleCreationThread(){
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
                        Segment road = Map.getRoads().get(i).getPath(direction);
                        if(rand.nextInt() % 2 == 0){
                            new Thread(new Car(road, rand.nextInt(Map.getRoads().get(i).getMaxSpeed() - MIN_SPEED) + MIN_SPEED, "brand", "model", 1, 4)).start();
                        } else {
                            new Thread(new Truck(road, rand.nextInt(Map.getRoads().get(i).getMaxSpeed() - MIN_SPEED) + MIN_SPEED, "brand", "model", 1, 4)).start();
                        }

                        --numberOfVehicles[i];

                        try {
                            Thread.sleep(Constants.CREATION_TIME_GAP);
                        } catch (Exception ex) {
                            Logger.getLogger(VehicleCreationThread.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                }
            }
        }  
    }
}
