import javax.xml.crypto.Data;
import java.util.List;

/*
* Table class keeps basic table parametres: table name, number of columns, number of rows,
* column names, column types and table data.
* */
public class Table {

    private String tableName;
    private int numberOfColumns;
    private int numberOfRows;
    private List<List<Object>> data;
    private List<String> columnNames;
    private List<String> columnTypes;


    public Table(String tableName, int numberOfColumns, int numberOfRows, List<String> columnNames, List<String> columnTypes, List<List<Object>> data){

        this.tableName = tableName;
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
        this.data = data;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
    }

    public void numberOfRowsDeincrement(){
        numberOfRows--;
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

    public List<List<Object>> getData() {
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
                data;
    }
    public void setData(List<List<Object>> data){
        //if(!this.data.equals(data)){
            this.data = data;
        //}
    }
}
