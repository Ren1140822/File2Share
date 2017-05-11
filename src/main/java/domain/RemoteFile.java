package domain;

import util.Strings;

import java.net.InetAddress;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents the information of a remote file.
 *
 * @author Ivo Ferro
 */
public class RemoteFile extends Observable implements Comparable<RemoteFile> {

    private String name;

    private InetAddress address;

    private Integer tcpPort;

    private Timer timer;

    private TimerTask timerTask;

    public RemoteFile(String name, InetAddress address, Integer tcpPort) {
        if (Strings.isNullOrEmptyOrWhiteSpace(name)
                || address == null
                || tcpPort == null) {
            throw new IllegalStateException("Fields can't be null");
        }

        this.name = name;
        this.address = address;
        this.tcpPort = tcpPort;
        this.timer = new Timer();
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

    public void activateTimer() {
        this.timer = new Timer();
        this.timerTask = new TimerTask() {
            @Override
            public void run() {
                setChanged();
                notifyObservers();
            }
        };
        this.timer.schedule(timerTask, 45 * 1000);
    }

    public void cancelTimer() {
        this.timerTask.cancel();
        this.timer.cancel();
        this.timer.purge();
    }

    @Override
    public int compareTo(RemoteFile remoteFile) {
        int nameComp = this.name.compareTo(remoteFile.name);
        return (nameComp != 0) ? (nameComp) : (this.address.getHostAddress().compareTo(remoteFile.address.getHostAddress()));
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + tcpPort.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RemoteFile that = (RemoteFile) o;

        if (!name.equals(that.name)) return false;
        if (!address.equals(that.address)) return false;
        return tcpPort.equals(that.tcpPort);
    }
}
