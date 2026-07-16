package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;
import model.Game;
import utils.SaveManager;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MenuController {

    @FXML private Button continueButton;
    @FXML private Button loadButon;

    @FXML
    public void initialize() {
        boolean hasSave = SaveManager.hasSavedGame();
        continueButton.setDisable(!hasSave);
        loadButon.setDisable(!hasSave);
    }

    @FXML
    private void handleNewGameButton(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("../view/Game.fxml"));
            Parent gameRoot = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene gameScene = new Scene(gameRoot);
            gameScene.getStylesheets().add(MenuController.class.getResource("../view/styles.css").toExternalForm());

            currentStage.setTitle("Batalla Naval - Partida");
            currentStage.setScene(gameScene);
            currentStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleContinueButton(ActionEvent event){
        goToLoadedGame(event, "");
    }

    @FXML
    private void handleLoadButton(ActionEvent event) {
        List<String> savedGames = SaveManager.getSavedGames();

        if (savedGames.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sin partidas");
            alert.setContentText("No hay partidas guardadas.");
            alert.showAndWait();
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(savedGames.get(0), savedGames);
        dialog.setTitle("Cargar Partida");
        dialog.setHeaderText("Selecciona una partida guardada");
        dialog.setContentText("Partida:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(saveName -> {
            Game loadedGame = SaveManager.loadGame(saveName);
            if (loadedGame != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Game.fxml"));
                    Parent gameRoot = loader.load();
                    GameController controller = loader.getController();
                    controller.resumeGame(loadedGame);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(gameRoot));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void goToLoadedGame(ActionEvent event, String saveName) {
        Game savedGame = SaveManager.loadGame(saveName);
        if (savedGame == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("No se pudo cargar la partida.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Game.fxml"));
            Parent gameRoot = loader.load();

            GameController gameController = loader.getController();
            gameController.resumeGame(savedGame);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene gameScene = new Scene(gameRoot);

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
}
