/**
 * Package location for connection concepts.
 */
package connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Represents an UDP connection with utilitary methods.
 *
 * @author Ivo Ferro
 */
public final class UdpConnection {

    /**
     * The UDP maximum payload safe amount to be sent over the internet.
     */
    public static final int MAXIMUM_BYTES_PAYLOAD = 512;

    /**
     * Sends the given data to the broadcast address over the given port.
     *
     * @param data data to be sent
     * @param port port to be sent
     * @throws IOException input/output exception
     */
    public static void sendBroadcast(byte data[], int port) throws IOException {
        InetAddress serverAddress = InetAddress.getByName("255.255.255.255");
        DatagramPacket udpPacket = new DatagramPacket(data, data.length, serverAddress, port);

        udpPacket.setData(data);
        udpPacket.setLength(data.length);

        DatagramSocket sock = new DatagramSocket();
        sock.setBroadcast(true);

        sock.send(udpPacket);

        sock.close();
    }

    /**
     * Sends the given data to the given unicast address over the given port.
     *
     * @param data data to be sent to
     * @param port port to be sent to
     * @param host destination host
     * @throws IOException input/output exception
     */
    public static void sendUnicast(byte data[], int port, String host) throws IOException {
        InetAddress serverAddress = InetAddress.getByName(host);
        DatagramPacket udpPacket = new DatagramPacket(data, data.length, serverAddress, port);

        udpPacket.setData(data);
        udpPacket.setLength(data.length);

        DatagramSocket sock = new DatagramSocket();
        sock.setBroadcast(true);

        sock.send(udpPacket);

        sock.close();
    }
}
