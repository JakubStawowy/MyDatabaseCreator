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
    private String[] columnNames;
    private Object[][] data;
    private List<String> columnTypes = new ArrayList<>();


    public Table(String tableName, int numberOfColumns, int numberOfRows, String[] columnNames, Object[][] data){

        this.tableName = tableName;
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;
        this.columnNames = columnNames;
        this.data = data;
        for(Object obj : data[0]){
            columnTypes.add(String.valueOf(obj.getClass()));
        }
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

    public String[] getColumnNames() {
        return columnNames;
    }

    public Object[][] getData() {
        return data;
    }

    public List<String> getColumnTypes(){
        return columnTypes;
    }

    @Override
    public String toString(){
        return "Nazwa tabeli: "+tableName+"\nIlosc kolumn: "+numberOfColumns+"\nIlosc wierszy: "+
                numberOfRows+"\nNazwy kolumn: "+ Arrays.toString(columnNames) +"\nKontent: "+
                Arrays.deepToString(data)+"\nTypy kolumn: "+columnTypes;
    }
}
