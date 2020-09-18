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

        setTitle("Warning");
        setLocationRelativeTo(null);

        initWindow();
        pack();
        setVisible(true);
    }
    @Override
    public void initWindow() {

        JPanel mainPanel = createGridPanel(2,1,0,20,20);
        JPanel buttonsPanel;

        JLabel messageLabel = createLabel(text);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);

        if(action!=null) {
            buttonsPanel = createGridPanel(1,2,20,0,0);
            JButton yesButton = createButton("Yes",action,true);

            yesButton.addActionListener(event -> dispose());
            yesButton.addActionListener(finalAction);

            JButton noButton = createButton("No", event->dispose(),true);

            if(finalAction!=null) {
                noButton.addActionListener(finalAction);
            }

            buttonsPanel.add(yesButton);
            buttonsPanel.add(noButton);

        }
        else{
            buttonsPanel = createGridPanel(1,1,20,0,0);
            JButton okButton = createButton("Ok", event->dispose(), true);
            if(finalAction!=null) {
                okButton.addActionListener(finalAction);
            }
            buttonsPanel.add(okButton);
        }

        mainPanel.add(messageLabel);
        mainPanel.add(buttonsPanel);
        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {}
}
