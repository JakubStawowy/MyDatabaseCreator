package GUI;
import Logic.Model;
import Logic.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;

/*
* DisplayTableWindow class is used to display all table data.
* */
public class EditTableWindow extends MyFrame{

    private Table table;
    private Model model;
    private JTable displayedTable;
    private Object[][] tableData;
    private JScrollPane scrollPane;
    private Object buffer;
    private Color backgroundColor = new Color(67,67,67);
    JButton removeRowButton;

    public EditTableWindow(Model model, String tableName){

        this.table = model.importTable(tableName);
        this.model = model;
        setLocation(new Point(300,200));
        setPreferredSize(new Dimension(600,350));
        setTitle(table.getTableName());
        tableData = new Object[table.getData().size()][table.getColumnNames().size()];
        initWindow();
        setVisible(true);
        pack();
    }

    @Override
    public void initWindow() {
        JPanel mainPanel = createGridPanel(2,1,0,20,20);
        JPanel tablePanel = createGridPanel(1,1,0,0,0);
        JPanel buttonsPanel = createGridPanel(2,2,20,20,0);

        //------------------------------------DisplayedTable---------------------------------------------------

        scrollPane = new JScrollPane(displayedTable);
        scrollPane.getViewport().setBackground(backgroundColor);
        displayTable(table.getData());
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));


        //------------------------------------SearchTableButton---------------------------------------------------

        JButton searchTableButton = createButton("Search Table", event->new SearchTableWindow(this),true);

        //------------------------------------AddRowButton---------------------------------------------------

        JButton addRowButton = createButton("Add Row", event->new AddRowWindow(table.getTableName(), model, this), true);

        //------------------------------------RemoveRowButton---------------------------------------------------

        removeRowButton = createButton("Remove Row", event->new WarningWindow("Remove row?",subEvent->{

            try {
                List<Object> row = new ArrayList<>();
                for(int index = 0; index < table.getNumberOfColumns(); index++){
                    row.add(displayedTable.getValueAt(displayedTable.getSelectedRow(), index));
                }
                model.deleteRow_2(table.getTableName(), row);
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

        //------------------------------------CloseButton---------------------------------------------------

        JButton closeButton = createButton("Close", event->{
            model.dropCopiedTable(table.getTableName());
            dispose();
        }, true);

        buttonsPanel.add(searchTableButton);
        buttonsPanel.add(addRowButton);
        buttonsPanel.add(removeRowButton);
        buttonsPanel.add(closeButton);

        tablePanel.add(scrollPane);
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
    /*
    * displayTable method displays table using data parameter
    *
    * @param data (multidimensional ArrayList)
    * */

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
        scrollPane.setViewportView(displayedTable);
    }
}