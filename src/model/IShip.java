package model;

import java.util.List;

public interface IShip {
    int getSize();
    Orientation getOrientation();
    List<int[]> getPositions();
    boolean isSunk();
    void registerHit(int row, int col);
    boolean contains(int row, int col);
    int getHitsCount();
    void setPosition(int startRow, int startCol, Orientation orientation);
}
