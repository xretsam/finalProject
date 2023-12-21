package Client.Network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class GuestListener implements Runnable{

    private TreeMap<String, String> hosts = new TreeMap<>();
    public List<String> getHosts() {
        return new Vector<>(hosts.keySet());
    }
    public String getMessage(String key){
        return hosts.get(key);
    }
    @Override
    public void run() {
        final int port = 4524;
        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                InetAddress senderAddress = packet.getAddress();
                int senderPort = packet.getPort();
                if(message.equals("DISCONNECT")) {
                    hosts.remove(senderAddress.getHostAddress() + ":" + senderPort);
                    System.out.println("removed host");
                }
                else hosts.put(senderAddress.getHostAddress() + ":" + senderPort, message);
//                System.out.println("Received broadcast message from " + senderAddress + ":" + senderPort + ": " + message);
                //TODO : removal of dead hosts
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}