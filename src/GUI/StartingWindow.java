package GUI;

import Logic.Run;

import javax.swing.*;
import java.awt.*;
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

    }
    @Override
    public void initWindow() {



        JPanel mainPanel = new JPanel(null);

        mainPanel.setLayout(new GridLayout(3,1,0,20));
        mainPanel.setBackground(new Color(67,67,67));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        addButton(0,0,0,0, "Import database", event->new ConnectWindow(this), true, mainPanel);
        addButton(0,0,0,0, "Skip", event->dispose(),false, mainPanel);
        addButton(0,0,0,0,  "Close", event->dispose(),true, mainPanel);

        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {

    }
}
