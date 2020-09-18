package Logic.MyExceptions;

public class BadColumnTypeException extends MyException {

    public BadColumnTypeException(){
        this.message = "Bad Column Type";
    }
    public BadColumnTypeException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
