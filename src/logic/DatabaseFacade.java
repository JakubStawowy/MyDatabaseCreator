package logic;

import logic.connectors.MySqlDatabaseConnector;
import logic.managers.TableDataManager;
import logic.managers.TableManager;
import logic.models.Table;
import logic.repositories.DatabaseRepository;
import logic.templates.DatabaseConnector;
import logic.templates.DdlManager;
import logic.templates.DmlManager;
import logic.templates.TableRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DatabaseFacade implements TableRepository, DdlManager, DmlManager, DatabaseConnector {

    private TableRepository tableRepository;
    private DdlManager ddlManager;
    private DmlManager dmlManager;
    private DatabaseConnector databaseConnector;

    public DatabaseFacade(String host, String port, String database, String username, String password) throws SQLException {
        databaseConnector = new MySqlDatabaseConnector(host, port, database, username, password);
        tableRepository = new DatabaseRepository(databaseConnector);
        ddlManager = new TableManager(databaseConnector, tableRepository);
        dmlManager = new TableDataManager(databaseConnector, tableRepository);
    }

    @Override
    public void importDatabase() throws SQLException {
        tableRepository.importDatabase();
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
        return tableRepository.getPrimaryKeys();
    }

    @Override
    public void removeTableFromList(String tableName) {
        tableRepository.removeTableFromList(tableName);
    }

    @Override
    public List<Table> getTables() {
        return tableRepository.getTables();
    }

    @Override
    public Table getTable(String tableName) {
        return tableRepository.getTable(tableName);
    }

    @Override
    public List<String> getTableNames() {
        return tableRepository.getTableNames();
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
    public ResultSet executeQuery(String query) throws SQLException {
        return databaseConnector.executeQuery(query);
    }

    @Override
    public void execute(String query) throws SQLException {
        databaseConnector.execute(query);
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
