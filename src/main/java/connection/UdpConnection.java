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
     * Receives data from a given port.
     *
     * @param port port to receive the data
     * @return received data
     * @throws IOException input/output exception
     */
    public static byte[] receive(int port) throws IOException {
        byte[] data = new byte[MAXIMUM_BYTES_PAYLOAD];
        DatagramSocket sock = new DatagramSocket(port);

        DatagramPacket udpPacket = new DatagramPacket(data, data.length);

        udpPacket.setData(data);
        udpPacket.setLength(data.length);
        sock.receive(udpPacket);

        return data;
    }
}
