package Logic;

import GUI.CreateTableWindow;
import Logic.MyExceptions.*;

import java.util.Vector;
/*
* Controller
*
* This class contains methods that check if values typed by user are correct.
* */
public class Controller {
    private final String[] numericTypes = {"bit", "tinyint", "smallint","mediumint", "bigint",
            "int", "boolean", "bool", "integer", "float" ,"double", "decimal", "dec"};
    private final String[] stringTypes ={"char", "varchar", "binary", "tinyblob", "tinytext", "text",
            "blob", "mediumtext", "mediumblob", "longtext", "longblob", "enum", "set"};

    /*
    * checkTableName
    *
    * this method throws new BadTableNameException if table name contains white spaces
    *
    * @param String tableName
    *
    * @throws BadTableNameException
    * */
    public void checkTableName(String tableName) throws BadTableNameException {
        for(char character: tableName.toCharArray()){
            if(Character.isWhitespace(character))
                throw new BadTableNameException("Bad table name. Table name cannot contain white spaces");
        }
    }
    /*
    * checkColumnName
    *
    * this method throws new BadColumnNameException if column name contains white spaces
    *
    * @param String columnName
    *
    * @throws BadTableNameException
    * */
    public void checkColumnName(String columnName) throws BadColumnNameException{
        for(char character: columnName.toCharArray()){
            if(Character.isWhitespace(character))
                throw new BadColumnNameException("Bad column name. Column name cannot contain white spaces");
        }
    }
    /*
    * checkNumberOfColumns
    *
    * this method throws BadColumnNumberException if number of columns equals 0
    *
    * @param int numberOfColumns
    *
    * @throws BadColumnNumberException
    * */
    public void checkNumberOfColumns(int numberOfColumns) throws BadColumnNumberException {
        if(numberOfColumns==0)
            throw new BadColumnNumberException("Bad number of columns. You must add at least one column to a table");
    }
    /*
    * checkSize
    *
    * this method throws new BadTypeSizeException if type size is less or equal 0 or when size cannot be casted to int.
    *
    * @param String size
    *
    * @return String
    *
    * @throws BadTypeSizeException
    * */
    public String checkSize(String size) throws BadTypeSizeException {
        if(!(size.equals("Size") || size.equals(""))) {
            try {
                if(Integer.parseInt(size)<=0)
                    throw  new BadTypeSizeException("Bad length. Length cannot be less or equal 0");
            } catch (NumberFormatException ignored) {
                throw new BadTypeSizeException("Bad length. Length must be a number");
            }
            return "("+size+")";
        }
        else return "";
    }
    /*
    * checkType
    *
    * this method throws new BadColumnTypeException if column type wasn't selected
    *
    * @param String type
    *
    * @throws BadColumnTypeException
    */
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
        if(flag) {
            throw new BadColumnTypeException("Bad Column Type");
        }
    }
    /*
    * checkColumnNameUniqueness
    *
    * this method throws new RepeteadColumnNameException if column name was repeated while creating new table
    *
    * @param String columnName
    * @param Vector<String> columnNames
    *
    * @throws RepeatedColumnNameException
    * */
    public void checkColumnNameUniqueness(String columnName, Vector<String> columnNames) throws RepeteadColumnNameException {
        for(String name: columnNames){
            if(name.equals(columnName))
                throw new RepeteadColumnNameException();
        }
    }
    /*
    * checkNamesTypesQuantity
    *
    * this method throws new BadNamesTypesQuantityException if number of column names is not equal number of column types of created table
    *
    * @param CreateTableWindow createTableWindow
    *
    * @throws BadNamesTypesQuantityException
    * */
    public void checkNamesTypesQuantity(CreateTableWindow createTableWindow) throws BadNamesTypesQuantityException {
        int numberOfNames = createTableWindow.getColumnNames().size();
        int numberOfTypes = createTableWindow.getColumnTypes().size();
        int numberOfConstraints = createTableWindow.getConstraintsVector().size();
        if(numberOfNames!=numberOfTypes || numberOfNames!=numberOfConstraints)
            throw new BadNamesTypesQuantityException();
    }

}
