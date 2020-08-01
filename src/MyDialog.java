import javax.swing.*;
import java.awt.event.ActionListener;

/*
* Abstract Class MyDialog
* */
abstract class MyDialog extends JDialog implements MyWindow{

    /*
    * Abstract method initWindow is responsible for adding all components to parent class window
    * It requires an override.
    * */
    @Override
    public abstract void initWindow();

    /*
    * addButton method allows to add new button to subclass window.
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
    public void addButton(int x, int y, int width, int height, String text, ActionListener actionListener){

        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setBounds(x,y,width,height);
        add(button);
    }

    /*
    * addTextField method allows to add and return new text field to subclass window.
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
    public JTextField addTextField(int x, int y, int width, int height) {

        JTextField textField = new JTextField();
        textField.setBounds(x,y,width,height);
        add(textField);

        return textField;
    }

    /*
    * addLabel method allows to add new label to subclass window.
    * It doesn't require an override.
    *
    * @param x - x axis coordinate
    * @param y - y axis coordinate
    * @param width - label width
    * @param height - label height
    * @param text - text displayed on the button
    * */
    @Override
    public void addLabel(int x, int y, int width, int height, String text) {
        JLabel label = new JLabel(text);
        label.setBounds(x,y,width,height);
        add(label);
    }
}

