import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
* MainWindow class is used only after successful connection with database.
* MainWindow allows to use all functions operating on database (create table, drop table etc.)
* */
public class MainWindow extends JFrame implements MyWindow{

    private Model model;
    private List<String> tableList;
    private JList<String> list;
    private JScrollPane scroll;

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
        tableList = model.getTableNames();
        list = new JList<>(tableList.toArray(new String[0]));

        MainWindowButtons mainWindowButtons = new MainWindowButtons(model, list);
        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainWindowButtons.enableButtons();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        scroll = new JScrollPane();
        scroll.setViewportView(list);
        list.setLayoutOrientation(JList.VERTICAL);

        add(scroll);
        add(new JPanel());
        //add(new JPanel());
        add(mainWindowButtons);

    }

    @Override
    public JButton addButton(int x, int y, int width, int height, String text, ActionListener actionListener, Boolean buttonEnable){

        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setBounds(x,y,width,height);
        add(button);
        return button;
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
    public void removeTableFromJList(int index){

        tableList.remove(index);
        list = new JList<>(tableList.toArray(new String[0]));
        scroll.setViewportView(list);
        list.setLayoutOrientation(JList.VERTICAL);
    }
}
