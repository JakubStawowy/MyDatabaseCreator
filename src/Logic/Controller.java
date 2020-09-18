package Logic;

import Logic.MyExceptions.*;

import java.util.Vector;

public class Controller {
    private final String[] numericTypes = {"bit", "tinyint", "smallint","mediumint", "bigint",
            "int", "boolean", "bool", "integer", "float" ,"double", "decimal", "dec"};
    private final String[] stringTypes ={"char", "varchar", "binary", "tinyblob", "tinytext", "text",
            "blob", "mediumtext", "mediumblob", "longtext", "longblob", "enum", "set"};

    public void checkTableName(String tableName) throws BadTableNameException {
        for(char character: tableName.toCharArray()){
            if(Character.isWhitespace(character))
                throw new BadTableNameException("Bad table name. Table name cannot contain white spaces");
        }
    }
    public void checkColumnName(String columnName) throws BadColumnNameException{
        for(char character: columnName.toCharArray()){
            if(Character.isWhitespace(character))
                throw new BadColumnNameException("Bad column name. Column name cannot contain white spaces");
        }
    }
    public void checkNumberOfColumns(int numberOfColumns) throws BadColumnNumberException {
        if(numberOfColumns==0)
            throw new BadColumnNumberException("Bad number of columns. You must add at least one column to a table");
    }
    public String checkLength(String length) throws BadTypeLengthException {
        if(!(length.equals("Length") || length.equals(""))) {

            try {
                if(Integer.parseInt(length)<=0)
                    throw  new BadTypeLengthException("Bad length. Length cannot be less or equal 0");
            } catch (NumberFormatException ignored) {
                throw new BadTypeLengthException("Bad length. Length must be a number");
            }
            return "("+length+")";
        }
        else return "";
    }
    public void checkType(String type) throws BadColumnTypeException {
        boolean flag = true;
        if(type.equals("null"))
            throw new BadColumnTypeException("Choose column type");
        for(String numericType: numericTypes){
            if (numericType.equals(type.toLowerCase())) {
                flag = false;
                break;
            }
        }
        for(String stringType: stringTypes){
            if (stringType.equals(type.toLowerCase())) {
                flag = false;
                break;
            }
        }
        if(flag)
            throw new BadColumnTypeException("Bad Column Type");
    }
    public void checkColumnNameUniqueness(String columnName, Vector<String> columnNames) throws RepeteadColumnNameException {
        for(String name: columnNames){
            if(name.equals(columnName))
                throw new RepeteadColumnNameException();

        }
    }
    public void checkNamesTypesQuantity(Vector<String> columnNames, Vector<String> columnTypes) throws BadNamesTypesQuantityException {
        if(columnNames.size()!=columnTypes.size())
            throw new BadNamesTypesQuantityException();
    }
}
