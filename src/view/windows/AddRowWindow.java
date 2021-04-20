package view.windows;

import logic.facades.DatabaseFacade;
import view.components.MdcFrame;
import logic.models.Table;

import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddRowWindow extends MdcFrame {

    private String tableName;
    private Table table;
    private JTable displayedTable;
    private MdcFrame tableWindow;
    private JPanel tablePanel = createGridPanel(1,1,0,0, 0);
    private DatabaseFacade databaseFacade;

    public AddRowWindow(Table table, DatabaseFacade databaseFacade, MdcFrame tableWindow){

        this.databaseFacade = databaseFacade;
        this.tableWindow = tableWindow;
        final String title = "Add row";
        final int height = 200;
        final int width = 400;

        tableName = table.getTableName();
        this.table = table;
        setTitle(title);
        setSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        createWidgets();
        setVisible(true);
    }
    @Override
    public void createWidgets() {

        displayTable(null);

        JPanel mainPanel = createGridPanel(2,1,0,20, 20);

        JPanel buttonsPanel = createGridPanel(1,3,20,0, 0);

        JButton addRowButton = createButton("Add",event->{
            try {
                databaseFacade.addRow(getRow(), table);
                tableWindow.displayTable(databaseFacade.importTable(tableName).getData());
                dispose();
            }catch (SQLException sqlException){
                new WarningWindow(sqlException.getMessage(), null, null);
                System.out.println(sqlException.getMessage());
            }
        },true);

        JButton testButton = createButton("Test",event->{
            try {
                databaseFacade.addRow(getRow(), table);
                databaseFacade.deleteRow(table, getRow());
                new WarningWindow("Row ok",null,null);
            } catch (SQLException sqlException) {
                new WarningWindow(sqlException.getMessage(), null,  null);
            }
        },true);

        JButton cancelButton = createButton("Cancel",event->dispose(),true);

        buttonsPanel.add(addRowButton);
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
