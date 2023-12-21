package Client.Network;

import Server.GameSession;

import java.io.IOException;

public class Host {
    GameSession gameSession;
    Client client;
    HostAdvertiser hostAdvertiser;

    public static void main(String[] args) throws IOException, InterruptedException {
        GameSession gameSession1 = new GameSession();
        Thread thread1 = new Thread(gameSession1);
        thread1.start();
        System.out.println(1);
        HostAdvertiser hostAdvertiser = new HostAdvertiser(gameSession1.getPort());
        Thread thread = new Thread(hostAdvertiser);
        thread.start();
        System.out.println(2);
        Client client1 = new Client("localhost", gameSession1.getPort());
        Thread thread2 = new Thread(client1);
        thread2.start();
        System.out.println(3);
        thread1.join();
        System.out.println(4);
        thread.interrupt();
        System.out.println(5);
        gameSession1.start();
    }
}
