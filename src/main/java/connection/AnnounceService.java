/**
 * Package location for connection concepts.
 */
package connection;

import domain.Configuration;
import domain.DataFile;
import domain.KnownHosts;
import persistence.DataFileRepository;
import util.Bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;

/**
 * Represents a service to make announcements in the network.
 */
public class AnnounceService {

    /**
     * Sends the files names to the network
     *
     * @throws IOException input/output exception
     */
    public List<DataFile> sendFilesNames(int finalTcpPort) throws IOException {

        List<DataFile> filesToAnnounce = DataFileRepository.getSharedFiles();

        // byte 0 indicates the version
        // bytes 1 to 4 reserved for udp port
        // byte 5 reserved to announced files count
        byte startingPosition = 6, countFilesPosition = 5;

        byte data[] = new byte[UdpConnection.MAXIMUM_BYTES_PAYLOAD];

        // using 1st version of protocol
        data[0] = 1;

        byte dataCurrentSize = startingPosition;
        byte countFiles = 0;
        short pos_index = startingPosition;
        byte fileNameSize;

        int udpPort = Configuration.getUDPPortNumber();

        // FIXME get dynamic tcp port
        int tcpPort = finalTcpPort;
        byte tcpPortByte[] = ByteBuffer.allocate(4).putInt(tcpPort).array();
        Bytes.insertArrayIntoArray(data, 1, tcpPortByte);

        for (DataFile file : filesToAnnounce) {
            // if fits add, otherwise send and create new

            fileNameSize = file.nameSize();

            if (dataCurrentSize + fileNameSize + 1 > UdpConnection.MAXIMUM_BYTES_PAYLOAD) {
                data[countFilesPosition] = countFiles;
                UdpConnection.sendBroadcast(data, udpPort);

                Set<String> knownHosts = KnownHosts.getKnownHosts();
                for (String host : knownHosts) {
                    UdpConnection.sendUnicast(data, udpPort, host);
                }

                System.out.println("Data exceeds the recommended UDP size, compressing the message.");

                pos_index = startingPosition;

                data[pos_index] = fileNameSize;
                pos_index++;

                byte name[] = file.nameBytes();
                Bytes.insertArrayIntoArray(data, pos_index, name);
                pos_index += name.length;
            } else {
                countFiles++;

                data[pos_index] = fileNameSize;
                pos_index++;

                byte name[] = file.nameBytes();
                Bytes.insertArrayIntoArray(data, pos_index, name);
                pos_index += name.length;
            }
        }

        data[countFilesPosition] = countFiles;
        UdpConnection.sendBroadcast(data, udpPort);

        Set<String> knownHosts = KnownHosts.getKnownHosts();
        for (String host : knownHosts) {
            UdpConnection.sendUnicast(data, udpPort, host);
        }

        return filesToAnnounce;
    }
}
