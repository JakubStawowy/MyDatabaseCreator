package database.templates;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseQueryExecutorApi {

    ResultSet getExecutedQueryResultSet(final String query) throws SQLException;
    void executeQuery(final String query) throws SQLException;
}
