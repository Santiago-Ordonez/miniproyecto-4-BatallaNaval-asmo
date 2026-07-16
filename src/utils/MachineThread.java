package utils;

import model.Game;
import model.AttackResult;
import javafx.application.Platform;

public class MachineThread {
    public static void executeMachineTurn(Game game, Runnable onComplete){
        new Thread(() -> {
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> {
                AttackResult result = game.machineAttack();
                onComplete.run();
            });
        }).start();
    }
}
