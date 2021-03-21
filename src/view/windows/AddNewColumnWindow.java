package view.windows;

import logic.repositories.DataTypesRepository;
import view.components.MdcFrame;
import logic.controllers.ValidateController;
import exceptions.BadColumnTypeException;
import exceptions.BadColumnNameException;
import exceptions.BadTypeSizeException;
import exceptions.RepeteadColumnNameException;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.util.Vector;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* NewColumnWindow
*
* @extends MyDialog
*
* This window allows to create and add a new Column in created table
* */
public class AddNewColumnWindow extends MdcFrame {

    private CreateTableWindow createTableWindow;
    private Vector<String> columnNames;
    private Vector<String> columnTypes;
    private Vector<String> constraintsVector;
    private ValidateController controller = new ValidateController();
    private String foreignKey = null;
    private JComboBox<String> typeComboBox;
    private JButton foreignKeyButton;
    private final int defaultSize = 255;
    private JTextField sizeField;
    private final String[] numericTypes = {
            "bit", "tinyint", "smallint","mediumint", "bigint",
            "int", "boolean", "bool", "integer", "float" ,"double", "decimal", "dec"
    };
    private final String[] stringTypes ={
            "char", "varchar", "binary", "tinyblob", "tinytext", "text",
            "blob", "mediumtext", "mediumblob", "longtext", "longblob", "enum", "set"
    };
    private final String[] dateAndTimeTypes = {
            "date", "datetime", "timestamp", "time", "year"
    };
    private final String[] constraints = {
            "Not Null", "Unique"
    };
    public AddNewColumnWindow(CreateTableWindow createTableWindow){

        final String title = "New Column";
        this.createTableWindow = createTableWindow;
        columnNames = createTableWindow.getColumnNames();
        columnTypes = createTableWindow.getColumnTypes();
        constraintsVector = createTableWindow.getConstraintsVector();

        setTitle(title);
        setLocationRelativeTo(null);
        createWidgets();
        pack();
        setVisible(true);
    }
    public void setForeignKey(String foreignKey){
        this.foreignKey = foreignKey;
    }
    @Override
    public void createWidgets() {

//        -------------------------------------------mainPanel----------------------------------------------------------

        JPanel mainPanel = createGridPanel(8,1,0,20,20);

//        -------------------------------------------buttonsPanel-------------------------------------------------------

        JPanel buttonsPanel = createGridPanel(1,2,20,0,0);

//        -------------------------------------------sidePanel----------------------------------------------------------

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(new Color(67,67,67));

//        -------------------------------------------columnNameField----------------------------------------------------

        JTextField columnNameField = createTextField("Column Name");

//        -------------------------------------------sizeField----------------------------------------------------------

        sizeField = createTextField("Size");

//        -------------------------------------------typeComboBox-------------------------------------------------------

        typeComboBox = new JComboBox<>();
        typeComboBox.setRenderer(new MyComboBoxRenderer("Type"));

//        -------------------------------------------constraintsCheckBoxes----------------------------------------------

        Vector<JCheckBox> constraintsCheckBoxes = new Vector<>();

        for(String constraint: constraints)
            constraintsCheckBoxes.add(new JCheckBox(constraint));

//        -------------------------------------------foreignKeyButton---------------------------------------------------

        foreignKeyButton = createButton("Add Foreign Key", event->new AddForeignKeyReferenceWindow(this, createTableWindow.getDatabaseFacade()), true);

//        -------------------------------------------defaultValueField--------------------------------------------------

        JTextField defaultValueField = createTextField("Default value");
        defaultValueField.setEnabled(true);

//        -------------------------------------------checkTextBox-------------------------------------------------------

        JTextField checkTextBox = createTextField("Check");
        checkTextBox.setEnabled(true);

//        -------------------------------------------constraintsComboBox------------------------------------------------

        CheckBoxesComboBox constraintsComboBox = new CheckBoxesComboBox(constraintsCheckBoxes, defaultValueField, checkTextBox);

        List<String> dataTypesList = new ArrayList<>();

        dataTypesList.addAll(Arrays.asList(numericTypes));
        dataTypesList.addAll(Arrays.asList(stringTypes));
        dataTypesList.addAll(Arrays.asList(dateAndTimeTypes));

        Collections.sort(dataTypesList);

        for(String type: dataTypesList)
            typeComboBox.addItem(type);

        typeComboBox.setSelectedIndex(-1);

//        -------------------------------------------addColumnButton----------------------------------------------------

        JButton addColumnButton = createButton("Add Column", event->{

            String columnName = columnNameField.getText();
            String columnType = String.valueOf(typeComboBox.getSelectedItem());
            String size;
            StringBuilder _constraints = new StringBuilder();
            try {
                controller.checkColumnName(columnName);
                controller.checkType(columnType);
                controller.checkColumnNameUniqueness(columnName, columnNames);
                size = controller.checkSize(sizeField.getText());

                for(int i = 0 ; i < constraints.length ; i++)
                    if(constraintsCheckBoxes.get(i).isSelected()) {
                        _constraints.append(constraintsCheckBoxes.get(i).getText()).append(" ");
                    }

                if(!defaultValueField.getText().equals("") && !defaultValueField.getText().equals("Default value"))
                    _constraints.append("DEFAULT '").append(defaultValueField.getText()).append("'");

                if(!checkTextBox.getText().equals("") && !checkTextBox.getText().equals("Check"))
                    _constraints.append(", CHECK(").append(checkTextBox.getText()).append(")");

                Logger.getGlobal().log(Level.INFO, _constraints.toString());
                columnNames.add(columnName);

                Logger.getGlobal().log(Level.INFO, String.valueOf(!DataTypesRepository.isNumeric(columnType)));
                Logger.getGlobal().log(Level.INFO, String.valueOf(!columnType.toLowerCase().equals("text")));
                Logger.getGlobal().log(Level.INFO, String.valueOf(sizeField.getText().equals("")));
                Logger.getGlobal().log(Level.INFO, String.valueOf(sizeField.getText().equals("Size")));
                if(!DataTypesRepository.isNumeric(columnType) && !columnType.toLowerCase().contains("text") && (sizeField.getText().equals("") || sizeField.getText().equals("Size")))
                    columnType = columnType+"("+defaultSize+")";
                else
                    columnType = columnType+size;
                Logger.getGlobal().log(Level.INFO, columnType);
                columnTypes.add(columnType);
                constraintsVector.add(String.valueOf(_constraints));
                if(foreignKey != null)
                    createTableWindow.addForeignKey("FOREIGN KEY("+columnName+") REFERENCES "+foreignKey);
                createTableWindow.addColumnToComboBox(columnName);
                createTableWindow.displayTable(null);
                dispose();
            }catch (BadColumnNameException | BadTypeSizeException | BadColumnTypeException | RepeteadColumnNameException exception){
                new WarningWindow(exception.getMessage(), null, null);
            }
        },true);

//        -------------------------------------------cancelButton-------------------------------------------------------

        JButton cancelButton = createButton("Cancel",event->dispose(),true);

//        -------------------------------------------constraintsLabel---------------------------------------------------

        JLabel constraintsLabel = createLabel("Constraints:");
        constraintsLabel.setPreferredSize(new Dimension(80,20));

        buttonsPanel.add(addColumnButton);
        buttonsPanel.add(cancelButton);

        sidePanel.add(constraintsLabel, BorderLayout.WEST);
        sidePanel.add(constraintsComboBox, BorderLayout.EAST);

        mainPanel.add(columnNameField);
        mainPanel.add(typeComboBox);
        mainPanel.add(sizeField);
        mainPanel.add(sidePanel);
        mainPanel.add(foreignKeyButton);
        mainPanel.add(defaultValueField);
        mainPanel.add(checkTextBox);
        mainPanel.add(buttonsPanel);

        add(mainPanel);
    }
    public void setForeignKeyComponents(){
        foreignKeyButton.setText("Change Foreign Key");
    }
    public void setTypeAndSize(String type, String size){
        typeComboBox.removeAllItems();
        typeComboBox.addItem(type);
        sizeField.setText(size);
        sizeField.setEnabled(false);
    }
    @Override
    public void displayTable(List<List<Object>> data) {}
    private static class MyComboBoxRenderer extends JLabel implements ListCellRenderer<String>
    {
        private String title;

