import javax.swing.*;
import java.awt.event.ActionListener;

/*
* MyWindow Interface
* */
public interface MyWindow {

    void initWindow();

    JButton addButton(int x, int y, int width, int height, String text, ActionListener actionListener, Boolean buttonEnable);

    JTextField addTextField(int x, int y, int width, int height, String text);

    JLabel addLabel(int x, int y, int width, int height, String text);
}
