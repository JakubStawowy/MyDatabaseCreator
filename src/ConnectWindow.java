import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

/*
* ConnectWindow class allows to connect with database using database name, username and password.
* */
public class ConnectWindow extends MyDialog{

    private MyDialog startingWindow;

    public ConnectWindow(MyDialog window){

        this.startingWindow = window;

        setBounds(50,50,350,200);
        setTitle("Connect");
        setVisible(true);
        setLocationRelativeTo(null);
        initWindow();

    }

    @Override
    public void initWindow() {

        JTextField databaseName = addTextField(185,15,125,20,"jdbc:mysql://localhost:3306/");
        JTextField username = addTextField(185,45,125,20,"root");

        JPasswordField password = new JPasswordField();
        password.setBounds(185,75,125,20);
        add(password);

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
        hidePasswordCheckBox.setBounds(185,100,125,10);
        add(hidePasswordCheckBox);

        addButton(185, 120, 125, 20, "Cancel", event-> new WarningWindow("Are you sure you want to exit?", subEvent-> dispose()));
        addButton(20, 120, 125, 20, "Connect", event-> connect(databaseName.getText(), username.getText(), String.valueOf(password.getPassword())));

        addLabel(20,15,100,20,"Database name:");
        addLabel(20,45,100,20,"Username:");
        addLabel(20,75,100,20,"Password:");
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
            startingWindow.dispose();
            dispose();

        } catch (SQLException e) {
            new WarningWindow("Connecting failed. Do you want to try again?", event1 ->
                connect(databaseName, username, password));

        }
    }
}
