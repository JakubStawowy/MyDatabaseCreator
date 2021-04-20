package logic.templates;

import exceptions.BadColumnNumberException;
import exceptions.BadTableNameException;

public interface TableValidatorApi {

    /*
     * checkTableName
     *
     * this method throws new BadTableNameException if table name contains white spaces
     *
     * @param String tableName
     *
     * @throws BadTableNameException
     * */
    void checkTableName(final String tableName) throws BadTableNameException;


    /*
     * checkNumberOfColumns
     *
     * this method throws BadColumnNumberException if number of columns equals 0
     *
     * @param int numberOfColumns
     *
     * @throws BadColumnNumberException
     * */
    void checkNumberOfColumns(final int numberOfColumns) throws BadColumnNumberException;
}
