package GUI;

import Logic.Controller;
import Logic.MyExceptions.BadColumnTypeException;
import Logic.MyExceptions.BadColumnNameException;
import Logic.MyExceptions.BadTypeSizeException;
import Logic.MyExceptions.RepeteadColumnNameException;
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

/*
* NewColumnWindow
*
* @extends MyDialog
*
* This window allows to create and add a new Column in created table
* */
public class NewColumnWindow extends MyDialog {

    private CreateTableWindow createTableWindow;
    private Vector<String> columnNames;
    private Vector<String> columnTypes;
    private Vector<String> constraintsVector;
    private Controller controller = new Controller();
    private String foreignKey = null;
    private JComboBox<String> typeComboBox;
    private JButton foreignKeyButton;
    JTextField sizeField;
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
            "Not Null", "Unique", "Default", "Check"
    };
    public NewColumnWindow(CreateTableWindow createTableWindow){

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

        foreignKeyButton = createButton("Add Foreign Key", event->new AddForeignKeyReferenceWindow(this, createTableWindow.getModel()), true);

//        -------------------------------------------defaultValueField--------------------------------------------------

        JTextField defaultValueField = createTextField("Default value");
        defaultValueField.setEnabled(false);

//        -------------------------------------------checkTextBox-------------------------------------------------------

        JTextField checkTextBox = createTextField("Check");
        checkTextBox.setEnabled(false);

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

        JButton addColumnButton = createButton("Add Column",event->{

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
                columnNames.add(columnName);
                columnType = columnType+size;
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

        private final int defaultIndex = 2;
        private final int checkIndex = 3;

        public CheckBoxesComboBox(Vector<JCheckBox> checkBoxes, JTextField defaultTextField, JTextField checkTextBox) {
            super(checkBoxes);
            setRenderer(new ListCellRenderer<Component>() {
                @Override
                public Component getListCellRendererComponent(JList<? extends Component> list, Component value, int index, boolean isSelected, boolean cellHasFocus) {

                    if (isSelected) {
                        value.setBackground(list.getSelectionBackground());
                        value.setForeground(list.getSelectionForeground());
                    } else {
                        value.setBackground(list.getBackground());
                        value.setForeground(list.getForeground());
                    }
                    return value;
                }
            }
            );
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showPopup();
                    setPopupVisible(true);
                    JCheckBox checkBox = (JCheckBox)getSelectedItem();
                    if(checkBox!=null) {
                        checkBox.setSelected(!checkBox.isSelected());

                        if(getItemAt(defaultIndex).isSelected())
                            defaultTextField.setEnabled(true);
                        else
                            defaultTextField.setEnabled(false);

                        if(getItemAt(checkIndex).isSelected())
                            checkTextBox.setEnabled(true);
                        else
                            checkTextBox.setEnabled(false);
                    }
                }
            });
        }
    }
}

