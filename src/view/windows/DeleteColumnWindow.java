package view.windows;

import view.components.MyDialog;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.util.List;
import java.util.Vector;

/*
* DeleteColumnWindow
*
* @extends MyDialog
*
* This window allows to delete selected column from created table
*
* */
public class DeleteColumnWindow extends MyDialog {

    private Vector<String> columnNames;
    private CreateTableWindow createTableWindow;

    public DeleteColumnWindow(CreateTableWindow createTableWindow){
        this.createTableWindow = createTableWindow;
        columnNames = createTableWindow.getColumnNames();
        createWidgets();
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
    @Override
    public void createWidgets() {

//        -----------------------------------mainPanel------------------------------------------------------------------

        JPanel mainPanel = createGridPanel(2,1,0,20,20);

//        -----------------------------------buttonsPanel---------------------------------------------------------------

        JPanel buttonsPanel = createGridPanel(1,2,20,0,0);

//        -----------------------------------columnsComboBox------------------------------------------------------------

        JComboBox<String> columnsComboBox = new JComboBox<>();
        for(String columnName: columnNames)
            columnsComboBox.addItem(columnName);

//        -----------------------------------deleteButton---------------------------------------------------------------

        JButton deleteButton = createButton("Delete",event->{
            int index = columnsComboBox.getSelectedIndex();
            createTableWindow.getColumnNames().remove(index);
            createTableWindow.getColumnTypes().remove(index);
            createTableWindow.getConstraintsVector().remove(index);
            createTableWindow.displayTable(null);
            dispose();
        }, true);

//        -----------------------------------cancelButton---------------------------------------------------------------

        JButton cancelButton = createButton("Cancel", event->dispose(), true);

        buttonsPanel.add(deleteButton);
        buttonsPanel.add(cancelButton);

        mainPanel.add(columnsComboBox);
        mainPanel.add(buttonsPanel);
        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {}
}
