package GUI;

import Logic.Model;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AddForeignKeyReferenceWindow extends MyDialog{
    private Model model;
    private JList<String> primaryKeyJList;
    private List<String> tableNames;
    private List<Map<String, String>> primaryKeyList;
    private NewColumnWindow newColumnWindow;
    public AddForeignKeyReferenceWindow(NewColumnWindow newColumnWindow, Model model){
        this.model = model;
        tableNames = model.getTableNames();
        this.newColumnWindow = newColumnWindow;
        importPrimaryKeys();
        initWindow();
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
    @Override
    public void initWindow() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,0,20));
        mainPanel.setBackground(new Color(67,67,67));
        JPanel buttonsPanel = createGridPanel(1,2,20,0,20);

        JLabel primaryKeysLabel = createLabel("Primary Keys in "+model.getDatabaseName()+" schema");
        primaryKeysLabel.setHorizontalAlignment(JLabel.CENTER);
        primaryKeysLabel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));

        JButton addForeignKeyButton = createButton("Add Foreign Key", event->{
            newColumnWindow.setForeignKey(primaryKeyJList.getSelectedValue());
            dispose();
        },true);
        JButton cancelButton = createButton("Cancel", event->dispose(),true);

        primaryKeyJList.setBackground(new Color(105,105,105));
        primaryKeyJList.setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(primaryKeyJList);
        scroll.setBorder(null);
        primaryKeyJList.setLayoutOrientation(JList.VERTICAL);

        buttonsPanel.add(addForeignKeyButton);
        buttonsPanel.add(cancelButton);

        mainPanel.add(primaryKeysLabel, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {}
    private void importPrimaryKeys(){
        try {
            primaryKeyList = model.getPrimaryKeys();
        } catch (SQLException ignored) {
            new WarningWindow("There was a problem with primary keys import. Reconnect database and try again", null, null);
        }
        DefaultListModel<String> stringPrimaryKeyList = new DefaultListModel<>();
        for(int index = 0 ; index < tableNames.size() ; index++){
            String tablePrimaryKey = tableNames.get(index)+"("+primaryKeyList.get(index).get(tableNames.get(index))+")";
            stringPrimaryKeyList.addElement(tablePrimaryKey);
        }
        primaryKeyJList = new JList<>(stringPrimaryKeyList);
    }
}
