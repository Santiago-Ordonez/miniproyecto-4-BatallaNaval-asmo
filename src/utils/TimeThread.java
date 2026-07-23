package utils;

/**
 * Thread responsible for managing the game timer.
 * Updates a Label with the elapsed time in MM:SS format.
 */
import javafx.application.Platform;
import javafx.scene.control.Label;

public class TimeThread extends Thread{
    private int seconds;
    private Label timeLabel;
    
    /**
     * Creates a new timer thread.
     * @param timeLabel Label to update with time
     * @param initialSeconds starting seconds (for loaded games)
     */
    public TimeThread(Label timeLabel, int seconds){
        this.timeLabel = timeLabel;
        this.seconds = seconds;
    }
    
    /**
     * Main thread loop that increments time every second.
     */   
    @Override
    public void run(){
        System.out.println("Timer thread started...");
        while(!isInterrupted()){
            try{
                Thread.sleep(1000);
                seconds++;

                Platform.runLater(() -> {
                    int min = seconds / 60;
                    int sec = seconds % 60;
                    timeLabel.setText(String.format("%02d:%02d", min, sec));
                });
            }catch(InterruptedException e){
                System.out.println("Timer interrupted");
                break;
            }
        }
    }
    
    /**
     * Returns the current elapsed seconds.
     * @return seconds played
     */
    public int getSeconds(){return seconds;}
}
