package logic.controllers;

import exceptions.*;
import logic.repositories.DataTypesRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataValidatorTest {

    DataValidator dataValidator = new DataValidator();

    @Test
    void checkTableName() {
        assertThrows(BadTableNameException.class, ()->dataValidator.checkTableName("Table name"));
        assertDoesNotThrow(()->dataValidator.checkTableName("TableName"));
    }

    @Test
    void checkColumnName() {
        assertThrows(BadColumnNameException.class, ()->dataValidator.checkColumnName("Column name"));
        assertDoesNotThrow(()->dataValidator.checkColumnName("ColumnName"));
    }

    @Test
    void checkNumberOfColumns() {
        assertThrows(BadColumnNumberException.class, ()->dataValidator.checkNumberOfColumns(0));
        assertThrows(BadColumnNumberException.class, ()->dataValidator.checkNumberOfColumns(-1));
        assertDoesNotThrow(()->dataValidator.checkNumberOfColumns(1));
    }

    @Test
    void checkSize() {
        assertThrows(BadTypeSizeException.class, ()->dataValidator.checkSize("String"));
        assertThrows(BadTypeSizeException.class, ()->dataValidator.checkSize("-1"));
        assertThrows(BadTypeSizeException.class, ()->dataValidator.checkSize("0"));
        assertDoesNotThrow(()->dataValidator.checkSize("1"));
    }

    @Test
    void checkType() {
        assertThrows(BadColumnTypeException.class, ()->dataValidator.checkType("Bad type"));
        for(String type: DataTypesRepository.getAllTypes()){
            assertDoesNotThrow(()->dataValidator.checkType(type));
        }
    }

    @Test
    void checkColumnNameUniqueness() {
        List<String> columnNameVector = Arrays.asList("column_1", "column_2", "column_3");
        assertThrows(RepeatedColumnNameException.class, ()->dataValidator.checkColumnNameUniqueness("column_2", columnNameVector));
        assertDoesNotThrow(()->dataValidator.checkColumnNameUniqueness("column_4", columnNameVector));
    }
}