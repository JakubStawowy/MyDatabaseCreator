package GUI;

import Logic.Controller;
import Logic.MyExceptions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

public class NewColumnWindow extends MyDialog {

    private CreateTableWindow createTableWindow;
    private Vector<String> columnNames;
    private Vector<String> columnTypes;
    private Vector<String> constraintsVector;
    private Controller controller = new Controller();
    private final String[] numericTypes = {
            "bit", "tinyint", "smallint","mediumint", "bigint",
            "int", "boolean", "bool", "integer", "float" ,"double", "decimal", "dec"
    };
    private final String[] stringTypes ={
            "char", "varchar", "binary", "tinyblob", "tinytext", "text",
            "blob", "mediumtext", "mediumblob", "longtext", "longblob", "enum", "set"
    };
    private final String[] constraints = {
            "Not Null", "Unique", "Default", "Check"
    };
    public NewColumnWindow(CreateTableWindow createTableWindow){

        this.createTableWindow = createTableWindow;
        columnNames = createTableWindow.getColumnNames();
        columnTypes = createTableWindow.getColumnTypes();
        constraintsVector = createTableWindow.getConstraintsVector();

        setTitle("New Column");
        setLocationRelativeTo(null);
        initWindow();
        pack();
        setVisible(true);
    }
    @Override
    public void initWindow() {

        JPanel mainPanel = createGridPanel(8,1,0,20,20);
        JPanel buttonsPanel = createGridPanel(1,2,20,0,0);
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(new Color(67,67,67));


        JTextField columnNameField = createTextField("Column Name");
        JTextField lengthField = createTextField("Length");

        JComboBox<String> typeComboBox = new JComboBox<>();
        typeComboBox.setRenderer(new MyComboBoxRenderer("Type"));

        Vector<JCheckBox> constraintsCheckBoxes = new Vector<>();

        for(String constraint: constraints)
            constraintsCheckBoxes.add(new JCheckBox(constraint));

        JButton foreignKeyButton = createButton("Add Foreign Key", null, true);
        JTextField defaultTextBox = createTextField("Default value");
        JTextField checkTextBox = createTextField("Check");

        defaultTextBox.setEnabled(false);
        checkTextBox.setEnabled(false);

        CheckBoxesComboBox constraintsComboBox = new CheckBoxesComboBox(constraintsCheckBoxes, defaultTextBox, checkTextBox);

        for(String numericType: numericTypes)
            typeComboBox.addItem(numericType);

        for(String stringType: stringTypes)
            typeComboBox.addItem(stringType);

        typeComboBox.setSelectedIndex(-1);

        JButton addColumnButton = createButton("Add Column",event->{

            String columnName = columnNameField.getText();
            String columnType = String.valueOf(typeComboBox.getSelectedItem());
            String length;
            StringBuilder _constraints = new StringBuilder();
            try {
                controller.checkColumnName(columnName);
                controller.checkType(columnType);
                controller.checkColumnNameUniqueness(columnName, columnNames);
                length = controller.checkLength(lengthField.getText());

                for(int i = 0 ; i < constraints.length ; i++)
                    if(constraintsCheckBoxes.get(i).isSelected()) {
                        _constraints.append(constraintsCheckBoxes.get(i).getText()).append(" ");
                    }

                columnNames.add(columnName);
                columnType = columnType+length;
                columnTypes.add(columnType);
                constraintsVector.add(String.valueOf(_constraints));
                createTableWindow.addColumnToComboBox(columnName);
                createTableWindow.displayTable(null);
                dispose();
            }catch (BadColumnNameException | BadTypeLengthException | BadColumnTypeException | RepeteadColumnNameException exception){
                new WarningWindow(exception.getMessage(), null, null);
            }
        },true);
        JButton cancelButton = createButton("Cancel",event->dispose(),true);

        buttonsPanel.add(addColumnButton);
        buttonsPanel.add(cancelButton);

        JLabel constraintsLabel = createLabel("Constraints:");
        constraintsLabel.setPreferredSize(new Dimension(80,20));
        sidePanel.add(constraintsLabel, BorderLayout.WEST);
        sidePanel.add(constraintsComboBox, BorderLayout.EAST);

        mainPanel.add(columnNameField);
        mainPanel.add(typeComboBox);
        mainPanel.add(lengthField);
        mainPanel.add(sidePanel);
        mainPanel.add(foreignKeyButton);
        mainPanel.add(defaultTextBox);
        mainPanel.add(checkTextBox);
        mainPanel.add(buttonsPanel);

        add(mainPanel);
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

