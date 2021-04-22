package controllers.validators;

import exceptions.BadColumnNameException;
import exceptions.RepeatedColumnNameException;
import controllers.templates.ColumnValidatorApi;

import java.util.List;

public final class ColumnValidator implements ColumnValidatorApi {

    @Override
    public void checkColumnName(final String columnName) throws BadColumnNameException {
        for(char character: columnName.toCharArray()){
            if(Character.isWhitespace(character))
                throw new BadColumnNameException("Bad column name. Column name cannot contain white spaces");
        }
    }

    @Override
    public void checkColumnNameUniqueness(final String columnName, final List<String> columnNames) throws RepeatedColumnNameException {
        for(String name: columnNames){
            if(name.equals(columnName))
                throw new RepeatedColumnNameException(name+" - repeated column name");
        }
    }
}
