package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/*
* WarningWindow class is responsible for displaying warnings and executing action passed as constructor parameters.
* */
public class WarningWindow extends MyDialog{

    private ActionListener action;
    private ActionListener finalAction;
    private String text;

    public WarningWindow(String text, ActionListener action, ActionListener finalAction){

        this.text = text;
        this.action = action;
        this.finalAction = finalAction;

        setSize(340,120);
        setTitle("Warning");

        initWindow();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void initWindow() {

        JPanel mainPanel = new JPanel(new GridLayout(2,1));
        JPanel subPanel = new JPanel();

        mainPanel.add(new JLabel(text, SwingConstants.CENTER));

        if(action!=null) {

            subPanel.setLayout(null);
            JButton yesButton = new JButton("Yes");
            yesButton.setBounds(50, 0, 80, 20);

            yesButton.addActionListener(action);
            yesButton.addActionListener(event -> dispose());
            yesButton.addActionListener(finalAction);

            JButton noButton = new JButton("No");
            noButton.setBounds(190, 0, 80, 20);
            noButton.addActionListener(event -> dispose());

            if(finalAction!=null)
                noButton.addActionListener(finalAction);

            subPanel.add(yesButton);
            subPanel.add(noButton);

        }
        else{
            JButton okButton = new JButton("Ok");
            okButton.setBounds(65,25,80,20);
            okButton.addActionListener(event->dispose());
            if(finalAction!=null)
                okButton.addActionListener(finalAction);
            subPanel.add(okButton);
        }

        mainPanel.add(subPanel);
        add(mainPanel);

    }

    @Override
    public void displayTable(List<List<Object>> data) {

    }
}
