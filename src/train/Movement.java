package train;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import map.Coordinates;
import railwaystation.RailwayStation;

public class Movement implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Coordinates> path = new ArrayList<>();
    private long movementTime;
    private HashMap<String, Long> stationsRetentionTime = new HashMap<>();
    private ArrayList<String> stationPath = new ArrayList<>();
    private int trainId;

    public Movement(){
        super();
        movementTime = 0;
    }

    public int getTrainId(){
        return this.trainId;
    }

    public void setTrainId(int trainId){
        this.trainId = trainId;
    }

    public ArrayList<Coordinates> getPath(){
        return this.path;
    }

    public ArrayList<String> getStationPath(){
        return this.stationPath;
    }

    public void setStationPath(LinkedList<RailwayStation> stations){
        stations.stream().forEach(s -> stationPath.add(s.getName()));
    }

    public long getMovementTime(){
        return movementTime;
    }

    public void setMovementTime(long movementTime){
        this.movementTime = movementTime;
    }

    public void updateMovementTime(long movementTime){
        this.movementTime = movementTime - this.movementTime;
    }

    public void setStationRetentionTime(String stationName, Long retentionTime){
        stationsRetentionTime.put(stationName, retentionTime);
    }

    public void updateStationRetentionTime(String stationName, Long retentionTime){
        Long duration = retentionTime - stationsRetentionTime.get(stationName);
        stationsRetentionTime.put(stationName, duration);
    }

    public void addNewCoordinates(Coordinates c){
        if(!path.contains(c)){
            path.add(c);
        }
    }

    public String toString() {
        String toReturn = "";
		toReturn += "Voz [" + this.trainId + "]\n";
        toReturn += "Posjecene stanice : " + this.stationPath.toString() + "\n";
		toReturn += "Putanja : \n";
        int i = 0;
        for(Coordinates t : this.path){
            toReturn += t.toString() + " ";
            ++i;
            if(i % 5 == 0){
                toReturn += "\n";
            }
        }
		toReturn += "\nVrijeme kretanja : " + (this.movementTime / 1_000) + "\n";
		toReturn += "Vremena zadrzavanja na stanicama : \n";
        for(Map.Entry<String, Long> set : stationsRetentionTime.entrySet()){
            toReturn += ("\t" + set.getKey() + " - " + (set.getValue() / 1_000) + "\n");
        }

		return toReturn;
	}
}
