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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;
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

    private Game game;
    private List<Pane> playerCells = new ArrayList<>();
    private List<Pane> enemyCells = new ArrayList<>();

    private boolean isPlacementPhase;
    private int currentShipIndex = 0;
    private Orientation currentOrientation;
    private boolean showEnemyShips;
    private TimeThread timerThread;

    @FXML
    public void initialize(String name){
        game = new Game(name);
        createBoards();
        game.startNewGame();

        isPlacementPhase = true;
        currentShipIndex = 0;
        currentOrientation = Orientation.HORIZONTAL;
        showEnemyShips = false;
        updateStateLabel("fase de colocación de barcos");

        if(timerThread != null && timerThread.isAlive()){
            timerThread.interrupt();
            try{
                timerThread.join();
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        timerThread = new TimeThread(timeLabel);
        timerThread.start();

        updateVisuals();
    }

    public void resumeGame(Game loadedGame) {
        this.game = loadedGame;
        createBoards();

        isPlacementPhase = false;
        currentShipIndex = game.getHumanPlayer().getShips().size();
        currentOrientation = Orientation.HORIZONTAL;
        showEnemyShips = false;

        if (game.isGameOver()) {
            updateStateLabel("Juego Terminado");
        } else if (game.isHumanTurn()) {
            updateStateLabel("Partida cargada. Tu turno");
        } else {
            updateStateLabel("Partida cargada. Turno de la máquina");
        }

        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
            try {
                timerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        timerThread = new TimeThread(timeLabel);
        timerThread.start();

        updateVisuals();

        if (!game.isGameOver() && !game.isHumanTurn()) {
            machineTurn();
        }
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
                cell.setStyle("-fx-border-color: black; -fx-background-color: lightblue;");

                final int r = row;
                final int c = col;

                cell.setOnMouseClicked(e -> handleCellClick(e, r, c, isPlayer));

                grid.add(cell, col, row);
                cells.add(cell);
            }
        }
    }

    private void handleCellClick(MouseEvent e, int row, int col, boolean isPlayerBoard) {
        System.out.println("Click en: "+row+", "+col+") - playerBoard: "+isPlayerBoard+" - placement phase " + isPlacementPhase);

        if (isPlacementPhase && isPlayerBoard) {
            placeCurrentShip(row, col);
        } else if (!isPlacementPhase && !isPlayerBoard) {
            System.out.println("Intentando disparo enemigo...");
            AttackResult result = game.humanAttack(row, col);
            System.out.println("Resultado de disparo: "+result);
            updateVisuals();
            if (result != null) {
                updateStateLabel("Disparo: " + result);
                if (!game.isHumanTurn()) {
                    machineTurn();
                }
            }
        }
    }

    private void placeCurrentShip(int row, int col) {
        boolean placed = ((HumanPlayer) game.getHumanPlayer())
                .placeShip(currentShipIndex, row, col, currentOrientation);

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
                updateStateLabel("Juego Terminado");
            }
        });
    }

    private void updateVisuals() {
        updateGridVisual(playerCells, game.getHumanPlayer().getBoard(), true);
        updateGridVisual(enemyCells, game.getMachinePlayer().getBoard(), false);
    }

    private void updateGridVisual(List<Pane> cells, Board board, boolean isHumanBoard) {
        int[][] matrix = board.getMatrix();
        int index = 0;

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Pane cell = cells.get(index++);
                int value = matrix[row][col];

                if(!isHumanBoard && !showEnemyShips && value==1){
                    cell.setStyle("-fx-border-color: black; -fx-background-color: lightblue;");
                }else {
                    switch (value) {
                        case 0 -> cell.setStyle("-fx-border-color: black; -fx-background-color: lightblue;");
                        case 1 -> cell.setStyle("-fx-border-color: black; -fx-background-color: gray;");
                        case 2 -> cell.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
                        case 3 -> cell.setStyle("-fx-border-color: black; -fx-background-color: orange;");
                        case 4 -> cell.setStyle("-fx-border-color: black; -fx-background-color: red;");
                    }
                }
            }
        }
    }

    private void updateStateLabel(String text) {
        stateLabel.setText(text);
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
            SaveManager.saveGame(game, "partida_"+game.getHumanPlayer().getName());
            try {
                FXMLLoader loader = new FXMLLoader(GameController.class.getResource("../view/Menu.fxml"));
                Parent menuRoot = loader.load();

                Scene menuScene = new Scene(menuRoot);

                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setTitle("Batalla Naval");
                currentStage.setScene(menuScene);
                currentStage.show();
            }catch(Exception exception){exception.printStackTrace();}
        });

        newGameItem.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("Jugador");
            dialog.setTitle("Nuevo Juego");
            dialog.setHeaderText("Ingrese su nombre:");
            dialog.setContentText("Nombre: ");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {
                initialize(name);
            });
        });

        exitItem.setOnAction(e -> {
            SaveManager.saveGame(game, "partida-"+game.getHumanPlayer().getName());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });

        pauseMenu.getItems().addAll(resumeItem, menuItem, newGameItem, exitItem);
        pauseMenu.show((Node) event.getSource(), Side.BOTTOM, 0, 0);
    }
}
