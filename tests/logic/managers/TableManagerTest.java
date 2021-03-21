package logic.managers;

import logic.DatabaseFacade;
import logic.database.TestDatabaseCreator;
import logic.models.Table;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TableManagerTest {

    @Test
    void dropTable() {
        try {
            DatabaseFacade databaseFacade = TestDatabaseCreator.createTestDatabase();

            assertThrows(SQLException.class, ()->databaseFacade.dropTable("table"));

            for(int index = databaseFacade.getTables().size()-1; index>=0; index--){
                Table table = databaseFacade.getTables().get(index);
                assertDoesNotThrow(()->databaseFacade.dropTable(table.getTableName()));
                assertEquals(index, databaseFacade.getTables().size());
                assertThrows(SQLException.class, ()->databaseFacade.importTable(table.getTableName()));
            }
            databaseFacade.dropAllTables();
            databaseFacade.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void dropAllTables() {

        assertDoesNotThrow(() -> {
            DatabaseFacade databaseFacade = TestDatabaseCreator.createTestDatabase();
            databaseFacade.dropAllTables();
            assertEquals(0, databaseFacade.getTableNames().size());
        });

    }

    @Test
    void createTable() {
        assertDoesNotThrow(TestDatabaseCreator::createTestDatabase);
        try {
            DatabaseFacade databaseFacade = TestDatabaseCreator.createTestDatabase();
            for(Table table: databaseFacade.getTables()){
                assertDoesNotThrow(()->databaseFacade.importTable(table.getTableName()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}