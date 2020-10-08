package GUI;

import Logic.Model;
import Logic.Table;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
* DisplayTableWindow
*
* @extends MyFrame
*
* This window allows to edit table (display table, search table, add new row, delete row).
*
* */
public class EditTableWindow extends MyFrame{

    private Table table;
    private Model model;
    private JTable displayedTable;
    private Object[][] tableData;
    private JScrollPane tableScrollPane;
    private Object buffer;
    private Color backgroundColor = new Color(67,67,67);
    private JButton removeRowButton;

    public EditTableWindow(Model model, String tableName){

//        this.table = model.getTable(tableName);
        try {
            this.table = model.importTable(tableName);
            this.model = model;
            final int width = 600;
            final int height = 350;

            tableData = new Object[table.getData().size()][table.getColumnNames().size()];
            setTitle(tableName);
            setPreferredSize(new Dimension(width, height));
            createWidgets();
            setVisible(true);
            pack();
        } catch (SQLException sqlException) {
            new WarningWindow(sqlException.getMessage(),null,null);
        }
    }

    @Override
    public void createWidgets() {

//        -----------------------------------mainPanel------------------------------------------------------------------

        JPanel mainPanel = createGridPanel(2,1,0,20,20);

//        -----------------------------------tablePanel-----------------------------------------------------------------

        JPanel tablePanel = createGridPanel(1,1,0,0,0);

//        -----------------------------------buttonsPanel---------------------------------------------------------------

        JPanel buttonsPanel = createGridPanel(2,2,20,20,0);

//-------------------------------------------tableScrollPane------------------------------------------------------------

        tableScrollPane = new JScrollPane(displayedTable);
        tableScrollPane.getViewport().setBackground(backgroundColor);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

        displayTable(table.getData());


//--------------------------------------------searchTableButton---------------------------------------------------------

        JButton searchTableButton = createButton("Search Table", event->new SearchTableWindow(this, table.getTableName()),true);

//--------------------------------------------addRowButton--------------------------------------------------------------

        JButton addRowButton = createButton("Add Row", event->new AddRowWindow(table, model, this),true);

//--------------------------------------------removeRowButton-----------------------------------------------------------

        removeRowButton = createButton("Remove Row", event->new WarningWindow("Remove row?",subEvent->{

            try {
                List<Object> row = new ArrayList<>();
                for(int index = 0; index < table.getNumberOfColumns(); index++){
                    row.add(displayedTable.getValueAt(displayedTable.getSelectedRow(), index));
                }
                model.deleteRow(table, row);
                displayTable(model.importTable(table.getTableName()).getData());


                removeRowButton.setEnabled(false);
            } catch(IndexOutOfBoundsException exception){
                new WarningWindow("No row selected",null, null);
                System.out.println(exception.getMessage());
            } catch (SQLException sqlException){
                new WarningWindow(sqlException.getMessage(),null, null);
                System.out.println(sqlException.getMessage());
            }
        }, null), false);

//---------------------------------------------closeButton--------------------------------------------------------------

        JButton closeButton = createButton("Close", event->dispose(), true);

        buttonsPanel.add(searchTableButton);
        buttonsPanel.add(addRowButton);
        buttonsPanel.add(removeRowButton);
        buttonsPanel.add(closeButton);

        tablePanel.add(tableScrollPane);
        mainPanel.add(tablePanel);
        mainPanel.add(buttonsPanel);
        add(mainPanel);
    }

    /*
    * saveTable method returns multidimensional ArrayList with data collected from newTable (JTable)
    *
    * @return rows
    * */

    private List<List<Object>> saveTable(){

        List<List<Object>> rows = new ArrayList<>();
        List<Object> columns;
        displayedTable.clearSelection();
        for(int i = 0; i < displayedTable.getRowCount();i++) {
            columns = new ArrayList<>();
            for (int j = 0; j < displayedTable.getColumnCount(); j++)
                columns.add(displayedTable.getValueAt(i, j));
            rows.add(columns);
        }
        return rows;
    }
    public Table getTable(){
        return table;
    }
    public Object[][] getTableData(){
        return tableData;
    }
    public Model getModel(){
        return model;
    }
    @Override
    public void displayTable(List<List<Object>> data){


        tableData = new Object[data.size()][table.getColumnNames().size()];
        for(int i = 0 ; i < data.size() ; i++)
            for(int j = 0 ; j < table.getNumberOfColumns() ; j++)
                tableData[i][j] = data.get(i).get(j);


        displayedTable = new JTable(new DefaultTableModel(tableData,table.getColumnNames().toArray()));
        displayedTable.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if(displayedTable.getSelectedRow() != -1) {
                    removeRowButton.setEnabled(true);
                    buffer = displayedTable.getValueAt(displayedTable.getSelectedRow(),displayedTable.getSelectedColumn());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        displayedTable.getModel().addTableModelListener(event -> {

            int rowIndex = displayedTable.getSelectedRow();
            int columnIndex = displayedTable.getSelectedColumn();
            List<List<Object>> newData;
            Object value = displayedTable.getValueAt(rowIndex, columnIndex);

            if(!value.equals(buffer))
                try{
                    newData = saveTable();

                    model.updateRow(table.getTableName(), newData, rowIndex, columnIndex, buffer, value);

                }
                catch (SQLException sqlException){

                    new WarningWindow(sqlException.getMessage(), null, null);
                    System.out.println(sqlException.getMessage());
                }
        });
        displayedTable.setBorder(null);
        tableScrollPane.setViewportView(displayedTable);
    }
}