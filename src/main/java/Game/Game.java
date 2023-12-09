package Game;

import java.util.Scanner;

public class Game {

    public static void main(String[] args) {
        Chess chess = new Chess();
        Scanner scan = new Scanner(System.in);
        while (!chess.end) {
            chess.print();
            System.out.print("WRITE YOUR MOVE: ");
            chess.move(scan.nextLine());
        }
    }

}
