import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*
* MainWindowButtons class is a class that extends JPanel class and MyWindow interface.
* It contains all main window buttons such as create table, delete table, display table etc.
* */
public class MainWindowButtons extends JPanel implements MyWindow {

    private Model model;
    private JList<String> tableList;
    private JButton editTableButton;
    private JButton addNewRowButton;
    private JButton createTableButton;
    private JButton removeTableButton;
    private JButton displayTableButton;

    public MainWindowButtons(Model model, JList<String> tableList){

        this.model = model;
        this.tableList = tableList;
        setLayout(new GridLayout(5,1,0,50));
        initWindow();
    }
    @Override
    public void initWindow() {

        editTableButton = addButton(0,0,0,0,"Edit table",event->{

        },false);
        addNewRowButton = addButton(0,0,0,0,"Add new row",event->{

        },false);
        displayTableButton = addButton(0,0,0,0,"Display table",event->new DisplayTableWindow(model.getTable(tableList.getSelectedValue())),false);
        removeTableButton = addButton(0,0,0,0,"Remove table",event->{
            String tableName = tableList.getSelectedValue();
            new WarningWindow("Remove table "+tableName+"?",subEvent->{
                model.removeTableFromList(tableName);

            }
            );
        },false);
        createTableButton = addButton(0,0,0,0,"Create new table",event->{

        },true);

    }

    @Override
    public JButton addButton(int x, int y, int width, int height, String text, ActionListener actionListener, Boolean buttonEnable) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setEnabled(buttonEnable);
        add(button);
        return button;
    }

    @Override
    public JTextField addTextField(int x, int y, int width, int height, String text) {
        return null;
    }

    @Override
    public void addLabel(int x, int y, int width, int height, String text) {

    }
    public void enableButtons(){
        editTableButton.setEnabled(true);
        addNewRowButton.setEnabled(true);
        createTableButton.setEnabled(true);
        removeTableButton.setEnabled(true);
        displayTableButton.setEnabled(true);
    }
}
