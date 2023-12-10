package Client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket server = new Socket("localhost", 4523);
        DataInputStream inputStream = new DataInputStream(server.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(server.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        try {
            while(true){
                String inp = inputStream.readUTF();
                while(!inp.equals("YOU")){
                    System.out.println(inp);
                    inp = inputStream.readUTF();
                }
                do {
                    outputStream.writeUTF(scanner.nextLine());
                } while (!inputStream.readUTF().equals("OK"));
            }
        } catch (SocketException socketException){
            return;
        }
    }
}
