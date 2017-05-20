/**
 * Package location for connection concepts.
 */
package connection;

import domain.Configuration;
import domain.RemoteFile;
import framework.BaseObservable;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Enumeration;
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
            int port = Configuration.getUDPPortNumber();

            byte[] data = new byte[UdpConnection.MAXIMUM_BYTES_PAYLOAD];
            DatagramSocket sock = null;
            sock = new DatagramSocket(port);

            DatagramPacket udpPacket = new DatagramPacket(data, data.length);

            udpPacket.setData(data);
            udpPacket.setLength(data.length);

            while (true) {
                try {
                    sock.receive(udpPacket);

                    InetAddress address = udpPacket.getAddress();
                    if (!isLocalAddress(address)) {

                        ByteBuffer wrapped = ByteBuffer.wrap(data);

                        byte protocolVersion = wrapped.get();
                        if (protocolVersion != 1) {
                            System.out.println("Protocol only supports version 1.");
                            continue;
                        }

                        int tcpPort = wrapped.getInt();
                        byte announcements = wrapped.get();

                        for (int i = 0; i < announcements; i++) {
                            try {
                                byte nameLength = wrapped.get();

                                byte[] nameBytes = new byte[nameLength];
                                wrapped.get(nameBytes, 0, nameLength);
                                String name = null;
                                name = new String(nameBytes, "UTF-8");

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
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private boolean isLocalAddress(InetAddress address) throws SocketException {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                if (i.equals(address)) {
                    return true;
                }
            }
        }
        return false;
    }
}
