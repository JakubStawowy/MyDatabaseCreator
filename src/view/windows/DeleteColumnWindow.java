package view.windows;

import view.components.MdcFrame;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.util.List;
import java.util.Vector;

public class DeleteColumnWindow extends MdcFrame {

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

        JPanel mainPanel = createGridPanel(2,1,0,20,20);

        JPanel buttonsPanel = createGridPanel(1,2,20,0,0);

        JComboBox<String> columnsComboBox = new JComboBox<>();
        for(String columnName: columnNames)
            columnsComboBox.addItem(columnName);

        JButton deleteButton = createButton("Delete",event->{
            int index = columnsComboBox.getSelectedIndex();
            createTableWindow.getColumnNames().remove(index);
            createTableWindow.getColumnTypes().remove(index);
            createTableWindow.getConstraintsVector().remove(index);
            createTableWindow.displayTable(null);
            dispose();
        }, true);

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
