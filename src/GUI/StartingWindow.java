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

        JButton importButton = createButton("Import database", event->new ConnectWindow(this), true);
        JButton skipButton = createButton("Skip", event->dispose(),false);
        JButton closeButton = createButton("Close", event->dispose(),true);

        mainPanel.add(importButton);
        mainPanel.add(skipButton);
        mainPanel.add(closeButton);

        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {

    }
}
