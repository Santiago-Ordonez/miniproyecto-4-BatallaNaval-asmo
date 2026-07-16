import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        try{
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/Menu.fxml"));
            Parent root = loader.load();

            stage.setTitle("Batalla Naval");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("view/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){launch(args);}
}
