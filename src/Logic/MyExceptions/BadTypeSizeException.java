package Logic.MyExceptions;

public class BadTypeSizeException extends MyException {
    public BadTypeSizeException(){
        this.message = "Bad Type Length";
    }
    public BadTypeSizeException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
