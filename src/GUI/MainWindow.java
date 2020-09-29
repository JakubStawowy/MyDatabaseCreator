package GUI;

import Logic.Model;
import Logic.Run;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;

/*
* MainWindow
*
* @extends MyFrame
*
* MainWindow allows to use all functions operating on database (create table, drop table, remove row etc.)
* */
public class MainWindow extends MyFrame{

    private Model model;
    private DefaultListModel<String> tableList = new DefaultListModel<>();
    private JList<String> tableJList;
    private JScrollPane tablesScroll;
    private JScrollPane scrollPane;
    private MainWindowButtons mainWindowButtons;
    private Color backgroundColor = new Color(67,67,67);

    public MainWindow(Model model){

        this.model = model;
        final String title = "MyDatabaseCreator";

        setTitle(title);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        createWidgets();
        setVisible(true);
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
    public void createWidgets() {

//        -----------------------------------------mainPanel------------------------------------------------------------

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);

//        -----------------------------------------tablePanel------------------------------------------------------------

        JPanel tablePanel = createGridPanel(1, 1, 0, 0, 20);

//        -----------------------------------------scrollPane-----------------------------------------------------------

        scrollPane = new JScrollPane();
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.setBorder(null);

//        -----------------------------------------mainPanel------------------------------------------------------------

        mainPanel.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        mainPanel.setPreferredSize(new Dimension(800,600));

//        -----------------------------------------menu-----------------------------------------------------------------

        JMenu menu = new JMenu("menu");

//        -----------------------------------------menuBar--------------------------------------------------------------

        JMenuBar menuBar = new JMenuBar();

//        -----------------------------------------newItem--------------------------------------------------------------

        JMenu newItem = new JMenu("New");

//        -----------------------------------------newConnectionItem----------------------------------------------------

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

//        -----------------------------------------reconnectItem--------------------------------------------------------

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

//        -----------------------------------------closeConnectionItem--------------------------------------------------

        JMenuItem closeConnectionItem = new JMenuItem("Close connection");
        closeConnectionItem.addActionListener(event->
            new WarningWindow("Are you sure you want to close connection?",subEvent->{
                try {
                    model.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                dispose();
                new Run();
            }, null));

//        -----------------------------------------exitItem-------------------------------------------------------------

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


        for(String tableName: model.getTableNames())
            tableList.addElement(tableName);


//        -----------------------------------------tableJList-----------------------------------------------------------

        tableJList = new JList<>(tableList);
        tableJList.setBackground(new Color(105,105,105));
        tableJList.setForeground(Color.WHITE);
        setListMouseListener();

//        -----------------------------------------mainWindowButtons----------------------------------------------------

        mainWindowButtons = new MainWindowButtons(model, this);
        mainWindowButtons.setPreferredSize(new Dimension(300,0));
        mainWindowButtons.setBackground(new Color(67,67,67));

//        -----------------------------------------tablesScroll---------------------------------------------------------

        tablesScroll = new JScrollPane(tableJList);
        tablesScroll.setBorder(null);
        tablesScroll.setPreferredSize(new Dimension(300,0));
        tableJList.setLayoutOrientation(JList.VERTICAL);

        tablePanel.add(scrollPane);

        mainPanel.add(tablesScroll, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(mainWindowButtons, BorderLayout.EAST);

        add(mainPanel);

    }

    @Override
    public void displayTable(List<List<Object>> data) {
        JTable displayedTable;
        List<String>columnNames = model.importTable(tableJList.getSelectedValue()).getColumnNames();
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
    * @param int index
    * */
    public void removeTableFromJList(int index){

        tableList.remove(index);
        tableJList = new JList<>(tableList);
        tableJList.setBackground(new Color(105,105,105));
        tableJList.setForeground(Color.WHITE);
        setListMouseListener();
        tablesScroll.setViewportView(tableJList);
        tableJList.setLayoutOrientation(JList.VERTICAL);
    }

     /*
     * addTableToJlist method sets new list with new created table
     *
     * @param String tableName
     * */
    public void addTableToJlist(String tableName){
        tableList.addElement(tableName);
        tableJList = new JList<>(tableList);
        tableJList.setBackground(new Color(105,105,105));
        tableJList.setForeground(Color.WHITE);
        setListMouseListener();
        tablesScroll.setViewportView(tableJList);
        tableJList.setLayoutOrientation(JList.VERTICAL);
    }
    public String getSelectedTable(){
        return tableJList.getSelectedValue();
    }
    public int getSelectedTableIndex(){
        return tableJList.getSelectedIndex();
    }
    /*
    * setListMouseListener method sets all buttons which require table to be selected from table list enabled.
    * */
    private void setListMouseListener(){
        tableJList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                mainWindowButtons.setButtons(true);
                displayTable(model.importTable(tableJList.getSelectedValue()).getData());
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
