//package Model.MySQL;
//
//import Logic.Table;
//import Model.Objects.Table;
//import Model.Objects.TableColumnManager;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//public class MySQLTestConfig {
//
//    private final String host = "localhost";
//    private final int port = 3306;
//    private final String database = "test";
//    private final String user = "root";
//    private final String password = "";
//    private final List<Table<String, List<String>, List<List<String>>>> tables = Arrays.asList(
//            new Table<>(
//                    "test1",
//                    new TableColumnManager<>(
//                            Arrays.asList("id", "name", "price"),
//                            Arrays.asList("int(11)", "varchar(20)", "float"),
//                            Collections.singletonMap("id", "AUTO_INCREMENT")
//                    ),
//                    0,
//                    new ArrayList<>(),
//                    new ArrayList<>()
//            ),
//            new Table<>(
//                    "test2",
//                    new TableColumnManager<>(
//                            Arrays.asList("id", "name", "price"),
//                            Arrays.asList("int(11)", "varchar(20)", "float"),
//                            Collections.singletonMap("id", "AUTO_INCREMENT")
//                    ),
//                    0,
//                    new ArrayList<>(),
//                    new ArrayList<>()
//            ),
//            new Table<>(
//                    "test3",
//                    new TableColumnManager<>(
//                            Arrays.asList("id", "name", "price"),
//                            Arrays.asList("int(11)", "varchar(20)", "float"),
//                            Collections.singletonMap("id", "AUTO_INCREMENT")
//                    ),
//                    0,
//                    new ArrayList<>(),
//                    new ArrayList<>()
//            )
//    );
//
//    public String getHost() {
//        return host;
//    }
//
//    public int getPort() {
//        return port;
//    }
//
//    public String getDatabase() {
//        return database;
//    }
//
//    public String getUser() {
//        return user;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public List<Table<String, List<String>, List<List<String>>>> getTables() {
//        return tables;
//    }
//}
