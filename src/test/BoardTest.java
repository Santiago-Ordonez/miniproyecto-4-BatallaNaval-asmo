package test;

import model.AttackResult;
import model.Board;
import model.Orientation;
import model.Ship;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void attackTest(){
        Board board = new Board();
        Ship ship = new Ship(4, Orientation.VERTICAL, 0, 0);
        board.placeShip(ship);

    }
}