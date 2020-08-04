import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;

/*
* MainWindow class is used only after successful connection with database.
* MainWindow allows to use all functions operating on database (create table, drop table etc.)
* */
public class MainWindow extends JFrame implements MyWindow{

    private Model model;

    public MainWindow(Model model){

        this.model = model;

        setSize(800,600);
        setTitle("MyDatabaseCreator");
        initWindow();
        setLayout(new GridLayout(1,4,0,100));
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new WarningWindow("Are you sure you want to exit?",subEvent->{

                    dispose();
                    try {
                        model.closeConnection();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                    System.exit(0);
                });
            }
        });
    }

    @Override
    public void initWindow() {

        JMenu menu = new JMenu("menu");
        JMenuBar menuBar = new JMenuBar();
        JMenuItem it1 = new JMenuItem("Close connection");
        it1.addActionListener(event->
            new WarningWindow("Are you sure you want to close connection?",subEvent->{
                try {
                    model.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dispose();
                new StartingWindow();
            }));

        JMenuItem it2 = new JMenuItem("Exit");
        it2.addActionListener(event->
            new WarningWindow("Are you sure you want to exit?",subEvent->{
                dispose();
                try {
                    model.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }));
        menu.add(it1);
        menu.add(it2);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        List<String> l1 = model.getTableNames();

        JList<String> list = new JList<>(l1.toArray(new String[0]));
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(list);
        list.setLayoutOrientation(JList.VERTICAL);

        add(scroll);
        add(new JPanel());
        //add(new JPanel());
        add(new MainWindowButtons(model, list));
    }

    @Override
    public void addButton(int x, int y, int width, int height, String text, ActionListener actionListener){

        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setBounds(x,y,width,height);
        add(button);
    }

    @Override
    public JTextField addTextField(int x, int y, int width, int height, String text) {

        JTextField textField = new JTextField();
        textField.setBounds(x,y,width,height);
        add(textField);

        return textField;
    }

    @Override
    public void addLabel(int x, int y, int width, int height, String text) {
        JLabel label = new JLabel(text);
        label.setBounds(x,y,width,height);
        add(label);
    }
}
