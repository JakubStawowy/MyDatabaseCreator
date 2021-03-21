package logic.repositories;

import logic.models.Table;
import logic.templates.DatabaseConnector;
import logic.templates.TableRepository;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseRepository implements TableRepository {

    private DatabaseConnector databaseConnector;
    private List<String> tableNames = new LinkedList<>();
    private List<Table> tables = new LinkedList<>();

    public DatabaseRepository(final DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public void importDatabase() throws SQLException {

        ResultSet rs = databaseConnector.executeQuery("SHOW TABLES;");
        tableNames.clear();
        String tableName;
        String database = databaseConnector.getDatabasePropertiesMap().get("databaseName");
        while (rs.next()) {
            tableName = rs.getString("Tables_in_" + database.substring(database.lastIndexOf("/") + 1));
            tableNames.add(tableName);
            tables.add(importTable(tableName));
        }
    }

    @Override
    public Table importTable(final String tableName) throws SQLException {
        ResultSet rs;

        Vector<String> columnNames = new Vector<>();
        Vector<String> columnTypes = new Vector<>();
        List<Object> columns;
        List<List<Object>> data = new LinkedList<>();
        rs = databaseConnector.executeQuery("DESC " + tableName + ";");

        while (rs.next()) {

            if (rs.getString("Key").equals("PRI"))

            columnNames.add(rs.getString("Field"));
            columnTypes.add(rs.getString("Type"));

        }

        rs = databaseConnector.executeQuery("SELECT * FROM " + tableName + ";");

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
    public Map<String, String> getPrimaryKeys() throws SQLException {
        Map<String, String> primaryKeyMap = new HashMap<>();
        ResultSet rs;

        for(String tableName: getTableNames()) {
            rs = databaseConnector.executeQuery("DESC " + tableName + ";");
            while (rs.next())
                if(rs.getString("Key").equals("PRI")) {
                    primaryKeyMap.put(tableName, rs.getString("Field") + " " + rs.getString("Type"));
                    break;
                }
        }

        Logger.getGlobal().log(Level.INFO, String.valueOf(primaryKeyMap));
        return primaryKeyMap;
    }

    @Override
    public List<Table> getTables(){
        return tables;
    }

    @Override
    public void removeTableFromList(final String tableName){
        tables.removeIf(table -> table.getTableName().equals(tableName));
    }

    @Override
    public Table getTable(final String tableName) {

        for (Table table : tables) {
            if (table.getTableName().equals(tableName))
                return table;
        }
        return null;
    }

    @Override
    public List<String> getTableNames() throws SQLException {
        String database = databaseConnector.getDatabasePropertiesMap().get("databaseName");
        String query = "SHOW TABLES IN "+database+";";
        ResultSet rs = databaseConnector.executeQuery(query);
        tableNames.clear();

        while (rs.next())
            tableNames.add(rs.getString("Tables_in_"+database));

        return tableNames;
    }
}