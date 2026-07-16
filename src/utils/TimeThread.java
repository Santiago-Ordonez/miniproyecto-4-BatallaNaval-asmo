package utils;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class TimeThread extends Thread{
    private int seconds = 0;
    private Label timeLabel;

    public TimeThread(Label timeLabel){this.timeLabel = timeLabel;}

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
}
