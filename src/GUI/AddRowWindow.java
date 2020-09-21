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
    private MyFrame tableWindow;
    private JPanel tablePanel = createGridPanel(1,1,0,0, 0);
    private Model model;

    public AddRowWindow(String tableName, Model model, MyFrame tableWindow){

        this.model = model;
        this.tableName = tableName;
        this.tableWindow = tableWindow;

        table = model.getTable(tableName);
        String title = "Add row";
        setTitle(title);
        int height = 200;
        int width = 400;
        setSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        initWindow();
        setVisible(true);
    }
    @Override
    public void initWindow() {

        displayTable(null);
        JPanel mainPanel = createGridPanel(2,1,0,20, 20);
        JPanel buttonsPanel = createGridPanel(1,3,20,0, 0);

        JButton createButton = createButton("Add",event->{
            try {
                model.addRow(getRow(), tableName);
                tableWindow.displayTable(model.importTable(tableName).getData());
                dispose();
            }catch (SQLException sqlException){
                new WarningWindow(sqlException.getMessage(), null, null);
                System.out.println(sqlException.getMessage());
            }
            },true);

        JButton testButton = createButton("Test",event->{
            try {
                model.addRow(getRow(), tableName);
                model.deleteRow(tableName, table.getNumberOfRows());
                new WarningWindow("Row ok",null,null);
            } catch (SQLException sqlException) {
                new WarningWindow(sqlException.getMessage(), null,  null);
            }
        },true);

        JButton cancelButton = createButton("Cancel",event->dispose(),true);

        buttonsPanel.add(createButton);
        buttonsPanel.add(testButton);
        buttonsPanel.add(cancelButton);

        mainPanel.add(tablePanel);
        mainPanel.add(buttonsPanel);

        add(mainPanel);
    }
    @Override
    public void displayTable(List<List<Object>> data){

        displayedTable = new JTable(new DefaultTableModel(new Object[][]{{}}, table.getColumnNames().toArray()));
        JScrollPane scrollPane = new JScrollPane(displayedTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        scrollPane.getViewport().setBackground(new Color(67,67,67));
        tablePanel.add(scrollPane);
    }
    private List<Object> getRow(){
        List<Object> row = new ArrayList<>();
        for(int i = 0; i < displayedTable.getColumnCount(); i++)
            row.add(displayedTable.getValueAt(0,i));

        return row;
    }
}
