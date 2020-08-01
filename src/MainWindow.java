import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

/*
* MainWindow class is used only after successful connection with database.
* MainWindow allows to use all functions operating on database (create table, drop table etc.)
* */
public class MainWindow extends JFrame implements MyWindow{

    private Model model;

    public MainWindow(Model model){

        this.model = model;

        setSize(800,600);
        initWindow();
        setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new WarningWindow("Are you sure you want to exit?",subEvent->{

                    dispose();
                    try {
                        model.closeConnection();
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                    System.exit(0);
                });
            }
        });
    }

    @Override
    public void initWindow() {

        addButton(540, 400, 200, 40, "Import database", event->new ConnectWindow(null));
        addButton(540, 480, 200, 40, "Close", event->new WarningWindow("Are you sure you want to exit?",subEvent->{

            dispose();
            try {
                model.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }));

        JMenu menu = new JMenu("menu");
        JMenuBar menuBar = new JMenuBar();
        JMenuItem it1 = new JMenuItem("miau");
        menu.add(it1);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    @Override
    public void addButton(int x, int y, int width, int height, String text, ActionListener actionListener){

        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setBounds(x,y,width,height);
        add(button);
    }

    @Override
    public JTextField addTextField(int x, int y, int width, int height) {

        JTextField textField = new JTextField();
        textField.setBounds(x,y,width,height);
        add(textField);

        return textField;
    }

    @Override
    public void addLabel(int x, int y, int width, int height, String text) {
        JLabel label = new JLabel(text);
        label.setBounds(x,y,width,height);
        add(label);
    }
}
