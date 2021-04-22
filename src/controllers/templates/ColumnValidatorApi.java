package controllers.templates;

import exceptions.BadColumnNameException;
import exceptions.RepeatedColumnNameException;

import java.util.List;

public interface ColumnValidatorApi {

    /*
     * checkColumnName
     *
     * this method throws new BadColumnNameException if column name contains white spaces
     *
     * @param String columnName
     *
     * @throws BadTableNameException
     * */
    void checkColumnName(final String columnName) throws BadColumnNameException;


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
    void checkColumnNameUniqueness(final String columnName, final List<String> columnNames) throws RepeatedColumnNameException;
}
