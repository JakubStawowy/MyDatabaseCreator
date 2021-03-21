package logic.connectors;

import logic.templates.DatabaseConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* Database Connector
* this class is used to connect with sql database
* */
public class MySqlDatabaseConnector implements DatabaseConnector {

    private Connection connection;
    private String databaseName;
    private String host;
    private String port;
    private String username;
    private String password;

    /*
    * Connects with database
    *
    * @param host String
    * @param port String
    * @param databaseName String
    * @param username String
    * @param password String
    * */
//    public MySqlDatabaseConnector(String host, String port, String databaseName, String username, String password) throws SQLException {
//
//        this.databaseName = databaseName;
//        String url = "jdbc://"+host+":"+port+"/"+databaseName;
//        connection = DriverManager.getConnection(url, username, password);
//
//    }

    public MySqlDatabaseConnector(String host, String port, String databaseName, String username, String password) throws SQLException {
        this.databaseName = databaseName;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        String url = "jdbc:mysql://"+host+":"+port+"/"+databaseName;
        Logger.getGlobal().log(Level.INFO, url);
        connection = DriverManager.getConnection(url, username, password);
    }

    @Override
    public ResultSet executeQuery(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }

    /*
    * Executes sql query
    * @param query
    * */
    @Override
    public void execute(String query) throws SQLException {
        connection.createStatement().execute(query);
    }

    /*
    * Closes connection with database.
    * */
    @Override
    public void disconnect() throws SQLException {
        connection.close();
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String[] getDatabaseProperties() {
        return new String[]{host, port, databaseName, username, password};
    }
}
