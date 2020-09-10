package GUI;

import Logic.Model;
import Logic.Table;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddRowWindow extends MyDialog {

    private String tableName;
    private Table table;
    private JTable displayedTable;
    private DisplayTableWindow tableWindow;
    private Model model;

    public AddRowWindow(String tableName, Model model, DisplayTableWindow tableWindow){

        this.model = model;
        this.tableName = tableName;
        this.tableWindow = tableWindow;

        table = model.getTable(tableName);
        String title = "Add row";
        setTitle(title);
        int height = 115;
        int width = 400;
        setSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2,1));
        initWindow();
        setVisible(true);
    }
    @Override
    public void initWindow() {

        displayTable(null);
        JPanel buttonsPanel = new JPanel(new GridLayout(1,3));
        addButton(0,0,0,0,"Add",event->{
            try {
                model.addRow(getRow(), tableName);
//                tableWindow.displayTable(table.getData());
                tableWindow.displayTable(model.importTable(tableName).getData());
                dispose();
            }catch (SQLException sqlException){
                new WarningWindow(sqlException.getMessage(), null, null);
                System.out.println(sqlException.getMessage());
            }
            },true, buttonsPanel);
        addButton(0,0,0,0,"Test",event->{
            try {
                model.addRow(getRow(), tableName);
                model.deleteRow(tableName, table.getNumberOfRows());
                new WarningWindow("Row ok",null,null);
            } catch (SQLException sqlException) {
                new WarningWindow(sqlException.getMessage(), null,  null);
            }
        },true, buttonsPanel);
        addButton(0,0,0,0,"Cancel",event->dispose(),true, buttonsPanel);
        add(buttonsPanel);

    }
    @Override
    public void displayTable(List<List<Object>> data){

        displayedTable = new JTable(new DefaultTableModel(new Object[][]{{}}, table.getColumnNames().toArray()));
        JScrollPane scrollPane = new JScrollPane(displayedTable);
        add(scrollPane);
    }
    private List<Object> getRow(){
        List<Object> row = new ArrayList<>();
        for(int i = 0; i < displayedTable.getColumnCount(); i++)
            row.add(displayedTable.getValueAt(0,i));

        return row;
    }
}
