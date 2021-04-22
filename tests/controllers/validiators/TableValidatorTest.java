package controllers.validiators;

import controllers.validators.TableValidator;
import exceptions.BadColumnNumberException;
import exceptions.BadTableNameException;
import controllers.templates.TableValidatorApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TableValidatorTest {

    private static TableValidatorApi tableValidator;

    @BeforeAll
    static void beforeAll() {
        tableValidator = new TableValidator();
    }


    @Test
    void checkTableName() {
        assertThrows(BadTableNameException.class, ()->tableValidator.checkTableName("Table name"));
        assertDoesNotThrow(()->tableValidator.checkTableName("TableName"));
    }


    @Test
    void checkNumberOfColumns() {
        assertThrows(BadColumnNumberException.class, ()->tableValidator.checkNumberOfColumns(0));
        assertThrows(BadColumnNumberException.class, ()->tableValidator.checkNumberOfColumns(-1));
        assertDoesNotThrow(()->tableValidator.checkNumberOfColumns(1));
    }
}