package database.templates;

public interface DatabaseFacadeApi extends TableRepositoryApi, DatabaseRepositoryApi, DdlManagerApi, DmlManagerApi, DatabaseConnectorApi, DatabaseQueryExecutorApi {
}
