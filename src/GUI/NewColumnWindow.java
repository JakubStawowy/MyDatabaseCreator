package GUI;

import Logic.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Vector;

public class NewColumnWindow extends MyDialog {

    private CreateTableWindow createTableWindow;
    private Vector<String> columnNames;
    private String[] types;

    public NewColumnWindow(CreateTableWindow createTableWindow, String[] types, Vector<String> columnNames){

        this.types = types;
        this.createTableWindow = createTableWindow;
        this.columnNames = columnNames;

        setTitle("New Column");
        setBounds(new Rectangle(300,250));
        setLocationRelativeTo(null);

        initWindow();
        setVisible(true);
    }
    @Override
    public void initWindow() {
        String textFieldText = "Column Name";
        JPanel mainPanel = new JPanel(new GridLayout(4,1, 0,20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2,20,0));

        JTextField textField = addTextField(0,0,0,0, textFieldText, mainPanel);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setRenderer(new MyComboBoxRenderer("Type"));

        for(String types: types){
            comboBox.addItem(types);
        }
        comboBox.setSelectedIndex(-1);
        mainPanel.add(comboBox);

        addTextField(0,0,0,0,"Length",mainPanel);

        addButton(0,0,0,0,"Add Column",event->{
            columnNames.add(textField.getText());
            createTableWindow.displayTable(null);
            dispose();
        },true,buttonsPanel);
        addButton(0,0,0,0,"Cancel",event->dispose(),true,buttonsPanel);

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
}
