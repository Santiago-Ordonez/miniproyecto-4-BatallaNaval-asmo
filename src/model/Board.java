package model;

import java.util.ArrayList;
import java.util.List;

public class Board implements IBoard{
    private static final int SIZE = 10;
    private int[][] matrix;
    private List<Ship> ships;

    public Board(){
        matrix = new int[SIZE][SIZE];
        ships = new ArrayList<>();
    }

    @Override
    public int[][] getMatrix(){
        int[][] copy = new int[SIZE][SIZE];
        for(int i=0; i < SIZE; i++){
            System.arraycopy(matrix[i], 0, copy[i],0, SIZE);
        }
        return copy;
    }

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

    @Override
    public AttackResult attack(int row, int col, Ship ship){
        if(!isValidPosition(row, col)) return null;

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
}
