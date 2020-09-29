package GUI;

import Logic.Model;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import javax.swing.AbstractAction;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

/*
* ConnectWindow
*
* @extends MyDialog
*
* This window allows to connect with database using database name, username and password
*
* */
public class ConnectWindow extends MyDialog{

    private MyDialog startingWindow;

    public ConnectWindow(MyDialog window){

        this.startingWindow = window;
        final String title = "Connect";
        final int width = 400;
        final int height = 300;

        setSize(width,height);
        setTitle(title);

        createWidgets();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void createWidgets() {

//---------------------------------------mainPanel----------------------------------------------------------------------

        JPanel mainPanel = createGridPanel(5,2,20,20,20);

//--------------------------------------databaseNameLabel------------------------------------------------

        JLabel databaseNameLabel = createLabel("Database name:");

//--------------------------------------databaseNameField------------------------------------------------

        JTextField databaseNameField = createTextField("jdbc:mysql://localhost:3306/");

//--------------------------------------usernameLabel----------------------------------------------------

        JLabel usernameLabel = createLabel("Username:");

//--------------------------------------usernameField----------------------------------------------------

        JTextField usernameField = createTextField("root");

//--------------------------------------passwordLabel----------------------------------------------------

        JLabel passwordLabel = createLabel("Password:");

//--------------------------------------passwordField----------------------------------------------------

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBackground(new Color(105,105,105));
        passwordField.setForeground(Color.WHITE);

//--------------------------------------hidePasswordCheckBox---------------------------------------------

        JCheckBox hidePasswordCheckBox = new JCheckBox();
        hidePasswordCheckBox.setSelected(true);
        hidePasswordCheckBox.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hidePasswordCheckBox.isSelected()){

                    passwordField.setEchoChar('*');

                }else{
                    passwordField.setEchoChar((char)0);
                }
            }
        });
        hidePasswordCheckBox.setText("hide password");
        hidePasswordCheckBox.setBackground(new Color(67,67,67));
        hidePasswordCheckBox.setForeground(Color.WHITE);

//--------------------------------------connectButton----------------------------------------------------

        JButton connectButton = createButton("Connect", event-> connect(databaseNameField.getText(), usernameField.getText(), String.valueOf(passwordField.getPassword())),true);

//--------------------------------------cancelButton----------------------------------------------------

        JButton cancelButton = createButton("Cancel", event-> new WarningWindow("Are you sure you want to exit?", subEvent-> dispose(), null), true);

        mainPanel.add(databaseNameLabel);
        mainPanel.add(databaseNameField);
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(createLabel(null));
        mainPanel.add(hidePasswordCheckBox);
        mainPanel.add(connectButton);
        mainPanel.add(cancelButton);

        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {

    }

    /*
    * connect method allows to connect with database using given parameters and initializes WarningWindow class
    * if the connection was failed. If user decides to connect again, method calls itself.
    *
    * @param String databaseName
    * @param String username
    * @param String password
    * */
    public void connect(String databaseName, String username, String password){

        try {
            Model model = new Model(databaseName, username, password);
            model.importDatabase();
            new MainWindow(model);
            if(startingWindow!=null)
                startingWindow.dispose();
            dispose();

        } catch (SQLException ignored) {
            new WarningWindow("Connecting failed. Do you want to try again?", event ->
                connect(databaseName, username, password), null);

        }
    }
}
