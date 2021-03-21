
import exceptions.EmptyTableException;
import logic.DatabaseFacade;
import logic.RandomConditionGenerator;
import logic.models.Table;
import logic.repositories.DataTypesRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseFacadeTest {

    private final String host = "localhost";
    private final String port = "3306";
    private final String database = "mdc_db";
    private final String username = "root";
    private final String password = "";
    private DatabaseFacade createTestDatabase() throws SQLException{

            List<List<Object>> emptyData0 = new ArrayList<>();
            List<List<Object>> emptyData1 = new ArrayList<>();
            List<List<Object>> emptyData2 = new ArrayList<>();
            List<List<Object>> emptyData3 = new ArrayList<>();
            List<List<Object>> emptyData4 = new ArrayList<>();
            List<List<List<Object>>> data = new ArrayList<>();
            data.add(emptyData0);
            data.add(emptyData1);
            data.add(emptyData2);
            data.add(emptyData3);
            data.add(emptyData4);

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
            testColumnTypes.add("Boolean");

            testConstraints.add("");
            testConstraints.add("");
            testConstraints.add("");
            testConstraints.add("");
            testConstraints.add("");

            DatabaseFacade databaseFacade = new DatabaseFacade(host, port, database, username, password);
            databaseFacade.dropAllTables();
            List<Table> testTables = new ArrayList<>(5);
            for(int i = 0 ; i < 5 ; i++){
                testTables.add(new Table("TestTable"+i, data.get(i), testColumnNames,testColumnTypes,testConstraints,foreignkeys));
                databaseFacade.createTable(testTables.get(i), "col1", true);
            }

            return databaseFacade;
    }
    @Test
    void getTableNames() {
        try {
            int size = 5;
            DatabaseFacade databaseFacade = createTestDatabase();
            List<String> tableNames = databaseFacade.getTableNames();

            assertEquals(size, tableNames.size());

            for (int index = 0 ; index < size; index++)
                assertEquals(tableNames.get(index), "TestTable" + (index));

            databaseFacade.dropAllTables();
            databaseFacade.disconnect();

        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    @Test
    void getDatabaseName() {
        try {
            String _databaseName = "junit_tests";
            DatabaseFacade databaseFacade = new DatabaseFacade(host, port, database, username, password);
            assertEquals(_databaseName, databaseFacade.getDatabasePropertiesMap().get("databaseName"));
            databaseFacade.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void generateRandomCondition() {
        try {
            DatabaseFacade databaseFacade = createTestDatabase();
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

    @Test
    void getTable() {
        try {
            DatabaseFacade databaseFacade = createTestDatabase();
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
    void importDatabase() {
        DatabaseFacade databaseFacade;
        try {
            databaseFacade = createTestDatabase();
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
            DatabaseFacade databaseFacade = createTestDatabase();
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
    void deleteRow() {
        try {
            DatabaseFacade databaseFacade = createTestDatabase();
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
            databaseFacade.dropAllTables();
            databaseFacade.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateRow() {
        try {
            DatabaseFacade databaseFacade = createTestDatabase();
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
            databaseFacade.dropAllTables();
            databaseFacade.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addRow() throws SQLException {
        DatabaseFacade databaseFacade = createTestDatabase();

        List<Object> row = Arrays.asList("0", "abc", "2.33", "true");
        List<Object> badRow = Arrays.asList("0", "abc", "badValue", "true");
        for(Table table: databaseFacade.getTables()){
            assertDoesNotThrow(()->databaseFacade.addRow(row, table));
            assertThrows(SQLException.class, ()->databaseFacade.addRow(badRow, table));
            assertEquals(row, table.getData().get(0));
            assertEquals(1, table.getData().size());
        }
    }

    @Test
    void dropTable() {
        try {
            DatabaseFacade databaseFacade = createTestDatabase();

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
    void searchTable() {
        try {
            DatabaseFacade databaseFacade = createTestDatabase();
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
    void createTable() {
        assertDoesNotThrow(this::createTestDatabase);
        try {
            DatabaseFacade databaseFacade = createTestDatabase();
            for(Table table: databaseFacade.getTables()){
                assertDoesNotThrow(()->databaseFacade.importTable(table.getTableName()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isNumeric() {
        try {
            DatabaseFacade databaseFacade = new DatabaseFacade(host, port, database, username, password);
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

    @Test
    void getPrimaryKeys() {
        try {
            DatabaseFacade databaseFacade = createTestDatabase();

            Map<String, String> primaryKeysMap = new HashMap<>();
            assertDoesNotThrow(databaseFacade::getPrimaryKeys);
            for(int i = 0 ; i < 5 ; i++){
                primaryKeysMap.put("TestTable"+i, "col1 int(11)");
            }
            assertEquals(primaryKeysMap, databaseFacade.getPrimaryKeys());
            databaseFacade.dropAllTables();
            databaseFacade.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void closeConnection() {
        try {
            DatabaseFacade databaseFacade = new DatabaseFacade(host, port, database, username, password);
            assertDoesNotThrow(databaseFacade::disconnect);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}