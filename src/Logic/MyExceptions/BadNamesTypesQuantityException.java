package Logic.MyExceptions;

public class BadNamesTypesQuantityException extends MyException{
    @Override
    public String getMessage() {
        return "Error. The number of column names and types do not match";
    }
}
