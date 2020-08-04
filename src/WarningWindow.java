import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/*
* WarningWindow class is responsible for displaying warnings and executing action passed as constructor parameters.
* */
public class WarningWindow extends MyDialog{

    private ActionListener action;
    private String text;

    public WarningWindow(String text, ActionListener action){

        this.text = text;
        this.action = action;

        setSize(340,120);
        setTitle("Warning");

        initWindow();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void initWindow() {

        JPanel mainPanel = new JPanel(new GridLayout(2,1));
        JPanel subPanel = new JPanel(null);

        JButton yesButton = new JButton("Yes");
        yesButton.setBounds(50,0,80,20);

        yesButton.addActionListener(action);
        yesButton.addActionListener(event->dispose());

        JButton noButton = new JButton("No");
        noButton.setBounds(190,0,80,20);
        noButton.addActionListener(event->dispose());

        subPanel.add(yesButton);
        subPanel.add(noButton);

        mainPanel.add(new JLabel(text,SwingConstants.CENTER));
        mainPanel.add(subPanel);
        add(mainPanel);

    }
}
