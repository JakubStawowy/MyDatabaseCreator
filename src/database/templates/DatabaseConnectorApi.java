package database.templates;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public interface DatabaseConnectorApi {

    void disconnect()throws SQLException;
    Map<String, String> getDatabasePropertiesMap();
}
