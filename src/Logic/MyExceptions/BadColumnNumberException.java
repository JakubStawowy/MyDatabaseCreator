package Logic.MyExceptions;

public class BadColumnNumberException extends MyException {
    public BadColumnNumberException(){
        this.message = "Bad Column Number";
    }
    public BadColumnNumberException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
