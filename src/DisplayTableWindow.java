import javax.swing.*;
import java.awt.*;

/*
* DisplayTableWindow class is used to display all table data.
* */
public class DisplayTableWindow extends MyDialog{
    private Table table;
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
        String[][] tableData= new String[table.getData().size()][table.getColumnNames().size()];

        for(int i = 0 ; i < table.getData().size() ; i++)
            for(int j = 0 ; j < table.getColumnNames().size() ; j++)
                tableData[i][j] = String.valueOf(table.getData().get(i).get(j));

        JTable newTable = new JTable(tableData,table.getColumnNames().toArray());
        JScrollPane scrollPane = new JScrollPane(newTable);

        add(scrollPane);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2,100,100));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(event->dispose());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(event->dispose());

        panel.add(saveButton);
        panel.add(closeButton);
        add(panel);
    }
}
