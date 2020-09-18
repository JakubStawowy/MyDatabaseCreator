package Logic.MyExceptions;

public class BadColumnNameException extends MyException {
    public BadColumnNameException(){
        this.message = "Bad Column Name";
    }
    public BadColumnNameException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
