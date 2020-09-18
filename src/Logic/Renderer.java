package Logic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Renderer implements ListCellRenderer<Component>{

    @Override
    public Component getListCellRendererComponent(JList<? extends Component> list, Component value, int index, boolean isSelected, boolean cellHasFocus) {

            if (isSelected) {
                value.setBackground(list.getSelectionBackground());
                value.setForeground(list.getSelectionForeground());
            } else {
                value.setBackground(list.getBackground());
                value.setForeground(list.getForeground());
            }
            return value;
    }
    public static void main(String[] args){
        JFrame f = new JFrame();
        JPanel p = new JPanel(new GridLayout(1,1));
        Vector<JCheckBox> jc = new Vector<>();
        jc.add(new JCheckBox("Hallo"));
        JComboBox<JCheckBox> c = new JComboBox<>(jc);
        c.addActionListener(event-> {
                if(c.getSelectedItem() instanceof JCheckBox){
                    JCheckBox jcb = (JCheckBox)c.getSelectedItem();
                    jcb.setSelected(!jcb.isSelected());
                    System.out.println("tutej "+((JCheckBox) c.getSelectedItem()).getText());
                }
        });

        c.setRenderer(new Renderer());
//        c.addItem(new JCheckBox("hallo"));
        p.add(c);
        f.add(p);
        f.setVisible(true);
    }
}
