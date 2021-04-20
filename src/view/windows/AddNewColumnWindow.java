package view.windows;

import logic.facades.ValidatorFacade;
import logic.repositories.DataTypesRepository;
import logic.templates.ValidatorFacadeApi;
import view.components.MdcFrame;
import exceptions.BadColumnTypeException;
import exceptions.BadColumnNameException;
import exceptions.BadTypeSizeException;
import exceptions.RepeatedColumnNameException;
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
import java.awt.Component;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddNewColumnWindow extends MdcFrame {

    private CreateTableWindow createTableWindow;
    private Vector<String> columnNames;
    private Vector<String> columnTypes;
    private Vector<String> constraintsVector;
    private ValidatorFacadeApi validatorFacade;
    private String foreignKey = null;
    private JComboBox<String> typeComboBox;
    private JButton foreignKeyButton;
    private final int defaultSize = 255;
    private JTextField sizeField;

    public AddNewColumnWindow(CreateTableWindow createTableWindow){

        final String title = "New Column";
        this.createTableWindow = createTableWindow;
        columnNames = createTableWindow.getColumnNames();
        columnTypes = createTableWindow.getColumnTypes();
        constraintsVector = createTableWindow.getConstraintsVector();
        validatorFacade = ValidatorFacade.getInstance();
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

        JPanel mainPanel = createGridPanel(8,1,0,20,20);

        JPanel buttonsPanel = createGridPanel(1,2,20,0,0);

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setBackground(new Color(67,67,67));

        JTextField columnNameField = createTextField("Column Name");

        sizeField = createTextField("Size");

        typeComboBox = new JComboBox<>();
        typeComboBox.setRenderer(new MyComboBoxRenderer("Type"));

        Vector<JCheckBox> constraintsCheckBoxes = new Vector<>();

        for(String constraint: DataTypesRepository.getConstraints())
            constraintsCheckBoxes.add(new JCheckBox(constraint));

        foreignKeyButton = createButton("Add Foreign Key", event-> {
            try {
                new AddForeignKeyReferenceWindow(this, createTableWindow.getDatabaseFacade());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, true);

        JTextField defaultValueField = createTextField("Default value");
        defaultValueField.setEnabled(true);

        JTextField checkTextBox = createTextField("Check");
        checkTextBox.setEnabled(true);

        CheckBoxesComboBox constraintsComboBox = new CheckBoxesComboBox(constraintsCheckBoxes, defaultValueField, checkTextBox);

        List<String> dataTypesList = new ArrayList<>();

        dataTypesList.addAll(Arrays.asList(DataTypesRepository.getNumericTypes()));
        dataTypesList.addAll(Arrays.asList(DataTypesRepository.getStringTypes()));
        dataTypesList.addAll(Arrays.asList(DataTypesRepository.getDateAndTimeTypes()));

        Collections.sort(dataTypesList);

        for(String type: dataTypesList)
            typeComboBox.addItem(type);

        typeComboBox.setSelectedIndex(-1);

        JButton addColumnButton = createButton("Add Column", event->{

            String columnName = columnNameField.getText();
            String columnType = String.valueOf(typeComboBox.getSelectedItem());
            String size;
            StringBuilder constraints = new StringBuilder();
            try {
                validatorFacade.checkColumnName(columnName);
                validatorFacade.checkType(columnType);
                validatorFacade.checkColumnNameUniqueness(columnName, columnNames);
                size = validatorFacade.checkSize(sizeField.getText());

                for(int i = 0 ; i < DataTypesRepository.getConstraints().length ; i++)
                    if(constraintsCheckBoxes.get(i).isSelected()) {
                        constraints.append(constraintsCheckBoxes.get(i).getText()).append(" ");
                    }

                if(!defaultValueField.getText().equals("") && !defaultValueField.getText().equals("Default value"))
                    constraints.append("DEFAULT '").append(defaultValueField.getText()).append("'");

                if(!checkTextBox.getText().equals("") && !checkTextBox.getText().equals("Check"))
                    constraints.append(", CHECK(").append(checkTextBox.getText()).append(")");

                Logger.getGlobal().log(Level.INFO, constraints.toString());
                columnNames.add(columnName);

                if(!DataTypesRepository.isNumeric(columnType) && !columnType.toLowerCase().contains("text") && (sizeField.getText().equals("") || sizeField.getText().equals("Size")))
                    columnType = columnType+"("+defaultSize+")";
                else
                    columnType = columnType+size;

                columnTypes.add(columnType);
                constraintsVector.add(String.valueOf(constraints));
                if(foreignKey != null)
                    createTableWindow.addForeignKey("FOREIGN KEY("+columnName+") REFERENCES "+foreignKey);
                createTableWindow.addColumnToComboBox(columnName);
                createTableWindow.displayTable(null);
                dispose();
            }catch (BadColumnNameException | BadTypeSizeException | BadColumnTypeException | RepeatedColumnNameException exception){
                new WarningWindow(exception.getMessage(), null, null);
            }
        },true);

        JButton cancelButton = createButton("Cancel",event->dispose(),true);

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
    public void displayTable(List<List<Object>> data) {

    }

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
                if(checkBox!=null)
                    checkBox.setSelected(!checkBox.isSelected());
            });
        }
    }
}

