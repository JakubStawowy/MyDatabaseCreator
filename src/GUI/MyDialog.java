package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/*
* Abstract Class GUI.MyDialog
* */
abstract class MyDialog extends JDialog implements MyWindow{

    /*
    * Abstract method initWindow is responsible for adding all components to parent class window
    * It requires an override.
    * */
    @Override
    public abstract void initWindow();

    @Override
    public abstract void displayTable(List<List<Object>> data);

    /*
    * createButton method allows to add new button to subclass window.
    * It doesn't require an override.
    *
    * @param x - x axis coordinate
    * @param y - y axis coordinate
    * @param width - button width
    * @param height - button height
    * @param text - text displayed on the button
    * @param actionListener - action executed after clicking on the button
    * */
    @Override
    public JButton createButton(String text, ActionListener actionListener, Boolean buttonEnable){
        Color buttonColor = new Color(105,105,105);
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setEnabled(buttonEnable);
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);

        return button;
    }

    /*
    * createTextField method allows to add and return new text field to subclass window.
    * Returning JTextField object is necessary to get text from text field.
    * It doesn't require an override.
    *
    * @param x - x axis coordinate
    * @param y - y axis coordinate
    * @param width - text field width
    * @param height - text field height
    * @return textField
    * */
    @Override
    public JTextField createTextField(String text) {
        Color textFieldColor = new Color(105,105,105);
        JTextField textField = new JTextField(text);
        textField.setBackground(textFieldColor);
        textField.setForeground(Color.WHITE);
        setTextField(textField, text);

        return textField;
    }

    /*
    * createLabel method allows to add new label to subclass window.
    * It doesn't require an override.
    *
    * @param x - x axis coordinate
    * @param y - y axis coordinate
    * @param width - label width
    * @param height - label height
    * @param text - text displayed on the button
    * */
    @Override
    public JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }
    @Override
    public JPanel createGridPanel(int rows, int cols, int hgap, int vgap, int margin){
        JPanel panel = new JPanel(new GridLayout(rows,cols,hgap,vgap));
        panel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        panel.setBackground(new Color(67,67,67));
        panel.setForeground(Color.WHITE);
        return panel;
    }
    @Override
    public void setTextField(JTextField textField, String textFieldText){
        textField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(textField.getText().equals(textFieldText))
                    textField.setText("");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {

                if(textField.getText().equals(""))
                    textField.setText(textFieldText);
            }
        });
    }
}

