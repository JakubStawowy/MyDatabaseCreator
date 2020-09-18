package Logic.MyExceptions;

public class RepeteadColumnNameException extends MyException {
    public RepeteadColumnNameException(){
        this.message = "Repeated column name";
    }
    public RepeteadColumnNameException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
