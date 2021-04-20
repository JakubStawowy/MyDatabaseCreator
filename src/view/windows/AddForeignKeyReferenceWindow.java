package view.windows;
import logic.facades.DatabaseFacade;
import view.components.MdcFrame;

import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AddForeignKeyReferenceWindow extends MdcFrame {

    private DatabaseFacade databaseFacade;
    private JList<String> primaryKeyJList;
    private List<String> tableNames;
    private Map<String, String> primaryKeysMap;
    private AddNewColumnWindow newColumnWindow;
    private String primaryKeyWithType;

    public AddForeignKeyReferenceWindow(AddNewColumnWindow newColumnWindow, DatabaseFacade databaseFacade) throws SQLException{
        this.databaseFacade = databaseFacade;
        tableNames = databaseFacade.getTableNames();
        this.newColumnWindow = newColumnWindow;
        importPrimaryKeys();
        createWidgets();
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
    @Override
    public void createWidgets() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,0,20));
        mainPanel.setBackground(new Color(67,67,67));

        JPanel buttonsPanel = createGridPanel(1,2,20,0,20);

        JButton addForeignKeyButton = createButton("Add Foreign Key", event->{

            String type = primaryKeyWithType.substring(primaryKeyWithType.indexOf(" ")+1, primaryKeyWithType.indexOf("("));
            String size = primaryKeyWithType.substring(primaryKeyWithType.indexOf("(")+1,primaryKeyWithType.indexOf(")"));
            newColumnWindow.setForeignKey(primaryKeyJList.getSelectedValue());
            newColumnWindow.setTypeAndSize(type, size);
            newColumnWindow.setForeignKeyComponents();
            dispose();

        },true);

        JButton cancelButton = createButton("Cancel", event->dispose(),true);

        JLabel primaryKeysLabel = createLabel("Primary Keys in "+databaseFacade.getDatabasePropertiesMap().get("databaseName")+" schema");
        primaryKeysLabel.setHorizontalAlignment(JLabel.CENTER);
        primaryKeysLabel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));

        primaryKeyJList.setBackground(new Color(105,105,105));
        primaryKeyJList.setForeground(Color.WHITE);

        JScrollPane primaryKeyScrollPane = new JScrollPane();
        primaryKeyScrollPane.setViewportView(primaryKeyJList);
        primaryKeyScrollPane.setBorder(null);
        primaryKeyJList.setLayoutOrientation(JList.VERTICAL);

        buttonsPanel.add(addForeignKeyButton);
        buttonsPanel.add(cancelButton);

        mainPanel.add(primaryKeysLabel, BorderLayout.NORTH);
        mainPanel.add(primaryKeyScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {}
    private void importPrimaryKeys(){
        try {
            primaryKeysMap = databaseFacade.getPrimaryKeys();
        } catch (SQLException ignored) {
            new WarningWindow("There was a problem with primary keys import. Reconnect database and try again", null, null);
        }
        DefaultListModel<String> stringPrimaryKeyList = new DefaultListModel<>();
        for (String tableName : tableNames) {
            primaryKeyWithType = primaryKeysMap.get(tableName);
            String tablePrimaryKey = tableName + "(" + primaryKeyWithType.substring(0, primaryKeyWithType.indexOf(" ")) + ")";
            stringPrimaryKeyList.addElement(tablePrimaryKey);
        }
        primaryKeyJList = new JList<>(stringPrimaryKeyList);
    }
}
