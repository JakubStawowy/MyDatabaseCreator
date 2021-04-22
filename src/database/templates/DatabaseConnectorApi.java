package database.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface DatabaseConnectorApi {

    ResultSet executeQuery(final String query) throws SQLException;
    void execute(final String query) throws SQLException;
    void disconnect()throws SQLException;
    Map<String, String> getDatabasePropertiesMap();
}
