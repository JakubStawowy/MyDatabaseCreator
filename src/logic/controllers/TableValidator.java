package logic.controllers;

import exceptions.BadColumnNumberException;
import exceptions.BadTableNameException;
import logic.templates.TableValidatorApi;

public final class TableValidator implements TableValidatorApi {

    @Override
    public void checkTableName(final String tableName) throws BadTableNameException {
        for(char character: tableName.toCharArray()){
            if(Character.isWhitespace(character))
                throw new BadTableNameException("Bad table name. Table name cannot contain white spaces");
        }
    }

    @Override
    public void checkNumberOfColumns(final int numberOfColumns) throws BadColumnNumberException {
        if(numberOfColumns<=0)
            throw new BadColumnNumberException("Bad number of columns. You must add at least one column to a table");
    }
}
