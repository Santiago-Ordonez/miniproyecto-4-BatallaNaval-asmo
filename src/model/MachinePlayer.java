package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MachinePlayer extends Player{
    private final Random random = new Random();

    public MachinePlayer(){
        super("Machine");
        placeShips();
    }

    @Override
    public boolean placeShips(){
        for(Ship ship : getShips()){
            boolean placed = false;
            while(!placed) {
                List<int[]> validPositions = board.getValidShipsPositions();

                int[] position = validPositions.get(random.nextInt(validPositions.size()));
                Orientation orientation = random.nextBoolean() ? Orientation.HORIZONTAL : Orientation.VERTICAL;

                ship.setPosition(position[0], position[1], orientation);
                placed = board.placeShip(ship);
            }
        }
        return true;
    }

    public int[] makeRandomAttack(Board opponentBoard){
        if(opponentBoard == null) return null;

        List<int[]> adjacentShots = new ArrayList<>();
        int[][] matrix = opponentBoard.getMatrix();
        List<int[]> possibleShots = opponentBoard.getValidHitPositions();

        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(matrix[i][j] == 3){
                    adjacentShots = addAdjacentValidShots(possibleShots, i, j, matrix);
                }
            }
        }

        if(!adjacentShots.isEmpty()) return adjacentShots.get(random.nextInt(adjacentShots.size()));

        return possibleShots.get(random.nextInt(possibleShots.size()));
    }

    private List<int[]> addAdjacentValidShots(List<int[]> shots, int row, int col, int[][] matrix){
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        List<int[]> adjacentShots = new ArrayList<>();
        for(int[] d : dirs){
            int r = row + d[0];
            int c = col + d[1];
            if(r >= 0 && r < 10 && c >= 0 && c<10 && (matrix[r][c] == 0 || matrix[r][c] == 1)){
                adjacentShots.add(new int[]{r, c});
            }
        }

        return adjacentShots;
    }
}
