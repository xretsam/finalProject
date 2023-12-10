package Server;

import Game.Chess;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Game {

    public static void main(String[] args) throws IOException {
        Chess game = new Chess();
        ServerSocket serverSocket = new ServerSocket(4523);
        Socket player1 = serverSocket.accept();
        DataInputStream inp1 = new DataInputStream(player1.getInputStream());
        DataOutputStream outp1 = new DataOutputStream(player1.getOutputStream());
        System.out.println("p1");
        Socket player2 = serverSocket.accept();
        DataInputStream inp2 = new DataInputStream(player2.getInputStream());
        DataOutputStream outp2 = new DataOutputStream(player2.getOutputStream());
        System.out.println("p2");
        boolean turn = true;
        while (!game.end) {
            outp1.writeUTF(game.print());
            outp2.writeUTF(game.print());
            if(turn){
                outp1.writeUTF("YOU");
                while(true){
                    String move = inp1.readUTF();
                    if(game.move(move).equals(move)){
                        outp1.writeUTF("OK");
                        break;
                    }
                }
            }else{
                outp2.writeUTF("YOU");
                while(true){
                    String move = inp2.readUTF();
                    if(game.move(move).equals(move)) {
                        outp2.writeUTF("OK");
                        break;
                    }
                }
            }
            turn = !turn;
        }
        player1.close();
        player2.close();
    }
}
