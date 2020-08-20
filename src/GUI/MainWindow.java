package GUI;
import Logic.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
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
    private MainWindowButtons mainWindowButtons;

    public MainWindow(Model model){

        this.model = model;

        //setSize(800,600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("MyDatabaseCreator");
        initWindow();
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
                }, null);
            }
        });
    }

    @Override
    public void initWindow() {

        JPanel mainPanel = new JPanel(new GridLayout(1,4,0,100));
        //------------------------------------MenuBar---------------------------------------------------
        JMenu menu = new JMenu("menu");
        JMenuBar menuBar = new JMenuBar();

        JMenu newItem = new JMenu("New");

        JMenuItem newConnectionItem = new JMenuItem("Connection");
        newConnectionItem.addActionListener(event->
                        new WarningWindow("Close current connection?", subEvent -> {
                            try {
                                model.closeConnection();
                            } catch (SQLException sqlException) {
                                sqlException.getMessage();
                            }
                            dispose();
                        }, finalAction->new ConnectWindow(null))
        );

        JMenuItem reconnectItem = new JMenuItem("Reconnect");
        reconnectItem.addActionListener(event->
            new WarningWindow("Reconnect?", subEvent->{
                String[] loginData = model.getLoginData();
                try {
                    model.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    model = new Model(loginData[0], loginData[1], loginData[2]);
                    model.importDatabase();
                    new MainWindow(model);
                    dispose();
                } catch (SQLException ignored) {
                    new WarningWindow("Reconnecting failed", null, null);
                }
            }, null)
        );
        JMenuItem closeConnectionItem = new JMenuItem("Close connection");
        closeConnectionItem.addActionListener(event->
            new WarningWindow("Are you sure you want to close connection?",subEvent->{
                try {
                    model.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dispose();
                new StartingWindow();
            }, null));

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(event->
            new WarningWindow("Are you sure you want to exit?",subEvent->{
                dispose();
                try {
                    model.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }, null));
        newItem.add(newConnectionItem);
        menu.add(newItem);
        menu.add(reconnectItem);
        menu.add(closeConnectionItem);
        menu.add(exitItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        //------------------------------------TableList---------------------------------------------------
        tableList = model.getTableNames();
        list = new JList<>(tableList.toArray(new String[0]));
        setListMouseListener();
        //------------------------------------GUI.MainWindowButtons---------------------------------------------------
        mainWindowButtons = new MainWindowButtons(model, this);

        scroll = new JScrollPane();
        scroll.setViewportView(list);
        list.setLayoutOrientation(JList.VERTICAL);

        mainPanel.add(scroll);
        mainPanel.add(new JPanel());
        mainPanel.add(mainWindowButtons);

        add(mainPanel);

    }

    @Override
    public void displayTable(List<List<Object>> data) {

    }

    @Override
    public JButton addButton(int x, int y, int width, int height, String text, ActionListener actionListener, Boolean buttonEnable, JPanel panel){

        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setBounds(x,y,width,height);
        panel.add(button);
        return button;
    }

    @Override
    public JTextField addTextField(int x, int y, int width, int height, String text, JPanel panel) {

        JTextField textField = new JTextField();
        textField.setBounds(x,y,width,height);
        panel.add(textField);

        return textField;
    }

    @Override
    public JLabel addLabel(int x, int y, int width, int height, String text, JPanel panel) {
        JLabel label = new JLabel(text);
        label.setBounds(x,y,width,height);
        panel.add(label);
        return label;
    }
    /*
    * removeTableFromJList method sets new list without removed table (using table index).
    *
    * @param index - table index
    * */
    public void removeTableFromJList(int index){

        tableList.remove(index);
        list = new JList<>(tableList.toArray(new String[0]));
        setListMouseListener();
        scroll.setViewportView(list);
        list.setLayoutOrientation(JList.VERTICAL);
    }
    public String getSelectedTable(){
        return list.getSelectedValue();
    }
    public int getSelectedTableIndex(){
        return list.getSelectedIndex();
    }
    /*
    * setListMouseListener method sets all buttons which require table to be selected from table list enabled.
    * */
    private void setListMouseListener(){
        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                mainWindowButtons.setButtons(true);
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
    }
}
