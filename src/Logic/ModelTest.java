package Logic;

import Logic.MyExceptions.EmptyTableException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    private final String databaseName = "jdbc:mysql://localhost:3306/junit_tests";
    private final String username = "root";
    private final String password = "";
    private Model createTestDatabase() throws SQLException{

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

            Model model = new Model(databaseName, username,password);
            model.dropAllTables();
            List<Table> testTables = new ArrayList<>(5);
            for(int i = 0 ; i < 5 ; i++){
                testTables.add(new Table("TestTable"+i, 0, data.get(i), testColumnNames,testColumnTypes,testConstraints,foreignkeys));
                model.createTable(testTables.get(i), "col1", true);
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
            String _databaseName = "junit_tests";
            Model model = new Model(databaseName,username,password);
            assertEquals(_databaseName,model.getDatabaseName());
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
    void deleteRow() {
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
        try {
            Model model = createTestDatabase();
            List<Object> row = Arrays.asList("0", "abc", "2.33", "true");
            List<Object> updatedRow = Arrays.asList("0", "abc", "2.33", "false");
            List<List<Object>> data = new ArrayList<>();
            data.add(updatedRow);
            for(Table table: model.getTables()){
                
                model.addRow(row, table);
                assertEquals(row, table.getData().get(0));

                model.updateRow(table.getTableName(), data, 0, 3, "true", "false");
                assertEquals(updatedRow, table.getData().get(0));
            }
            model.dropAllTables();
            model.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addRow() throws SQLException {
        Model model = createTestDatabase();

        List<Object> row = Arrays.asList("0", "abc", "2.33", "true");
        List<Object> badRow = Arrays.asList("0", "abc", "badValue", "true");
        for(Table table: model.getTables()){
            assertDoesNotThrow(()->model.addRow(row, table));
            assertThrows(SQLException.class, ()->model.addRow(badRow, table));
            assertEquals(row, table.getData().get(0));
            assertEquals(1, table.getData().size());
        }
    }

    @Test
    void dropTable() {
        try {
            Model model = createTestDatabase();

            assertThrows(SQLException.class, ()->model.dropTable("table"));

            for(int index = model.getTables().size()-1; index>=0; index--){
                Table table = model.getTables().get(index);
                assertDoesNotThrow(()->model.dropTable(table.getTableName()));
                assertEquals(index, model.getTables().size());
                assertThrows(SQLException.class, ()->model.importTable(table.getTableName()));
            }
            model.dropAllTables();
            model.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void searchTable() {
        try {
            Model model = createTestDatabase();
            List<List<Object>> searchData = new ArrayList<>();
            searchData.add(Arrays.asList("2", "abc", "2.2", "true"));
            searchData.add(Arrays.asList("4", "abc", "2.2", "true"));
            for(Table table: model.getTables()) {
                for (int id = 0; id < 5; id++)
                    model.addRow(Arrays.asList(String.valueOf(id), "abc", "2.2", "true"), table);
                assertEquals(String.valueOf(searchData), String.valueOf(model.searchTable(table.getTableName(),"col1%2=0 AND col1!=0", null, null)));
            }
            model.dropAllTables();
            model.closeConnection();
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    @Test
    void createTable() {
        assertDoesNotThrow(this::createTestDatabase);
        try {
            Model model = createTestDatabase();
            for(Table table: model.getTables()){
                assertDoesNotThrow(()->model.importTable(table.getTableName()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isNumeric() {
        try {
            Model model = new Model(databaseName, username, password);
            String[] numericTypes = {"Bit", "tinyInt", "smallint","meDiumint", "biGInt",
                    "int(20)", "boolean", "bool", "INTEGER", "float" ,"double", "decimal", "dec"};
            String[] stringTypes ={"char", "varchar", "binary", "tinyblob", "tinytext", "text",
                    "blob", "mediumtext", "mediumblob", "longtext", "longblob", "enum", "set"};
            for(String type: numericTypes)
                assertEquals(true, model.isNumeric(type));
            for(String type: stringTypes)
                assertEquals(false, model.isNumeric(type));
            model.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    void getPrimaryKeys() {
        try {
            Model model = createTestDatabase();

            List<Map<String, String>> primaryKeys = new LinkedList<>();
            Map<String, String> primaryKeyMap;

            assertDoesNotThrow(model::getPrimaryKeys);
            for(int i = 0 ; i < 5 ; i++){
                primaryKeyMap = new HashMap<>();
                primaryKeyMap.put("TestTable"+i, "col1 int(11)");
                primaryKeys.add(primaryKeyMap);
            }
            assertIterableEquals(primaryKeys, model.getPrimaryKeys());
            model.dropAllTables();
            model.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void closeConnection() {
        try {
            Model model = new Model(databaseName, username, password);
            assertDoesNotThrow(model::closeConnection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}