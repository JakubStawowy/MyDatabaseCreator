package exceptions;

public class NoPrimaryKeyException extends Exception {
    public NoPrimaryKeyException(String message) {
        super(message);
    }
}
