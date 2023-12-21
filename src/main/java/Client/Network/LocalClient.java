package Client.Network;

import Client.Model.Board;
import Client.Model.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LocalClient extends Client{

    private Socket server2;
    private DataInputStream inp2;
    private DataOutputStream out2;
    public LocalClient(String address, int port) throws IOException {
        super(address, port);
        server2 = new Socket(address, port);
        inp2 = new DataInputStream(server2.getInputStream());
        out2 = new DataOutputStream(server2.getOutputStream());
    }
    @Override
    public void run() {
        System.out.println("In local client run");
        try {
            String color1 = inp.readUTF();
            String color2 = inp2.readUTF();
            System.out.println(color1);
            System.out.println(color2);
            while(true){
                String response1 = inp.readUTF();
                System.out.println("got response");
                inp2.readUTF();
                Board board = JsonParser.parse(response1);
                board.setColor(board.getCurrentTurn());
                controller.setBoardModel(board);
                controller.drawBoard();
                if(board.isEnd()) break;
                if(color1.equals(board.getCurrentTurn())) {
                    do {
                        System.out.println("1 Waiting for a move");
                        String move = scanner.nextLine();
                        out.writeUTF(move);
                        System.out.println("acquired move " + move);
                    } while (!inp.readUTF().equals("OK"));
                } else {
                    do {
                        System.out.println("2 Waiting for a move");
                        String move = scanner.nextLine();
                        out2.writeUTF(move);
                        System.out.println("acquired move " + move);
                    } while (!inp2.readUTF().equals("OK"));
                }
            }
            System.out.println("Client acquired end");
            controller.setEndGame();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
