package database.templates;

public interface DatabaseQueryExecutorConnectionApi extends DatabaseQueryExecutorApi {
    DatabaseConnectorApi getDatabaseConnector();
}
