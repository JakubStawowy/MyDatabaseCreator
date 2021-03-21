package logic.repositories;

import logic.DatabaseFacade;
import logic.database.TestDatabaseCreator;
import logic.models.Table;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseRepositoryTest {

    @Test
    void importDatabase() {
        DatabaseFacade databaseFacade;
        try {
            databaseFacade = TestDatabaseCreator.createTestDatabase();
            assertDoesNotThrow(databaseFacade::importDatabase);
            databaseFacade.dropAllTables();
            databaseFacade.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void importTable() {
        try {
            DatabaseFacade databaseFacade = TestDatabaseCreator.createTestDatabase();
            for(Table table: databaseFacade.getTables()){
                assertDoesNotThrow(()->databaseFacade.importTable(table.getTableName()));
            }
            databaseFacade.dropAllTables();
            databaseFacade.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void searchTable() {
        try {
            DatabaseFacade databaseFacade = TestDatabaseCreator.createTestDatabase();
            List<List<Object>> searchData = new ArrayList<>();
            searchData.add(Arrays.asList("2", "abc", "2.2", "true"));
            searchData.add(Arrays.asList("4", "abc", "2.2", "true"));
            for(Table table: databaseFacade.getTables()) {
                for (int id = 0; id < 5; id++)
                    databaseFacade.addRow(Arrays.asList(String.valueOf(id), "abc", "2.2", "true"), table);
                assertEquals(String.valueOf(searchData), String.valueOf(databaseFacade.searchTable(table.getTableName(),"col1%2=0 AND col1!=0", null, null)));
            }
            databaseFacade.dropAllTables();
            databaseFacade.disconnect();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    @Test
    void getPrimaryKeys() {
        try {
            DatabaseFacade databaseFacade = TestDatabaseCreator.createTestDatabase();

            Map<String, String> primaryKeysMap = new HashMap<>();
            assertDoesNotThrow(databaseFacade::getPrimaryKeys);
            for(int i = 0 ; i < 5 ; i++){
                primaryKeysMap.put("testtable"+i, "col1 int(11)");
            }
            assertEquals(primaryKeysMap, databaseFacade.getPrimaryKeys());
            databaseFacade.dropAllTables();
            databaseFacade.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getTables() {
    }

    @Test
    void removeTableFromList() {
    }

    @Test
    void getTable() {
        try {
            DatabaseFacade databaseFacade = TestDatabaseCreator.createTestDatabase();
            for(Table table: databaseFacade.getTables()){
                assertEquals(table, databaseFacade.getTable(table.getTableName()));
            }
            databaseFacade.dropAllTables();
            databaseFacade.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getTableNames() {
        try {
            int size = 5;
            DatabaseFacade databaseFacade = TestDatabaseCreator.createTestDatabase();
            List<String> tableNames = databaseFacade.getTableNames();
            Logger.getGlobal().log(Level.INFO, String.valueOf(tableNames));
            assertEquals(size, tableNames.size());

            for (int index = 0 ; index < size; index++)
                assertEquals("testtable" + (index), tableNames.get(index));

            databaseFacade.dropAllTables();
            databaseFacade.disconnect();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
}