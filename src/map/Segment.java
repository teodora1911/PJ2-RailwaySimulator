package map;

import java.util.ArrayList;
import java.util.Optional;

import element.Element;

public class Segment {
    
    private ArrayList<Field> segment = new ArrayList<>();

    public Segment(){
        super();
    }

    public void addField(Field field){
        segment.add(field);
    }

    public boolean contains(Field field){
        return segment.contains(field);
    }

    public Field getNextField(Field previous, boolean direction) {
        int index = segment.indexOf(previous);
        if(index == -1){
            return null;
        }

        if(direction){
            ++index;
        } else {
            --index;
        }
        
        if(index >= 0 && index < segment.size()){
            return segment.get(index);
        } else {
            return null;
        }
    }

    public Field getStartingPoint(boolean direction){
        if(!segment.isEmpty()){
            if(direction){
                return segment.get(0);
            } else {
                return segment.get(segment.size() - 1);
            }
        } else {
            return null;
        }
    }

    public Field getEndPoint(boolean direction){
        return getStartingPoint(!direction);
    }

    public Element onTheRoad(){
        Optional<Field> field = segment.stream().filter(f -> f.getElement() != null).findAny();

        if(field.isEmpty()){
            return null;
        } else {
            return field.get().getElement();
        }
    }
}
