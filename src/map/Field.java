package map;

import element.Element;
import railwaystation.RailwayStation;

public class Field {
    private Element element;
    private FieldType fieldType;
    private RailwayStation station;
    private boolean underVoltage;

    private Coordinates coordinates;

    private Field(int x, int y){
        this.coordinates = new Coordinates(x, y);
        this.underVoltage = false;
        this.element = null;
    }

    public Field(int x, int y, FieldType fieldType){
        this(x, y);
        this.fieldType = fieldType;
    }

    public Field(int x, int y, RailwayStation station){
        this(x, y);
        this.station = station;
    }

    public Element getElement(){
        return this.element;
    }

    public void setElement(Element element){
        this.element = element;
    }

    public FieldType getFieldType(){
        return this.fieldType;
    }

    public void setFieldType(FieldType fieldType){
        this.fieldType = fieldType;
    }

    public RailwayStation getStation(){
        return this.station;
    }

    public void setStation(RailwayStation station){
        this.station = station;
    }

    public boolean isUnderVoltage(){
        return this.underVoltage;
    }

    public void setUnderVoltage(boolean underVoltage){
        this.underVoltage = underVoltage;
    }

    public Coordinates getCoordinates(){
        return new Coordinates(this.coordinates);
    }

    //public void setCoordinates(Coordinates coordinates){
    //    this.coordinates = coordinates;
    //}

    public int getX(){
        return coordinates.getX();
    }

    public int getY(){
        return coordinates.getY();
    }

    public boolean isEmpty(){
        return (this.element == null && this.station == null);
    }

    //@Override
    //// mozda cak i ne treba da redefinisemo ovu metodu jer imamo jednu istancu svakog polja i treba da ih poredimo po adresi
    //public boolean equals(Object object){
    //    if(this == object){
    //        return true;
    //    }
//
    //    if(object == null || (getClass() != object.getClass())){
    //        return false;
    //    }
//
    //    Field other = (Field)object;
    //    return (this.coordinates.equals(other.getCoordinates()));
    //}
//
    //@Override
    //public int hashCode(){
    //    int prime = 31;
    //    return prime + prime * (this.coordinates != null ? this.coordinates.hashCode() : 0);
    //}
}
