package Client.Network;

import Client.Controller.GameController;
import Client.Model.Board;
import Client.Model.JsonParser;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client implements Runnable{
    protected Socket server;
    protected DataInputStream inp;
    protected DataOutputStream out;
    protected Scanner scanner;

    protected GameController controller;

    public Client(String address, int port) throws IOException {
        server = new Socket(address, port);
        inp = new DataInputStream(server.getInputStream());
        out = new DataOutputStream(server.getOutputStream());
    }

    @Override
    public void run() {
        System.out.println("In client run");
        try {
            String color = inp.readUTF();
            System.out.println(color);
            while(true){
                String response = inp.readUTF();
                Board board = JsonParser.parse(response);
                board.setColor(color);
//                System.out.println(response); //TODO: act accordingly the turn
                controller.setBoardModel(board);
                controller.drawBoard();
                if(board.isEnd()) break;
                //if currentTurn = turn getMove else readUTF
                if(color.equals(board.getCurrentTurn())) {
                    do {
                        System.out.println("Waiting for a move");
                        String move = scanner.nextLine();
                        out.writeUTF(move);
                        System.out.println("acquired move " + move);
                    } while (!inp.readUTF().equals("OK"));
                }
            }
            System.out.println("Client acquired end");
            controller.setEndGame();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }
    public void setScanner(PipedOutputStream pipedOutputStream) throws IOException {
        this.scanner = new Scanner(new PipedInputStream(pipedOutputStream));
    }
}
