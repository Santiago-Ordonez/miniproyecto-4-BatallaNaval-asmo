package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single ship in the game.
 * Tracks its position, orientation, hits and status.
 */
public class Ship implements IShip, Serializable {
    private static final long serialVersionUID = 1L;
    private final int size;
    private Orientation orientation;
    private List<int[]> positions;
    private List<int[]> hits;

    /**
     * Creates a new ship.
     * @param size ship size (1-4)
     * @param orientation HORIZONTAL or VERTICAL
     * @param startRow starting row
     * @param startCol starting column
     */
    public Ship(int size, Orientation orientation, int startRow, int startCol){
        this.size = size;
        this.orientation = orientation;
        hits = new ArrayList<>();
        positions = calculatePositions(startRow, startCol);
    }

    public Ship(int size){
        this(size, Orientation.HORIZONTAL, 0, 0);
    }

    private List<int[]> calculatePositions(int startRow, int startCol){
        List<int[]> pos = new ArrayList<>();

        for(int i=0; i < size; i++){
            if(orientation == Orientation.HORIZONTAL){pos.add(new int[]{startRow, startCol + i});}
            else{pos.add(new int[]{startRow + i, startCol});}
        }

        return pos;
    }

    @Override
    public int getSize(){return size;}

    @Override
    public List<int[]> getPositions() {
        return new ArrayList<>(positions);
    }

    @Override
    public boolean contains(int row, int col){
        for(int[] pos : positions){
            if(pos[0] == row && pos[1] == col) return true;
        }
        return false;
    }

    /**
     * Registers a hit on the ship.
     * @param row hit row
     * @param col hit column
     */
    @Override
    public void registerHit(int row, int col){
        if(contains(row, col) && !isHitAt(row, col)) hits.add(new int[]{row, col});
    }

    private boolean isHitAt(int row, int col){
        for(int[] hit : hits){
            if (hit[0] == row && hit[1] == col) return true;
        }
        return false;
    }

    /**
     * Checks if the ship is completely sunk.
     * @return true if all parts are hit
     */
    @Override
    public boolean isSunk(){
        return hits.size() == size;
    }

    @Override
    public int getHitsCount(){
        return hits.size();
    }

    @Override
    public Orientation getOrientation(){return orientation;}

    @Override
    public void setPosition(int startRow, int startCol, Orientation orientation){
        this.orientation = orientation;
        positions = calculatePositions(startRow, startCol);
    }
}
