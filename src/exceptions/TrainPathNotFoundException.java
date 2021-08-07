package exceptions;

public class TrainPathNotFoundException extends Exception {
    
    public TrainPathNotFoundException(){
        super();
    }

    public TrainPathNotFoundException(String message){
        super(message);
    }
}
