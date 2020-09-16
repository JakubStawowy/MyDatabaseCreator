package Logic;

import GUI.ConnectWindow;
import GUI.StartingWindow;

import java.util.LinkedList;
import java.util.List;

public class Run implements Runnable {
    public Run(){
        run();
    }
    @Override
    public void run() {
        new StartingWindow();
    }
    public static void main(String[] args){
        new Run();
    }
}
