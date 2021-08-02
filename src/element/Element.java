package element;

import static util.Constants.MIN_SPEED;

import map.Coordinates;

public abstract class Element {
    protected Coordinates coordinates;
    protected ElementColor mapColor;
    protected int speed;

    public Element(int x, int y, int speed, ElementColor mapColor){
        this.coordinates = new Coordinates(x, y);
        setSpeed(speed);
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

    public int getSpeed(){
        return this.speed;
    }

    public void setSpeed(int speed){
        if(speed < MIN_SPEED){
            this.speed = MIN_SPEED;
        } else {
            this.speed = speed;
        }
    }

    @Override
    public String toString(){
        return "(" + coordinates.getX() + ", " + coordinates.getY() + ")";
    }
}
