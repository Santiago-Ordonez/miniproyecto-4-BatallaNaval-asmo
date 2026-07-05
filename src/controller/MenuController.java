package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
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
    private void handleExitButton(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
