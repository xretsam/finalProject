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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LocalNetworkGameController {

    Stage stage;

    private Thread hostThread;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Label localLabel;

    @FXML
    private Button playAsGuestBT;

    @FXML
    private Button playAsHostBT;

    @FXML
    private Button returnBT;

    @FXML
    private AnchorPane waitingDialog;
    @FXML
    void onCancelAction(ActionEvent event) {
        cancelWait();
    }

    @FXML
    private void onReturnBTAction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Mainmenu.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
        MainMenuController controller = fxmlLoader.getController();
        controller.setStage(stage);
    }

    @FXML
    void playAsGuest(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GuestMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        GuestMenuController guestMenuController = fxmlLoader.getController();
        guestMenuController.setStage(stage);
        stage.setScene(scene);
    }

    private void startWait(){
        hostThread = new Thread(() -> {
            Thread gameThread = null;
            Thread hostAdvertiserThread = null;
            Thread clientThread = null;
            try {
                GameSession gameSession = new GameSession();

                gameThread = new Thread(gameSession);
                gameThread.start();

                HostAdvertiser hostAdvertiser = new HostAdvertiser(gameSession.getPort());
                hostAdvertiserThread = new Thread(hostAdvertiser);
                hostAdvertiserThread.start();
                Client client = new Client("localhost", gameSession.getPort());
                clientThread = new Thread(client);
                //
                gameThread.join();
                hostAdvertiserThread.interrupt();
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
            } catch (InterruptedException | IOException e) {
                if(gameThread != null) gameThread.interrupt();
                if(hostAdvertiserThread != null) hostAdvertiserThread.interrupt();
                if(clientThread != null) clientThread.interrupt();
            }
        });
        hostThread.start();
        waitingDialog.setVisible(true);
        localLabel.setVisible(false);
        playAsGuestBT.setVisible(false);
        playAsHostBT.setVisible(false);
        returnBT.setVisible(false);

    }
    private void cancelWait(){
        hostThread.interrupt();
        waitingDialog.setVisible(false);
        localLabel.setVisible(true);
        playAsGuestBT.setVisible(true);
        playAsHostBT.setVisible(true);
        returnBT.setVisible(true);
    }
    @FXML
    void playAsHost(ActionEvent event) throws IOException, InterruptedException {
        startWait();
//        GameSession gameSession = new GameSession();
//
//        Thread gameThread = new Thread(gameSession);
//        gameThread.start();
//
//        HostAdvertiser hostAdvertiser = new HostAdvertiser(gameSession.getPort());
//        Thread hostAdvertiserThread = new Thread(hostAdvertiser);
//        hostAdvertiserThread.start();
//        Client client = new Client("localhost", gameSession.getPort());
//        Thread clientThread = new Thread(client);
//        //
//        gameThread.join();
//        hostAdvertiserThread.interrupt();
//        gameThread = new Thread(gameSession);
//        gameThread.start();
//        //
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Client1.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        GameController controller = fxmlLoader.getController();
//        client.setController(controller);
//        controller.setClient(client);
//        controller.setStage(stage);
//        stage.setScene(scene);
//        controller.setOutput();
//        //
//        clientThread.start();
    }

    @FXML
    void initialize() {

    }

}

