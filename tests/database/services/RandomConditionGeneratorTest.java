package database.services;

import exceptions.EmptyTableException;
import database.creator.TestDatabaseCreator;
import database.facades.DatabaseFacade;
import database.models.Table;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomConditionGeneratorTest {

    @Test
    void generateRandomCondition() {
        try {
            DatabaseFacade databaseFacade = TestDatabaseCreator.createTestDatabase();
            List<Object> row = Arrays.asList("1", "abc", "2.33", "true");

            for(Table table: databaseFacade.getTables()){

                assertThrows(EmptyTableException.class, ()-> RandomConditionGenerator.generateRandomCondition(table));

                table.addRow(row);
                assertDoesNotThrow(()->RandomConditionGenerator.generateRandomCondition(table));

                table.removeRow(0);
                assertThrows(EmptyTableException.class, ()->RandomConditionGenerator.generateRandomCondition(table));

                table.addRow(row);
                assertDoesNotThrow(()->RandomConditionGenerator.generateRandomCondition(table));

                table.removeRow(row);
                assertThrows(EmptyTableException.class, ()->RandomConditionGenerator.generateRandomCondition(table));
            }
            databaseFacade.dropAllTables();
            databaseFacade.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}