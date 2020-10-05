package Logic;

import Logic.MyExceptions.EmptyTableException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    private Model createTestDatabase() throws SQLException{

            List<List<Object>> emptyData = new ArrayList<>();
            Vector<String> testColumnNames = new Vector<>();
            Vector<String> testColumnTypes = new Vector<>();
            Vector<String> testConstraints = new Vector<>();
            Vector<String> foreignkeys = new Vector<>();

            testColumnNames.add("col1");
            testColumnNames.add("col2");
            testColumnNames.add("col3");
            testColumnNames.add("col4");

            testColumnTypes.add("int");
            testColumnTypes.add("varchar(20)");
            testColumnTypes.add("float");
            testColumnTypes.add("bool");

            testConstraints.add("");
            testConstraints.add("");
            testConstraints.add("");
            testConstraints.add("");
            testConstraints.add("");

            Model model = new Model("jdbc:mysql://localhost:3306/junit_tests", "root","");
            for(int i = 0 ; i < 5 ; i++){
                Table testTable = new Table("TestTable"+i, 0, emptyData, testColumnNames,testColumnTypes,testConstraints,foreignkeys);
                model.createTable(testTable, "col1", true);
            }

            return model;
    }
    @Test
    void getTableNames() {
        try {
            int size = 5;
            Model model = createTestDatabase();
            List<String> tableNames = model.getTableNames();

            assertEquals(size, tableNames.size());

            for (int index = 0 ; index < size; index++)
                assertEquals(tableNames.get(index), "TestTable" + (index));

            model.dropAllTables();
            model.closeConnection();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    @Test
    void getDatabaseName() {
        try {
            String databaseName = "junit_tests";
            Model model = new Model("jdbc:mysql://localhost:3306/"+databaseName,"root","");
            assertEquals(databaseName,model.getDatabaseName());
            model.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void generateRandomCondition() {
        try {
            Model model = createTestDatabase();
            List<Object> row = Arrays.asList("1", "abc", "2.33", "true");

            for(Table table: model.getTables()){

                assertThrows(EmptyTableException.class, ()->model.generateRandomCondition(table));

                table.addRow(row);
                assertDoesNotThrow(()->model.generateRandomCondition(table));

                table.removeRow(0);
                assertThrows(EmptyTableException.class, ()->model.generateRandomCondition(table));

                table.addRow(row);
                assertDoesNotThrow(()->model.generateRandomCondition(table));

                table.removeRow(row);
                assertThrows(EmptyTableException.class, ()->model.generateRandomCondition(table));
            }
            model.dropAllTables();
            model.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getTable() {
        try {
            Model model = createTestDatabase();
            for(Table table: model.getTables()){
                assertEquals(table, model.getTable(table.getTableName()));
            }
            model.dropAllTables();
            model.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void importDatabase() {
        Model model;
        try {
            model = createTestDatabase();
            assertDoesNotThrow(model::importDatabase);
            model.dropAllTables();
            model.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void importTable() {
        try {
            Model model = createTestDatabase();
            for(Table table: model.getTables()){
                assertDoesNotThrow(()->model.importTable(table.getTableName()));
            }
            model.dropAllTables();
            model.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteRowUsingIndex() {
        try {
            Model model = createTestDatabase();
            List<Object> row1 = Arrays.asList("0", "abc", "2.33", "true");
            List<Object> row2 = Arrays.asList("1", "abc", "2.33", "true");
            for(Table table: model.getTables()){

                assertEquals(0, table.getNumberOfRows());

                model.addRow(row1, table);
                assertNotEquals(0, table.getNumberOfRows());

                model.deleteRow(table, row1);
                assertEquals(0, table.getNumberOfRows());

                model.addRow(row2, table);
                assertNotEquals(0, table.getNumberOfRows());

                model.deleteRow(table, 0);
                assertEquals(0, table.getNumberOfRows());

            }
            model.dropAllTables();
            model.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateRow() {
    }

    @Test
    void addRow() {
    }

    @Test
    void copyTable() {
    }

    @Test
    void dropCopiedTable() {
    }

    @Test
    void dropTable() {
    }

    @Test
    void searchTable() {
    }

    @Test
    void createTable() {
    }

    @Test
    void isNumeric() {
    }

    @Test
    void getPrimaryKeys() {
    }

    @Test
    void getLoginData() {
    }

    @Test
    void closeConnection() {
    }
}