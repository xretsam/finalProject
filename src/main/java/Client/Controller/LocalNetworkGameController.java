package Client.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Client.Network.Client;
import Client.Network.HostAdvertiser;
import Server.GameSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LocalNetworkGameController {

    Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void playAsGuest(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GuestMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        GuestMenuController guestMenuController = fxmlLoader.getController();
        guestMenuController.setStage(stage);
        stage.setScene(scene);
    }

    @FXML
    void playAsHost(ActionEvent event) throws IOException, InterruptedException {
        GameSession gameSession = new GameSession();

        Thread gameThread = new Thread(gameSession);
        gameThread.start();

        HostAdvertiser hostAdvertiser = new HostAdvertiser(gameSession.getPort());
        Thread hostThread = new Thread(hostAdvertiser);
        hostThread.start();
        Client client = new Client("localhost", gameSession.getPort());
        Thread clientThread = new Thread(client);
        //
        gameThread.join();
        hostThread.interrupt();
        gameThread = new Thread(gameSession);
        gameThread.start();
        //
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Client1.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        GameController controller = fxmlLoader.getController();
        client.setController(controller);
        controller.setClient(client);
        controller.setStage(stage);
        stage.setScene(scene);
        controller.setOutput();
        //
        clientThread.start();
    }

    @FXML
    void initialize() {

    }

}

