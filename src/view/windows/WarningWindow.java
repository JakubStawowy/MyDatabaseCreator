package view.windows;

import view.components.MdcFrame;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.util.List;

/*
* WarningWindow
*
* @extends MyDialog
*
* This window is responsible for displaying warnings and executing action passed as constructor parameters.
* */
public class WarningWindow extends MdcFrame {

    private ActionListener action;
    private ActionListener finalAction;
    private String text;

    public WarningWindow(String text, ActionListener action, ActionListener finalAction){

        this.text = text;
        this.action = action;
        this.finalAction = finalAction;
        final String title = "Warning";

        setTitle(title);
        setLocationRelativeTo(null);
        createWidgets();
        pack();
        setVisible(true);
    }
    @Override
    public void createWidgets() {

//        ---------------------------------------mainPanel--------------------------------------------------------------

        JPanel mainPanel = createGridPanel(2,1,0,20,20);

//        ---------------------------------------buttonsPanel-----------------------------------------------------------

        JPanel buttonsPanel;

//        ---------------------------------------messageLabel-----------------------------------------------------------

        JLabel messageLabel = createLabel(text);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);

        if(action!=null) {

//        ---------------------------------------buttonsPanel-----------------------------------------------------------

            buttonsPanel = createGridPanel(1,2,20,0,0);

//        ---------------------------------------yesButton--------------------------------------------------------------

            JButton yesButton = createButton("Yes",action,true);
            yesButton.addActionListener(event -> dispose());
            yesButton.addActionListener(finalAction);

//        ---------------------------------------noButton---------------------------------------------------------------

            JButton noButton = createButton("No", event->dispose(),true);

            if(finalAction!=null) {
                noButton.addActionListener(finalAction);
            }

            buttonsPanel.add(yesButton);
            buttonsPanel.add(noButton);

        }
        else{

//        ---------------------------------------buttonsPanel-----------------------------------------------------------

            buttonsPanel = createGridPanel(1,1,20,0,0);

//        ---------------------------------------okButton---------------------------------------------------------------

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
