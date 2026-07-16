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
import javafx.stage.Stage;
import model.Game;
import utils.SaveManager;

import java.io.IOException;

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

            currentStage.setTitle("Batalla Naval - Partida");
            currentStage.setScene(gameScene);
            currentStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleContinueButton(ActionEvent event){
        goToLoadedGame(event);
    }

    @FXML
    private void handleLoadButton(ActionEvent event){
        Alert info = new Alert(Alert.AlertType.CONFIRMATION);
        info.setTitle("Cargar Partida");
        info.setHeaderText("Datos de la partida guardada");
        info.setContentText(SaveManager.readSummary());

        info.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                goToLoadedGame(event);
            }
        });
    }

    private void goToLoadedGame(ActionEvent event){
        Game savedGame = SaveManager.loadGame();
        if (savedGame == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("../view/Game.fxml"));
            Parent gameRoot = loader.load();

            GameController gameController = loader.getController();
            gameController.resumeGame(savedGame);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene gameScene = new Scene(gameRoot);

            currentStage.setTitle("Batalla Naval - Partida");
            currentStage.setScene(gameScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExitButton(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
