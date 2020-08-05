import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
* StartingWindow class
* */
public class StartingWindow extends MyDialog {

    public StartingWindow(){

        setLayout(null);
        setSize(400,300);
        setTitle("MyDatabaseCreator");
        initWindow();
        setVisible(true);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    @Override
    public void initWindow() {

        addButton(100, 60, 200, 40, "Import database", event->new ConnectWindow(this), true);
        //addButton(100, 120, 200, 40, "Skip", event->{ });
        addButton(100, 180, 200, 40, "Close", event->{dispose(); System.exit(0);},true);

    }
    public static void main(String[] args) {

        new StartingWindow();

    }
}
