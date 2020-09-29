package GUI;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.util.List;

/*
* MyWindow Interface
* */
public interface MyWindow {

    /*
     * createWidgets method is responsible for adding all components to parent class window
     * */
    void createWidgets();

    /*
    * displayTable method allows to display table (JTable) on a subclass window
    * */
    void displayTable(List<List<Object>> data);

    /*
    * setTextField method sets the textField in a subclass window
    *
    * @param JTextField textField
    * @param String textFieldText
    * */
    void setTextField(JTextField textField, String textFieldText);

    /*
     * createButton method allows to add new button to subclass window.
     * It doesn't require an override.
     *
     * @param String text - text displayed on the button
     * @param ActionListener actionListener - action executed after clicking on the button
     * @param Boolean buttonEnable
     *
     * @return JButton
     * */
    JButton createButton(String text, ActionListener actionListener, Boolean buttonEnable);

    /*
     * createTextField method allows to add and return new text field to subclass window.
     * Returning JTextField object is necessary to get text from text field.
     * It doesn't require an override.
     *
     * @param String text
     *
     * @return JTextField textField
     * */
    JTextField createTextField(String text);

    /*
     * createLabel method allows to add new label to subclass window.
     * It doesn't require an override.
     *
     * @param String text - text displayed on the button
     *
     * @return JLabel
     * */
    JLabel createLabel(String text);

    /*
    * createGridPanel method allows to add new Panel with GridLayout and empty margin to subclass window.
    *
    * @param int rows
    * @param int cols
    * @param int hgap
    * @param int vgap
    * @param int margin
    * */
    JPanel createGridPanel(int rows, int cols, int hgap, int vgap, int margin);
}
