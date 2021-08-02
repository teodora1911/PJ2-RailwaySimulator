package locomotive;

public enum Engine {
    STEAM(1),
    DIESEL(2),
    ELECTRIC(3);

    private int value;

    private Engine(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static Engine fromId(int id){
        for(Engine e : Engine.values()){
            if(e.getValue() == id){
                return e;
            }
        }
        return null;
    }
}
