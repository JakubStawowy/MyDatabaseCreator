
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/*
* Model class is responsible for most logical methods such as creating tables, saving tables etc.
* */
public class Model {

    private List<Table> tables = new LinkedList<>();
    private List<String> tableNames = new LinkedList<>();
    private DatabaseConnector dbConnector;
    private String databaseName;

    public List<String> getTableNames(){

        return tableNames;
    }
    /*
    * Creates new instance of DatabaseConnector(connects with database)
    * @param String databaseName
    * @param String username
    * @param String password
    * */
    public Model(String databaseName, String username, String password) throws SQLException {

        dbConnector = new DatabaseConnector(databaseName, username, password);
        this.databaseName = databaseName;
    }

    /*
    * Saves tables structure (this method forms sql query to create all tables from tables List in database).
    * */
    public void saveTables() throws SQLException {

        for(Table table: tables){

            StringBuilder query = new StringBuilder("CREATE TABLE " + table.getTableName() + " (");

            int helpIndex = 0;

            for(String columnName : table.getColumnNames()){
                query.append(columnName);
                if(table.getColumnTypes().get(helpIndex).equals(String.valueOf(Integer.class))){
                    query.append(" int");
                }
                else if(table.getColumnTypes().get(helpIndex).equals(String.valueOf(String.class))){
                    query.append(" varchar");
                }

                else if(table.getColumnTypes().get(helpIndex).equals(String.valueOf(Float.class))){
                    query.append(" float");
                }

                else if(table.getColumnTypes().get(helpIndex).equals(String.valueOf(Double.class))){
                    query.append(" double");
                }
                query.append(", ");
                helpIndex++;
            }

            query.deleteCharAt(query.length()-2);
            query.deleteCharAt(query.length()-1);
            query.append(");");
            dbConnector.execute(String.valueOf(query));

        }
    }
    /*
    * Adds new table object to tables List.
    *
    * @param Table table
    * */
    public void addTableToList(Table table){
        tables.add(table);
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
    * @param Table table
    * */
    public String generateRandomCondition(Table table){

        Random random = new Random();
        int randomColumnIndex = random.nextInt(table.getNumberOfColumns());
        int randomRowIndex = random.nextInt(table.getNumberOfRows());
        int randomOperatorIndex;

        String[] operators = new String[]{"=", "!=", ">", "<", "<=",">="};
        String cellData;
        String columnType = table.getColumnTypes().get(randomColumnIndex);
        if(columnType.equals("date") || columnType.equals("text") || columnType.substring(0,3).equals("var")) {

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
    * @returns Table table
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
    * @returns new Table
    * */
    public Table importTable(String tableName) {
        ResultSet rs;

        List<String> columnNames = new ArrayList<>();
        List<String> columnTypes = new ArrayList<>();
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

        return new Table(tableName, numberOfColumns,numberOfRows, primaryKeyColumnIndex, data, columnNames, columnTypes);
    }
    /*
    * DeleteRow method removes selected row using sql query.
    *
    * @param String tableName
    * @param int rowIndex
    * */
    public void deleteRow(String tableName, int rowIndex){

        StringBuilder condition = new StringBuilder();
        Table table = getTable(tableName);
        Object value;
        String type;

        for(int index = 0; index < table.getNumberOfColumns(); index++){

            type = table.getColumnTypes().get(index);
            value = table.getData().get(rowIndex).get(index);
            if(type.equals("date") || type.equals("text")  || type.substring(0,3).equals("var"))
                value = "\""+value+"\"";

            condition.append(table.getColumnNames().get(index)).append(" = ").append(value);
            if(index < table.getNumberOfColumns()-1)
                condition.append(" AND ");
        }

        table.getData().remove(rowIndex);
        table.numberOfRowsDeincrement();
        String query = "DELETE FROM "+tableName+" WHERE "+condition+";";
        try{
            dbConnector.execute(query);
        }
        catch (SQLException sqlException){
            System.out.println("Blad przy usuwaniu wiersza");
        }
    }
    public void updateRow(String tableName, int rowIndex, int columnIndex, Object oldValue, Object newValue) throws SQLException{

        Table table = getTable(tableName);
        String type = table.getColumnTypes().get(columnIndex);
        String columnName = table.getColumnNames().get(columnIndex);

        if(type.equals("date") || type.equals("text")  || type.substring(0,3).equals("var")){
            newValue = "\""+newValue+"\"";
            oldValue = "\""+oldValue+"\"";
        }

        StringBuilder query = new StringBuilder("UPDATE "+tableName+" SET "+columnName+" = "+newValue+" WHERE ");
        for(int index = 0; index < table.getNumberOfColumns(); index++){
            if(index == columnIndex)
                query.append(columnName).append(" = ").append(oldValue);
            else {
                String columnType = table.getColumnTypes().get(index);
                if(columnType.equals("date") || columnType.equals("text") || columnType.substring(0,3).equals("var")){

                    query.append(table.getColumnNames().get(index)).append(" = ").append("\"").append(table.getData().get(rowIndex).get(index)).append("\"");
                }
                else
                    query.append(table.getColumnNames().get(index)).append(" = ").append(table.getData().get(rowIndex).get(index));
            }
            if(index < table.getNumberOfColumns()-1)
                query.append(" AND ");
        }
        query.append(";");

        System.out.println(query);
        dbConnector.execute(String.valueOf(query));
    }
    public void undoChanges(String tableName, Table oldTable) throws SQLException{

        Table actualTable = importTable(tableName);

        Object oldValue, actualValue;

        for(int rowIndex = 0 ; rowIndex < oldTable.getNumberOfRows(); rowIndex++)
            for(int columnIndex = 0 ; columnIndex < oldTable.getNumberOfColumns(); columnIndex++){

                oldValue = oldTable.getData().get(rowIndex).get(columnIndex);
                actualValue = actualTable.getData().get(rowIndex).get(columnIndex);

                if(!oldValue.equals(actualValue)){

                    updateRow(tableName,rowIndex, columnIndex,actualValue,oldValue);
                }
            }

    }
    /*
    * dropTable method removes table from database using table name
    *
    * @param String tableName
    * */
    public void dropTable(String tableName){

        try {

            dbConnector.execute("DROP TABLE " + tableName + ";");

        }catch (SQLException sqlException){

            System.out.println("Blad przy usuwaniu tabeli");

        }
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

    /*
    * Closes connection with database.
    * */
    public void closeConnection() throws SQLException {
        dbConnector.disconnect();
    }
}
