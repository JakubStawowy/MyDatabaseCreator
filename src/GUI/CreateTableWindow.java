package GUI;

import Logic.Model;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class CreateTableWindow extends MyDialog{
    private Model model;
    private Object[][] tableData;
    private JTable displayedTable;
    private JScrollPane scrollPane;
    private JLabel label;
    private int numberOfRows = 0;
    private Vector<String> columnNames = new Vector<>();
    public CreateTableWindow(Model model){

        this.model = model;
        setTitle("Create Table");
        setBounds(new Rectangle(800,600));
        setLocationRelativeTo(null);
        initWindow();
        setVisible(true);
    }
    @Override
    public void initWindow() {
        Color backgroundColor = new Color(67,67,67);
        String textFieldText = "Table Name";
        JPanel mainPanel = new JPanel(new GridLayout(1,2,0,0));
        JPanel panel = new JPanel(new GridLayout(6,1,20,20));
        JPanel tablePanel = new JPanel(new GridLayout(1,1,20,20));
        panel.setBackground(backgroundColor);
        tablePanel.setBackground(backgroundColor);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        scrollPane = new JScrollPane(displayedTable);

        label = addLabel(0,0,0,0,"Number Of Columns: "+columnNames.size(),panel);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField textField = addTextField(0,0,0,0, textFieldText,panel);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setCaretPosition(textFieldText.length());

        addButton(0,0,0,0,"New Column",event->new NewColumnWindow(this, model.getNumericTypes(), columnNames),true,panel);
        addButton(0,0,0,0,"Add Row", event->{
            numberOfRows++;
            tableData = new Object[numberOfRows][columnNames.size()];
            displayTable(null);
        }, true, panel);
        addButton(0,0,0,0,"Create Table",null,true,panel);
        addButton(0,0,0,0,"Cancel",event->dispose(),true, panel);

        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(backgroundColor);
        tablePanel.add(scrollPane);

        mainPanel.add(tablePanel);
        mainPanel.add(panel);
        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {
        if(!columnNames.isEmpty()){
            label.setText("Number Of Columns: "+columnNames.size());
            displayedTable = new JTable(new DefaultTableModel(tableData, columnNames.toArray()));
            scrollPane.setViewportView(displayedTable);
            scrollPane.setBorder(null);
        }
    }
}
