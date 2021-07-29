package element;

import map.Coordinates;

public abstract class Element {
    protected Coordinates coordinates;
    protected boolean active;
    protected ElementColor mapColor;

    public Element(int x, int y, ElementColor mapColor){
        this.coordinates = new Coordinates(x, y);
        this.mapColor = mapColor;
        this.active = true;
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

    public boolean isActive(){
        return this.active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public ElementColor getMapColor(){
        return this.mapColor;
    }

    public void setMapColor(ElementColor mapColor){
        this.mapColor = mapColor;
    }

    @Override
    public String toString(){
        return "(" + coordinates.getX() + ", " + coordinates.getY() + ")";
    }
}
