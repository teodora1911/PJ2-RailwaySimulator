package map;

import java.io.Serializable;

import util.Constants;

public class Coordinates implements Serializable {
    
    private int x;
    private int y;

    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Coordinates(Coordinates c){
        if(c != null){
            this.x = c.getX();
            this.y = c.getY();
        }
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

    @Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

    @Override
	public boolean equals(Object object) {
		if(this == object){
            return true;
        }

		if((object == null) || (getClass() != object.getClass())){
            return false;
        }

		Coordinates other = (Coordinates)object;
		return ((this.x == other.getX()) && (this.y == other.getY()));
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
        int result = 1;

        result = prime * result + (int)(x ^ (x >>> Integer.MAX_VALUE));
        result = prime * result + (int)(y ^ (y >>> Integer.MAX_VALUE));

        return result;
	}

    public boolean isValid(){
        return validCoordinates(this.x, this.y);
    }

    public static boolean validCoordinates(int x, int y){
        return (x >= 0 && x < Constants.MAP_SIZE && y >= 0 && y < Constants.MAP_SIZE);
    }
}
