import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* Table class keeps basic table parametres: table name, number of columns, number of rows,
* column names, column types and table data.
* */
public class Table {

    private String tableName;
    private int numberOfColumns;
    private int numberOfRows;
    private Object[][] data;
    private List<String> columnNames;
    private List<String> columnTypes;


    public Table(String tableName, int numberOfColumns, int numberOfRows, List<String> columnNames, List<String> columnTypes, Object[][] data){

        this.tableName = tableName;
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
        this.data = data;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
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

    public List<String> getColumnNames() {
        return columnNames;
    }

    public Object[][] getData() {
        return data;
    }

    public List<String> getColumnTypes(){
        return columnTypes;
    }

    public void addColumn(String columnName, String columnType){

        columnNames.add(columnName);

        switch (columnType) {
            case "String":
                columnTypes.add(String.valueOf(String.class));
                break;
            case "Integer":
                columnTypes.add(String.valueOf(Integer.class));
                break;
            case "Double":
                columnTypes.add(String.valueOf(Double.class));
                break;
            case "Float":
                columnTypes.add(String.valueOf(Float.class));
                break;
        }
    }
    @Override
    public String toString(){
        return "Nazwa tabeli: "+tableName+"\nIlosc kolumn: "+numberOfColumns+"\nIlosc wierszy: "+
                numberOfRows+"\nNazwy kolumn: "+ columnNames +"\nTypy kolumn: "+columnTypes +"\nKontent: "+
                Arrays.deepToString(data);
    }
}
