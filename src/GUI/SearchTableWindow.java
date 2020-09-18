package GUI;

import Logic.Table;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.EventListener;
import java.util.List;

/*
* SearchTableWindow class
* */
public class SearchTableWindow extends MyDialog{

    private EditTableWindow displayTableWindow;
    private JCheckBox ascSortCheckBox;
    private JCheckBox descSortCheckBox;
    private Color backgroundColor = new Color(67,67,67);
    private Color componentColor = new Color(105, 105, 105);
    public SearchTableWindow(EditTableWindow displayTableWindow){

        this.displayTableWindow = displayTableWindow;

//        setPreferredSize(new Dimension(500,100));
        setTitle("Search table "+displayTableWindow.getTable().getTableName());
        initWindow();
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
    @Override
    public void initWindow() {

        JPanel mainPanel = createGridPanel(2,3,20,20,20);
        JButton searchButton;

        //------------------------------------Textfield---------------------------------------------------
        JTextField textField = createTextField("Condition");
        setTextField(textField, "Condition");

        //------------------------------------ColumnComboBox---------------------------------------------------
        JComboBox<String> columnComboBox = new JComboBox<>();

        for(String column: displayTableWindow.getTable().getColumnNames())
            columnComboBox.addItem(column);

        columnComboBox.setEnabled(false);



        //------------------------------------RandomConditionButton---------------------------------------------------
        JButton randomButton = createButton("Random condition",event->{
            Table table = displayTableWindow.getTable();
            textField.setText(displayTableWindow.getModel().generateRandomCondition(table));
        },true);

        //------------------------------------SearchButton---------------------------------------------------
        searchButton = createButton("Search",event->search(textField.getText(), (String) columnComboBox.getSelectedItem()),true);
        searchButton.setEnabled(false);

        //------------------------------------CloseButton---------------------------------------------------
        JButton closeButton = createButton("Close", event->dispose(), true);
        add(mainPanel);

        //------------------------------------TextFieldDocumentListener---------------------------------------------------
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                    searchButton.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(textField.getText().equals("") && !descSortCheckBox.isSelected() && !ascSortCheckBox.isSelected())
                    searchButton.setEnabled(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        //------------------------------------CheckBoxes---------------------------------------------------
        ascSortCheckBox = new JCheckBox("Sort ascending");
        ascSortCheckBox.setBackground(backgroundColor);
        ascSortCheckBox.setForeground(Color.WHITE);

        descSortCheckBox = new JCheckBox("Sort descending");
        descSortCheckBox.setBackground(backgroundColor);
        descSortCheckBox.setForeground(Color.WHITE);

        ascSortCheckBox.addActionListener(event-> {

            descSortCheckBox.setSelected(false);
            searchButton.setEnabled(true);

            if(ascSortCheckBox.isSelected())
                columnComboBox.setEnabled(true);

            else if(!ascSortCheckBox.isSelected() && !descSortCheckBox.isSelected()) {
                columnComboBox.setEnabled(false);

                if(textField.getText().equals(""))
                    searchButton.setEnabled(false);
            }
        });

        descSortCheckBox.addActionListener(event->{

            ascSortCheckBox.setSelected(false);
            searchButton.setEnabled(true);

            if(descSortCheckBox.isSelected())
                columnComboBox.setEnabled(true);

            else if(!ascSortCheckBox.isSelected() && !descSortCheckBox.isSelected()) {
                columnComboBox.setEnabled(false);

                if(textField.getText().equals(""))
                    searchButton.setEnabled(false);
            }
        });

        //------------------------------------SortByLabel---------------------------------------------------
        JLabel columnLabel = createLabel("Sort by:");
        columnLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(textField);
        mainPanel.add(randomButton);
        mainPanel.add(searchButton);
        mainPanel.add(closeButton);
        mainPanel.add(columnLabel);
        mainPanel.add(columnComboBox);
        mainPanel.add(ascSortCheckBox);
        mainPanel.add(descSortCheckBox);



        }

    @Override
    public void displayTable(List<List<Object>> data) {

    }

    public void search(String condition, String columnName){
        if(ascSortCheckBox.isSelected())
            displayTableWindow.displayTable(displayTableWindow.getModel().searchTable(displayTableWindow.getTable().getTableName(),condition,"ASC",columnName));
        else if(descSortCheckBox.isSelected())
            displayTableWindow.displayTable(displayTableWindow.getModel().searchTable(displayTableWindow.getTable().getTableName(),condition,"DESC", columnName));
        else
            displayTableWindow.displayTable(displayTableWindow.getModel().searchTable(displayTableWindow.getTable().getTableName(),condition,null,null));
    }
}
