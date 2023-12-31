package Server;

import Game.Chess;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class GameSession implements Runnable{
    private Chess game;
    private ServerSocket serverSocket;

    private Socket p1;
    private Socket p2;
    private DataInputStream inp1;
    private DataOutputStream out1;
    private DataInputStream inp2;
    private DataOutputStream out2;
    public GameSession() throws IOException {
        game = new Chess();
        serverSocket = new ServerSocket(0);
    }

    public void start() throws IOException {
        System.out.println("session start");
        boolean turn;
        Random random = new Random();
        double r = random.nextDouble();
        turn = r < 0.5;
        out1.writeUTF(turn ? "white" : "black");
        out2.writeUTF(turn ? "black" : "white");
        JsonGame jsonAdapter = new JsonAdapter(game);
        do {
            out1.writeUTF(jsonAdapter.getJsonAsString());
            out2.writeUTF(jsonAdapter.getJsonAsString());
//            out1.writeUTF(game.print());
//            out2.writeUTF(game.print());
            if(turn){
                while(true){
                    String move = inp1.readUTF();
                    System.out.println("move " + move);
                    if(game.move(move).equals(move)){
                        System.out.println("inp1");
                        out1.writeUTF("OK");
                        break;
                    }
                }
            }else{
                while(true){
                    String move = inp2.readUTF();
                    System.out.println("move " + move);
                    if(game.move(move).equals(move)) {
                        System.out.println("inp2");
                        out2.writeUTF("OK");
                        break;
                    }
                }
            }
            turn = !turn;
        } while (!game.end);
        out1.writeUTF(jsonAdapter.getJsonAsString());
        out2.writeUTF(jsonAdapter.getJsonAsString());
        System.out.println("session end");

    }

    public int getPort(){
        return serverSocket.getLocalPort();
    }

    @Override
    public void run() {
        //TODO potential state pattern
        if (p1 == null) {
            try {
                p1 = serverSocket.accept();
                inp1 = new DataInputStream(p1.getInputStream());
                out1 = new DataOutputStream(p1.getOutputStream());
                System.out.println("p1");

                p2 = serverSocket.accept();
                inp2 = new DataInputStream(p2.getInputStream());
                out2 = new DataOutputStream(p2.getOutputStream());
                System.out.println("p2");
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            this.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
