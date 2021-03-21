package logic.templates;

import logic.models.Table;

import java.sql.SQLException;

public interface DdlManager {
    void dropTable(String tableName) throws SQLException;
    void dropAllTables() throws SQLException;
    void createTable(Table table, String primaryKey, Boolean dropExistingTable) throws SQLException;
}
