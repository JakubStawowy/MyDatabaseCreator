package GUI;

import Logic.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/*
* GUI.MainWindowButtons class is a class that extends JPanel class and MyWindow interface.
* It contains all main window buttons such as create table, delete table, display table etc.
* */
public class MainWindowButtons extends JPanel implements MyWindow {

    private Model model;
    private MainWindow mainWindow;
    private JButton addNewRowButton;
    private JButton createTableButton;
    private JButton removeTableButton;
    private JButton editTableButton;

    public MainWindowButtons(Model model, MainWindow mainWindow){

        this.model = model;
        this.mainWindow = mainWindow;
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        initWindow();
        setLayout(new GridLayout(4,1,0,50));
    }
    @Override
    public void initWindow() {



        addNewRowButton = createButton("Add new row",event->new AddRowWindow(mainWindow.getSelectedTable(), model, mainWindow),false);
        editTableButton = createButton("Edit table",event->new EditTableWindow(model, mainWindow.getSelectedTable()), false);
        removeTableButton = createButton("Remove table",event->{

            String tableName = mainWindow.getSelectedTable();
            int tableIndex = mainWindow.getSelectedTableIndex();

            new WarningWindow("Remove table "+tableName+"?",subEvent->{
                try {
                    model.dropTable(tableName);
                    model.removeTableFromList(tableName);
                    mainWindow.removeTableFromJList(tableIndex);
                    setButtons(false);
                } catch (SQLException exception) {
                    new WarningWindow(exception.getMessage(), null, null);
                }
            }, null
            );
        },false);
        createTableButton = createButton("Create new table",event->new CreateTableWindow(model, mainWindow),true);

        add(addNewRowButton);
        add(editTableButton);
        add(removeTableButton);
        add(createTableButton);

    }

    @Override
    public void displayTable(List<List<Object>> data) {}

    @Override
    public void setTextField(JTextField textField, String textFieldText) {}

    @Override
    public JButton createButton(String text, ActionListener actionListener, Boolean buttonEnable) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setEnabled(buttonEnable);
        button.setBackground(new Color(105,105,105));
        button.setForeground(Color.WHITE);
        return button;
    }

    @Override
    public JTextField createTextField(String text) {
        return null;
    }

    @Override
    public JLabel createLabel(String text) {
        return null;
    }

    @Override
    public JPanel createGridPanel(int rows, int cols, int hgap, int vgap, int margin) {
        return null;
    }

    public void setButtons(Boolean enable){

        addNewRowButton.setEnabled(enable);
        removeTableButton.setEnabled(enable);
        editTableButton.setEnabled(enable);
    }
}
