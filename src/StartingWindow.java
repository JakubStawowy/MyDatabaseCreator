/*
* StartingWindow class
* */
public class StartingWindow extends MyDialog {

    public StartingWindow(){

        setLayout(null);
        setSize(400,300);
        initWindow();
        setVisible(true);
    }
    @Override
    public void initWindow() {

        addButton(100, 60, 200, 40, "Import database", event->new ConnectWindow(this));
        //addButton(100, 120, 200, 40, "Skip", event->{new View(); dispose();});
        addButton(100, 180, 200, 40, "Close", event->{dispose(); System.exit(0);});

    }
    public static void main(String[] args) {

        new StartingWindow();

    }
}
