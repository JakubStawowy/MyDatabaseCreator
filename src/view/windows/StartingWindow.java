package view.windows;

import view.components.MdcFrame;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.util.List;

/*
* StartingWindow
*
* @extends MyDialog
*
* */
public class StartingWindow extends MdcFrame {

    public StartingWindow(){

        final String title = "MyDatabaseCreator";
        final int width = 400;
        final int height = 300;

        setSize(width, height);
        setTitle(title);
        createWidgets();
        setVisible(true);
        setLocationRelativeTo(null);

    }
    @Override
    public void createWidgets() {

//        ---------------------------------------mainPanel--------------------------------------------------------------

        JPanel mainPanel = createGridPanel(2,1,0,20,20);

//        ---------------------------------------importButton-----------------------------------------------------------

        JButton importButton = createButton("Import database", event->new ConnectWindow(this), true);


//        ---------------------------------------closeButton------------------------------------------------------------

        JButton closeButton = createButton("Close", event->dispose(),true);

        mainPanel.add(importButton);
        mainPanel.add(closeButton);

        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {

    }
}
