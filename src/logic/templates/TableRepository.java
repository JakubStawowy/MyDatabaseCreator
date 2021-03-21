package logic.templates;

import logic.models.Table;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface TableRepository {
    void importDatabase() throws SQLException;
    Table importTable(final String tableName) throws SQLException;
    List<List<Object>> searchTable(final String tableName, String condition, final String sorted, final String columnName);
    Map<String, String> getPrimaryKeys() throws SQLException;
    void removeTableFromList(final String tableName);
    List<Table> getTables();
    Table getTable(final String tableName);
    List<String> getTableNames();
}
