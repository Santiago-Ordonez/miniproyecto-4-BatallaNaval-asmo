package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 10x10 game board.
 * Handles ship placement and attack registration.
 */
public class Board implements IBoard, Serializable {
    private static final long serialVersionUID = 1L;
    private static final int SIZE = 10;
    private int[][] matrix;
    private List<Ship> ships;

    public Board(){
        matrix = new int[SIZE][SIZE];
        ships = new ArrayList<>();
    }

    /**
     * Returns a copy of the board matrix.
     * @return 10x10 matrix copy
     */
    @Override
    public int[][] getMatrix(){
        int[][] copy = new int[SIZE][SIZE];
        for(int i=0; i < SIZE; i++){
            System.arraycopy(matrix[i], 0, copy[i],0, SIZE);
        }
        return copy;
    }

    /**
     * Places a ship on the board if possible.
     * @param ship ship to place
     * @return true if placed successfully
     */
    @Override
    public boolean placeShip(Ship ship){
        if(ship == null || !canPlaceShip(ship)) return false;

        ships.add(ship);
        for(int[] pos : ship.getPositions()){
            matrix[pos[0]][pos[1]] = 1;
        }

        return true;
    }

    private boolean canPlaceShip(Ship ship){
        for(int[] pos : ship.getPositions()){
            int r = pos[0];
            int c = pos[1];
            if(!isValidPosition(r, c) || matrix[r][c] != 0) return false;
        }
        return true;
    }

    private boolean isValidPosition(int row, int col){
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    /**
     * Performs an attack on a position.
     * @param row row
     * @param col column
     * @param ship affected ship (null for miss)
     * @return attack result
     */
    @Override
    public AttackResult attack(int row, int col, Ship ship){
        if(!isValidPosition(row, col)) return null;
        if(matrix[row][col] > 1) return null;

        if(ship != null){
            ship.registerHit(row, col);
            matrix[row][col] = 3;

            if(ship.isSunk()){
                for(int[] pos : ship.getPositions()){
                    matrix[pos[0]][pos[1]] = 4;
                }
                return AttackResult.SUNK;
            }

            return AttackResult.HIT;
        }else{
            matrix[row][col] = 2;
            return AttackResult.MISS;
        }
    }


    @Override
    public List<int[]> getValidHitPositions(){
        List<int[]> valid = new ArrayList<>();
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(matrix[i][j] < 2) valid.add(new int[]{i, j});
            }
        }

        return valid;
    }

    @Override
    public List<int[]> getValidShipsPositions(){
        List<int[]> valid = new ArrayList<>();
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(matrix[i][j] == 0) valid.add(new int[]{i, j});
            }
        }

        return valid;
    }

    public Ship getShipAt(int row, int col) {
        for (Ship ship : ships) {
            if (ship.contains(row, col)) return ship;
        }
        return null;
    }

    public int getShipPartAt(int row, int col) {
        Ship ship = getShipAt(row, col);
        if (ship == null) return -1;
        List<int[]> positions = ship.getPositions();
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i)[0] == row && positions.get(i)[1] == col) {
                return i;
            }
        }
        return -1;
    }

    public int getShipTypeAt(int row, int col) {
        Ship ship = getShipAt(row, col);
        return ship != null ? ship.getSize() : 0;
    }

    public Orientation getShipOrientationAt(int row, int col) {
        Ship ship = getShipAt(row, col);
        return ship != null ? ship.getOrientation() : Orientation.HORIZONTAL;
    }
}
