package Logic.MyExceptions;

public class EmptyTableException extends MyException {
    public EmptyTableException(String message){
        this.message = message;
    }
    public EmptyTableException(){
        message = "Empty table!";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
