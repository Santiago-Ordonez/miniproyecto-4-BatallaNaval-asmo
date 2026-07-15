package test;

import model.AttackResult;
import model.Board;
import model.Orientation;
import model.Ship;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {
    @Test
    void testShipCreationAndPositions(){
        Board board = new Board();
        Ship ship = new Ship(4);
        Ship ship2 = new Ship(4, Orientation.VERTICAL, 2, 3);
        ship.setPosition(3, 3, Orientation.HORIZONTAL);

        List<int[]> positions = ship.getPositions();

        for(int[] pos : positions){
            System.out.println("["+pos[0]+","+pos[1]+"]");
        }

        assertTrue(board.placeShip(ship));
        assertFalse(board.placeShip(ship2));
    }
}