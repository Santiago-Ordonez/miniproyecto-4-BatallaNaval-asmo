package model;

import java.util.List;

public interface IPlayer {
    String getName();
    Board getBoard();
    List<Ship> getShips();
    boolean isDefeated();
    boolean placeShips();
    AttackResult receiveAttack(int row, int col);
}
