package logic.templates;

import logic.models.Table;

import java.sql.SQLException;
import java.util.List;

public interface DmlManager {

    void deleteRow(Table table, int rowIndex) throws SQLException;
    void deleteRow(Table table, List<Object> row) throws SQLException;
    void updateRow(String tableName, List<List<Object>> newData, int rowIndex, int columnIndex, Object oldValue, Object newValue) throws SQLException;
    void addRow(List<Object> row, Table table) throws SQLException;
}
