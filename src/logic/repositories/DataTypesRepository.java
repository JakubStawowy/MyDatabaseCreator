package logic.repositories;

public class DataTypesRepository {

    private final static String[] numericTypes = {"bit", "tinyint", "smallint","mediumint", "bigint",
            "int", "boolean", "bool", "integer", "float" ,"double", "decimal", "dec"};
    private final static String[] stringTypes ={"char", "varchar", "binary", "tinyblob", "tinytext", "text",
            "blob", "mediumtext", "mediumblob", "longtext", "longblob", "enum", "set"};

    public static String[] getNumericTypes() {
        return numericTypes;
    }

    public static String[] getStringTypes() {
        return stringTypes;
    }

    /*
     * isNumeric method is used to check if given type is numeric or no.
     *
     * @param String type
     * @return Boolean
     * */
    public static Boolean isNumeric(String type) {

        type = type.toLowerCase();
        for (String types : numericTypes) {
            if(type.length()>=types.length() && types.equals(type.substring(0,types.length())))
                return true;
            else if(type.length()<types.length() && (type.equals(types.substring(0,type.length()))))
                return true;
        }
        return false;
    }
}
