package Client.Network;

import java.io.IOException;
import java.util.*;

public class Guest {
    Client client;
    GuestListener guestListener;

    public static void main(String[] args) throws InterruptedException, IOException {
        GuestListener guestListener1 = new GuestListener();
        Client client1;
        Thread thread = new Thread(guestListener1);
        thread.start();
        int c = 0;
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println(++c);
            List<String> hosts = guestListener1.getHosts();
            for (int i = 0; i < hosts.size(); i++) {
                System.out.println(i + 1 + ". " + hosts.get(i));
            }
            int i = sc.nextInt();
            if(i > 0 && i <= hosts.size()) {
//                System.out.println(hosts.get(i-1).split(":")[0] + " " + guestListener1.getMessage(hosts.get(i-1)));
                thread.interrupt();
                client1 = new Client(hosts.get(i-1).split(":")[0], Integer.parseInt(guestListener1.getMessage(hosts.get(i-1)).split(":")[0]));
                break;
            }
        }
        client1.start();
    }
}
