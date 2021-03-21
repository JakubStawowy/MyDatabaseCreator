package view.windows;

import logic.DatabaseFacade;
import view.components.MdcFrame;
import logic.controllers.ValidateController;
import exceptions.BadColumnNumberException;
import exceptions.BadNamesTypesQuantityException;
import exceptions.BadTableNameException;
import exceptions.NoPrimaryKeyException;
import logic.models.Table;

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

/*
* CreateTableWindow
*
* @extends MyDialog
*
* This window allows to create new table.
*
* */
public class CreateTableWindow extends MdcFrame {

    private DatabaseFacade databaseFacade;
    private ValidateController controller = new ValidateController();
    private Object[][] tableData;
    private JTable displayedTable;
    private JScrollPane tableScrollPane;
    private JLabel numberOfColumnslabel;
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
    public Vector<String> getForeignKeys(){
        return foreignKeys;
    }
    @Override
    public void createWidgets() {
        Color backgroundColor = new Color(67,67,67);

//        ----------------------------------------mainPanel-------------------------------------------------------------

        JPanel mainPanel = createGridPanel(1,2,0,0,0);

//        ----------------------------------------sidePanel-------------------------------------------------------------

        JPanel sidePanel = createGridPanel(8,1,20,20,20);

//        ----------------------------------------tablePanel------------------------------------------------------------

        JPanel tablePanel = createGridPanel(1,1,20,20,20);

//        ----------------------------------------subPanel--------------------------------------------------------------

        JPanel subPanel = createGridPanel(1,2,20,0,0);

        String textFieldText = "Table Name";

//        ----------------------------------------tableScrollPane-------------------------------------------------------

        tableScrollPane = new JScrollPane(displayedTable);

//        ----------------------------------------numberOfColumnslabel--------------------------------------------------

        numberOfColumnslabel = createLabel("Number Of Columns: "+columnNames.size());
        numberOfColumnslabel.setHorizontalAlignment(SwingConstants.CENTER);

//        ----------------------------------------tableNameField--------------------------------------------------------

        tableNameField = createTextField(textFieldText);
        tableNameField.setHorizontalAlignment(SwingConstants.CENTER);
        tableNameField.setCaretPosition(textFieldText.length());

//        ----------------------------------------newColumnButton-------------------------------------------------------

        JButton newColumnButton = createButton("New Column",event->new AddNewColumnWindow(this),true);

//        ----------------------------------------deleteColumnButton----------------------------------------------------

        JButton deleteColumnButton = createButton("Delete Column", event->{
            try {
                controller.checkNumberOfColumns(columnNames.size());
                new DeleteColumnWindow(this);
            } catch (BadColumnNumberException ignored) {
                new WarningWindow("No columns to delete", null, null);
            }
        }, true);

//        ----------------------------------------addRowButton----------------------------------------------------------

        JButton addRowButton = createButton("Add Row", event->{
            numberOfRows++;
            tableData = new Object[numberOfRows][columnNames.size()];
            displayTable(null);
        }, true);

//        ----------------------------------------createTableButton-----------------------------------------------------

        JButton createTableButton = createButton("Create Table",event->{
            String tableName = getTableName();
            String primaryKey = String.valueOf(primaryKeyComboBox.getSelectedItem());
            int primaryKeyIndex = primaryKeyComboBox.getSelectedIndex();
            List<Object>emptyRow = new ArrayList<>();
            List<List<Object>> emptyData = new ArrayList<>();
            emptyData.add(emptyRow);
            try{
                controller.checkTableName(tableName);
                controller.checkNumberOfColumns(columnNames.size());
                controller.checkNamesTypesQuantity(this);
                if(primaryKeyComboBox.getSelectedItem().equals("None"))
                    throw new NoPrimaryKeyException("No primary key chosen");
                databaseFacade.createTable(new Table(tableName, primaryKeyIndex, emptyData, columnNames,columnTypes,constraintsVector, foreignKeys),primaryKey, true);
                mainWindow.addTableToJlist(tableName);
                dispose();
            } catch (BadTableNameException | BadColumnNumberException | BadNamesTypesQuantityException | SQLException exception) {
                new WarningWindow(exception.getMessage(), null, null);
            }
            catch (NoPrimaryKeyException exception){
                new WarningWindow(exception.getMessage(), subEvent->{
                    try {
                        databaseFacade.createTable(new Table(tableName, primaryKeyIndex, emptyData, columnNames,columnTypes,constraintsVector, foreignKeys) ,primaryKey, true);
                        mainWindow.addTableToJlist(tableName);
                        dispose();
                    } catch (SQLException subException) {
                        new WarningWindow(subException.getMessage(), null, null);
                    }
                }, null);
            }
        },true);

//        ----------------------------------------cancelButton----------------------------------------------------------

        JButton cancelButton = createButton("Cancel",event->dispose(),true);

//        ----------------------------------------primaryKeyLabel-------------------------------------------------------

        JLabel primaryKeyLabel = createLabel("Primary Key:");
        primaryKeyLabel.setHorizontalAlignment(JLabel.CENTER);

//        ----------------------------------------primaryKeyComboBox-------------------------------------------------------

        primaryKeyComboBox = new JComboBox<>();
        primaryKeyComboBox.addItem("None");


        subPanel.add(primaryKeyLabel);
        subPanel.add(primaryKeyComboBox);
        subPanel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        sidePanel.add(tableNameField);
        sidePanel.add(numberOfColumnslabel);
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
        numberOfColumnslabel.setText("Number Of Columns: "+columnNames.size());
        displayedTable = new JTable(new DefaultTableModel(tableData, columnNames.toArray()));
        tableScrollPane.setViewportView(displayedTable);
        tableScrollPane.setBorder(null);
    }
    public void addColumnToComboBox(String columnName){
        primaryKeyComboBox.addItem(columnName);
    }
}
