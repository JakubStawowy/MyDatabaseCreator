package logic.templates;

import logic.models.Table;

import java.sql.SQLException;
import java.util.List;

public interface DmlManager {

    void deleteRow(Table table, final int rowIndex) throws SQLException;
    void deleteRow(Table table, final List<Object> row) throws SQLException;
    void updateRow(final String tableName, List<List<Object>> newData, final int rowIndex, final int columnIndex, Object oldValue, Object newValue) throws SQLException;
    void addRow(final List<Object> row, Table table) throws SQLException;
}
