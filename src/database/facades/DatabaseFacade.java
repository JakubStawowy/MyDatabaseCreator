package database.facades;

import database.connectors.DatabaseConnector;
import database.connectors.DatabaseQueryExecutor;
import database.managers.TableDataManager;
import database.managers.TableManager;
import database.models.Table;
import database.repositories.DatabaseRepository;
import database.repositories.TableRepository;
import database.templates.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public final class DatabaseFacade implements DatabaseFacadeApi {

    private final DdlManagerApi ddlManager;
    private final DmlManagerApi dmlManager;
    private final DatabaseConnectorConnectionApi databaseConnector;
    private final DatabaseRepositoryApi databaseRepository;
    private final TableRepositoryApi tableRepository;
    private final DatabaseQueryExecutorConnectionApi databaseQueryExecutor;

    public DatabaseFacade(String host, String port, String database, String username, String password) throws SQLException {
        databaseConnector = new DatabaseConnector(host, port, database, username, password);
        databaseQueryExecutor = new DatabaseQueryExecutor(databaseConnector);
        databaseRepository = new DatabaseRepository(databaseQueryExecutor);
        tableRepository = new TableRepository(databaseQueryExecutor, databaseRepository.getTables());
        ddlManager = new TableManager(databaseQueryExecutor, databaseRepository);
        dmlManager = new TableDataManager(databaseQueryExecutor, tableRepository);
    }

    @Override
    public void importDatabase() throws SQLException {
        databaseRepository.importDatabase();
    }

    @Override
    public Table importTable(String tableName) throws SQLException {
        return tableRepository.importTable(tableName);
    }

    @Override
    public List<List<Object>> searchTable(String tableName, String condition, String sorted, String columnName) {
        return tableRepository.searchTable(tableName, condition, sorted, columnName);
    }

    @Override
    public Map<String, String> getPrimaryKeys() throws SQLException {
        return databaseRepository.getPrimaryKeys();
    }

    @Override
    public void removeTableFromList(String tableName) {
        databaseRepository.removeTableFromList(tableName);
    }

    @Override
    public List<Table> getTables() {
        return databaseRepository.getTables();
    }

    @Override
    public Table getTable(String tableName) {
        return tableRepository.getTable(tableName);
    }

    @Override
    public List<String> getTableNames() throws SQLException {
        return databaseRepository.getTableNames();
    }

    @Override
    public void dropTable(String tableName) throws SQLException {
        ddlManager.dropTable(tableName);
    }

    @Override
    public void dropAllTables() throws SQLException {
        ddlManager.dropAllTables();
    }

    @Override
    public void createTable(Table table, String primaryKey, Boolean dropExistingTable) throws SQLException {
        ddlManager.createTable(table, primaryKey, dropExistingTable);
    }

    @Override
    public void deleteRow(Table table, int rowIndex) throws SQLException {
        dmlManager.deleteRow(table, rowIndex);
    }

    @Override
    public void deleteRow(Table table, List<Object> row) throws SQLException {
        dmlManager.deleteRow(table, row);
    }

    @Override
    public void updateRow(String tableName, List<List<Object>> newData, int rowIndex, int columnIndex, Object oldValue, Object newValue) throws SQLException {
        dmlManager.updateRow(tableName, newData, rowIndex, columnIndex, oldValue, newValue);
    }

    @Override
    public void addRow(List<Object> row, Table table) throws SQLException {
        dmlManager.addRow(row, table);
    }

    @Override
    public ResultSet getExecutedQueryResultSet(String query) throws SQLException {
        return databaseQueryExecutor.getExecutedQueryResultSet(query);
    }

    @Override
    public void executeQuery(String query) throws SQLException {
        databaseQueryExecutor.executeQuery(query);
    }

    @Override
    public void disconnect() throws SQLException {
        databaseConnector.disconnect();
    }

    @Override
    public Map<String, String> getDatabasePropertiesMap() {
        return databaseConnector.getDatabasePropertiesMap();
    }

}
