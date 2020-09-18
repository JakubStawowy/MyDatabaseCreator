package Logic.MyExceptions;

public class BadTableNameException extends MyException {
    public BadTableNameException(){
        this.message = "Bad Table Name";
    }
    public BadTableNameException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
