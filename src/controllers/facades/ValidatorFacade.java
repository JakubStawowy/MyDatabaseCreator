package controllers.facades;

import exceptions.*;
import controllers.validators.ColumnValidator;
import controllers.validators.DataValidator;
import controllers.validators.TableValidator;
import controllers.templates.ColumnValidatorApi;
import controllers.templates.DataValidatorApi;
import controllers.templates.TableValidatorApi;
import controllers.templates.ValidatorFacadeApi;

import java.util.List;

public final class ValidatorFacade implements ValidatorFacadeApi {
    private final TableValidatorApi tableValidator;
    private final ColumnValidatorApi columnValidator;
    private final DataValidatorApi dataValidator;
    private static ValidatorFacadeApi instance;
    private ValidatorFacade(TableValidatorApi tableValidator, ColumnValidatorApi columnValidator, DataValidatorApi dataValidator) {
        this.tableValidator = tableValidator;
        this.columnValidator = columnValidator;
        this.dataValidator = dataValidator;
    }

    public static ValidatorFacadeApi getInstance() {
        if (instance == null)
            instance = new ValidatorFacade(
                    new TableValidator(),
                    new ColumnValidator(),
                    new DataValidator()
            );
        return instance;
    }
    @Override
    public void checkColumnName(String columnName) throws BadColumnNameException {
        columnValidator.checkColumnName(columnName);
    }

    @Override
    public void checkColumnNameUniqueness(String columnName, List<String> columnNames) throws RepeatedColumnNameException {
        columnValidator.checkColumnNameUniqueness(columnName, columnNames);
    }

    @Override
    public String checkSize(String size) throws BadTypeSizeException {
        return dataValidator.checkSize(size);
    }

    @Override
    public void checkType(String type) throws BadColumnTypeException {
        dataValidator.checkType(type);
    }

    @Override
    public void checkTableName(String tableName) throws BadTableNameException {
        tableValidator.checkTableName(tableName);
    }

    @Override
    public void checkNumberOfColumns(int numberOfColumns) throws BadColumnNumberException {
        tableValidator.checkNumberOfColumns(numberOfColumns);
    }
}
