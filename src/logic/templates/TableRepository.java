package logic.templates;

import logic.models.Table;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface TableRepository {
    void importDatabase() throws SQLException;
    Table importTable(String tableName) throws SQLException;
    List<List<Object>> searchTable(String tableName, String condition, String sorted, String columnName);
    List<Map<String, String>> getPrimaryKeys() throws SQLException;
    void removeTableFromList(String tableName);
    List<Table> getTables();
    Table getTable(String tableName);
    List<String> getTableNames();
}
