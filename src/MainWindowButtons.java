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
    private MainWindow mainWindow;
    private JButton editTableButton;
    private JButton addNewRowButton;
    private JButton createTableButton;
    private JButton removeTableButton;
    private JButton displayTableButton;

    public MainWindowButtons(Model model, MainWindow mainWindow){

        this.model = model;
        this.mainWindow = mainWindow;
        setLayout(new GridLayout(5,1,0,50));
        initWindow();
    }
    @Override
    public void initWindow() {

        editTableButton = addButton(0,0,0,0,"Edit table",event->{

        },false);
        addNewRowButton = addButton(0,0,0,0,"Add new row",event->{

        },false);
        displayTableButton = addButton(0,0,0,0,"Display table",event->new DisplayTableWindow(model, model.getTable(mainWindow.getSelectedTable())),false);
        removeTableButton = addButton(0,0,0,0,"Remove table",event->{

            String tableName = mainWindow.getSelectedTable();
            int tableIndex = mainWindow.getSelectedTableIndex();

            new WarningWindow("Remove table "+tableName+"?",subEvent->{
                model.removeTableFromList(tableName);
                mainWindow.removeTableFromJList(tableIndex);
                setButtons(false);
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
    public JLabel addLabel(int x, int y, int width, int height, String text) {
        return null;
    }
    public void setButtons(Boolean enable){

        editTableButton.setEnabled(enable);
        addNewRowButton.setEnabled(enable);
        removeTableButton.setEnabled(enable);
        displayTableButton.setEnabled(enable);
    }
}