        public MyComboBoxRenderer(String title) {
            this.title = title;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
            if (index == -1 && value == null)
                setText(title);
            else
                setText(value);
            return this;
        }
    }
    public static class CheckBoxesComboBox extends JComboBox<JCheckBox>{

        private final int checkIndex = 0;

        public CheckBoxesComboBox(Vector<JCheckBox> checkBoxes, JTextField defaultTextField, JTextField checkTextBox) {
            super(checkBoxes);
            setRenderer((ListCellRenderer<Component>) (list, value, index, isSelected, cellHasFocus) -> {
                if (isSelected) {
                    value.setBackground(list.getSelectionBackground());
                    value.setForeground(list.getSelectionForeground());
                } else {
                    value.setBackground(list.getBackground());
                    value.setForeground(list.getForeground());
                }
                return value;
            }
            );
            addActionListener(e -> {
                showPopup();
                setPopupVisible(true);
                JCheckBox checkBox = (JCheckBox)getSelectedItem();
                if(checkBox!=null) {
                    checkBox.setSelected(!checkBox.isSelected());
//                        Logger.getGlobal().log(Level.INFO, String.valueOf(checkIndex));
//                        if(getItemAt(checkIndex).isSelected())
//                            checkTextBox.setEnabled(true);
//                        else
//                            checkTextBox.setEnabled(false);
                }
            });
        }
    }
}

