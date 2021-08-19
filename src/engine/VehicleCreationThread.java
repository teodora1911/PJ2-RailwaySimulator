package engine;

import static util.Constants.MIN_SPEED;
import static util.Constants.MODELS;
import static util.Constants.BRANDS;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import map.Map;
import util.Constants;
import util.LoggerUtilClass;
import vehicle.Car;
import vehicle.Truck;
import map.Segment;

public class VehicleCreationThread extends Thread {

    private static Random rand = new Random();

    private static FileHandler handler;
    private static Logger logger = Logger.getLogger(VehicleCreationThread.class.getName());

    public static final int START_YEAR = 1950;
    public static final int YEAR_PERIOD = 70;

    static{
        LoggerUtilClass.setLogger(logger, handler, "vehicle.log");
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
                            new Thread(new Car(road, rand.nextInt(Map.getRoads().get(i).getMaxSpeed() - MIN_SPEED) + MIN_SPEED, BRANDS[rand.nextInt(BRANDS.length)], MODELS[rand.nextInt(MODELS.length)], rand.nextInt(YEAR_PERIOD) + START_YEAR, rand.nextInt(5) + 2)).start();
                        } else {
                            new Thread(new Truck(road, rand.nextInt(Map.getRoads().get(i).getMaxSpeed() - MIN_SPEED) + MIN_SPEED, BRANDS[rand.nextInt(BRANDS.length)], MODELS[rand.nextInt(MODELS.length)], rand.nextInt(YEAR_PERIOD) + START_YEAR, rand.nextDouble() * 1000.0 + 500.0)).start();
                        }

                        --numberOfVehicles[i];

                        try {
                            Thread.sleep(Constants.CREATION_TIME_GAP);
                        } catch (Exception ex) {
                            logger.log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                }
            }
        }  
    }
}
