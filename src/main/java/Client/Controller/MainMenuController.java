package Client.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {


    Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void playLocal(ActionEvent event) {

    }

    @FXML
    void playLocalNet(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LocalNetworkGame.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        LocalNetworkGameController localNetworkGameController = fxmlLoader.getController();
        localNetworkGameController.setStage(stage);
        stage.setScene(scene);
    }

    @FXML
    void playOnline(ActionEvent event) {

    }

}