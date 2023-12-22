package Client.Network;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class HostAdvertiser implements Runnable {

    private final int broadcastPort = 4524;
    private final int interval = 1000;

    private List<InetAddress> broadcasts;

    private DatagramSocket UDPSocket;

    private int sessionPort;

    private boolean end = false;

    public void setEnd(boolean end) {
        this.end = end;
    }

    public HostAdvertiser(int port) {
        sessionPort = port;
        broadcasts = getBroadcasts();
        try {
            UDPSocket = new DatagramSocket();
            UDPSocket.setBroadcast(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<InetAddress> getBroadcasts() {
        List<InetAddress> broadcasts = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();

                for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast != null) broadcasts.add(broadcast);
                }
            }
        } catch (SocketException e) {
            return broadcasts;
        }
        return broadcasts;
    }

    @Override
    public void run() {
        if (broadcasts.isEmpty()) {
            System.out.println("No broadcasts found");
            return;
        }
//        for (InetAddress broadcast : broadcasts) {
//            System.out.println(broadcast.getHostAddress());
//        }
        try {
            int c = 0;
            while (!end) {
                String message = sessionPort + "";
                byte[] buffer = message.getBytes();
                for (InetAddress broadcast : broadcasts) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcast, broadcastPort);
                    UDPSocket.send(packet);
                    System.out.println("Sent packet #" + ++c);
                }
                Thread.sleep(interval);
            }
        } catch (InterruptedException | IOException e) {
            byte[] buffer = "DISCONNECT".getBytes();
            for (InetAddress broadcast : broadcasts) {
                try {
                    UDPSocket.send(new DatagramPacket(buffer, buffer.length, broadcast, broadcastPort));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            System.out.println("Interrupted");
        }
    }
}
