import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/*
* MainWindowButtons class is a class that extends JPanel class and MyWindow interface.
* It contains all main window buttons such as create table, delete table, display table etc.
* */
public class MainWindowButtons extends JPanel implements MyWindow {

    private Model model;
    private JList<String> tableList;

    public MainWindowButtons(Model model, JList<String> tableList){

        this.model = model;
        this.tableList = tableList;
        setLayout(new GridLayout(5,1,0,50));
        initWindow();
    }
    @Override
    public void initWindow() {

        addButton(0,0,0,0,"Edit table",event->{

        });
        addButton(0,0,0,0,"Add new row",event->{

        });
        addButton(0,0,0,0,"Show table",event->new DisplayTableWindow(model.getTable(tableList.getSelectedValue())));
        addButton(0,0,0,0,"Delete table",event->{

        });
        addButton(0,0,0,0,"Create new table",event->{

        });
    }

    @Override
    public void addButton(int x, int y, int width, int height, String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        add(button);
    }

    @Override
    public JTextField addTextField(int x, int y, int width, int height, String text) {
        return null;
    }

    @Override
    public void addLabel(int x, int y, int width, int height, String text) {

    }
}
