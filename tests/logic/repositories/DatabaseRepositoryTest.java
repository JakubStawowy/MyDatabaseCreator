package logic.repositories;

import logic.facades.DatabaseFacade;
import logic.database.TestDatabaseCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseRepositoryTest {

    private DatabaseFacade databaseFacade;

    @BeforeEach
    void setUp() throws SQLException {
        databaseFacade = TestDatabaseCreator.createTestDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {

        databaseFacade.dropAllTables();
        databaseFacade.disconnect();
    }

    @Test
    void importDatabase() {
        assertDoesNotThrow(databaseFacade::importDatabase);
    }

    @Test
    void getPrimaryKeys() {
        Map<String, String> primaryKeysMap = new HashMap<>();
        assertDoesNotThrow(databaseFacade::getPrimaryKeys);
        for(int i = 0 ; i < 5 ; i++){
            primaryKeysMap.put("testtable"+i, "col1 int(11)");
        }
        assertDoesNotThrow(() -> assertEquals(primaryKeysMap, databaseFacade.getPrimaryKeys()));
    }

    @Test
    void getTableNames() {
        try {
            int size = 5;
            List<String> tableNames = databaseFacade.getTableNames();
            Logger.getGlobal().log(Level.INFO, String.valueOf(tableNames));
            assertEquals(size, tableNames.size());

            for (int index = 0 ; index < size; index++)
                assertEquals("testtable" + (index), tableNames.get(index));

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
}