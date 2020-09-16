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

        JPanel mainPanel = new JPanel(new GridLayout(5,2,20,20));
        mainPanel.setBackground(new Color(67,67,67));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        addLabel(0,0,0,0,"Database name:", mainPanel);

        JTextField databaseName = addTextField(0,0,0,0,"jdbc:mysql://localhost:3306/", mainPanel);

        addLabel(0,0,0,0,"Username:", mainPanel);

        JTextField username = addTextField(0,0,0,0,"root", mainPanel);

        addLabel(0,0,0,0,"Password:", mainPanel);

        JPasswordField password = new JPasswordField();
        password.setBackground(new Color(105,105,105));
        password.setForeground(Color.WHITE);
        mainPanel.add(password);

        addLabel(0,0,0,0,null, mainPanel);

        JCheckBox hidePasswordCheckBox = new JCheckBox();
        hidePasswordCheckBox.setSelected(true);
        hidePasswordCheckBox.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(hidePasswordCheckBox.isSelected()){

                    password.setEchoChar('*');

                }else{
                    password.setEchoChar((char)0);
                }
            }
        });
        hidePasswordCheckBox.setText("hide password");
        hidePasswordCheckBox.setBackground(new Color(67,67,67));
        hidePasswordCheckBox.setForeground(Color.WHITE);

        mainPanel.add(hidePasswordCheckBox);

        addButton(0,0,0,0, "Connect", event-> connect(databaseName.getText(), username.getText(), String.valueOf(password.getPassword())),true, mainPanel);
        addButton(0,0,0,0, "Cancel", event-> new WarningWindow("Are you sure you want to exit?", subEvent-> dispose(), null), true, mainPanel);


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
