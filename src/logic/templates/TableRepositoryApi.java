package logic.templates;

import logic.models.Table;

import java.sql.SQLException;
import java.util.List;

public interface TableRepositoryApi {
    Table importTable(final String tableName) throws SQLException;
    List<List<Object>> searchTable(final String tableName, String condition, final String sorted, final String columnName);
    Table getTable(final String tableName);
}
