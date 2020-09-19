package Logic.MyExceptions;

import javax.swing.*;

public class TwoCheckBoxesSelectedException extends MyException {
    public TwoCheckBoxesSelectedException(JCheckBox checkBox1, JCheckBox checkBox2){
        message = "Checkboxes "+checkBox1.getText()+" and "+checkBox2.getText()+" cannot be both selected at the same time";
    }
    @Override
    public String getMessage() {
        return message;
    }
}
