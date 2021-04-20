package logic.templates;

import logic.models.Table;

import java.sql.SQLException;

public interface DdlManagerApi {
    void dropTable(final String tableName) throws SQLException;
    void dropAllTables() throws SQLException;
    void createTable(final Table table, final String primaryKey, final Boolean dropExistingTable) throws SQLException;
}
