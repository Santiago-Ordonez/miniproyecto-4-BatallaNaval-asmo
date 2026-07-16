package utils;

import model.Game;
import model.Ship;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SaveManager {

    private static final String SAVE_DIR = "saves";
    private static final String SAVE_FILE = SAVE_DIR + File.separator + "partida.sav";
    private static final String INFO_FILE = SAVE_DIR + File.separator + "partida.txt";

    private SaveManager() {}

    public static boolean hasSavedGame() {
        return new File(SAVE_FILE).exists() && new File(INFO_FILE).exists();
    }

    public static boolean saveGame(Game game) {
        if (game == null) return false;

        try {
            Files.createDirectories(Path.of(SAVE_DIR));

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
                out.writeObject(game);
            }

            writePlainInfo(game);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Game loadGame() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return null;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Game) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readSummary() {
        File file = new File(INFO_FILE);
        if (!file.exists()) return "No hay partida guardada.";

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "No se pudo leer la información de la partida.";
        }
        return sb.toString().trim();
    }

    private static void writePlainInfo(Game game) throws IOException {
        String playerName = game.getHumanPlayer().getName();
        int sunkHuman = countSunkShips(game.getHumanPlayer().getShips());
        int sunkMachine = countSunkShips(game.getMachinePlayer().getShips());
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INFO_FILE))) {
            writer.write("jugador=" + playerName);
            writer.newLine();
            writer.write("barcosHundidosJugador=" + sunkHuman);
            writer.newLine();
            writer.write("barcosHundidosMaquina=" + sunkMachine);
            writer.newLine();
            writer.write("turnoHumano=" + game.isHumanTurn());
            writer.newLine();
            writer.write("juegoTerminado=" + game.isGameOver());
            writer.newLine();
            writer.write("guardadoEn=" + timestamp);
            writer.newLine();
        }
    }

    private static int countSunkShips(List<Ship> ships) {
        int count = 0;
        for (Ship ship : ships) {
            if (ship.isSunk()) count++;
        }
        return count;
    }
}
