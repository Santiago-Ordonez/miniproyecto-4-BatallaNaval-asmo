package utils;

import model.Game;
import model.Ship;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages saving and loading of games using serialization and text files.
 */
public class SaveManager {
    private static final String SAVE_DIR = "saves";

    private SaveManager(){}

    /**
     * Checks if there is at least one saved game.
     * @return true if saved games exist
     */
    public static boolean hasSavedGame(){
        File dir = new File(SAVE_DIR);

        if(!dir.exists()) return false;

        File[] files = dir.listFiles((d, name) -> name.endsWith(".sav"));
        return files != null && files.length > 0;
    }

    /**
     * Saves the complete game state.
     * @param game game to save
     * @param secondsPlayed elapsed seconds
     * @return true if saved successfully
     */
    public static void saveGame(Game game, int secondsPlayed){
        if(game==null) return;

        String saveName = game.getHumanPlayer().getName();

        try {
            Files.createDirectories(Path.of(SAVE_DIR));
            String savePath = SAVE_DIR + File.separator + saveName + ".sav";

            try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(savePath))){
                out.writeObject(game);
            }

            writePlainInfo(game, saveName, secondsPlayed);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Loads a game by name.
     * @param saveName name of the save
     * @return loaded Game or null
     */
    public static Game loadGame(String saveName){
        if(saveName == null || saveName.trim().isEmpty()){
            saveName = "last_game";
        }

        String path = SAVE_DIR + File.separator + saveName + ".sav";
        File file = new File(path);

        if(!file.exists()) return null;

        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
            return (Game) in.readObject();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a list of all available save names.
     * @return list of save names
     */
    public static List<String> getSavedGames(){
        File dir = new File(SAVE_DIR);
        List<String> saves = new ArrayList<>();

        if(dir.exists()){
            File[] files = dir.listFiles((d, name) -> name.endsWith(".sav"));
            if(files != null){
                for(File f : files){
                    String name = f.getName().replace(".sav", "");
                    saves.add(name);
                }
            }
        }
        return saves;
    }

    private static void writePlainInfo(Game game, String saveName, int secondsPlayed){
        String playerName = game.getHumanPlayer().getName();
        int sunkHuman = countSunkShips(game.getHumanPlayer().getShips());
        int sunkMachine = countSunkShips(game.getMachinePlayer().getShips());
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String infoPath = SAVE_DIR + File.separator + saveName + ".txt";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(infoPath))){
            writer.write("saveName=" + saveName);
            writer.newLine();
            writer.write("jugador=" + playerName);
            writer.newLine();
            writer.write("barcosHundidosJugador=" + sunkHuman);
            writer.newLine();
            writer.write("barcosHundidosMaquina=" + sunkMachine);
            writer.newLine();
            writer.write("segundosJugados=" + secondsPlayed);
            writer.newLine();
            writer.write("turnoHumano=" + game.isHumanTurn());
            writer.newLine();
            writer.write("juegoTerminado" + game.isGameOver());
            writer.newLine();
            writer.write("guardadoEn=" + timestamp);
            writer.newLine();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static int countSunkShips(List<Ship> ships){
        int count = 0;
        for(Ship ship : ships){
            if(ship.isSunk()) count++;
        }
        return count;
    }

    /**
     * Gets the most recent save based on file modification time.
     * @return Optional containing the most recent save name
     */
    public static Optional<String> getMostRecentSave(){
        File dir = new File(SAVE_DIR);
        if(!dir.exists()) return Optional.empty();

        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if(files == null || files.length == 0) return Optional.empty();

        File mostRecent = null;
        long lastModified = 0;

        for(File f : files){
            if(f.lastModified() > lastModified){
                lastModified = f.lastModified();
                mostRecent = f;
            }
        }

        if(mostRecent != null){
            String name = mostRecent.getName().replace(".txt", "");
            return Optional.of(name);
        }
        return Optional.empty();
    }

    /**
     * Gets the saved seconds for a game.
     * @param saveName name of the save
     * @return saved seconds or 0
     */
    public static int getSavedSeconds(String saveName){
        if(saveName == null || saveName.trim().isEmpty()) return 0;

        String txtPath = SAVE_DIR +File.separator + saveName + ".txt";
        File file = new File(txtPath);
        if(!file.exists()) return 0;

        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            while((line = reader.readLine()) != null){
                if(line.startsWith("segundosJugados=")){
                    try{
                        System.out.println("Success");
                        return Integer.parseInt(line.substring("segundosJugados=".length()).trim());
                    }catch(NumberFormatException e){
                        System.out.println("Couldn't obtain seconds...");
                        return 0;
                    }
                }
            }
        }catch(IOException e){
            System.out.println("Reader failed...");
            e.printStackTrace();
        }
        return 0;
    }
}
