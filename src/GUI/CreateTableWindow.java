package GUI;

import Logic.Controller;
import Logic.Model;
import Logic.MyExceptions.BadColumnNumberException;
import Logic.MyExceptions.BadNamesTypesQuantityException;
import Logic.MyExceptions.BadTableNameException;
import Logic.MyExceptions.NoPrimaryKeyException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class CreateTableWindow extends MyDialog{
    private Model model;
    private Controller controller = new Controller();
    private Object[][] tableData;
    private JTable displayedTable;
    private JScrollPane scrollPane;
    private JLabel label;
    private JTextField tableNameField;
    private int numberOfRows = 0;
    private JComboBox<String> primaryKeyComboBox;
    private Vector<String> columnNames = new Vector<>();
    private Vector<String> columnTypes = new Vector<>();
    private Vector<String> constraintsVector = new Vector<>();
    public CreateTableWindow(Model model){

        this.model = model;
        setTitle("Create Table");
        setBounds(new Rectangle(800,600));
        setLocationRelativeTo(null);
        initWindow();
        setVisible(true);
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
    @Override
    public void initWindow() {
        Color backgroundColor = new Color(67,67,67);
        JPanel mainPanel = createGridPanel(1,2,0,0,0);
        JPanel sidePanel = createGridPanel(7,1,20,20,20);
        JPanel tablePanel = createGridPanel(1,1,20,20,20);
        JPanel subPanel = createGridPanel(1,2,20,0,0);

        String textFieldText = "Table Name";
        scrollPane = new JScrollPane(displayedTable);

        label = createLabel("Number Of Columns: "+columnNames.size());
        label.setHorizontalAlignment(SwingConstants.CENTER);

        tableNameField = createTextField(textFieldText);
        tableNameField.setHorizontalAlignment(SwingConstants.CENTER);
        tableNameField.setCaretPosition(textFieldText.length());

        JButton newColumnButton = createButton("New Column",event->new NewColumnWindow(this),true);

        JLabel primaryKeyLabel = createLabel("Primary Key:");
        primaryKeyLabel.setHorizontalAlignment(JLabel.CENTER);

        primaryKeyComboBox = new JComboBox<>();
        primaryKeyComboBox.addItem("None");

        JButton addRowButton = createButton("Add Row", event->{
            numberOfRows++;
            tableData = new Object[numberOfRows][columnNames.size()];
            displayTable(null);
        }, true);
        JButton createTableButton = createButton("Create Table",event->{
            String tableName = tableNameField.getText();
            String primaryKey = String.valueOf(primaryKeyComboBox.getSelectedItem());
            try{
                controller.checkTableName(tableName);
                controller.checkNumberOfColumns(columnNames.size());
                controller.checkNamesTypesQuantity(this);
                if(primaryKeyComboBox.getSelectedItem().equals("None"))
                    throw new NoPrimaryKeyException();
                model.createTable(tableName, this ,primaryKey, false);
            } catch (BadTableNameException | BadColumnNumberException | BadNamesTypesQuantityException exception) {
                new WarningWindow(exception.getMessage(), null, null);
            }
            catch (NoPrimaryKeyException exception){
                new WarningWindow(exception.getMessage(), subEvent->{
                    model.createTable(tableName, this ,primaryKey, false);
                }, null);
            }
        },true);
        JButton cancelButton = createButton("Cancel",event->dispose(),true);

        subPanel.add(primaryKeyLabel);
        subPanel.add(primaryKeyComboBox);
        subPanel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        sidePanel.add(tableNameField);
        sidePanel.add(label);
        sidePanel.add(newColumnButton);
        sidePanel.add(subPanel);
        sidePanel.add(addRowButton);
        sidePanel.add(createTableButton);
        sidePanel.add(cancelButton);

        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(backgroundColor);
        tablePanel.add(scrollPane);

        mainPanel.add(tablePanel);
        mainPanel.add(sidePanel);
        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {
        if(!columnNames.isEmpty()){
            label.setText("Number Of Columns: "+columnNames.size());
            displayedTable = new JTable(new DefaultTableModel(tableData, columnNames.toArray()));
            scrollPane.setViewportView(displayedTable);
            scrollPane.setBorder(null);
        }
    }
    public void addColumnToComboBox(String columnName){
        primaryKeyComboBox.addItem(columnName);
    }
}
