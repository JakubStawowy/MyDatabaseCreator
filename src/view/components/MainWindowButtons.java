package view.components;

import logic.DatabaseFacade;
import view.windows.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/*
* MainWindowButtons
*
* @extends JPanel
* @implements MyWindow
*
* This panel contains all buttons included in MainWindow
*
* */
public class MainWindowButtons extends JPanel implements MyWindow {

    private DatabaseFacade databaseFacade;
    private MainWindow mainWindow;
    private JButton addNewRowButton;
    private JButton removeTableButton;
    private JButton editTableButton;

    public MainWindowButtons(DatabaseFacade databaseFacade, MainWindow mainWindow){

        this.databaseFacade = databaseFacade;
        this.mainWindow = mainWindow;

        createWidgets();
        setLayout(new GridLayout(4,1,0,50));
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

    }
    @Override
    public void createWidgets() {


        addNewRowButton = createButton("Add new row",event->new AddRowWindow(databaseFacade.getTable(mainWindow.getSelectedTable()), databaseFacade, mainWindow),false);
        editTableButton = createButton("Edit table",event->new EditTableWindow(databaseFacade, mainWindow.getSelectedTable()), false);
        removeTableButton = createButton("Remove table",event->{

            String tableName = mainWindow.getSelectedTable();
            int tableIndex = mainWindow.getSelectedTableIndex();

            new WarningWindow("Remove table "+tableName+"?",subEvent->{
                try {
                    databaseFacade.dropTable(tableName);
                    databaseFacade.removeTableFromList(tableName);
                    mainWindow.removeTableFromJList(tableIndex);
                    setButtons(false);
                } catch (SQLException exception) {
                    new WarningWindow(exception.getMessage(), null, null);
                }
            }, null
            );
        },false);

        JButton createTableButton = createButton("Create new table", event -> new CreateTableWindow(databaseFacade, mainWindow), true);

        add(addNewRowButton);
        add(editTableButton);
        add(removeTableButton);
        add(createTableButton);

    }

    @Override
    public void displayTable(List<List<Object>> data) {}


    @Override
    public void setTextField(JTextField textField, String textFieldText) {}

    @Override
    public JButton createButton(String text, ActionListener actionListener, Boolean buttonEnable) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setEnabled(buttonEnable);
        button.setBackground(new Color(105,105,105));
        button.setForeground(Color.WHITE);
        return button;
    }

    @Override
    public JTextField createTextField(String text) {
        return null;
    }

    @Override
    public JLabel createLabel(String text) {
        return null;
    }

    @Override
    public JPanel createGridPanel(int rows, int cols, int hgap, int vgap, int margin) {

        JPanel panel = new JPanel(new GridLayout(rows,cols,hgap,vgap));
        panel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        panel.setBackground(new Color(67,67,67));
        panel.setForeground(Color.WHITE);
        return panel;
    }

    public void setButtons(Boolean enable){

        addNewRowButton.setEnabled(enable);
        removeTableButton.setEnabled(enable);
        editTableButton.setEnabled(enable);
    }
}
