
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
    * generateRandomCondition method generates random condition using random column and random cell value in that column.
    *
    * @param table
    * */
    public String generateRandomCondition(Table table){

        Random random = new Random();
        int randomColumnIndex = random.nextInt(table.getNumberOfColumns());
        int randomRowIndex = random.nextInt(table.getNumberOfRows());
        int randomOperatorIndex;

        String[] operators = new String[]{"=", "!=", ">", "<", "<=",">="};
        String cellData;
        String columnType = table.getColumnTypes().get(randomColumnIndex);
        if(columnType.equals("varchar(30)") || columnType.equals("date")) {

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
    /*
    * dropTable method removes table from database using table name
    *
    * @param tableName
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
    * @param tableName
    * @param condition
    * @param sorted ("ASC"/"DESC"/null)
    * @param columnName - table would be sorted using column with that name
    *
    * @return data (multidimensional ArrayList)
    * */
    public List<List<Object>> searchTable(String tableName, String condition, String sorted, String columnName) {

        ResultSet rs;

        List<String> columnNames = new ArrayList<>();
        List<Object> columns;
        List<List<Object>> data = new LinkedList<>();

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
