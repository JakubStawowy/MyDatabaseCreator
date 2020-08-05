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
    private JTable newTable;
    public DisplayTableWindow(Table table){
        this.table = table;
        initWindow();
        setTitle(table.getTableName());
        setLayout(new GridLayout(2,1));
        setLocationRelativeTo(null);
        setPreferredSize(new Dimension(600,300));
        setVisible(true);
        pack();
    }

    @Override
    public void initWindow() {

        //------------------------------------DisplayedTable---------------------------------------------------
        Object[][] tableData= new Object[table.getData().size()][table.getColumnNames().size()];
        for(int i = 0 ; i < table.getNumberOfRows() ; i++)
            for(int j = 0 ; j < table.getNumberOfColumns() ; j++)
                tableData[i][j] = table.getData().get(i).get(j);

        newTable = new JTable(new DefaultTableModel(tableData,table.getColumnNames().toArray()));

        JScrollPane scrollPane = new JScrollPane(newTable);

        add(scrollPane);


        //------------------------------------ButtonsPanel---------------------------------------------------
        JPanel Panel = new JPanel(null);
        JPanel subPanel = new JPanel(new GridLayout(2,2,20,20));
        subPanel.setBounds(10,10,565,110);


        JButton addRowButton = new JButton("Add row");
        addRowButton.addActionListener(event->{

            ((DefaultTableModel) newTable.getModel()).addRow(new Object[][]{});
            Rectangle rect = newTable.getCellRect(newTable.getRowCount(),0,true);
            newTable.scrollRectToVisible(rect);
        });

        JButton removeRowButton = new JButton("Delete row");
        removeRowButton.setEnabled(false);
        removeRowButton.addActionListener(event->{
            try {
                ((DefaultTableModel) newTable.getModel()).removeRow(newTable.getSelectedRow());
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

        newTable.getModel().addTableModelListener(event -> saveButton.setEnabled(true));
        newTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(newTable.getSelectedRow() != -1)
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
    public void addLabel(int x, int y, int width, int height, String text) {

    }

    /*
    * saveTable method returns multidimensional ArrayList with data collected from newTable (JTable)
    *
    * @return rows
    * */
    private List<List<Object>> saveTable(){

        List<List<Object>> rows = new ArrayList<>();
        List<Object> columns;
        newTable.clearSelection();
        System.out.println(newTable.getRowCount());
        for(int i = 0; i < newTable.getRowCount();i++) {
            columns = new ArrayList<>();
            for (int j = 0; j < newTable.getColumnCount(); j++)
                columns.add(newTable.getValueAt(i, j));
            rows.add(columns);
        }
        return rows;
    }
}