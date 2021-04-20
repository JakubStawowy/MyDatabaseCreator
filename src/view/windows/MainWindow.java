package view.windows;

import logic.facades.DatabaseFacade;
import view.components.MainWindowButtons;
import view.components.MdcFrame;
import run.Run;
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
import java.util.Map;

public class MainWindow extends MdcFrame {

    private DatabaseFacade databaseFacade;
    private DefaultListModel<String> tableList = new DefaultListModel<>();
    private JList<String> tableJList;
    private JScrollPane tablesScroll;
    private JScrollPane scrollPane;
    private MainWindowButtons mainWindowButtons;
    private Color backgroundColor = new Color(67,67,67);

    public MainWindow(DatabaseFacade databaseFacade) throws SQLException{

        this.databaseFacade = databaseFacade;
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
                        databaseFacade.disconnect();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                }, null);
            }
        });
    }

    @Override
    public void createWidgets() throws SQLException {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);

        JPanel tablePanel = createGridPanel(1, 1, 0, 0, 20);

        scrollPane = new JScrollPane();
        scrollPane.getViewport().setBackground(backgroundColor);
        scrollPane.setBorder(null);

        mainPanel.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        mainPanel.setPreferredSize(new Dimension(800,600));

        JMenu menu = new JMenu("menu");

        JMenuBar menuBar = new JMenuBar();

        JMenu newItem = new JMenu("New");

        JMenuItem newConnectionItem = new JMenuItem("Connection");
        newConnectionItem.addActionListener(event->
            new WarningWindow("Close current connection?", subEvent -> {
                try {
                    databaseFacade.disconnect();
                } catch (SQLException sqlException) {
                    sqlException.getMessage();
                }
                dispose();
            }, finalAction->new Run())
        );

        JMenuItem reconnectItem = new JMenuItem("Reconnect");
        reconnectItem.addActionListener(event->
            new WarningWindow("Reconnect?", subEvent->{
                Map<String, String> propertiesMap = databaseFacade.getDatabasePropertiesMap();
                try {
                    databaseFacade.disconnect();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    databaseFacade = new DatabaseFacade(
                            propertiesMap.get("host"),
                            propertiesMap.get("port"),
                            propertiesMap.get("databaseName"),
                            propertiesMap.get("username"),
                            propertiesMap.get("password")
                    );
                    databaseFacade.importDatabase();
                    new MainWindow(databaseFacade);
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
                    databaseFacade.disconnect();
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
                    databaseFacade.disconnect();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }, null));

        newItem.add(newConnectionItem);
        menu.add(newItem);
        menu.add(reconnectItem);
        menu.add(closeConnectionItem);
        menu.add(exitItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        for(String tableName: databaseFacade.getTableNames())
            tableList.addElement(tableName);

        tableJList = new JList<>(tableList);
        tableJList.setBackground(new Color(105,105,105));
        tableJList.setForeground(Color.WHITE);
        setListMouseListener();

        mainWindowButtons = new MainWindowButtons(databaseFacade, this);
        mainWindowButtons.setPreferredSize(new Dimension(300,0));
        mainWindowButtons.setBackground(new Color(67,67,67));

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
        try {
            JTable displayedTable;
            List<String>columnNames;
            columnNames = databaseFacade.importTable(tableJList.getSelectedValue()).getColumnNames();

            Object[][] tableData = new Object[data.size()][columnNames.size()];
            for(int i = 0 ; i < data.size(); i++)
                for(int j = 0 ; j < columnNames.size() ; j++)
                    tableData[i][j] = data.get(i).get(j);
            displayedTable = new JTable(new DefaultTableModel(tableData,columnNames.toArray()));
            scrollPane.setViewportView(displayedTable);
            scrollPane.getViewport().setBackground(backgroundColor);
            scrollPane.setBorder(null);
        } catch (SQLException sqlException) {
            new WarningWindow(sqlException.getMessage(),null,null);
        }
    }

    public void removeTableFromJList(int index){

        tableList.remove(index);
        tableJList = new JList<>(tableList);
        tableJList.setBackground(new Color(105,105,105));
        tableJList.setForeground(Color.WHITE);
        setListMouseListener();
        tablesScroll.setViewportView(tableJList);
        tableJList.setLayoutOrientation(JList.VERTICAL);
    }

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

    private void setListMouseListener(){
        tableJList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    displayTable(databaseFacade.importTable(tableJList.getSelectedValue()).getData());
                    mainWindowButtons.setButtons(true);
                }catch (SQLException sqlException){
                    new WarningWindow(sqlException.getMessage(),null,null);
                }
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
