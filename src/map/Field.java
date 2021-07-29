package map;

import element.Element;

public class Field {
    private Element element;
    private FieldType fieldType; // null - if it's empty
    private boolean underVoltage;

    // da li je potrebno da sadrzi koordinate
    private int x;
    private int y;
    // Coordinate coordinates;

    public Field(int x, int y, FieldType fieldType, boolean underVoltage, Element element){
        this.x = x;
        this.y = y;
        this.fieldType = fieldType;
        this.underVoltage = underVoltage;
        this.element = element;
    }

    public Field(int x, int y, FieldType fieldType){
        this(x, y, fieldType, false, null);
    }

    public Field(int x, int y, FieldType fieldType, boolean underVoltage){
        this(x, y, fieldType, underVoltage, null);
    }

    public Field(int x, int y){
        this(x, y, null, false, null);
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

    public boolean isUnderVoltage(){
        return this.underVoltage;
    }

    public void setUnderVoltage(boolean underVoltage){
        this.underVoltage = underVoltage;
    }

    public int getX(){
        return this.x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return this.y;
    }

    public void setY(int y){
        this.y = y;
    }
}
