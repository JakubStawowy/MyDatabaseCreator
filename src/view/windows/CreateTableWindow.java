package view.windows;

import database.facades.DatabaseFacade;
import controllers.facades.ValidatorFacade;
import controllers.templates.ValidatorFacadeApi;
import view.components.MdcFrame;
import exceptions.BadColumnNumberException;
import exceptions.BadTableNameException;
import exceptions.NoPrimaryKeyException;
import database.models.Table;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.Rectangle;
import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CreateTableWindow extends MdcFrame {

    private DatabaseFacade databaseFacade;
    private ValidatorFacadeApi validatorFacade;
    private Object[][] tableData;
    private JTable displayedTable;
    private JScrollPane tableScrollPane;
    private JLabel numberOfColumnLabel;
    private JTextField tableNameField;
    private int numberOfRows = 0;
    private JComboBox<String> primaryKeyComboBox;
    private Vector<String> columnNames = new Vector<>();
    private Vector<String> columnTypes = new Vector<>();
    private Vector<String> constraintsVector = new Vector<>();
    private Vector<String> foreignKeys = new Vector<>();
    private MainWindow mainWindow;

    public CreateTableWindow(DatabaseFacade databaseFacade, MainWindow mainWindow){

        this.databaseFacade = databaseFacade;
        this.mainWindow = mainWindow;
        final String title = "Create Table";
        final int width = 800;
        final int height = 600;
        validatorFacade = ValidatorFacade.getInstance();
        setTitle(title);
        setBounds(new Rectangle(width,height));
        setLocationRelativeTo(null);
        createWidgets();
        setVisible(true);
    }
    public DatabaseFacade getDatabaseFacade(){
        return databaseFacade;
    }
    public Vector<String> getColumnNames(){
        return columnNames;
    }
    public Vector<String> getColumnTypes(){
        return columnTypes;
    }
    public Vector<String> getConstraintsVector(){
        return constraintsVector;
    }
    public String getTableName(){
        return tableNameField.getText();
    }
    public void addForeignKey(String foreignKey){
        foreignKeys.add(foreignKey);
    }

    @Override
    public void createWidgets() {
        Color backgroundColor = new Color(67,67,67);

        JPanel mainPanel = createGridPanel(1,2,0,0,0);
        JPanel sidePanel = createGridPanel(8,1,20,20,20);

        JPanel tablePanel = createGridPanel(1,1,20,20,20);

        JPanel subPanel = createGridPanel(1,2,20,0,0);

        String textFieldText = "Table Name";

        tableScrollPane = new JScrollPane(displayedTable);

        numberOfColumnLabel = createLabel("Number Of Columns: "+columnNames.size());
        numberOfColumnLabel.setHorizontalAlignment(SwingConstants.CENTER);

        tableNameField = createTextField(textFieldText);
        tableNameField.setHorizontalAlignment(SwingConstants.CENTER);
        tableNameField.setCaretPosition(textFieldText.length());

        JButton newColumnButton = createButton("New Column",event->new AddNewColumnWindow(this),true);

        JButton deleteColumnButton = createButton("Delete Column", event->{
            try {
                validatorFacade.checkNumberOfColumns(columnNames.size());
                new DeleteColumnWindow(this);
            } catch (BadColumnNumberException ignored) {
                new WarningWindow("No columns to delete", null, null);
            }
        }, true);

        JButton addRowButton = createButton("Add Row", event->{
            numberOfRows++;
            tableData = new Object[numberOfRows][columnNames.size()];
            displayTable(null);
        }, true);

        JButton createTableButton = createButton("Create Table",event->{
            String tableName = getTableName();
            String primaryKey = String.valueOf(primaryKeyComboBox.getSelectedItem());
//            int primaryKeyIndex = primaryKeyComboBox.getSelectedIndex();
            List<Object>emptyRow = new ArrayList<>();
            List<List<Object>> emptyData = new ArrayList<>();
            emptyData.add(emptyRow);
            try{
                validatorFacade.checkTableName(tableName);
                validatorFacade.checkNumberOfColumns(columnNames.size());
                if(primaryKeyComboBox.getSelectedItem().equals("None"))
                    throw new NoPrimaryKeyException("No primary key chosen");
                databaseFacade.createTable(new Table(tableName, emptyData, columnNames,columnTypes,constraintsVector, foreignKeys),primaryKey, true);
                mainWindow.addTableToJlist(tableName);
                dispose();
            } catch (BadTableNameException | BadColumnNumberException | SQLException exception) {
                new WarningWindow(exception.getMessage(), null, null);
            }
            catch (NoPrimaryKeyException exception){
                new WarningWindow(exception.getMessage(), subEvent->{
                    try {
                        databaseFacade.createTable(new Table(tableName, emptyData, columnNames,columnTypes,constraintsVector, foreignKeys) ,primaryKey, true);
                        mainWindow.addTableToJlist(tableName);
                        dispose();
                    } catch (SQLException subException) {
                        new WarningWindow(subException.getMessage(), null, null);
                    }
                }, null);
            }
        },true);

        JButton cancelButton = createButton("Cancel",event->dispose(),true);

        JLabel primaryKeyLabel = createLabel("Primary Key:");
        primaryKeyLabel.setHorizontalAlignment(JLabel.CENTER);

        primaryKeyComboBox = new JComboBox<>();
        primaryKeyComboBox.addItem("None");

        subPanel.add(primaryKeyLabel);
        subPanel.add(primaryKeyComboBox);
        subPanel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        sidePanel.add(tableNameField);
        sidePanel.add(numberOfColumnLabel);
        sidePanel.add(newColumnButton);
        sidePanel.add(deleteColumnButton);
        sidePanel.add(subPanel);
        sidePanel.add(addRowButton);
        sidePanel.add(createTableButton);
        sidePanel.add(cancelButton);

        tableScrollPane.setBorder(null);
        tableScrollPane.getViewport().setBackground(backgroundColor);
        tablePanel.add(tableScrollPane);

        mainPanel.add(tablePanel);
        mainPanel.add(sidePanel);
        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {
        numberOfColumnLabel.setText("Number Of Columns: "+columnNames.size());
        displayedTable = new JTable(new DefaultTableModel(tableData, columnNames.toArray()));
        tableScrollPane.setViewportView(displayedTable);
        tableScrollPane.setBorder(null);
    }

    public void addColumnToComboBox(String columnName){
        primaryKeyComboBox.addItem(columnName);
    }
}
