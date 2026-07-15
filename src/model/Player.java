package model;

import java.util.ArrayList;
import java.util.List;

public class Player implements IPlayer{
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

    private int getSunkShipsCount(){
        int count = 0;
        for(Ship ship : ships){
            if(ship.isSunk()) count++;
        }
        return count;
    }

    @Override
    public boolean isDefeated(){return getSunkShipsCount() == ships.size();}

    @Override
    public boolean placeShips(){
        return false;
    }

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
