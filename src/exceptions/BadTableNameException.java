package exceptions;

public class BadTableNameException extends Exception {
    public BadTableNameException(String message) {
        super(message);
    }
}
