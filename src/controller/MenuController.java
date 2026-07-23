package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Game;
import utils.SaveManager;

import java.util.List;
import java.util.Optional;

public class MenuController {

    @FXML private Button continueButton;
    @FXML private Button loadButton;

    @FXML
    public void initialize() {
        boolean hasSave = SaveManager.hasSavedGame();
        continueButton.setDisable(!hasSave);
        loadButton.setDisable(!hasSave);
    }

    @FXML
    private void handleNewGameButton(ActionEvent event){
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
                showErrorAlert("Error al iniciar nuevo juego");
            }
        });
    }

    @FXML
    private void handleContinueButton(ActionEvent event){
        Optional<String> recent = SaveManager.getMostRecentSave();
        if(recent.isPresent()){
            goToLoadedGame(event, recent.get());
        }else{
            showErrorAlert("No hay partidas guardadas para continuar");
        }
    }

    @FXML
    private void handleLoadButton(ActionEvent event) {
        List<String> savedGames = SaveManager.getSavedGames();

        if (savedGames.isEmpty()) {
            showErrorAlert("No hay partidas guardadas");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(savedGames.get(0), savedGames);
        dialog.setTitle("Cargar Partida");
        dialog.setHeaderText("Selecciona una partida");
        dialog.setContentText("Partida:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(saveName -> {
            Game loadedGame = SaveManager.loadGame(saveName);

            if (loadedGame != null) {
                if(loadedGame.isGameOver()){
                    showGameOverScreen(event, loadedGame);
                }else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Game.fxml"));
                        Parent gameRoot = loader.load();

                        GameController controller = loader.getController();

                        controller.setSecondsPlayed(SaveManager.getSavedSeconds(saveName));
                        controller.resumeGame(loadedGame);

                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Scene gameScene = new Scene(gameRoot);
                        gameScene.getStylesheets().add(MenuController.class.getResource("../view/styles.css").toExternalForm());

                        stage.setScene(gameScene);
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showErrorAlert("Error al cargar la partida");
                    }
                }
            }else{showErrorAlert("No se pudo cargar la partida");}
        });
    }

    private void goToLoadedGame(ActionEvent event, String saveName) {
        Game savedGame = SaveManager.loadGame(saveName);
        if (savedGame == null) {
            showErrorAlert("No se encontró la partida");
            return;
        }

        if(savedGame.isGameOver()){
            showGameOverScreen(event, savedGame);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Game.fxml"));
            Parent gameRoot = loader.load();

            GameController gameController = loader.getController();
            gameController.setSecondsPlayed(SaveManager.getSavedSeconds(saveName));
            gameController.resumeGame(savedGame);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene gameScene = new Scene(gameRoot);
            gameScene.getStylesheets().add(MenuController.class.getResource("../view/styles.css").toExternalForm());

            currentStage.setTitle("Batalla Naval - Partida Cargada");
            currentStage.setScene(gameScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExitButton(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showErrorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showGameOverScreen(ActionEvent event, Game game) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/GameOver.fxml"));
            Parent root = loader.load();

            GameOverController controller = loader.getController();
            controller.setGame(game);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/view/styles.css");
            stage.setScene(scene);
            stage.setTitle("Game Over - Batalla Naval");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
