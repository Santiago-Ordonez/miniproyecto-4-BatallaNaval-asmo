package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.Game;

import java.util.Optional;

public class GameOverController {

    @FXML private Label resultLabel;
    @FXML private Label statsLabel;

    private Game game;

    public void setGame(Game game){
        this.game = game;
        updateUI();
    }

    private void updateUI() {
        if (game == null) return;
        String winner = game.getWinner();
        boolean playerWon = "human".equals(winner);

        resultLabel.setText(playerWon ? "¡VICTORIA!" : "DERROTA");
        resultLabel.setStyle(playerWon ? "-fx-text-fill: green;" : "-fx-text-fill: red;");

        statsLabel.setText("Jugador: " + game.getMachinePlayer().getSunkShipsCount() + " barcos destruidos\nMáquina: " + game.getHumanPlayer().getSunkShipsCount() + " barcos destruidos");
    }

    @FXML
    private void handleNewGame(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("Jugador");
        dialog.setTitle("Nuevo Juego");
        dialog.setHeaderText("Ingresa tu nombre: ");
        dialog.setContentText("Nombre: ");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            try {
                FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("../view/Game.fxml"));
                Parent gameRoot = loader.load();

                GameController controller = loader.getController();
                controller.setPlayerName(name);
                controller.newGame();

                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene gameScene = new Scene(gameRoot);
                gameScene.getStylesheets().add(MenuController.class.getResource("../view/styles.css").toExternalForm());

                currentStage.setTitle("Batalla Naval - Partida");
                currentStage.setScene(gameScene);
                currentStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/view/styles.css");
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}