
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
    * @param host
    * @param username
    * @param password
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
                    query.append(" varchar(20)");
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
    * */
    public void addTableToList(Table table){
        tables.add(table);
    }

    /*
    * Removes table from tables List using table's name.
    *
    * @param tableName
    * */
    public void removeTableFromList(String tableName){

        tables.removeIf(table -> table.getTableName().equals(tableName));
    }

    /*
    * Prints table structure
    * */
    public void showTables(){
        for(Table table : tables){
            System.out.println(table);
        }
    }

    /*
    * Returns table object using table name.
    *
    * @param tableName
    * @returns table
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
    * @param tableName
    * @returns new Table object
    * */
    public Table importTable(String tableName) {
        ResultSet rs;

        List<String> columnNames = new ArrayList<>();
        List<String> columnTypes = new ArrayList<>();
        List<Object> columns;
        List<List<Object>> data = new LinkedList<>();
        int numberOfColumns = 0;
        int numberOfRows = 0;

        try {
            rs = dbConnector.executeQuery("DESC " + tableName + ";");

            while(rs.next()) {

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

        return new Table(tableName, numberOfColumns,numberOfRows,columnNames, columnTypes, data);
    }
    public void dropTable(String tableName){

        try {

            dbConnector.execute("DROP TABLE " + tableName + ";");

        }catch (SQLException sqlException){

            System.out.println("Blad przy usuwaniu tabeli");

        }
    }
    /*
    * Closes connection with database.
    * */
    public void closeConnection() throws SQLException {
        dbConnector.disconnect();
    }
}
