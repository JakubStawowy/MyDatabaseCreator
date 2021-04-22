package database.templates;

import database.models.Table;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DatabaseRepositoryApi {
    void importDatabase() throws SQLException;
    Map<String, String> getPrimaryKeys() throws SQLException;
    void removeTableFromList(final String tableName);
    List<Table> getTables();
    List<String> getTableNames() throws SQLException;
}
