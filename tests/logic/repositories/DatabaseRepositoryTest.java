package logic.repositories;

import logic.DatabaseFacade;
import logic.database.TestDatabaseCreator;
import logic.models.Table;
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
    void importTable() {
        for(Table table: databaseFacade.getTables()){
            assertDoesNotThrow(()->databaseFacade.importTable(table.getTableName()));
        }
    }

    @Test
    void searchTable() {
        List<List<Object>> searchData = new ArrayList<>();
        searchData.add(Arrays.asList("2", "abc", "2.2", "true"));
        searchData.add(Arrays.asList("4", "abc", "2.2", "true"));
        for(Table table: databaseFacade.getTables()) {
            for (int id = 0; id < 5; id++) {
                int finalId = id;
                assertDoesNotThrow(() -> databaseFacade.addRow(Arrays.asList(String.valueOf(finalId), "abc", "2.2", "true"), table));
            }
            assertEquals(String.valueOf(searchData), String.valueOf(databaseFacade.searchTable(table.getTableName(),"col1%2=0 AND col1!=0", null, null)));
        }
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
    void getTables() {
    }

    @Test
    void removeTableFromList() {
    }

    @Test
    void getTable() {
        for(Table table: databaseFacade.getTables()){
            assertEquals(table, databaseFacade.getTable(table.getTableName()));
        }
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