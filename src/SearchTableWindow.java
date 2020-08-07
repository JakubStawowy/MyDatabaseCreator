import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/*
* SearchTableWindow class
* */
public class SearchTableWindow extends MyDialog{
    private DisplayTableWindow displayTableWindow;
    private JCheckBox ascSortCheckBox;
    private JCheckBox descSortCheckBox;
    public SearchTableWindow(DisplayTableWindow displayTableWindow){

        this.displayTableWindow = displayTableWindow;

        setPreferredSize(new Dimension(500,100));
        setTitle("Search table "+displayTableWindow.getTable().getTableName());
        setLayout(new GridLayout(2,3));
        initWindow();
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
    @Override
    public void initWindow() {

        JButton searchButton;

        //------------------------------------ConditionLabel---------------------------------------------------
        JLabel conditionLabel = addLabel(0,0,0,0,"Condition:");
        conditionLabel.setHorizontalAlignment(SwingConstants.CENTER);


        //------------------------------------Textfield---------------------------------------------------
        JTextField textField = addTextField(0,0,0,0,"");

        //------------------------------------ColumnComboBox---------------------------------------------------
        JComboBox<String> columnComboBox = new JComboBox<>();

        for(String column: displayTableWindow.getTable().getColumnNames())
            columnComboBox.addItem(column);
        columnComboBox.setEnabled(false);

        //------------------------------------RandomConditionButton---------------------------------------------------
        addButton(0,0,0,0,"Random condition",event->{
            Table table = displayTableWindow.getTable();
            textField.setText(displayTableWindow.getModel().generateRandomCondition(table));
        },true);

        //------------------------------------SearchButton---------------------------------------------------
        searchButton = addButton(0,0,0,0,"Search",event->search(textField.getText(), (String) columnComboBox.getSelectedItem()),true);
        searchButton.setEnabled(false);

        //------------------------------------TextFieldDocumentListener---------------------------------------------------
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                if(!textField.getText().equals(""))
                    searchButton.setEnabled(true);
                else
                    searchButton.setEnabled(false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                if(!textField.getText().equals(""))
                    searchButton.setEnabled(true);
                else
                    searchButton.setEnabled(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        //------------------------------------CheckBoxes---------------------------------------------------
        ascSortCheckBox = new JCheckBox("Sort ascending");
        descSortCheckBox = new JCheckBox("Sort descending");

        ascSortCheckBox.addActionListener(event-> {

            descSortCheckBox.setSelected(false);
            if(ascSortCheckBox.isSelected())
                columnComboBox.setEnabled(true);
            else if(!ascSortCheckBox.isSelected() && !descSortCheckBox.isSelected())
                columnComboBox.setEnabled(false);
        });

        descSortCheckBox.addActionListener(event->{

            ascSortCheckBox.setSelected(false);

            if(descSortCheckBox.isSelected())
                columnComboBox.setEnabled(true);
            else if(!ascSortCheckBox.isSelected() && !descSortCheckBox.isSelected())
                columnComboBox.setEnabled(false);
        });

        //------------------------------------SortByLabel---------------------------------------------------
        JLabel columnLabel = addLabel(0,0,0,0,"Sort by:");
        columnLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(columnComboBox);
        add(ascSortCheckBox);
        add(descSortCheckBox);

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
