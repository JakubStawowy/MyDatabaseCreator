package GUI;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.util.List;

/*
* StartingWindow
*
* @extends MyDialog
*
* */
public class StartingWindow extends MyDialog {

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

        JPanel mainPanel = createGridPanel(3,1,0,20,20);

//        ---------------------------------------importButton-----------------------------------------------------------

        JButton importButton = createButton("Import database", event->new ConnectWindow(this), true);

//        ---------------------------------------skipButton-------------------------------------------------------------

        JButton skipButton = createButton("Skip", event->dispose(),false);

//        ---------------------------------------closeButton------------------------------------------------------------

        JButton closeButton = createButton("Close", event->dispose(),true);

        mainPanel.add(importButton);
        mainPanel.add(skipButton);
        mainPanel.add(closeButton);

        add(mainPanel);
    }

    @Override
    public void displayTable(List<List<Object>> data) {

    }
}
