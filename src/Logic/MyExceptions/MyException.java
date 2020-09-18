package Logic.MyExceptions;

public abstract class MyException extends Exception {
    protected String message;
    public MyException(){
        message = null;
    }
    public MyException(String message){
        this.message = message;

    }
    public abstract String getMessage();
}
