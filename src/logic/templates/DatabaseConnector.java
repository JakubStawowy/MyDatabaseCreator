package logic.templates;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseConnector {

    ResultSet executeQuery(String query) throws SQLException;
    void execute(String query) throws SQLException;
    void disconnect()throws SQLException;
    String getDatabaseName();
    String[] getDatabaseProperties();
}
