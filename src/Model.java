import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
/*
* Model class is responsible for most logical methods such as creating tables, saving tables etc.
* */
public class Model {

    private List<Table> tables = new LinkedList<>();
    private DatabaseConnector dbConnector;

    /*
    * Creates new instance of DatabaseConnector(connects with database)
    * @param host
    * @param username
    * @param password
    * */
    public Model(String host, String username, String password) throws SQLException {
        dbConnector = new DatabaseConnector(host, username, password);
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
}
