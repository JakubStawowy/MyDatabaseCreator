package GUI;

import Logic.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/*
* GUI.MainWindowButtons class is a class that extends JPanel class and MyWindow interface.
* It contains all main window buttons such as create table, delete table, display table etc.
* */
public class MainWindowButtons extends JPanel implements MyWindow {

    private Model model;
    private MainWindow mainWindow;
    private JButton editTableButton;
    private JButton addNewRowButton;
    private JButton createTableButton;
    private JButton removeTableButton;
    private JButton displayTableButton;

    public MainWindowButtons(Model model, MainWindow mainWindow){

        this.model = model;
        this.mainWindow = mainWindow;
        initWindow();
        setLayout(new GridLayout(5,1,0,50));
    }
    @Override
    public void initWindow() {


        editTableButton = addButton(0,0,0,0,"Edit table",event->{

        },false, this);
        addNewRowButton = addButton(0,0,0,0,"Add new row",event->{

        },false, this);
        displayTableButton = addButton(0,0,0,0,"Display table",event->new DisplayTableWindow(model, mainWindow.getSelectedTable()), false, this);
        removeTableButton = addButton(0,0,0,0,"Remove table",event->{

            String tableName = mainWindow.getSelectedTable();
            int tableIndex = mainWindow.getSelectedTableIndex();

            new WarningWindow("Remove table "+tableName+"?",subEvent->{
                model.removeTableFromList(tableName);
                model.dropTable(tableName);
                mainWindow.removeTableFromJList(tableIndex);
                setButtons(false);
            }, null
            );
        },false, this);
        createTableButton = addButton(0,0,0,0,"Create new table",event->{

        },true, this);

    }

    @Override
    public void displayTable(List<List<Object>> data) {

    }

    @Override
    public JButton addButton(int x, int y, int width, int height, String text, ActionListener actionListener, Boolean buttonEnable, JPanel panel) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setEnabled(buttonEnable);
        panel.add(button);
        return button;
    }

    @Override
    public JTextField addTextField(int x, int y, int width, int height, String text, JPanel panel) {
        return null;
    }

    @Override
    public JLabel addLabel(int x, int y, int width, int height, String text, JPanel panel) {
        return null;
    }
    public void setButtons(Boolean enable){

        editTableButton.setEnabled(enable);
        addNewRowButton.setEnabled(enable);
        removeTableButton.setEnabled(enable);
        displayTableButton.setEnabled(enable);
    }
}
