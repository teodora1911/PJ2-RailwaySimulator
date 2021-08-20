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
        } else {
            // ignore less or equal number of vehicles on the road
        }
    }

    public int getAdditionalNumberOfVehicles(){
        synchronized(numLock){
            int toReturn = additionalNumberOfVehicles;
            additionalNumberOfVehicles = 0;
            return toReturn;
        }
    }

    public Segment contains(Field field){
        if(leftTrack.contains(field)){
            return leftTrack;
        } else if (rightTrack.contains(field)){
            return rightTrack;
        } else {
            return null;
        }
    }
    
    public Field getStartingPoint(boolean direction){
        if(direction){
            return leftTrack.getStartingPoint(true);
        } else {
            return rightTrack.getStartingPoint(true);
        }
    }

    public Segment getPath(boolean direction){
        if(direction){
            return this.leftTrack;
        } else {
            return this.rightTrack;
        }
    }

    public void addLeftTrackField(Field field){
        leftTrack.addField(field);
    }

    public void addRightTrackField(Field field){
        rightTrack.addField(field);
    }

}
