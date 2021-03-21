package view.windows;
import logic.DatabaseFacade;
import view.components.MyDialog;
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

/*
* AddForeignKeyReferenceWindow
*
* @extends MyDialog
*
* This window displays all Primary Keys from connected Database and allows to select Foreign Key reference for created column.
*
* */
public class AddForeignKeyReferenceWindow extends MyDialog {
    private DatabaseFacade databaseFacade;
    private JList<String> primaryKeyJList;
    private List<String> tableNames;
    private List<Map<String, String>> primaryKeyList;
    private NewColumnWindow newColumnWindow;
    private String primaryKeyWithType;
    public AddForeignKeyReferenceWindow(NewColumnWindow newColumnWindow, DatabaseFacade databaseFacade){
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

//        --------------------------------------mainPanel-----------------------------------------------------------------

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,0,20));
        mainPanel.setBackground(new Color(67,67,67));

//        --------------------------------------buttonsPanel-----------------------------------------------------------------

        JPanel buttonsPanel = createGridPanel(1,2,20,0,20);

//        -------------------------------------addForeignKeyButton----------------------------------

        JButton addForeignKeyButton = createButton("Add Foreign Key", event->{

            String type = primaryKeyWithType.substring(primaryKeyWithType.indexOf(" ")+1, primaryKeyWithType.indexOf("("));
            String size = primaryKeyWithType.substring(primaryKeyWithType.indexOf("(")+1,primaryKeyWithType.indexOf(")"));
            newColumnWindow.setForeignKey(primaryKeyJList.getSelectedValue());
            newColumnWindow.setTypeAndSize(type, size);
            newColumnWindow.setForeignKeyComponents();
            dispose();

        },true);

//        -------------------------------------cancelButton-----------------------------------------

        JButton cancelButton = createButton("Cancel", event->dispose(),true);

//        -------------------------------------primaryKeysLabel----------------------------------

        JLabel primaryKeysLabel = createLabel("Primary Keys in "+databaseFacade.getDatabaseName()+" schema");
        primaryKeysLabel.setHorizontalAlignment(JLabel.CENTER);
        primaryKeysLabel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));

//        -------------------------------------primaryKeyJList----------------------------------

        primaryKeyJList.setBackground(new Color(105,105,105));
        primaryKeyJList.setForeground(Color.WHITE);

//        -------------------------------------primaryKeyScrollPane----------------------------------

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
            primaryKeyList = databaseFacade.getPrimaryKeys();
        } catch (SQLException ignored) {
            new WarningWindow("There was a problem with primary keys import. Reconnect database and try again", null, null);
        }
        DefaultListModel<String> stringPrimaryKeyList = new DefaultListModel<>();
        for(int index = 0 ; index < tableNames.size() ; index++){
            primaryKeyWithType = primaryKeyList.get(index).get(tableNames.get(index));
            String tablePrimaryKey = tableNames.get(index)+"("+primaryKeyWithType.substring(0,primaryKeyWithType.indexOf(" "))+")";
            stringPrimaryKeyList.addElement(tablePrimaryKey);
        }
        primaryKeyJList = new JList<>(stringPrimaryKeyList);
    }
}
