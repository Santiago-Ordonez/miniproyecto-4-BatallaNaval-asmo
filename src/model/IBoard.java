package model;

import java.util.List;

public interface IBoard {
    boolean placeShip(Ship ship);
    AttackResult attack(int row, int col, Ship ship);
    int[][] getMatrix();
    List<int[]> getValidHitPositions();
    List<int[]> getValidShipsPositions();
}
