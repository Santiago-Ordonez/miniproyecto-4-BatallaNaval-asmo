package test;

import model.AttackResult;
import model.HumanPlayer;
import model.MachinePlayer;
import model.Orientation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HumanPlayerTest {
    @Test
    void placeShipTest(){
        HumanPlayer player = new HumanPlayer("Alan");

        assertTrue(player.placeShip(0, 0, 0, Orientation.HORIZONTAL));
        assertFalse(player.placeShip(1, 0, 0, Orientation.HORIZONTAL));
    }

    @Test
    void attackTest(){
        HumanPlayer player = new HumanPlayer("Alan");
        MachinePlayer machine = new MachinePlayer();

        List<int[]> positions = machine.getShips().get(0).getPositions();
        int[] positionAttack = positions.get(0);

        assertEquals(AttackResult.HIT, machine.receiveAttack(positionAttack[0], positionAttack[1]));
    }

}