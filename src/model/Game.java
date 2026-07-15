package model;

public class Game {
    private HumanPlayer player;
    private MachinePlayer machine;
    private boolean isHumanTurn;
    private boolean gameOver;

    public Game(String playerName){
        player = new HumanPlayer(playerName);
        machine = new MachinePlayer();
    }

    public void startNewGame(){
        isHumanTurn = true;
        gameOver = false;
    }

    public HumanPlayer getHumanPlayer(){return player;}

    public MachinePlayer getMachinePlayer(){return machine;}

    public boolean isHumanTurn(){return isHumanTurn;}

    public AttackResult humanAttack(int row, int col){
        if(!isHumanTurn || gameOver) return null;

        AttackResult result = machine.receiveAttack(row, col);

        if(result != null){
            isHumanTurn = false;
            checkGameOver();
        }

        return result;
    }

    public AttackResult machineAttack(){
        if(isHumanTurn || gameOver) return null;

        int[] attackPos = machine.makeRandomAttack(player.getBoard());
        AttackResult result = player.receiveAttack(attackPos[0], attackPos[1]);

        isHumanTurn = true;
        checkGameOver();

        return result;
    }

    String getWinner(){
        if(!gameOver) return null;

        return player.isDefeated() ? "machine" : "human";
    }

    private void checkGameOver(){
        if(player.isDefeated()){gameOver = true;}
        else if(machine.isDefeated()){gameOver = true;}
    }
}
