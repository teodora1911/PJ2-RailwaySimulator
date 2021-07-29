package map;

import static util.Constants.MIN_SPEED;

public class Road {
    
    private int id;
    private int maxSpeed;
    private int currentNumberOfVehicles = 0;
    private int additionalNumberOfVehicles = 0;
    private Segment leftTrack = new Segment();
    private Segment rightTrack = new Segment();

    private Object speedLock = new Object();
    private Object numLock = new Object();

    public Road(int id){
        this.id = id;
        maxSpeed = MIN_SPEED;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getMaxSpeed(){
        synchronized(speedLock){
            return this.maxSpeed;
        }
    }

    public void setMaxSpeed(int maxSpeed){
        if(maxSpeed >= MIN_SPEED){
            synchronized(speedLock){
                this.maxSpeed = maxSpeed;
            }
        }
    }

    public void setNumberOfVehicles(int numberOfVehicles){
        if(numberOfVehicles > currentNumberOfVehicles){
            synchronized(numLock){
                additionalNumberOfVehicles += numberOfVehicles - currentNumberOfVehicles;
                currentNumberOfVehicles = numberOfVehicles;
            }
        }
    }

    public int getAdditionalNumberOfVehicles(){
        synchronized(numLock){
            int toReturn = additionalNumberOfVehicles;
            additionalNumberOfVehicles = 0;
            return toReturn;
        }
    }

    public Segment contains(Coordinates c){
        if(leftTrack.contains(c)){
            return leftTrack;
        } else if (rightTrack.contains(c)){
            return rightTrack;
        } else {
            return null;
        }
    }
    
    public Coordinates getStartingCoordinates(boolean direction){
        if(direction){
            return leftTrack.getStartingCoordinates(true);
        } else {
            return rightTrack.getStartingCoordinates(true);
        }
    }

    public void addLeftTrackCoordinates(int x, int y){
        leftTrack.addCoordinates(x, y);
    }

    public void addRightTrackCoordinates(int x, int y){
        rightTrack.addCoordinates(x, y);
    }
}
