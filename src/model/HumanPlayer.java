package model;

public class HumanPlayer extends Player{
    public HumanPlayer(String name){super(name);}

    public boolean placeShip(int ship, int startRow, int startCol, Orientation orientation){
        ships.get(ship).setPosition(startRow, startCol, orientation);
        return board.placeShip(ships.get(ship));
    }

    public int[] attack(int row, int col, Board opponentBoard){
        if(opponentBoard == null) return new int[]{0, 0};

        for(int[] pos : opponentBoard.getValidHitPositions()){
            if(pos[0] == row && pos[1] == col) return new int[]{row, col};
        }

        return new int[]{0, 0};
    }
}
