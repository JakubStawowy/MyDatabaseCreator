package GUI;

import Logic.Model;
import Logic.Run;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

/*
* ConnectWindow class allows to connect with database using database name, username and password.
* */
public class ConnectWindow extends MyDialog{

    private MyDialog startingWindow;

    public ConnectWindow(MyDialog window){

        this.startingWindow = window;

        setSize(400,300);
        setTitle("Connect");

        initWindow();
        setVisible(true);
        setLocationRelativeTo(null);

    }

    @Override
    public void initWindow() {

        JPanel mainPanel = createGridPanel(5,2,20,20,20);

        JLabel databaseNameLabel = createLabel("Database name:");

        JTextField databaseNameField = createTextField("jdbc:mysql://localhost:3306/");

        JLabel usernameLabel = createLabel("Username:");

        JTextField usernameField = createTextField("root");

        JLabel passwordLabel = createLabel("Password:");

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBackground(new Color(105,105,105));
        passwordField.setForeground(Color.WHITE);



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


        JButton connectButton = createButton("Connect", event-> connect(databaseNameField.getText(), usernameField.getText(), String.valueOf(passwordField.getPassword())),true);
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
    * connect method tries to connect with database using given parameters and initializes WarningWindow class
    * if the connection was failed. If user decides to connect again, method calls itself.
    *
    * @param databaseName
    * @param username
    * @param password
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
