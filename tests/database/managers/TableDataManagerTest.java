package database.managers;

import database.facades.DatabaseFacade;
import database.creator.TestDatabaseCreator;
import database.models.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableDataManagerTest {

    private DatabaseFacade databaseFacade;

    @BeforeEach
    void init() throws SQLException {
        databaseFacade = TestDatabaseCreator.createTestDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        databaseFacade.dropAllTables();
        databaseFacade.disconnect();
    }

    @Test
    void deleteRow() {
        try {
            List<Object> row1 = Arrays.asList("0", "abc", "2.33", "true");
            List<Object> row2 = Arrays.asList("1", "abc", "2.33", "true");
            for(Table table: databaseFacade.getTables()){

                assertEquals(0, table.getNumberOfRows());

                databaseFacade.addRow(row1, table);
                assertNotEquals(0, table.getNumberOfRows());

                databaseFacade.deleteRow(table, row1);
                assertEquals(0, table.getNumberOfRows());

                databaseFacade.addRow(row2, table);
                assertNotEquals(0, table.getNumberOfRows());

                databaseFacade.deleteRow(table, 0);
                assertEquals(0, table.getNumberOfRows());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateRow() {
        try {

            List<Object> row = Arrays.asList("0", "abc", "2.33", "true");
            List<Object> updatedRow = Arrays.asList("0", "abc", "2.33", "false");
            List<List<Object>> data = new ArrayList<>();
            data.add(updatedRow);
            for(Table table: databaseFacade.getTables()){

                databaseFacade.addRow(row, table);
                assertEquals(row, table.getData().get(0));

                databaseFacade.updateRow(table.getTableName(), data, 0, 3, "true", "false");
                assertEquals(updatedRow, table.getData().get(0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addRow() {

        List<Object> row = Arrays.asList("0", "abc", "2.33", "true");
        List<Object> badRow = Arrays.asList("0", "abc", "badValue", "true");
        for(Table table: databaseFacade.getTables()){
            assertDoesNotThrow(()->databaseFacade.addRow(row, table));
            assertThrows(SQLException.class, ()->databaseFacade.addRow(badRow, table));
            assertEquals(row, table.getData().get(0));
            assertEquals(1, table.getData().size());
        }
    }
}