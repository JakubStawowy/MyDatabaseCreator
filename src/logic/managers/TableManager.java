package logic.managers;

import logic.models.Table;
import logic.templates.DatabaseConnector;
import logic.templates.DdlManager;
import logic.templates.TableRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableManager implements DdlManager {

    private DatabaseConnector databaseConnector;
    private TableRepository tableRepository;
    private List<String> tableNames = new ArrayList<>();

    public TableManager(DatabaseConnector databaseConnector, TableRepository tableRepository) {
        this.databaseConnector = databaseConnector;
        this.tableRepository = tableRepository;
    }

    /*
     * dropTable method removes table from database using table name
     *
     * @param String tableName
     * */
    @Override
    public void dropTable(String tableName) throws SQLException {
        databaseConnector.execute("DROP TABLE IF EXISTS " + tableName + ";");
        tableRepository.removeTableFromList(tableName);
    }

    @Override
    public void dropAllTables() throws SQLException {
        for(String tableName: tableNames)
            dropTable(tableName);
    }

    @Override
    public void createTable(Table table, String primaryKey, Boolean dropExistingTable) throws SQLException {
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

        Logger.getGlobal().log(Level.INFO, String.valueOf(query));
        databaseConnector.execute(String.valueOf(query));
        tableNames.add(table.getTableName());
        tableRepository.getTables().add(table);
    }
}
