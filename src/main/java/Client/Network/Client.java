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
    private Socket server;
    private DataInputStream inp;
    private DataOutputStream out;
    private Scanner scanner;

    private GameController controller;

    public Client(String address, int port) throws IOException {
        server = new Socket(address, port);
        inp = new DataInputStream(server.getInputStream());
        out = new DataOutputStream(server.getOutputStream());
        scanner = new Scanner(System.in);
    }
    public Client(InetAddress address, int port) throws IOException {
        server = new Socket(address, port);
        inp = new DataInputStream(server.getInputStream());
        out = new DataOutputStream(server.getOutputStream());
        scanner = new Scanner(System.in);
    }

    public void start() throws IOException{
        System.out.println(inp.readUTF());
        try {
            while(true){
                String response = inp.readUTF();
                Board board = JsonParser.parse(response);
                System.out.println(response);
                response = inp.readUTF();
                if(response.equals("END")) break;
                do {
                    out.writeUTF(scanner.nextLine());
                } while (!inp.readUTF().equals("OK"));
            }
        } catch (SocketException socketException){
            server.close();
        }
    }

    @Override
    public void run() {
        System.out.println("In client run");
        try {
            String color = inp.readUTF();
            System.out.println(color);
            while(true){
                String response = inp.readUTF();
                if(response.equals("END")) break;
                Board board = JsonParser.parse(response);
                board.setColor(color);
//                System.out.println(response); //TODO: act accordingly the turn
                controller.setBoardModel(board);
                controller.drawBoard();
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
