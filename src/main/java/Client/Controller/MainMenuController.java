package Client.Controller;

import Client.Network.Client;
import Client.Network.LocalClient;
import Server.GameSession;
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
    void playLocal(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Client1.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        GameController controller = fxmlLoader.getController();

        GameSession gameSession = new GameSession();
        Thread gameThread = new Thread(gameSession);
        gameThread.start();

        LocalClient client = new LocalClient("localhost", gameSession.getPort());
        Thread clientThread = new Thread(client);

        gameThread = new Thread(gameSession);
        gameThread.start();

        client.setController(controller);
        controller.setClient(client);
        controller.setStage(stage);

        stage.setScene(scene);
        controller.setOutput();

        clientThread.start();
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