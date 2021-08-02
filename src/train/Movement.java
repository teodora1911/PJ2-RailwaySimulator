package train;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import map.Coordinates;

public class Movement implements Serializable {
    private ArrayList<Coordinates> path = new ArrayList<>();
    private long movementTime;
    private HashMap<String, Long> stationsRetentionTime = new HashMap<>();
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
        // pozivamo ovu metodu kada voz krene da ceka na stanici - 1.
        stationsRetentionTime.put(stationName, retentionTime);
    }

    public void updateStationRetentionTime(String stationName, Long retentionTime){
        // pozivamo ovu metodu kada voz krene sa date stanice - 2.
        Long duration = retentionTime - stationsRetentionTime.get(stationName);
        stationsRetentionTime.put(stationName, duration);
    }

    public void addNewCoordinates(Coordinates c){
        if(!path.contains(c)){
            path.add(c);
        }
    }

    public String toString() {
		String toReturn = "Voz [" + trainId + "]\n";
		toReturn = "Putanja : " + this.path.toString() + "\n";
		toReturn += "Vrijeme kretanja : " + this.movementTime + "\n";
		toReturn += "Vremena zadrzavanja na stanicama : \n";
        for(Map.Entry<String, Long> set : stationsRetentionTime.entrySet()){
            toReturn += ("\t" + set.getKey() + " - " + set.getValue() + "\n");
        }

		return toReturn;
	}
}
