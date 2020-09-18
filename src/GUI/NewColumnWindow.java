package GUI;

import Logic.Controller;
import Logic.MyExceptions.BadColumnNameException;
import Logic.MyExceptions.BadColumnTypeException;
import Logic.MyExceptions.BadTypeLengthException;
import Logic.MyExceptions.RepeteadColumnNameException;

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
            "Not Null", "Unique", "Default", "Check",
            "Primary Key", "Foreign Key"
    };

    public NewColumnWindow(CreateTableWindow createTableWindow){

        this.createTableWindow = createTableWindow;
        columnNames = createTableWindow.getColumnNames();
        columnTypes = createTableWindow.getColumnTypes();

        setTitle("New Column");
        setBounds(new Rectangle(300,400));
        setLocationRelativeTo(null);

        initWindow();
        setVisible(true);
    }
    @Override
    public void initWindow() {
//        String textFieldText = "Column Name";

        JPanel mainPanel = createGridPanel(6,1,0,20,20);
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

        JButton foreignKeyButton = createButton("Add Foreign Key", null, false);

        CheckBoxesComboBox constraintsComboBox = new CheckBoxesComboBox(constraintsCheckBoxes, foreignKeyButton);

        for(String numericType: numericTypes)
            typeComboBox.addItem(numericType);

        for(String stringType: stringTypes)
            typeComboBox.addItem(stringType);

        typeComboBox.setSelectedIndex(-1);

        JButton addColumnButton = createButton("Add Column",event->{

            String columnName = columnNameField.getText();
            String columnType = String.valueOf(typeComboBox.getSelectedItem());
            String length;
            try {
                controller.checkColumnName(columnName);
                controller.checkType(columnType);
                controller.checkColumnNameUniqueness(columnName, columnNames);
                length = controller.checkLength(lengthField.getText());

                columnNames.add(columnName);
                columnType = columnType+length;
                columnTypes.add(columnType);
                createTableWindow.addColumnToComboBox(columnName);
                createTableWindow.displayTable(null);
                dispose();
            }catch (BadColumnNameException | BadTypeLengthException | BadColumnTypeException | RepeteadColumnNameException exception){
                new WarningWindow(exception.getMessage(), null, null);
            }
            for(int i = 0 ; i < columnNames.size() ; i++){
                System.out.println(columnNames.get(i)+" - "+columnTypes.get(i));
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
        public CheckBoxesComboBox(Vector<JCheckBox> checkBoxes, JButton foreignKeyButton) {
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

                        if (getItemAt(5).isSelected()) {
                            foreignKeyButton.setEnabled(true);

                            getItemAt(4).setSelected(false);
                            }
                        }
                        else if(getItemAt(4).isSelected()){
                            foreignKeyButton.setEnabled(false);
                            getItemAt(5).setSelected(false);
                        }
                        else
                            foreignKeyButton.setEnabled(false);

                    }
                }
            );
        }
    }
}

