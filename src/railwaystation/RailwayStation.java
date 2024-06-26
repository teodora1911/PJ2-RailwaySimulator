package railwaystation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

import map.Coordinates;
import map.Railway;
import train.Train;

public class RailwayStation extends Thread {

    private Vector<Train> trainQueue = new Vector<>();
    private ArrayList<Railway> paths = new ArrayList<>();
    private ArrayList<Coordinates> coordinates = new ArrayList<>();

    public RailwayStation(String name){
        super(name);
    }


    public void addTrainToWaitingQueue(Train train){
        trainQueue.add(train);
    }


    public void addRailway(Railway railway){
        paths.add(railway);
    }

    public void addCoordinates(int x, int y){
        coordinates.add(new Coordinates(x, y));
    }

    public List<Coordinates> getCoordinates(){
        List<Coordinates> toReturn = new ArrayList<>();
        coordinates.stream().forEach(c -> toReturn.add(new Coordinates(c)));
        return toReturn;
    }

    @Override
    public void run(){

        while(true){

            if(!trainQueue.isEmpty()){

                for(Railway path : paths){
                    Train nextTrain = null;
                    Optional<Train> potentialTrain = trainQueue.stream().filter(t -> {
                        RailwayStation nextStation = t.getNextStation();
                        return (nextStation == path.getStartStation() || nextStation == path.getEndStation());
                    }).findFirst();

                    if(!potentialTrain.isEmpty()){
                        nextTrain = potentialTrain.get();
                    }

                    if(nextTrain != null){
                        boolean direction = (this == path.getStartStation());
                        int speed = path.offerTrain(nextTrain, direction);

                        if(speed >= 0){
                           if((speed != 0) && (speed > nextTrain.getSpeed())){
                               nextTrain.changeSpeed(speed);
                           }
                           nextTrain.setStartingPosition(path.getStartingCoordinates(direction));
                           Object lock = nextTrain.getMovementLock();
                           
                           synchronized(lock){
                               lock.notify();
                           }

                           trainQueue.remove(nextTrain);
                        }
                    }
                }
            }
        }
    }

    public void outOfStation(Train train){
        for(Railway path : paths){
            if(path.contains(train) != 0){
                path.setReadyForNext();
            }
        }
    }

    public void inTheStation(Train train){
        for(Railway path : paths){
            if(path.pollTrain(train)){
                train.resetSpeed();
                return;
            }
        }
    }
}
