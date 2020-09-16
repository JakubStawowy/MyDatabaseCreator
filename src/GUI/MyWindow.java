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

    JButton addButton(int x, int y, int width, int height, String text, ActionListener actionListener, Boolean buttonEnable, JPanel panel);

    JTextField addTextField(int x, int y, int width, int height, String text, JPanel panel);

    JLabel addLabel(int x, int y, int width, int height, String text, JPanel panel);
}
