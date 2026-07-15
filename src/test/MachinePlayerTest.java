package test;

import model.*;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class MachinePlayerTest {
    @Test
    void placeShipsTest() {
        MachinePlayer machine = new MachinePlayer();

        assertTrue(machine.placeShips());
    }

    @Test
    void makeRandomAttackTest() {
        HumanPlayer player = new HumanPlayer("Alan");
        Ship ship = new Ship(4);
        player.getBoard().placeShip(ship);
        MachinePlayer machine = new MachinePlayer();

        int[] position = machine.makeRandomAttack(player.getBoard());


        assertNotNull(player.receiveAttack(position[0], position[1]));
    }
}