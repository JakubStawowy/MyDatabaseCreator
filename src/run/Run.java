package run;

import view.windows.StartingWindow;


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
