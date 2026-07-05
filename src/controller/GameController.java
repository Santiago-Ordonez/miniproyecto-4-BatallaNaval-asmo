package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class GameController {
    @FXML
    public void handlePauseButton(ActionEvent event){
        ContextMenu pauseMenu = new ContextMenu();

        MenuItem resumeItem = new MenuItem("Reanudar");
        MenuItem menuItem = new MenuItem("Menu");

        resumeItem.setOnAction(e -> pauseMenu.hide());

        menuItem.setOnAction(e -> {
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

        pauseMenu.getItems().addAll(resumeItem, menuItem);
        pauseMenu.show((Node) event.getSource(), Side.BOTTOM, 0, 0);
    }
}
