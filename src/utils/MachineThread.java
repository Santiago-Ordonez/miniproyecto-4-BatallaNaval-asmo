package utils;

import model.Game;
import javafx.application.Platform;

/**
 * Utility class that executes the machine's turn in a separate thread
 * to prevent freezing the JavaFX UI.
 */
public class MachineThread {
    /**
     * Executes the machine's attack asynchronously.
     * @param game current game instance
     * @param onComplete callback executed after the machine's turn
     */
    public static void executeMachineTurn(Game game, Runnable onComplete){
        new Thread(() -> {
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> {
                game.machineAttack();
                onComplete.run();
            });
        }).start();
    }
}
