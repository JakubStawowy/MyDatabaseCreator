package view.windows;

import database.services.RandomConditionGenerator;
import view.components.MdcFrame;
import exceptions.EmptyTableException;
import database.models.Table;
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
import java.sql.SQLException;
import java.util.List;

public class SearchTableWindow extends MdcFrame {

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

        JPanel mainPanel = createGridPanel(2,3,20,20,20);
        JButton searchButton;

        JTextField conditionField = createTextField("Condition");
        setTextField(conditionField, "Condition");

        JComboBox<String> columnComboBox = new JComboBox<>();

        for(String column: displayTableWindow.getTable().getColumnNames())
            columnComboBox.addItem(column);

        columnComboBox.setEnabled(false);

        JButton randomConditionButton = createButton("Random condition",event->{
            try {
                Table table = displayTableWindow.getDatabaseFacade().importTable(tableName);
                conditionField.setText(RandomConditionGenerator.generateRandomCondition(table));
            } catch (EmptyTableException | SQLException emptyTableException) {
                new WarningWindow(emptyTableException.getMessage(), null, null);
            }
        },true);

        searchButton = createButton("Search",event->search(conditionField.getText(), (String) columnComboBox.getSelectedItem()),true);
        searchButton.setEnabled(false);

        JButton closeButton = createButton("Close", event->dispose(), true);
        add(mainPanel);

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
            displayTableWindow.displayTable(displayTableWindow.getDatabaseFacade().searchTable(displayTableWindow.getTable().getTableName(),condition,"ASC",columnName));
        else if(descSortCheckBox.isSelected())
            displayTableWindow.displayTable(displayTableWindow.getDatabaseFacade().searchTable(displayTableWindow.getTable().getTableName(),condition,"DESC", columnName));
        else
            displayTableWindow.displayTable(displayTableWindow.getDatabaseFacade().searchTable(displayTableWindow.getTable().getTableName(),condition,null,null));
    }
}
