package Logic.MyExceptions;

public class NoPrimaryKeyException extends MyException {
    public NoPrimaryKeyException(){
        message = "No Primary Key chosen. Create table anyway?";
    }
}
