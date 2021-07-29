package element;

public abstract class RailwayElement extends Element {
    
    protected String label;

    public RailwayElement(int x, int y, ElementColor mapColor, String label){
        super(x, y, mapColor);
        this.label = label;
    }

    public String getLabel(){
        return this.label;
    }
}
