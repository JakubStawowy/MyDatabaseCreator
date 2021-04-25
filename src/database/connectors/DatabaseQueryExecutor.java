package database.connectors;

import database.templates.DatabaseConnectorApi;
import database.templates.DatabaseConnectorConnectionApi;
import database.templates.DatabaseQueryExecutorConnectionApi;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class DatabaseQueryExecutor implements DatabaseQueryExecutorConnectionApi {

    private final DatabaseConnectorConnectionApi databaseConnector;

    public DatabaseQueryExecutor(DatabaseConnectorConnectionApi databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public ResultSet getExecutedQueryResultSet(final String query) throws SQLException {
        return databaseConnector.getConnection().createStatement().executeQuery(query);
    }


    @Override
    public void executeQuery(final String query) throws SQLException {
        databaseConnector.getConnection().createStatement().execute(query);
    }

    @Override
    public DatabaseConnectorApi getDatabaseConnector() {
        return databaseConnector;
    }
}
