package Logic.MyExceptions;

public class BadTypeLengthException extends MyException {
    public BadTypeLengthException(){
        this.message = "Bad Type Length";
    }
    public BadTypeLengthException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
