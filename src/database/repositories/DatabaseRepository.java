package database.repositories;

import database.models.Table;
import database.templates.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DatabaseRepository implements DatabaseRepositoryApi {

    private final DatabaseQueryExecutorConnectionApi databaseQueryExecutor;
    private final TableRepositoryApi tableRepository;
    private List<String> tableNames;
    private List<Table> tables;

    public DatabaseRepository(final DatabaseQueryExecutorConnectionApi databaseQueryExecutor) {
        this.databaseQueryExecutor = databaseQueryExecutor;
        tables = new ArrayList<>();
        tableRepository = new TableRepository(databaseQueryExecutor, tables);
        tableNames = new ArrayList<>();
    }

    @Override
    public void importDatabase() throws SQLException {

        ResultSet rs = databaseQueryExecutor.getExecutedQueryResultSet("SHOW TABLES;");
        tableNames.clear();
        String tableName;
        String database = databaseQueryExecutor.getDatabaseConnector().getDatabasePropertiesMap().get("databaseName");
        while (rs.next()) {
            tableName = rs.getString("Tables_in_" + database.substring(database.lastIndexOf("/") + 1));
            tableNames.add(tableName);
            tables.add(tableRepository.importTable(tableName));
        }
    }

    @Override
    public Map<String, String> getPrimaryKeys() throws SQLException {
        Map<String, String> primaryKeyMap = new HashMap<>();
        ResultSet rs;

        for(String tableName: tableNames) {
            rs = databaseQueryExecutor.getExecutedQueryResultSet("DESC " + tableName + ";");
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
    public List<String> getTableNames() throws SQLException {
        String database = databaseQueryExecutor.getDatabaseConnector().getDatabasePropertiesMap().get("databaseName");
        String query = "SHOW TABLES IN "+database+";";
        ResultSet rs = databaseQueryExecutor.getExecutedQueryResultSet(query);
        tableNames.clear();

        while (rs.next())
            tableNames.add(rs.getString("Tables_in_"+database));

        return tableNames;
    }
}