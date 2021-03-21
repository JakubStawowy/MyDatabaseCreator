package logic.repositories;

import logic.models.Table;
import logic.templates.DatabaseConnector;
import logic.templates.TableRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseRepository implements TableRepository {

    private DatabaseConnector databaseConnector;
    private List<String> tableNames = new LinkedList<>();
    private List<Table> tables = new LinkedList<>();

    public DatabaseRepository(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    /*
     * Imports all tables from database using sql command "SHOW TABLES" and importTable() method.
     * */
    @Override
    public void importDatabase() throws SQLException {

        ResultSet rs = databaseConnector.executeQuery("SHOW TABLES;");
        tableNames.clear();
        String tableName;
        while (rs.next()) {
            tableName = rs.getString("Tables_in_" + databaseConnector.getDatabaseName().substring(databaseConnector.getDatabaseName().lastIndexOf("/") + 1));
            tableNames.add(tableName);
            tables.add(importTable(tableName));
        }
    }

    /*
     * Imports all table parameters.
     *
     * @param String tableName
     * @returns new Logic.Table
     * */
    @Override
    public Table importTable(String tableName) throws SQLException {
        ResultSet rs;

        Vector<String> columnNames = new Vector<>();
        Vector<String> columnTypes = new Vector<>();
        List<Object> columns;
        List<List<Object>> data = new LinkedList<>();
        int numberOfColumns = 0;
        int numberOfRows = 0;
        int primaryKeyColumnIndex = 0;

        rs = databaseConnector.executeQuery("DESC " + tableName + ";");

        while (rs.next()) {

            if (rs.getString("Key").equals("PRI"))
                primaryKeyColumnIndex = numberOfColumns;

            columnNames.add(rs.getString("Field"));
            columnTypes.add(rs.getString("Type"));

            numberOfColumns++;
        }

        rs = databaseConnector.executeQuery("SELECT * FROM " + tableName + ";");

        while (rs.next()) {

            columns = new LinkedList<>();

            for (String column : columnNames) {

                columns.add(rs.getObject(column));
            }

            data.add(columns);
            numberOfRows++;
        }
        return new Table(tableName,primaryKeyColumnIndex, data, columnNames, columnTypes, null, null);
    }
    /*
     * Search table method returns a new table containing all rows that met the conditions specified as an argument.
     * It uses sql command SELECT * FROM tablename WHERE condition
     *
     * @param String tableName
     * @param String condition
     * @param String sorted ("ASC"/"DESC"/null)
     * @param String columnName - table would be sorted using column with that name
     *
     * @return data (multidimensional ArrayList)
     * */
    @Override
    public List<List<Object>> searchTable(String tableName, String condition, String sorted, String columnName) {

        ResultSet rs;

        List<String> columnNames = new ArrayList<>();
        List<Object> columns;
        List<List<Object>> data = new LinkedList<>();

        if(condition.equals(""))
            condition = "TRUE";

        try {
            rs = databaseConnector.executeQuery("DESC "+tableName+";");

            while(rs.next()) {

                columnNames.add(rs.getString("Field"));
            }
            if(sorted!=null)
                rs = databaseConnector.executeQuery("SELECT * FROM " + tableName + " WHERE "+condition+" ORDER BY("+columnName+") "+sorted+";");
            else
                rs = databaseConnector.executeQuery("SELECT * FROM " + tableName + " WHERE "+condition+";");

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
    public List<Map<String, String>> getPrimaryKeys() throws SQLException {
        List<Map<String, String>> primaryKeys = new ArrayList<>();
        Map<String, String> primaryKeyMap;
        ResultSet rs;

        for(String tableName: tableNames) {
            primaryKeyMap = new HashMap<>();
            rs = databaseConnector.executeQuery("DESC " + tableName + ";");
            while (rs.next()){
                if(rs.getString("Key").equals("PRI")) {
                    primaryKeyMap.put(tableName, rs.getString("Field") + " " + rs.getString("Type"));
                    break;
                }
            }
            primaryKeys.add(primaryKeyMap);
        }
        return primaryKeys;
    }

    @Override
    public List<Table> getTables(){
        return tables;
    }
    /*
     * Removes table from tables List using table's name.
     *
     * @param String tableName
     * */
    @Override
    public void removeTableFromList(String tableName){
        tables.removeIf(table -> table.getTableName().equals(tableName));
    }

    /*
     * Returns table object using table name.
     *
     * @param String tableName
     * @returns Logic.Table table
     * */
    @Override
    public Table getTable(String tableName) {

        for (Table table : tables) {
            if (table.getTableName().equals(tableName))
                return table;
        }
        return null;
    }

    @Override
    public List<String> getTableNames() {
        return tableNames;
    }
}