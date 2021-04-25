package database.repositories;

import database.models.Table;
import database.templates.DatabaseQueryExecutorApi;
import database.templates.TableRepositoryApi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public final class TableRepository implements TableRepositoryApi {

    private final DatabaseQueryExecutorApi databaseQueryExecutor;
    private List<Table> tables;

    public TableRepository(final DatabaseQueryExecutorApi databaseQueryExecutor, List<Table> tables) {
        this.databaseQueryExecutor = databaseQueryExecutor;
        this.tables = tables;
    }

    @Override
    public Table importTable(final String tableName) throws SQLException {
        ResultSet rs;

        Vector<String> columnNames = new Vector<>();
        Vector<String> columnTypes = new Vector<>();
        List<Object> columns;
        List<List<Object>> data = new LinkedList<>();
        rs = databaseQueryExecutor.getExecutedQueryResultSet("DESC " + tableName + ";");

        while (rs.next()) {

            if (rs.getString("Key").equals("PRI"))

                columnNames.add(rs.getString("Field"));
            columnTypes.add(rs.getString("Type"));

        }

        rs = databaseQueryExecutor.getExecutedQueryResultSet("SELECT * FROM " + tableName + ";");

        while (rs.next()) {

            columns = new LinkedList<>();

            for (String column : columnNames) {

                columns.add(rs.getObject(column));
            }
            data.add(columns);
        }
        return new Table(tableName, data, columnNames, columnTypes, null, null);
    }

    @Override
    public List<List<Object>> searchTable(final String tableName, String condition, final String sorted, final String columnName) {

        ResultSet rs;

        List<String> columnNames = new ArrayList<>();
        List<Object> columns;
        List<List<Object>> data = new LinkedList<>();

        if(condition.equals(""))
            condition = "TRUE";

        try {
            rs = databaseQueryExecutor.getExecutedQueryResultSet("DESC "+tableName+";");

            while(rs.next()) {

                columnNames.add(rs.getString("Field"));
            }
            if(sorted!=null)
                rs = databaseQueryExecutor.getExecutedQueryResultSet("SELECT * FROM " + tableName + " WHERE "+condition+" ORDER BY("+columnName+") "+sorted+";");
            else
                rs = databaseQueryExecutor.getExecutedQueryResultSet("SELECT * FROM " + tableName + " WHERE "+condition+";");

            while(rs.next()){

                columns = new LinkedList<>();

                for(String column : columnNames){

                    columns.add(rs.getObject(column));
                }
                data.add(columns);
            }
        }catch(SQLException sqlException){
            System.out.println("Problemy z przeszukaniem tabeli");
        }

        return data;
    }

    @Override
    public Table getTable(final String tableName) {

        for (Table table : tables) {
            if (table.getTableName().equals(tableName))
                return table;
        }
        return null;
    }
}
