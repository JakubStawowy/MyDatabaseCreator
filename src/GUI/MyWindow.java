package GUI;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;

/*
* MyWindow Interface
* */
public interface MyWindow {

    void initWindow();

    void displayTable(List<List<Object>> data);

    void setTextField(JTextField textField, String textFieldText);

    JButton createButton(String text, ActionListener actionListener, Boolean buttonEnable);

    JTextField createTextField(String text);

    JLabel createLabel(String text);

    JPanel createGridPanel(int rows, int cols, int hgap, int vgap, int margin);
}
