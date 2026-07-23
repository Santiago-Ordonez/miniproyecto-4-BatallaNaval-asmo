package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for players (human and machine).
 */
public class Player implements IPlayer, Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected Board board;
    protected List<Ship> ships;

    protected Player(String name){
        this.name = name;
        this.board = new Board();
        this.ships = new ArrayList<>();
        initializeDefaultShips();
    }

    private void initializeDefaultShips(){
        ships.add(new Ship(4));

        ships.add(new Ship(3));
        ships.add(new Ship(3));

        ships.add(new Ship(2));
        ships.add(new Ship(2));
        ships.add(new Ship(2));

        ships.add(new Ship(1));
        ships.add(new Ship(1));
        ships.add(new Ship(1));
        ships.add(new Ship(1));
    }

    @Override
    public String getName(){return name;}

    @Override
    public Board getBoard(){return board;}

    @Override
    public List<Ship> getShips(){return new ArrayList<>(ships);}

    @Override
    public int getNoSunkShipsCount(){return 10 - getSunkShipsCount();}

    public int getSunkShipsCount(){
        int count = 0;
        for(Ship ship : ships){
            if(ship.isSunk()) count++;
        }
        return count;
    }

    /**
     * Checks if the player has been defeated.
     * @return true if all ships are sunk
     */
    @Override
    public boolean isDefeated(){return getSunkShipsCount() == ships.size();}

    @Override
    public boolean placeShips(){
        return false;
    }

    /**
     * Receives an attack on the specified position.
     * @param row row
     * @param col column
     * @return attack result
     */
    @Override
    public AttackResult receiveAttack(int row, int col){
        Ship hitShip = null;

        for(Ship ship : ships){
            if(ship.contains(row, col)){
                hitShip = ship;
                break;
            }
        }

        return board.attack(row, col, hitShip);
    }
}
