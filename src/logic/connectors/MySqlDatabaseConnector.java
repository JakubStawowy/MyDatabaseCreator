package logic.connectors;

import logic.templates.DatabaseConnectorApi;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* Database Connector
* this class is used to connect with sql database
* */
public final class MySqlDatabaseConnector implements DatabaseConnectorApi {

    private final Connection connection;
    private final Map<String, String> databasePropertiesMap = new HashMap<>();

    public MySqlDatabaseConnector(String host, String port, String databaseName, String username, String password) throws SQLException {

        databasePropertiesMap.put("databaseName", databaseName);
        databasePropertiesMap.put("host", host);
        databasePropertiesMap.put("port", port);
        databasePropertiesMap.put("username", username);
        databasePropertiesMap.put("password", password);
        String url = "jdbc:mysql://"+host+":"+port+"/"+databaseName;
        Logger.getGlobal().log(Level.INFO, url);
        connection = DriverManager.getConnection(url, username, password);
    }

    @Override
    public ResultSet executeQuery(final String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }


    @Override
    public void execute(final String query) throws SQLException {
        connection.createStatement().execute(query);
    }

    @Override
    public void disconnect() throws SQLException {
        connection.close();
    }

    @Override
    public Map<String, String> getDatabasePropertiesMap() {
        return databasePropertiesMap;
    }
}
