package logic.managers;

import logic.facades.DatabaseFacade;
import logic.database.TestDatabaseCreator;
import logic.models.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TableManagerTest {

    private DatabaseFacade databaseFacade;

    @BeforeEach
    void setUp() throws SQLException{
        databaseFacade = TestDatabaseCreator.createTestDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        databaseFacade.dropAllTables();
        databaseFacade.disconnect();
    }

    @Test
    void dropTable() {
        assertThrows(SQLException.class, ()->databaseFacade.dropTable("table"));
        for(int index = databaseFacade.getTables().size()-1; index>=0; index--){
            Table table = databaseFacade.getTables().get(index);
            assertDoesNotThrow(()->databaseFacade.dropTable(table.getTableName()));
            assertEquals(index, databaseFacade.getTables().size());
            assertThrows(SQLException.class, ()->databaseFacade.importTable(table.getTableName()));
        }
    }

    @Test
    void dropAllTables() {
        assertDoesNotThrow(() -> {
            databaseFacade.dropAllTables();
            assertEquals(0, databaseFacade.getTableNames().size());
        });
    }

    @Test
    void createTable() {
        for(Table table: databaseFacade.getTables()){
            assertDoesNotThrow(()->databaseFacade.importTable(table.getTableName()));
        }
    }
}