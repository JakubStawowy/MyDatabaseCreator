package logic.database;

import logic.facades.DatabaseFacade;
import logic.models.Table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TestDatabaseCreator {

    private final static String host = "localhost";
    private final static String port = "3306";
    private final static String database = "mdc_db";
    private final static String username = "root";
    private final static String password = "";

    public static DatabaseFacade createTestDatabase() throws SQLException {

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
            testTables.add(new Table("testtable"+i, data.get(i), testColumnNames,testColumnTypes,testConstraints,foreignkeys));
            databaseFacade.createTable(testTables.get(i), "col1", true);
        }

        return databaseFacade;
    }
}
