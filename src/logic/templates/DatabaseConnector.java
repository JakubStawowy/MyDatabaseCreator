package logic.templates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface DatabaseConnector {

    ResultSet executeQuery(final String query) throws SQLException;
    void execute(final String query) throws SQLException;
    void disconnect()throws SQLException;
    Map<String, String> getDatabasePropertiesMap();
}
