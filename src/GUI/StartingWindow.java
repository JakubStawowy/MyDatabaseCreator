package GUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/*
* StartingWindow class
* */
public class StartingWindow extends MyDialog {

    public StartingWindow(){

        setSize(400,300);
        setTitle("MyDatabaseCreator");
        initWindow();
        setVisible(true);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    @Override
    public void initWindow() {
        JPanel mainPanel = new JPanel(null);
        addButton(100, 60, 200, 40, "Import database", event->new ConnectWindow(this), true, mainPanel);
        addButton(100, 120, 200, 40, "Skip", event->dispose(),false, mainPanel);
        addButton(100, 180, 200, 40, "Close", event->{dispose(); System.exit(0);},true, mainPanel);

        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {

    }

    public static void main(String[] args) {

        new StartingWindow();

    }
}
