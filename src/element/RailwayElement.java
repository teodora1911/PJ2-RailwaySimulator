package element;

public abstract class RailwayElement extends Element {
    
    protected String label;

    public RailwayElement(int x, int y, int speed, ElementColor mapColor, String label){
        super(x, y, speed, mapColor);
        this.label = label;
    }

    public String getLabel(){
        return this.label;
    }
}
