package Client;

import Client.Controller.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Mainmenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        MainMenuController mainMenuController = fxmlLoader.getController();
        mainMenuController.setStage(stage);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
