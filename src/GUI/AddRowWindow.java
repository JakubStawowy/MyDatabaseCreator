package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AddRowWindow extends MyDialog {

    private List<String> columnNames;


    public AddRowWindow(List<String> columnNames){

        this.columnNames = columnNames;
        String title = "Add row";
        setTitle(title);
        int height = 200;
        int width = 400;
        setSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2,1));
        initWindow();
        setVisible(true);
    }
    @Override
    public void initWindow() {

        displayTable(null);
        JPanel buttonsPanel = new JPanel(null);
        addButton(20,10,60,20,"Add",null,true, buttonsPanel);
        add(buttonsPanel);

    }
    @Override
    public void displayTable(List<List<Object>> data){

        JTable table = new JTable(new DefaultTableModel(new Object[][]{}, columnNames.toArray()));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportView(table);
        add(scrollPane);
    }
}
