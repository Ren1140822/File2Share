/**
 * Package location for connection concepts.
 */
package connection;

import domain.RemoteFile;
import framework.BaseObservable;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Iterator;
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

    private Observer observer;

    public UdpReceiverThread(Set<RemoteFile> remoteFiles) {
        this.remoteFiles = remoteFiles;
        this.observable = new BaseObservable();
    }

    public void addObserver(Observer observer) {
        this.observer = observer;
        this.observable.addObserver(observer);
    }

    @Override
    public void run() {

        try {
            // FIXME get udp port from configuration
            int port = 32034;

            byte[] data = new byte[UdpConnection.MAXIMUM_BYTES_PAYLOAD];
            DatagramSocket sock = null;
            sock = new DatagramSocket(port);

            DatagramPacket udpPacket = new DatagramPacket(data, data.length);

            udpPacket.setData(data);
            udpPacket.setLength(data.length);

            while (true) {
                try {
                    sock.receive(udpPacket);

                    ByteBuffer wrapped = ByteBuffer.wrap(data);
                    int tcpPort = wrapped.getInt();
                    byte announcements = wrapped.get();

                    for (int i = 0; i < announcements; i++) {
                        try {
                            byte nameLength = wrapped.get();

                            byte[] nameBytes = new byte[nameLength];
                            wrapped.get(nameBytes, 0, nameLength);
                            String name = null;
                            name = new String(nameBytes, "UTF-8");

                            InetAddress address = udpPacket.getAddress();

                            RemoteFile remoteFile = new RemoteFile(name, address, tcpPort);

                            remoteFile.addObserver(observer);

                            if (remoteFiles.contains(remoteFile)) {
                                for (Iterator<RemoteFile> it = remoteFiles.iterator(); it.hasNext(); ) {
                                    RemoteFile copy = it.next();
                                    if (copy.equals(remoteFile)) {
                                        copy.cancelTimer();
                                        copy.activateTimer();
                                    }
                                }
                            } else {
                                remoteFiles.add(remoteFile);
                                remoteFile.activateTimer();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    observable.activateChanges();
                    observable.notifyObservers();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
