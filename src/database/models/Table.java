package database.models;

import java.util.List;
import java.util.Vector;

/*
* Table
* this class keeps basic table properties: table name, number of columns, number of rows, column names, column types and table data.
* */
public final class Table {

    private String tableName;
    private List<List<Object>> data;
    private Vector<String> columnNames;
    private Vector<String> columnTypes;
    private Vector<String> constraints;
    private Vector<String> foreignKeys;

    public Table(String tableName, List<List<Object>> data, Vector<String> columnNames, Vector<String> columnTypes, Vector<String> constraints, Vector<String> foreignKeys){

        this.tableName = tableName;
        this.data = data;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.constraints = constraints;
        this.foreignKeys = foreignKeys;
    }

    public void removeRow(int index){
        data.remove(index);
    }
    public void removeRow(List<Object> row){
        data.remove(row);
    }
    public String getTableName() {
        return tableName;
    }

    public int getNumberOfColumns() {
        return columnNames.size();
    }

    public int getNumberOfRows() {
        return data.size();
    }

    public Vector<String> getColumnNames() {
        return columnNames;
    }

    public List<List<Object>> getData() {
        return data;
    }

    public Vector<String> getColumnTypes(){
        return columnTypes;
    }

    public void setData(List<List<Object>> data){
            this.data = data;
    }

    public void addRow(List<Object> row){
        data.add(row); }

    public Vector<String> getConstraintsVector() {
        return constraints;
    }

    public Vector<String> getForeignKeys() {
        return foreignKeys;
    }

}
