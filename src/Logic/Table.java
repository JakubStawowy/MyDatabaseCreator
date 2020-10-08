package Logic;

import java.util.List;
import java.util.Vector;

/*
* Table
* this class keeps basic table parametres: table name, number of columns, number of rows, column names, column types and table data.
* */
public class Table {

    private String tableName;
    private int numberOfColumns;
    private int numberOfRows;
    private int primaryKeyColumnIndex;
    private List<List<Object>> data;
    private Vector<String> columnNames;
    private Vector<String> columnTypes;
    private Vector<String> constraints;
    private Vector<String> foreignKeys;

    public Table(String tableName, int primaryKeyColumnIndex, List<List<Object>> data, Vector<String> columnNames, Vector<String> columnTypes, Vector<String> constraints, Vector<String> foreignKeys){

        this.tableName = tableName;
        this.primaryKeyColumnIndex = primaryKeyColumnIndex;
        this.data = data;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.constraints = constraints;
        this.foreignKeys = foreignKeys;
        numberOfColumns = columnNames.size();
        numberOfRows = data.size();
    }
    public void removeRow(int index){
        data.remove(index);
        numberOfRows = data.size();
    }
    public void removeRow(List<Object> row){
        data.remove(row);
        numberOfRows = data.size();
    }
    public String getTableName() {
        return tableName;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getPrimaryKeyColumnIndex(){ return primaryKeyColumnIndex; }
    public Vector<String> getColumnNames() {
        return columnNames;
    }

    public List<List<Object>> getData() {
        return data;
    }

    public Vector<String> getColumnTypes(){
        return columnTypes;
    }
    /*
    * setData method sets new table data and table number of rows
    * */
    public void setData(List<List<Object>> data){

            this.data = data;
            numberOfRows = data.size();
    }
    public void addRow(List<Object> row){
        data.add(row); numberOfRows=data.size(); }

    public Vector<String> getConstraintsVector() {
        return constraints;
    }

    public Vector<String> getForeignKeys() {
        return foreignKeys;
    }

    @Override
    public String toString() {
        return "Name: "+tableName+"\nContent"+data;
    }
}
