package database.repositories;

import database.creator.TestDatabaseCreator;
import database.facades.DatabaseFacade;
import database.models.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableRepositoryTest {

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
    void getTable() {
        for(Table table: databaseFacade.getTables()){
            assertEquals(table, databaseFacade.getTable(table.getTableName()));
        }
    }
}