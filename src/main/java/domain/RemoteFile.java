package domain;

import util.Strings;

import java.net.InetAddress;

/**
 * Represents the information of a remote file.
 *
 * @author Ivo Ferro
 */
public class RemoteFile {

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
}
