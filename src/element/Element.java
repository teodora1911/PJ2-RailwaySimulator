package element;

import map.Coordinates;

public abstract class Element {
    protected Coordinates coordinates;
    protected ElementColor mapColor;
    protected String label;

    public Element(int x, int y, ElementColor mapColor, String label){
        this.coordinates = new Coordinates(x, y);
        this.label = label;
        this.mapColor = mapColor;
    }

    public int getX(){
        return this.coordinates.getX();
    }

    public void setX(int x){
        this.coordinates.setX(x);
    }

    public int getY(){
        return this.coordinates.getY();
    }

    public void setY(int y){
        this.coordinates.setY(y);
    }

    public Coordinates getCoordinates(){
        return this.coordinates;
    }

    public void setCoordinates(Coordinates c){
        this.coordinates = c;
    }

    public ElementColor getMapColor(){
        return this.mapColor;
    }

    public void setMapColor(ElementColor mapColor){
        this.mapColor = mapColor;
    }

    public String getLabel(){
        return this.label;
    }

    public void setLabel(String label){
        this.label = label;
    }

    @Override
    public String toString(){
        return "(" + coordinates.getX() + ", " + coordinates.getY() + ")";
    }
}
