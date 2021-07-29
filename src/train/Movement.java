package train;

import java.io.Serializable;
import java.util.ArrayList;

import map.Coordinates;

public class Movement implements Serializable {
    private ArrayList<Coordinates> path = new ArrayList<>();
    private long movementTime;
    private long stationRetention;
    // lista zeljeznickih stanica
    private int trainId;

    public Movement(){
        super();
        movementTime = 0;
        stationRetention = 0;
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

    public long getStationRetention(){
        return this.stationRetention;
    }

    public void setStationRetention(long stationRetention){
        this.stationRetention = stationRetention;
    }

    public void addNewCoordinates(Coordinates c){
        if(!path.contains(c)){
            path.add(c);
        }
    }

    // dodaj stanicu
    // get stanice
    // set stanice

    public String toString() {
		String toReturn = "Voz [" + trainId + "]\n";
		toReturn = "Putanja : " + this.path.toString() + "\n";
		toReturn += "Vrijeme kretanja : " + this.movementTime + "\n";
		toReturn += "Ukupno vrijeme zadrzavanja na stanicama : " + this.stationRetention + "\n";
		//toReturn += "Predjene stanice : " + this.stanice.toString() + "\n";
		return toReturn;
	}
}
