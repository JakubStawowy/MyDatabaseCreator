package database.managers;

import database.models.Table;
import database.templates.DatabaseConnectorApi;
import database.templates.DatabaseRepositoryApi;
import database.templates.DdlManagerApi;

import java.sql.SQLException;
import java.util.Vector;

public final class TableManager implements DdlManagerApi {

    private DatabaseConnectorApi databaseConnector;
    private DatabaseRepositoryApi databaseRepository;

    public TableManager(final DatabaseConnectorApi databaseConnector, final DatabaseRepositoryApi databaseRepository) {
        this.databaseConnector = databaseConnector;
        this.databaseRepository = databaseRepository;
    }

    @Override
    public void dropTable(final String tableName) throws SQLException {
        databaseConnector.execute("DROP TABLE IF EXISTS " + tableName + ";");
        databaseRepository.removeTableFromList(tableName);
    }

    @Override
    public void dropAllTables() throws SQLException {
        for(String tableName: databaseRepository.getTableNames())
            dropTable(tableName);
    }

    @Override
    public void createTable(final Table table, final String primaryKey, final Boolean dropExistingTable) throws SQLException {
        Vector<String> columnNames = table.getColumnNames();
        Vector<String> columnTypes = table.getColumnTypes();
        Vector<String> constraintsVector = table.getConstraintsVector();
        Vector<String> foreignKeys = table.getForeignKeys();

        StringBuilder query = new StringBuilder("CREATE TABLE "+table.getTableName()+"(");
        for(int index = 0 ; index < columnNames.size() ; index++) {
            query.append(columnNames.get(index)).append(" ").append(columnTypes.get(index).toUpperCase()).append(" ").append(constraintsVector.get(index).toUpperCase());
            if(index<columnNames.size()-1)
                query.append(", ");
        }
        if(!primaryKey.equals("None"))
            query.append(", PRIMARY KEY(").append(primaryKey).append(")");
        if(!foreignKeys.isEmpty())
            for(String foreignKey: foreignKeys)
                query.append(", ").append(foreignKey);
        query.append(");");
        if(dropExistingTable)
            dropTable(table.getTableName());

        databaseConnector.execute(String.valueOf(query));
        databaseRepository.getTables().add(table);
    }
}
