package map;

import java.util.ArrayList;

import railwaystation.RailwayStation;
import train.Train;
import vehicle.Vehicle;

public class Railway {
    
    private int id;
    private Segment path = new Segment();
    private RailwayStation startPoint;
    private RailwayStation endPoint;

    private ArrayList<Train> movingForwardTrains = new ArrayList<>();
    private ArrayList<Train> movingBackwardTrains = new ArrayList<>();

    private boolean readyForNext;
    private Train lastAdded;

    public Railway(int id, RailwayStation startPoint, RailwayStation endPoint){
        this.id = id;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.readyForNext = true;
        this.lastAdded = null;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public Segment getPath(){
        return this.path;
    }

    public Coordinates getStartingCoordinates(boolean direction){
        return path.getStartingPoint(direction).getCoordinates();
    }

    public RailwayStation getStartStation(){
        return this.startPoint;
    }

    public RailwayStation getEndStation(){
        return this.endPoint;
    }

    public synchronized Train getPresentLastAdded(){
        if(!isEmpty(true) || !isEmpty(false)){
            return this.lastAdded;
        } else {
            return null;
        }
    }

    public void addField(Field field){
        path.addField(field);
    }

    /**
     * @return speed of previous train (if exists)
     */
    public synchronized int offerTrain(Train train, boolean direction){

        if(!isEmpty(!direction) || (path.onTheRoad() instanceof Vehicle)){
            return -1;
        }

        if(!readyForNext){
            return -1;
        } else {
            int toReturn = speedOfLastTrain(direction);
            add(train, direction);
            lastAdded = train;
            readyForNext = false;
            return toReturn;
        }
    }

    public synchronized void setReadyForNext() {
        System.out.println("Setting ready for next...");
        this.readyForNext = true;
    }

    public synchronized boolean pollTrain(Train train){
        int result = contains(train);
        if(result > 0){
            return movingForwardTrains.remove(train);
        } else if (result < 0){
            return movingBackwardTrains.remove(train);
        } else {
            return false;
        }
    }

    public synchronized int contains(Train train){
        if(movingForwardTrains.contains(train)){
            return 1;
        } else if (movingBackwardTrains.contains(train)){
            return -1;
        } else {
            return 0;
        }
    }

    public synchronized boolean isEmpty(boolean direction){
        if(direction){
            return movingForwardTrains.isEmpty();
        } else {
            return movingBackwardTrains.isEmpty();
        }
    }

    public synchronized int getTrainCount(){
        return movingForwardTrains.size() + movingBackwardTrains.size();
    }

    private synchronized void add(Train train, boolean direction){
        if(direction){
            movingForwardTrains.add(train);
        } else {
            movingBackwardTrains.add(train);
        }
    }

    private synchronized int speedOfLastTrain(boolean direction){
        if(isEmpty(direction)){
            return 0;
        } else {
            if(direction){
                return movingForwardTrains.get(movingForwardTrains.size() - 1).getSpeed();
            } else {
                return movingBackwardTrains.get(movingBackwardTrains.size() - 1).getSpeed();
            }
        }
    }
}
