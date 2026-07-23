package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;
import utils.BoardCellRenderer;
import utils.MachineThread;
import utils.SaveManager;
import utils.TimeThread;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class GameController {
    @FXML private GridPane playerGrid;
    @FXML private GridPane enemyGrid;
    @FXML private Label stateLabel;
    @FXML private Label timeLabel;
    @FXML private Label playerShipsLabel;
    @FXML private Label machineShipsLabel;

    private Game game;
    private List<Pane> playerCells = new ArrayList<>();
    private List<Pane> enemyCells = new ArrayList<>();

    private boolean isPlacementPhase;
    private int currentShipIndex;
    private Orientation currentOrientation;
    private boolean showEnemyShips;
    private TimeThread timerThread;
    private String playerName;
    private int secondsPlayed;

    @FXML
    public void initialize(){}

    public void newGame(){
        game = new Game(playerName);
        setupNewGame();
    }

    private void setupNewGame(){
        createBoards();
        game.startNewGame();

        isPlacementPhase = true;
        currentShipIndex = 0;
        currentOrientation = Orientation.HORIZONTAL;
        showEnemyShips = false;

        startTimer();
        updateVisuals();
    }

    private void startTimer(){
        if(timerThread != null && timerThread.isAlive()){
            timerThread.interrupt();
            try{
                timerThread.join();
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        timerThread = new TimeThread(timeLabel, secondsPlayed);
        timerThread.start();
    }

    public void resumeGame(Game loadedGame) {
        this.game = loadedGame;
        setupLoadedGame();
    }

    private void setupLoadedGame(){
        createBoards();

        isPlacementPhase = false;
        showEnemyShips = false;

        if(game.isGameOver()){
            updateStateLabel("Juego Terminado - Ganador: " + game.getWinner());
        }else if (game.isHumanTurn()){
            updateStateLabel("Partida Cargada - Tu turno");
        }else{
            updateStateLabel("Partida cargada - Turno de la máquina");
        }

        startTimer();
        updateVisuals();

        if(!game.isGameOver() && !game.isHumanTurn()) machineTurn();
    }

    private void createBoards() {
        createGrid(playerGrid, playerCells, true);
        createGrid(enemyGrid, enemyCells, false);
    }

    private void createGrid(GridPane grid, List<Pane> cells, boolean isPlayer) {
        grid.getChildren().clear();
        cells.clear();

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Pane cell = new Pane();
                cell.setPrefSize(40, 40);
                cell.setStyle("-fx-border-color: black;");

                ImageView imageView = new ImageView();
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                imageView.setPreserveRatio(true);
                cell.getChildren().add(imageView);

                final int r = row;
                final int c = col;

                if(isPlayer){
                    cell.setOnMouseMoved(e -> showPreview(r, c));
                    cell.setOnMouseExited(e -> clearPreview());
                }

                cell.setOnMouseClicked(e -> handleCellClick(e, r, c, isPlayer));

                grid.add(cell, col, row);
                cells.add(cell);
            }
        }
    }

    private void handleCellClick(MouseEvent e, int row, int col, boolean isPlayerBoard) {
        if (isPlacementPhase && isPlayerBoard) {
            if(e.getButton() == MouseButton.SECONDARY){
                rotateCurrentShip();
            }else {
                placeCurrentShip(row, col);
            }
        } else if (!isPlacementPhase && !isPlayerBoard) {
            AttackResult result = game.humanAttack(row, col);
            if (result != null) {
                saveGame();
                updateStateLabel("Disparo: " + result);
                if (!game.isHumanTurn()) {
                    machineTurn();
                }
                if(game.isGameOver()){
                    updateStateLabel("Juego Terminado - Ganador " + game.getWinner());
                }
            }
        }
        updateVisuals();
    }

    private void rotateCurrentShip(){
        currentOrientation = (currentOrientation == Orientation.HORIZONTAL) ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    private void placeCurrentShip(int row, int col) {
        boolean placed = game.getHumanPlayer().placeShip(currentShipIndex, row, col, currentOrientation);

        if (placed) {
            currentShipIndex++;
            updateVisuals();

            if (currentShipIndex >= game.getHumanPlayer().getShips().size()) {
                isPlacementPhase = false;
                updateStateLabel("¡Batalla iniciada! Tu turno");
            }
        } else {
            updateStateLabel("Posición inválida. Intenta otra.");
        }
    }

    private void machineTurn() {
        updateStateLabel("Máquina pensando...");

        MachineThread.executeMachineTurn(game, () -> {
            updateVisuals();
            updateStateLabel("Máquina atacó");

            if(game.isGameOver()){
                updateStateLabel("Juego Terminado - Ganador " + game.getWinner());
                return;
            }

            if(!game.isHumanTurn()) machineTurn();
        });
        saveGame();
    }

    private void updateVisuals() {
        updateGridVisual(playerCells, game.getHumanPlayer().getBoard(), true);
        updateGridVisual(enemyCells, game.getMachinePlayer().getBoard(), false);
        if(isPlacementPhase) updateStateLabel("Posiciona tu " + getShipType());
        playerShipsLabel.setText("Barcos Restantes " + game.getHumanPlayer().getNoSunkShipsCount());
        machineShipsLabel.setText("Barcos Restantes " + game.getMachinePlayer().getNoSunkShipsCount());
    }

    private String getShipType(){
        if(currentShipIndex == 10) return "fragata";

        int size = game.getHumanPlayer().getShips().get(currentShipIndex).getSize();
        return switch(size){
            case 1 -> "fragata";
            case 2 -> "destructor";
            case 3 -> "submarino";
            case 4 -> "portaaviones";
            default -> "barco";
        };
    }

    private void updateGridVisual(List<Pane> cells, Board board, boolean isHumanBoard) {
        int[][] matrix = board.getMatrix();
        int index = 0;

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Pane cell = cells.get(index++);
                int value = matrix[row][col];

                boolean showShip = false;

                if(isHumanBoard){
                    showShip = true;
                }else if(showEnemyShips){
                    showShip = true;
                }

                int part = board.getShipPartAt(row, col);
                int shipType = board.getShipTypeAt(row, col);
                Orientation orientation = board.getShipOrientationAt(row, col);

                BoardCellRenderer.updateCell(cell, value, showShip, part, shipType, orientation);
            }
        }
    }

    private void showPreview(int row, int col){
        clearPreview();
        if(!isPlacementPhase) return;

        Ship tempShip = new Ship(game.getHumanPlayer().getShips().get(currentShipIndex).getSize());
        tempShip.setPosition(row, col, currentOrientation);
        for(int[] pos : tempShip.getPositions()){
            if(pos[0] >= 0 && pos[0] < 10 && pos[1] >= 0 && pos[1] < 10){
                int index = pos[0] * 10 + pos[1];
                if(index < playerCells.size()){
                    playerCells.get(index).setStyle("-fx-border-color: black; -fx-background-color: rgba(0, 255, 0, 0.3)");
                }
            }
        }
    }

    private void clearPreview(){
        for(Pane cell : playerCells){
            cell.setStyle("-fx-border-color: black; -fx-background-color: lightblue;");
        }
        updateVisuals();
    }

    private void updateStateLabel(String text) {
        stateLabel.setText(text);
    }

    public void setPlayerName(String name){this.playerName = name;}

    public void setSecondsPlayed(int secondsPlayed){
        this.secondsPlayed = secondsPlayed;
    }

    private void saveGame(){
        if(!isPlacementPhase){
            SaveManager.saveGame(game, timerThread.getSeconds());
        }
    }

    @FXML
    public void handleShowEnemyShipsButton(){
        showEnemyShips = !showEnemyShips;
        updateVisuals();
    }

    @FXML
    public void handlePauseButton(ActionEvent event){
        ContextMenu pauseMenu = new ContextMenu();

        MenuItem resumeItem = new MenuItem("Reanudar");
        MenuItem menuItem = new MenuItem("Menu");
        MenuItem newGameItem = new MenuItem("Nuevo Juego");
        MenuItem exitItem = new MenuItem("Salir");

        resumeItem.setOnAction(e -> pauseMenu.hide());

        menuItem.setOnAction(e -> {
            saveGame();
            try {
                FXMLLoader loader = new FXMLLoader(GameController.class.getResource("../view/Menu.fxml"));
                Parent menuRoot = loader.load();

                Scene menuScene = new Scene(menuRoot);
                menuScene.getStylesheets().add(MenuController.class.getResource("../view/styles.css").toExternalForm());

                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setTitle("Batalla Naval");
                currentStage.setScene(menuScene);
                currentStage.show();
            }catch(Exception exception){exception.printStackTrace();}
        });

        newGameItem.setOnAction(e -> {
            saveGame();
            TextInputDialog dialog = new TextInputDialog("Jugador");
            dialog.setTitle("Nuevo Juego");
            dialog.setHeaderText("Ingrese su nombre:");
            dialog.setContentText("Nombre: ");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                playerName = name;
                newGame();
            });
        });

        exitItem.setOnAction(e -> {
            saveGame();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });

        pauseMenu.getItems().addAll(resumeItem, menuItem, newGameItem, exitItem);
        pauseMenu.show((Node) event.getSource(), Side.BOTTOM, 0, 0);
    }
}
