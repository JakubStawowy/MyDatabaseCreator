package logic.managers;

import logic.models.Table;
import logic.repositories.DataTypesRepository;
import logic.templates.DatabaseConnector;
import logic.templates.DmlManager;
import logic.templates.TableRepository;

import java.sql.SQLException;
import java.util.List;

public class TableDataManager implements DmlManager {

    private DatabaseConnector databaseConnector;
    private TableRepository tableRepository;

    public TableDataManager(DatabaseConnector databaseConnector, TableRepository tableRepository) {
        this.databaseConnector = databaseConnector;
        this.tableRepository = tableRepository;
    }

    /*
     * DeleteRow method removes selected row (using index) using sql query.
     *
     * @param String tableName
     * @param int rowIndex
     * */
    @Override
    public void deleteRow(Table table, int rowIndex) throws SQLException {

        StringBuilder condition = new StringBuilder();
        Object value;
        String type;

        for(int index = 0; index < table.getNumberOfColumns(); index++){

            type = table.getColumnTypes().get(index);
            value = table.getData().get(rowIndex).get(index);
            if(!DataTypesRepository.isNumeric(type))
                value = "\""+value+"\"";

            condition.append(table.getColumnNames().get(index)).append(" = ").append(value);
            if(index < table.getNumberOfColumns()-1)
                condition.append(" AND ");
        }

        String query = "DELETE FROM "+table.getTableName()+" WHERE "+condition+";";
        databaseConnector.execute(query);
        table.removeRow(rowIndex);
    }

    /*
     * DeleteRow method removes selected row (using row) using sql query.
     *
     * @param String tableName
     * @param List<Object> row
     * */
    @Override
    public void deleteRow(Table table, List<Object> row) throws SQLException {
        StringBuilder condition = new StringBuilder();
        String type;
        for(int index = 0; index<table.getNumberOfColumns(); index++){
            type=table.getColumnTypes().get(index);
            if(!DataTypesRepository.isNumeric(type))
                condition.append(table.getColumnNames().get(index)).append("=\"").append(row.get(index)).append("\"");
            else
                condition.append(table.getColumnNames().get(index)).append("=").append(row.get(index));

            if(index < table.getNumberOfColumns()-1)
                condition.append(" AND ");
        }
        String query = "DELETE FROM "+table.getTableName()+" WHERE "+condition+";";
        databaseConnector.execute(query);
        table.removeRow(row);
    }

    /*
     * updateRow method is used to update row in a given table.
     * It uses SQL DML Language (UPDATE)
     *
     * @param String tableName
     * @param List<List<Object>> newData (table data after editing)
     * @param int rowIndex (selected row index)
     * @param int columnIndex (selected column index)
     * @param Object oldValue
     * @param Object newValue
     * @throws SQLException
     * */
    @Override
    public void updateRow(String tableName, List<List<Object>> newData, int rowIndex, int columnIndex, Object oldValue, Object newValue) throws SQLException{

        Table table = tableRepository.getTable(tableName);
        String type = table.getColumnTypes().get(columnIndex);
        String columnName = table.getColumnNames().get(columnIndex);

        if(!DataTypesRepository.isNumeric(type)){
            newValue = "\""+newValue+"\"";
            oldValue = "\""+oldValue+"\"";
        }

        StringBuilder query = new StringBuilder("UPDATE "+tableName+" SET "+columnName+" = "+newValue+" WHERE ");
        int floatSize = 2;
        for(int index = 0; index < table.getNumberOfColumns(); index++){
            if(index == columnIndex)
                if(table.getColumnTypes().get(index).equals("float"))
                    query.append("FORMAT(").append(columnName).append(", ").append(floatSize).append(")").append(" = FORMAT(").append(oldValue).append(", ").append(floatSize).append(")");
                else
                    query.append(columnName).append(" = ").append(oldValue);
            else {
                String columnType = table.getColumnTypes().get(index);
                if(!DataTypesRepository.isNumeric(columnType))
                    query.append(table.getColumnNames().get(index)).append(" = ").append("\"").append(newData.get(rowIndex).get(index)).append("\"");
                else
                if(table.getColumnTypes().get(index).equals("float"))
                    query.append("FORMAT(").append(table.getColumnNames().get(index)).append(", ").append(floatSize).append(")").append(" = ").append("FORMAT(").append(newData.get(rowIndex).get(index)).append(", ").append(floatSize).append(")");
                else
                    query.append(table.getColumnNames().get(index)).append(" = ").append(newData.get(rowIndex).get(index));
            }
            if(index < table.getNumberOfColumns()-1)
                query.append(" AND ");
        }
        query.append(";");
        databaseConnector.execute(String.valueOf(query));
        table.setData(newData);
    }

    @Override
    public void addRow(List<Object> row, Table table) throws SQLException {

        String columntype;
        Object value;
        StringBuilder query = new StringBuilder("INSERT INTO " + table.getTableName() + " VALUES (");
        for(int i = 0; i < table.getNumberOfColumns(); i++){
            columntype = table.getColumnTypes().get(i);
            value = row.get(i);
            if(!DataTypesRepository.isNumeric(columntype))
                value = "\""+row.get(i)+"\"";
            query.append(value).append(", ");
        }
        query = new StringBuilder(query.substring(0, query.length() - 2));
        query.append(");");
        databaseConnector.execute(String.valueOf(query));

        table.addRow(row);
    }
}
