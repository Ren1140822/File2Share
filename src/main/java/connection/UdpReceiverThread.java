/**
 * Package location for connection concepts.
 */
package connection;

import domain.RemoteFile;
import framework.BaseObservable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Observer;
import java.util.Set;

/**
 * Thread responsible for receiving udp announcements.
 *
 * @author Ivo Ferro
 */
public class UdpReceiverThread extends Thread {

    private final BaseObservable observable;

    private final Set<RemoteFile> remoteFiles;

    public UdpReceiverThread(Set<RemoteFile> remoteFiles) {
        this.remoteFiles = remoteFiles;
        this.observable = new BaseObservable();
    }

    public void addObserver(Observer observer) {
        this.observable.addObserver(observer);
    }

    @Override
    public void run() {

        // TODO get udp port from configuration
        int port = 32034;

        byte[] data = new byte[UdpConnection.MAXIMUM_BYTES_PAYLOAD];
        DatagramSocket sock = null;
        try {
            sock = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        DatagramPacket udpPacket = new DatagramPacket(data, data.length);

        udpPacket.setData(data);
        udpPacket.setLength(data.length);

        while (true) {
            try {
                sock.receive(udpPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteBuffer wrapped = ByteBuffer.wrap(data);
            int tcpPort = wrapped.getInt();
            byte announcements = wrapped.get();

            for (int i = 0; i < announcements; i++) {
                byte nameLength = wrapped.get();

                byte[] nameBytes = new byte[nameLength];
                wrapped.get(nameBytes, 0, nameLength);
                String name = null;
                try {
                    name = new String(nameBytes, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                InetAddress address = sock.getInetAddress();

                RemoteFile remoteFile = new RemoteFile(name, address, tcpPort);
                remoteFiles.add(remoteFile);
            }

            observable.activateChanges();
            observable.notifyObservers();
        }
    }
}
