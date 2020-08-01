import javax.swing.*;
import java.awt.event.ActionListener;

/*
* MyWindow Interface
* */
public interface MyWindow {

    void initWindow();

    void addButton(int x, int y, int width, int height, String text, ActionListener actionListener);

    JTextField addTextField(int x, int y, int width, int height);

    void addLabel(int x, int y, int width, int height, String text);
}
