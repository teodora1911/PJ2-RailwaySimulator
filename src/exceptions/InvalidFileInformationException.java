package exceptions;

public class InvalidFileInformationException extends Exception {
    
    public InvalidFileInformationException(){
        super();
    }

    public InvalidFileInformationException(String message){
        super(message);
    }
}
