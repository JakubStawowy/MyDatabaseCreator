package GUI;
import Logic.Model;
import Logic.Run;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

/*
* MainWindow class is used only after successful connection with database.
* MainWindow allows to use all functions operating on database (create table, drop table etc.)
* */
public class MainWindow extends MyFrame{

    private Model model;
    private List<String> tableList;
    private JList<String> list;
    private JScrollPane scroll;
    private JScrollPane scrollPane;
    private JPanel tablePanel;
    private MainWindowButtons mainWindowButtons;
    private Color backgroundColor = new Color(67,67,67);
    public MainWindow(Model model){

        this.model = model;

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
//                    System.exit(0);
                }, null);
            }
        });
    }

    @Override
    public void initWindow() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        tablePanel = createGridPanel(1,1,0,0,20);
        scrollPane = new JScrollPane();
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.setBorder(null);


        mainPanel.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        mainPanel.setPreferredSize(new Dimension(800,600));

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
                        }, finalAction->new Run())
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

                    for(String tableName: tableList){
                        System.out.println(tableName);
                        model.dropCopiedTable(tableName);
                    }
                    model.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dispose();
                new Run();
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
//                System.exit(0);
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
//        list = new JList<>(tableList.toArray(new String[0]));
        list = new JList<>(tableList.toArray(new String[0]));
        list.setBackground(new Color(105,105,105));
        list.setForeground(Color.WHITE);
        setListMouseListener();
        //------------------------------------GUI.MainWindowButtons---------------------------------------------------
        mainWindowButtons = new MainWindowButtons(model, this);
        mainWindowButtons.setPreferredSize(new Dimension(300,0));
        mainWindowButtons.setBackground(new Color(67,67,67));

        scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(300,0));
        list.setLayoutOrientation(JList.VERTICAL);

        tablePanel.add(scrollPane);

        mainPanel.add(scroll, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(mainWindowButtons, BorderLayout.EAST);

        add(mainPanel);

    }

    @Override
    public void displayTable(List<List<Object>> data) {
        JTable displayedTable;
        List<String>columnNames = model.importTable(list.getSelectedValue()).getColumnNames();
        Object[][] tableData = new Object[data.size()][columnNames.size()];
        for(int i = 0 ; i < data.size(); i++)
            for(int j = 0 ; j < columnNames.size() ; j++)
                tableData[i][j] = data.get(i).get(j);
        displayedTable = new JTable(new DefaultTableModel(tableData,columnNames.toArray()));
        scrollPane.setViewportView(displayedTable);
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.setBorder(null);
    }

    /*
    * removeTableFromJList method sets new list without removed table (using table index).
    *
    * @param index - table index
    * */
    public void removeTableFromJList(int index){

        tableList.remove(index);
        list = new JList<>(tableList.toArray(new String[0]));
        list.setBackground(new Color(105,105,105));
        list.setForeground(Color.WHITE);
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
                displayTable(model.importTable(list.getSelectedValue()).getData());
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
