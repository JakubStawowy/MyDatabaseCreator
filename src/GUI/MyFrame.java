package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

abstract class MyFrame extends JFrame implements MyWindow {
    @Override
    public abstract void initWindow();

    @Override
    public abstract void displayTable(List<List<Object>> data);

    @Override
    public void setTextField(JTextField textField, String textFieldText) {

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

    @Override
    public JButton createButton(String text, ActionListener actionListener, Boolean buttonEnable) {

        Color buttonColor = new Color(105,105,105);
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setEnabled(buttonEnable);
        button.setBackground(buttonColor);
        button.setForeground(Color.WHITE);

        return button;
    }

    @Override
    public JTextField createTextField(String text) {

        Color textFieldColor = new Color(105,105,105);
        JTextField textField = new JTextField(text);
        textField.setBackground(textFieldColor);
        textField.setForeground(Color.WHITE);
        setTextField(textField, text);

        return textField;
    }

    @Override
    public JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    @Override
    public JPanel createGridPanel(int rows, int cols, int hgap, int vgap, int margin) {
        JPanel panel = new JPanel(new GridLayout(rows,cols,hgap,vgap));
        panel.setBorder(BorderFactory.createEmptyBorder(margin, margin, margin, margin));
        panel.setBackground(new Color(67,67,67));
        panel.setForeground(Color.WHITE);
        return panel;
    }
}