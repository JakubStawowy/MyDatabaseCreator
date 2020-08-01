import javax.swing.*;
import java.sql.SQLException;

/*
* ConnectWindow class allows to connect with database using database name, username and password.
* */
public class ConnectWindow extends MyDialog{

    private MyDialog startingWindow;

    public ConnectWindow(MyDialog window){

        this.startingWindow = window;

        setBounds(50,50,400,200);
        setTitle("Connect");
        setVisible(true);
        initWindow();

    }

    @Override
    public void initWindow() {

        JTextField databaseName = addTextField(150,30,200,20);
        JTextField username = addTextField(150,60,200,20);
        JTextField password = addTextField(150,90,200,20);

        addButton(250, 120, 100, 20, "Cancel", event-> new WarningWindow("Are you sure?", subEvent-> dispose()));
        addButton(20, 120, 100, 20, "Connect", event-> {

            try{
                Model model = new Model(databaseName.getText(),username.getText(),password.getText());

                new MainWindow(model);

                startingWindow.dispose();
                dispose();
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        });

        addLabel(20,30,100,20,"Database name:");
        addLabel(20,60,100,20,"Username:");
        addLabel(20,90,100,20,"Password:");
    }
}
