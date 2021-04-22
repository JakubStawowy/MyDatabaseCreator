package controllers.validiators;

import controllers.validators.DataValidator;
import exceptions.*;
import database.repositories.DataTypesRepository;
import controllers.templates.DataValidatorApi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataValidatorTest {

    private static DataValidatorApi dataValidator;

    @BeforeAll
    static void beforeAll() {
        dataValidator = new DataValidator();
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

}