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
public class DisplayTableWindow extends JFrame implements MyWindow{

    private Table table;
    private Model model;
    private JTable displayedTable;
    private Object[][] tableData;
    private JScrollPane scrollPane;
    private Object buffer;
    JButton removeRowButton;

    public DisplayTableWindow(Model model, String tableName){

        this.table = model.importTable(tableName);
//        tablecopy = model.importTable(tableName);
//        model.copyTable(table.getTableName()); /

        this.model = model;
        tableData = new Object[table.getData().size()][table.getColumnNames().size()];
        initWindow();

        setLocation(new Point(300,200));
        setTitle(table.getTableName());

        setPreferredSize(new Dimension(600,350));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                model.dropCopiedTable(table.getTableName());
            }
        });
        setVisible(true);
        pack();


    }

    @Override
    public void initWindow() {
        JPanel mainPanel = new JPanel(new GridLayout(2,1));

        //------------------------------------DisplayedTable---------------------------------------------------
        scrollPane = new JScrollPane(displayedTable);
        displayTable(table.getData());

        mainPanel.add(scrollPane);

        //------------------------------------ButtonsPanel---------------------------------------------------
        JPanel panel = new JPanel(null);
        JPanel subPanel = new JPanel(new GridLayout(2,2,20,20));
        subPanel.setBounds(10,10,565,135);

        //------------------------------------SearchTableButton---------------------------------------------------
        JButton searchTableButton = new JButton("Search table");
        searchTableButton.addActionListener(event->new SearchTableWindow(this));

        //------------------------------------AddRowButton---------------------------------------------------
        JButton addRowButton = new JButton("Add row");
        addRowButton.addActionListener(event->new AddRowWindow(table.getTableName(), model, this));

        //------------------------------------RemoveRowButton---------------------------------------------------
        removeRowButton = new JButton("Remove row");
        removeRowButton.setEnabled(false);
        removeRowButton.addActionListener(event->new WarningWindow("Remove row?",subEvent->{

            try {
//                int index = displayedTable.getSelectedRow();
//                model.deleteRow(table.getTableName(), index);
//                table.removeRow(index);
                List<Object> row = new ArrayList<>();
                for(int index = 0; index < table.getNumberOfColumns(); index++){
                    row.add(displayedTable.getValueAt(displayedTable.getSelectedRow(), index));
                }
                model.deleteRow_2(table.getTableName(), row);
                displayTable(model.importTable(table.getTableName()).getData());

//                Rectangle rect = displayedTable.getCellRect(index-1,0,true);
//                displayedTable.scrollRectToVisible(rect);

                removeRowButton.setEnabled(false);
            } catch(IndexOutOfBoundsException exception){
                new WarningWindow("No row selected",null, null);
                System.out.println(exception.getMessage());
            } catch (SQLException sqlException){
                new WarningWindow(sqlException.getMessage(),null, null);
                System.out.println(sqlException.getMessage());
            }
        }, null));

        //------------------------------------UndoChangesButton---------------------------------------------------
//        JButton undoChangesButton = new JButton("Undo changes");
//        undoChangesButton.setEnabled(true);
//        undoChangesButton.addActionListener(event->new WarningWindow("Are you sure you want to undo changes?", subEvent->{
//
//            try {
//                model.undoChanges(table.getTableName());
//            } catch (SQLException sqlException) {
//                System.out.println(sqlException.getMessage());
//                new WarningWindow("Undo changes failed", null, null);
//            }
//            displayTable(tablecopy.getData());
//        }, null));

        //------------------------------------CloseButton---------------------------------------------------
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(event->{
            model.dropCopiedTable(table.getTableName());
            dispose();
        });


        subPanel.add(searchTableButton);
        subPanel.add(addRowButton);
        subPanel.add(removeRowButton);
//        subPanel.add(undoChangesButton);
        subPanel.add(closeButton);

        panel.add(subPanel);
        mainPanel.add(panel);
        add(mainPanel);
    }

    @Override
    public JButton addButton(int x, int y, int width, int height, String text, ActionListener actionListener, Boolean buttonEnable, JPanel panel) {
        return null;
    }

    @Override
    public JTextField addTextField(int x, int y, int width, int height, String text, JPanel panel) {
        return null;
    }

    @Override
    public JLabel addLabel(int x, int y, int width, int height, String text, JPanel panel) {
        return null;
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
                    System.out.println("ROW INDEX: "+rowIndex);
                    System.out.println("Column INDEX: "+columnIndex);
                    //displayedTable.setValueAt(buffer, rowIndex, columnIndex);

                    new WarningWindow(sqlException.getMessage(), null, null);
                    System.out.println(sqlException.getMessage());
                }
        });
        scrollPane.setViewportView(displayedTable);
    }
}