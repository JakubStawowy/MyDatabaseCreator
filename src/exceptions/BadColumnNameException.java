package exceptions;

public class BadColumnNameException extends Exception {
    public BadColumnNameException(String message){
        super(message);
    }
}
