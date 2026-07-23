package model;

import utils.SaveManager;

import java.io.Serializable;

/**
 * Main class that represents the complete state of a Battleship game.
 * Manages both players, current turn and game status.
 */
public class Game implements Serializable {
    private static final long serialVersionUID = 1L;
    private HumanPlayer player;
    private MachinePlayer machine;
    private boolean isHumanTurn;
    private boolean gameOver;

    /**
     * Creates a new game with the human player name.
     * @param playerName human player's name
     */
    public Game(String playerName){
        player = new HumanPlayer(playerName);
        machine = new MachinePlayer();
    }

    /**
     * Starts or restarts the game.
     */
    public void startNewGame(){
        isHumanTurn = true;
        gameOver = false;
    }

    public HumanPlayer getHumanPlayer(){return player;}

    public MachinePlayer getMachinePlayer(){return machine;}

    public boolean isHumanTurn(){return isHumanTurn;}

    /**
     * Performs a human player attack.
     * @param row row coordinate
     * @param col column coordinate
     * @return attack result
     */
    public AttackResult humanAttack(int row, int col){
        if(!isHumanTurn || gameOver) return null;

        AttackResult result = machine.receiveAttack(row, col);

        if(result == AttackResult.MISS){
            isHumanTurn = false;
        }
        checkGameOver();

        return result;
    }

    /**
     * Performs a machine attack.
     * @return attack result
     */
    public AttackResult machineAttack(){
        if(isHumanTurn || gameOver) return null;

        int[] attackPos = machine.makeRandomAttack(player.getBoard());
        AttackResult result = player.receiveAttack(attackPos[0], attackPos[1]);

        if(result == AttackResult.MISS){
            isHumanTurn = true;
        }
        checkGameOver();

        return result;
    }

    /**
     * Returns the winner of the game.
     * @return "human" or "machine"
     */
    public String getWinner(){
        if(!gameOver) return null;

        return player.isDefeated() ? "machine" : "human";
    }

    private void checkGameOver(){
        if(player.isDefeated()){gameOver = true;}
        else if(machine.isDefeated()){gameOver = true;}
    }

    /**
     * Checks if the game has ended.
     * @return true if game is over
     */
    public boolean isGameOver(){return gameOver;}
}
