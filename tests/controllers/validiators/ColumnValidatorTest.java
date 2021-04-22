package controllers.validiators;

import controllers.validators.ColumnValidator;
import exceptions.BadColumnNameException;
import exceptions.RepeatedColumnNameException;
import controllers.templates.ColumnValidatorApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColumnValidatorTest {
    private static ColumnValidatorApi columnValidator;

    @BeforeAll
    static void beforeAll() {
        columnValidator = new ColumnValidator();
    }

    @Test
    void checkColumnName() {
        assertThrows(BadColumnNameException.class, ()->columnValidator.checkColumnName("Column name"));
        assertDoesNotThrow(()->columnValidator.checkColumnName("ColumnName"));
    }

    @Test
    void checkColumnNameUniqueness() {
        List<String> columnNameVector = Arrays.asList("column_1", "column_2", "column_3");
        assertThrows(RepeatedColumnNameException.class, ()->columnValidator.checkColumnNameUniqueness("column_2", columnNameVector));
        assertDoesNotThrow(()->columnValidator.checkColumnNameUniqueness("column_4", columnNameVector));
    }
}