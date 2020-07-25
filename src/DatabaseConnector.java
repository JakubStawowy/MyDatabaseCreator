import java.sql.*;
/*
* Database Connector class is used to connect with sql database
* */
public class DatabaseConnector {

    private Connection connection;

    /*
    * Connects with database
    * @param host
    * @param username
    * @param password
    * */
    public DatabaseConnector(String host, String username, String password) throws SQLException {

        connection = DriverManager.getConnection(host, username, password);
    }
    public ResultSet executeQuery(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }
    /*
    * Executes sql query
    * @param query
    * */
    public void execute(String query) throws SQLException{
        connection.createStatement().execute(query);
    }

}
