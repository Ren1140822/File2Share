package domain;

import util.Strings;

import java.net.InetAddress;

/**
 * Represents the information of a remote file.
 *
 * @author Ivo Ferro
 */
public class RemoteFile implements Comparable<RemoteFile> {

    String name;

    InetAddress address;

    Integer tcpPort;

    public RemoteFile(String name, InetAddress address, Integer tcpPort) {
        if (Strings.isNullOrEmptyOrWhiteSpace(name)
                || address == null
                || tcpPort == null) {
            throw new IllegalStateException("Fields can't be null");
        }

        this.name = name;
        this.address = address;
        this.tcpPort = tcpPort;
    }

    @Override
    public int compareTo(RemoteFile remoteFile) {
        int nameComp = this.name.compareTo(remoteFile.name);
        return (nameComp != 0) ? (nameComp) : (this.address.getHostAddress().compareTo(remoteFile.address.getHostAddress()));
    }

    public String getName() {
        return name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public Integer getTcpPort() {
        return tcpPort;
    }
}
