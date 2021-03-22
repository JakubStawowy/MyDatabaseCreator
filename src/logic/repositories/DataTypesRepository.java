package logic.repositories;

public final class DataTypesRepository {

    private final static String[] numericTypes = {"bit", "tinyint", "smallint","mediumint", "bigint",
            "int", "boolean", "bool", "integer", "float" ,"double", "decimal", "dec"};
    private final static String[] stringTypes ={"char", "varchar", "binary", "tinyblob", "tinytext", "text",
            "blob", "mediumtext", "mediumblob", "longtext", "longblob", "enum", "set"};
    private final static String[] dateAndTimeTypes = {
            "date", "datetime", "timestamp", "time", "year"
    };
    private final static String[] constraints = {
            "Not Null", "Unique"
    };

    public static String[] getNumericTypes() {
        return numericTypes;
    }

    public static String[] getStringTypes() {
        return stringTypes;
    }

    public static String[] getDateAndTimeTypes() {
        return dateAndTimeTypes;
    }

    public static String[] getConstraints() {
        return constraints;
    }

    public static Boolean isNumeric(final String type) {

        String lowerCaseType = type.toLowerCase();
        for (String types : numericTypes) {
            if(lowerCaseType.length()>=types.length() && types.equals(lowerCaseType.substring(0,types.length())))
                return true;
            else if(lowerCaseType.length()<types.length() && (lowerCaseType.equals(types.substring(0,type.length()))))
                return true;
        }
        return false;
    }

    public static String[] getAllTypes() {

        String[] allTypes = new String[numericTypes.length + stringTypes.length + dateAndTimeTypes.length];
        System.arraycopy(numericTypes, 0, allTypes, 0, numericTypes.length);
        System.arraycopy(stringTypes, 0, allTypes, numericTypes.length, stringTypes.length);
        System.arraycopy(dateAndTimeTypes, 0, allTypes, numericTypes.length+stringTypes.length, dateAndTimeTypes.length);

        return allTypes;
    }
}
