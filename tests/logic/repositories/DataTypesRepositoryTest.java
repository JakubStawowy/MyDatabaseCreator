package logic.repositories;

import logic.DatabaseFacade;
import logic.database.TestDatabaseCreator;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DataTypesRepositoryTest {

    @Test
    void isNumeric() {
        try {
//            DatabaseFacade databaseFacade = new DatabaseFacade(host, port, database, username, password);
            DatabaseFacade databaseFacade = TestDatabaseCreator.createTestDatabase();
            String[] numericTypes = {"Bit", "tinyInt", "smallint","meDiumint", "biGInt",
                    "int(20)", "boolean", "bool", "INTEGER", "float" ,"double", "decimal", "dec"};
            String[] stringTypes ={"char", "varchar", "binary", "tinyblob", "tinytext", "text",
                    "blob", "mediumtext", "mediumblob", "longtext", "longblob", "enum", "set"};
            for(String type: numericTypes)
                assertEquals(true, DataTypesRepository.isNumeric(type));
            for(String type: stringTypes)
                assertEquals(false, DataTypesRepository.isNumeric(type));
            databaseFacade.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}