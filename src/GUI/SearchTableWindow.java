package GUI;

import Logic.MyExceptions.EmptyTableException;
import Logic.Table;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.util.List;

/*
* SearchTableWindow
*
* @extends MyDialog
*
* This window allows to search table using conditions, sort table ascending or descending
* */
public class SearchTableWindow extends MyDialog{

    private EditTableWindow displayTableWindow;
    private JCheckBox ascSortCheckBox;
    private JCheckBox descSortCheckBox;
    private Color backgroundColor = new Color(67,67,67);
    private String tableName;
    public SearchTableWindow(EditTableWindow displayTableWindow, String tableName){

        this.displayTableWindow = displayTableWindow;
        this.tableName = tableName;
        final String title = "Search table "+displayTableWindow.getTable().getTableName();
        setTitle(title);
        createWidgets();
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
    @Override
    public void createWidgets() {

//        --------------------------------------mainPanel---------------------------------------------------------------

        JPanel mainPanel = createGridPanel(2,3,20,20,20);
        JButton searchButton;

//        --------------------------------------conditionField----------------------------------------------------------

        JTextField conditionField = createTextField("Condition");
        setTextField(conditionField, "Condition");

//        --------------------------------------columnComboBox----------------------------------------------------------

        JComboBox<String> columnComboBox = new JComboBox<>();

        for(String column: displayTableWindow.getTable().getColumnNames())
            columnComboBox.addItem(column);

        columnComboBox.setEnabled(false);

//        -------------------------------------randomConditionButton----------------------------------------------------

        JButton randomConditionButton = createButton("Random condition",event->{
            Table table = displayTableWindow.getModel().importTable(tableName);
            try {
                conditionField.setText(displayTableWindow.getModel().generateRandomCondition(table));
            } catch (EmptyTableException emptyTableException) {
                new WarningWindow(emptyTableException.getMessage(), null, null);
            }
        },true);

//        ------------------------------------searchButton--------------------------------------------------------------

        searchButton = createButton("Search",event->search(conditionField.getText(), (String) columnComboBox.getSelectedItem()),true);
        searchButton.setEnabled(false);

//        ------------------------------------closeButton---------------------------------------------------------------

        JButton closeButton = createButton("Close", event->dispose(), true);
        add(mainPanel);

//        ------------------------------------conditionFieldDocumentListener--------------------------------------------

        conditionField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                    searchButton.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(conditionField.getText().equals("") && !descSortCheckBox.isSelected() && !ascSortCheckBox.isSelected())
                    searchButton.setEnabled(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

//        ------------------------------------ascSortCheckBox-----------------------------------------------------------

        ascSortCheckBox = new JCheckBox("Sort ascending");
        ascSortCheckBox.setBackground(backgroundColor);
        ascSortCheckBox.setForeground(Color.WHITE);

//        ------------------------------------descSortCheckBox----------------------------------------------------------

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

                if(conditionField.getText().equals(""))
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

                if(conditionField.getText().equals(""))
                    searchButton.setEnabled(false);
            }
        });

//        ------------------------------------sortByLabel---------------------------------------------------------------

        JLabel sortByLabel = createLabel("Sort by:");
        sortByLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mainPanel.add(conditionField);
        mainPanel.add(randomConditionButton);
        mainPanel.add(searchButton);
        mainPanel.add(closeButton);
        mainPanel.add(sortByLabel);
        mainPanel.add(columnComboBox);
        mainPanel.add(ascSortCheckBox);
        mainPanel.add(descSortCheckBox);



        }

    @Override
    public void displayTable(List<List<Object>> data) {

    }

    private void search(String condition, String columnName){
        if(ascSortCheckBox.isSelected())
            displayTableWindow.displayTable(displayTableWindow.getModel().searchTable(displayTableWindow.getTable().getTableName(),condition,"ASC",columnName));
        else if(descSortCheckBox.isSelected())
            displayTableWindow.displayTable(displayTableWindow.getModel().searchTable(displayTableWindow.getTable().getTableName(),condition,"DESC", columnName));
        else
            displayTableWindow.displayTable(displayTableWindow.getModel().searchTable(displayTableWindow.getTable().getTableName(),condition,null,null));
    }
}
