package Logic;

import GUI.CreateTableWindow;
import Logic.MyExceptions.EmptyTableException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

/*
* Model
* this class is responsible for most logical methods such as creating tables, saving tables etc.
* */
public class Model {

    private List<Table> tables = new LinkedList<>();
    private List<String> tableNames = new LinkedList<>();
    private final String[] numericTypes = {"bit", "tinyint", "smallint","mediumint", "bigint",
            "int", "boolean", "bool", "integer", "float" ,"double", "decimal", "dec"};
    private final String[] stringTypes ={"char", "varchar", "binary", "tinyblob", "tinytext", "text",
            "blob", "mediumtext", "mediumblob", "longtext", "longblob", "enum", "set"};
    private DatabaseConnector dbConnector;
    private String databaseName;
    private String username;
    private String password;

    public List<String> getTableNames(){
        return tableNames;
    }
    public String getDatabaseName(){
        return databaseName.substring(databaseName.lastIndexOf("/")+1);
    }

    /*
    * Creates new instance of Logic.DatabaseConnector(connects with database)
    * @param String databaseName
    * @param String username
    * @param String password
    * */
    public Model(String databaseName, String username, String password) throws SQLException {

        dbConnector = new DatabaseConnector(databaseName, username, password);
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    /*
    * Removes table from tables List using table's name.
    *
    * @param String tableName
    * */
    public void removeTableFromList(String tableName){
        tables.removeIf(table -> table.getTableName().equals(tableName));
    }

    /*
    * generateRandomCondition method generates random condition using random column and random cell value in that column.
    *
    * @param Logic.Table table
    * */
    public String generateRandomCondition(Table table) throws EmptyTableException {

        if(table.getNumberOfRows()==0)
            throw new EmptyTableException("Cannot generate condition. Table "+table.getTableName()+" is empty");
        Random random = new Random();
        int randomColumnIndex = random.nextInt(table.getNumberOfColumns());
        int randomRowIndex = random.nextInt(table.getNumberOfRows());
        int randomOperatorIndex;

        String[] operators = new String[]{"=", "!=", ">", "<", "<=",">="};
        String cellData;
        String columnType = table.getColumnTypes().get(randomColumnIndex);
        if(!isNumeric(columnType)) {

            randomOperatorIndex = random.nextInt(2);
            cellData = "\""+table.getData().get(randomRowIndex).get(randomColumnIndex)+"\"";
        }
        else {

            randomOperatorIndex = random.nextInt(operators.length);
            cellData = String.valueOf(table.getData().get(randomRowIndex).get(randomColumnIndex));
        }

        return table.getColumnNames().get(randomColumnIndex)+operators[randomOperatorIndex]+cellData;
    }
    /*
    * Returns table object using table name.
    *
    * @param String tableName
    * @returns Logic.Table table
    * */
    public Table getTable(String tableName) {

        for (Table table : tables) {
            if (table.getTableName().equals(tableName))
                return table;
        }
        return null;
    }
    /*
    * Imports all tables from database using sql command "SHOW TABLES" and importTable() method.
    * */
    public void importDatabase() throws SQLException{

        ResultSet rs = dbConnector.executeQuery("SHOW TABLES;");
        tableNames.clear();
        String tableName;
        while(rs.next()){
            tableName = rs.getString("Tables_in_"+databaseName.substring(databaseName.lastIndexOf("/")+1));
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
    public Table importTable(String tableName) {
        ResultSet rs;

        Vector<String> columnNames = new Vector<>();
        Vector<String> columnTypes = new Vector<>();
        List<Object> columns;
        List<List<Object>> data = new LinkedList<>();
        int numberOfColumns = 0;
        int numberOfRows = 0;
        int primaryKeyColumnIndex = 0;
        try {
            rs = dbConnector.executeQuery("DESC " + tableName + ";");

            while(rs.next()) {

                if(rs.getString("Key").equals("PRI"))
                    primaryKeyColumnIndex = numberOfColumns;

                columnNames.add(rs.getString("Field"));
                columnTypes.add(rs.getString("Type"));

                numberOfColumns++;
            }

            rs = dbConnector.executeQuery("SELECT * FROM "+tableName+";");

            while(rs.next()){

                columns = new LinkedList<>();

                for(String column : columnNames){

                    columns.add(rs.getObject(column));
                }

                data.add(columns);
                numberOfRows++;
            }
        }catch(SQLException sqlException){

            System.out.println("Problemy z importem tabeli");
        }

        return new Table(tableName,primaryKeyColumnIndex, data, columnNames, columnTypes, null, null);
//        return new Table(tableName, numberOfColumns,numberOfRows, primaryKeyColumnIndex, data, columnNames, columnTypes);
    }
    /*
    * DeleteRow method removes selected row (using index) using sql query.
    *
    * @param String tableName
    * @param int rowIndex
    * */
    public void deleteRow(Table table, int rowIndex) throws SQLException {

        StringBuilder condition = new StringBuilder();
        Object value;
        String type;

        for(int index = 0; index < table.getNumberOfColumns(); index++){

            type = table.getColumnTypes().get(index);
            value = table.getData().get(rowIndex).get(index);
            if(!isNumeric(type))
                value = "\""+value+"\"";

            condition.append(table.getColumnNames().get(index)).append(" = ").append(value);
            if(index < table.getNumberOfColumns()-1)
                condition.append(" AND ");
        }

        String query = "DELETE FROM "+table.getTableName()+" WHERE "+condition+";";
        dbConnector.execute(query);
        table.removeRow(rowIndex);
//        table.getData().remove(rowIndex);
//        table.numberOfRowsDeincrement();
    }
     /*
     * DeleteRow method removes selected row (using row) using sql query.
     *
     * @param String tableName
     * @param List<Object> row
     * */
    public void deleteRow(Table table, List<Object> row) throws SQLException {
        StringBuilder condition = new StringBuilder();
        String type;
        for(int index = 0; index<table.getNumberOfColumns(); index++){
            type=table.getColumnTypes().get(index);
            if(!isNumeric(type))
                condition.append(table.getColumnNames().get(index)).append("=\"").append(row.get(index)).append("\"");
            else
                condition.append(table.getColumnNames().get(index)).append("=").append(row.get(index));

            if(index < table.getNumberOfColumns()-1)
                condition.append(" AND ");
        }
        String query = "DELETE FROM "+table.getTableName()+" WHERE "+condition+";";
        dbConnector.execute(query);
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
    public void updateRow(String tableName, List<List<Object>> newData, int rowIndex, int columnIndex, Object oldValue, Object newValue) throws SQLException{

        Table table = getTable(tableName);
        String type = table.getColumnTypes().get(columnIndex);
        String columnName = table.getColumnNames().get(columnIndex);

        if(!isNumeric(type)){
            newValue = "\""+newValue+"\"";
            oldValue = "\""+oldValue+"\"";
        }

        StringBuilder query = new StringBuilder("UPDATE "+tableName+" SET "+columnName+" = "+newValue+" WHERE ");
        for(int index = 0; index < table.getNumberOfColumns(); index++){
            if(index == columnIndex)
                query.append(columnName).append(" = ").append(oldValue);
            else {
                String columnType = table.getColumnTypes().get(index);
                if(!isNumeric(columnType)){

                    query.append(table.getColumnNames().get(index)).append(" = ").append("\"").append(newData.get(rowIndex).get(index)).append("\"");
                }
                else
                    query.append(table.getColumnNames().get(index)).append(" = ").append(newData.get(rowIndex).get(index));
            }
            if(index < table.getNumberOfColumns()-1)
                query.append(" AND ");
        }
        query.append(";");
        dbConnector.execute(String.valueOf(query));
    }
    public void addRow(List<Object> row, Table table) throws SQLException {

        String columntype;
        Object value;
        StringBuilder query = new StringBuilder("INSERT INTO " + table.getTableName() + " VALUES (");
        for(int i = 0; i < table.getNumberOfColumns(); i++){
            columntype = table.getColumnTypes().get(i);
            value = row.get(i);
            if(!isNumeric(columntype))
                value = "\""+row.get(i)+"\"";
            query.append(value).append(", ");
        }
        query = new StringBuilder(query.substring(0, query.length() - 2));
        query.append(");");
        dbConnector.execute(String.valueOf(query));

        table.addRow(row);
    }
    /*
    * dropCopiedTable method is used to drop buffer table created before editing table.
    *
    * @param String tableName
    * */
    public void dropCopiedTable(String tableName){
        String query = "DROP TABLE IF EXISTS "+tableName+"_cpy;";
        try{
            dbConnector.execute(query);
        }catch(SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }
    /*
    * dropTable method removes table from database using table name
    *
    * @param String tableName
    * */
    public void dropTable(String tableName) throws SQLException {

            dbConnector.execute("DROP TABLE IF EXISTS " + tableName + ";");

    }
    public void dropAllTables() throws SQLException {
        for(String tableName: tableNames)
            dropTable(tableName);
    }
    public List<Table> getTables(){
        return tables;
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
    public List<List<Object>> searchTable(String tableName, String condition, String sorted, String columnName) {

        ResultSet rs;

        List<String> columnNames = new ArrayList<>();
        List<Object> columns;
        List<List<Object>> data = new LinkedList<>();

        if(condition.equals(""))
            condition = "TRUE";

        try {
            rs = dbConnector.executeQuery("DESC "+tableName+";");

            while(rs.next()) {

                columnNames.add(rs.getString("Field"));
            }
            if(sorted!=null)
                rs = dbConnector.executeQuery("SELECT * FROM " + tableName + " WHERE "+condition+" ORDER BY("+columnName+") "+sorted+";");
            else
                rs = dbConnector.executeQuery("SELECT * FROM " + tableName + " WHERE "+condition+";");

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

        dbConnector.execute(String.valueOf(query));
        tableNames.add(table.getTableName());
        tables.add(table);
    }
    /*
    * isNumeric method is used to check if given type is numeric or no.
    *
    * @param String type
    * @return Boolean
    * */
    public Boolean isNumeric(String type) {

        type = type.toLowerCase();
        for (String types : numericTypes) {
            if(type.length()>=types.length() && types.equals(type.substring(0,types.length())))
                return true;
            else if(type.length()<types.length() && (type.equals(types.substring(0,type.length()))))
                return true;
        }
        return false;
    }
    public List<Map<String, String>> getPrimaryKeys() throws SQLException {
        List<Map<String, String>> primaryKeys = new ArrayList<>();
        Map<String, String> primaryKeyMap;
        ResultSet rs;

        for(String tableName: tableNames) {
            primaryKeyMap = new HashMap<>();
            rs = dbConnector.executeQuery("DESC " + tableName + ";");
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

    /*
    * getLoginData method is used to reconnect database with the same database name, username and password
    *
    * @return String[] LoginData
    * */
    public String[] getLoginData(){
        return new String[]{databaseName, username, password};
    }
    /*
    * Closes connection with database.
    * */
    public void closeConnection() throws SQLException {
        dbConnector.disconnect();
    }
}
