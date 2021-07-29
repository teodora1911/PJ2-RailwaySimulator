package map;

import java.util.ArrayList;

public class Segment {
    
    private ArrayList<Coordinates> segment = new ArrayList<>();

    public Segment(){
        super();
    }

    public void addCoordinates(int x, int y){
        segment.add(new Coordinates(x, y));
    }

    public boolean contains(Coordinates c){
        return segment.contains(c);
    }

    public Coordinates getNextCoordinates(Coordinates c, boolean direction) {
        int index = segment.indexOf(c);
        if(index == -1){
            return null;
        }

        if(direction){
            ++index;
        } else {
            --index;
        }
        
        if(index >= 0 && index < segment.size()){
            return new Coordinates(segment.get(index));
        } else {
            return null;
        }
    }

    public Coordinates getStartingCoordinates(boolean direction){
        if(!segment.isEmpty()){
            if(direction){
                return new Coordinates(segment.get(0));
            } else {
                return new Coordinates(segment.get(segment.size() - 1));
            }
        } else {
            return null;
        }
    }
}
