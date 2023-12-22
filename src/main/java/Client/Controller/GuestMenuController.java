package Client.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import Client.Network.Client;
import Client.Network.GuestListener;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class GuestMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox vbox;

    private Stage stage;

    private Thread listenerTask;
    private ScheduledExecutorService scheduler;

    private GuestListener guestListener;

    private List<String> hosts;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    void initialize() throws InterruptedException {
        assert vbox != null : "fx:id=\"vbox\" was not injected: check your FXML file 'GuestMenu.fxml'.";
        guestListener = new GuestListener();
        listenerTask = new Thread(guestListener);
        listenerTask.start();
        scheduler = Executors.newScheduledThreadPool(1);
        Runnable uiUpdateTask = () -> {
            Platform.runLater(() -> {
                vbox.getChildren().clear();
                hosts = guestListener.getHosts();
                for (int i = 0; i < hosts.size(); i++) {
                    Button button = new Button(hosts.get(i) + " " + guestListener.getMessage(hosts.get(i)));
                    vbox.getChildren().add(button);
                    button.setOnAction(this::onChoice);
                }
            });
        };
        scheduler.scheduleAtFixedRate(uiUpdateTask,0,1,TimeUnit.SECONDS);
    }


    private void onChoice(ActionEvent actionEvent){
        Button chosenButton = (Button) actionEvent.getSource();
        int i = vbox.getChildren().indexOf(chosenButton);
        String address = hosts.get(i).split(":")[0];
        int port = Integer.parseInt(guestListener.getMessage(hosts.get(i)));
        Client client;
        try {
            client = new Client(address,port);
            scheduler.shutdown();
            guestListener.setEnd(true);
            listenerTask.interrupt();
            Thread clientThread = new Thread(client);
            clientThread.start();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Client1.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            GameController controller = fxmlLoader.getController();
            client.setController(controller);
            controller.setClient(client);
            controller.setOutput();
            controller.setStage(stage);
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }



}
