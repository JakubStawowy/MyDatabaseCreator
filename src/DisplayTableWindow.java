import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/*
* DisplayTableWindow class is used to display all table data.
* */
public class DisplayTableWindow extends JFrame implements MyWindow{

    private Table table;
    private Model model;
    private JTable displayedTable;
    private Object[][] tableData;
    private JScrollPane scrollPane;

    public DisplayTableWindow(Model model, Table table){
        this.table = table;
        this.model = model;
        tableData = new Object[table.getData().size()][table.getColumnNames().size()];
        initWindow();
        setLocation(new Point(300,200));
        setTitle(table.getTableName());
        setLayout(new GridLayout(2,1));
        setPreferredSize(new Dimension(600,450));
        setVisible(true);
        pack();
    }

    @Override
    public void initWindow() {

        //------------------------------------DisplayedTable---------------------------------------------------
        scrollPane = new JScrollPane(displayedTable);
        displayTable(table.getData());

        add(scrollPane);

        //------------------------------------ButtonsPanel---------------------------------------------------
        JPanel Panel = new JPanel(null);
        JPanel subPanel = new JPanel(new GridLayout(3,2,20,20));
        subPanel.setBounds(10,10,565,185);

        JButton searchTableButton = new JButton("Search table");
        searchTableButton.addActionListener(event->new SearchTableWindow(this));

        JButton addRowButton = new JButton("Add row");
        addRowButton.addActionListener(event->{

            ((DefaultTableModel) displayedTable.getModel()).addRow(new Object[][]{});
            Rectangle rect = displayedTable.getCellRect(displayedTable.getRowCount(),0,true);
            displayedTable.scrollRectToVisible(rect);
        });

        JButton removeRowButton = new JButton("Remove row");
        removeRowButton.setEnabled(false);
        removeRowButton.addActionListener(event->{
            try {
                ((DefaultTableModel) displayedTable.getModel()).removeRow(displayedTable.getSelectedRow());
                table.numberOfRowsDeincrement();
                removeRowButton.setEnabled(false);
            }
            catch(ArrayIndexOutOfBoundsException exception){
                new WarningWindow("No row selected",null);
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(event->{
            table.setData(saveTable());
            saveButton.setEnabled(false);
            removeRowButton.setEnabled(false);
        });


        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(event->dispose());

        displayedTable.getModel().addTableModelListener(event -> saveButton.setEnabled(true));
        displayedTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(displayedTable.getSelectedRow() != -1)
                    removeRowButton.setEnabled(true);

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
        subPanel.add(searchTableButton);
        subPanel.add(addRowButton);
        subPanel.add(removeRowButton);
        subPanel.add(saveButton);
        subPanel.add(closeButton);

        Panel.add(subPanel);
        add(Panel);
    }

    @Override
    public JButton addButton(int x, int y, int width, int height, String text, ActionListener actionListener, Boolean buttonEnable) {
        return null;
    }

    @Override
    public JTextField addTextField(int x, int y, int width, int height, String text) {
        return null;
    }

    @Override
    public JLabel addLabel(int x, int y, int width, int height, String text) {
        return null;
    }

    /*
    * saveTable method returns multidimensional ArrayList with data collected from newTable (JTable)
    *
    * @return rows
    * */
    private List<List<Object>> saveTable(){

        List<List<Object>> rows = new ArrayList<>();
        List<Object> columns;
        displayedTable.clearSelection();
        for(int i = 0; i < displayedTable.getRowCount();i++) {
            columns = new ArrayList<>();
            for (int j = 0; j < displayedTable.getColumnCount(); j++)
                columns.add(displayedTable.getValueAt(i, j));
            rows.add(columns);
        }
        return rows;
    }
    public Table getTable(){
        return table;
    }
    public Object[][] getTableData(){
        return tableData;
    }
    public Model getModel(){
        return model;
    }

    /*
    * displayTable method displays table using data parameter
    *
    * @param data (multidimensional ArrayList)
    * */
    public void displayTable(List<List<Object>> data){

        tableData = new Object[data.size()][table.getColumnNames().size()];
        for(int i = 0 ; i < data.size() ; i++)
            for(int j = 0 ; j < table.getNumberOfColumns() ; j++)
                tableData[i][j] = data.get(i).get(j);


        displayedTable = new JTable(new DefaultTableModel(tableData,table.getColumnNames().toArray()));
        scrollPane.setViewportView(displayedTable);
        //scrollPane = new JScrollPane(displayedTable);
    }
}