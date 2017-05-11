/**
 * Package location for connection concepts.
 */
package connection;

import domain.Configuration;
import domain.DataFile;
import persistence.DataFileRepository;
import util.Bytes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Represents a service to make announcements in the network.
 */
public class AnnounceService {

    /**
     * Sends the files names to the network
     *
     * @throws IOException input/output exception
     */
    public List<DataFile> sendFilesNames() throws IOException {

        List<DataFile> filesToAnnounce = DataFileRepository.getSharedFiles();

        // bytes 0 to 3 reserved for udp port and byte 4 reserved to announced files count
        byte startingPosition = 5;

        byte data[] = new byte[UdpConnection.MAXIMUM_BYTES_PAYLOAD];

        byte dataCurrentSize = startingPosition;
        byte countFiles = 0;
        byte pos_index = startingPosition;
        byte fileNameSize;

        int udpPort = Configuration.getUDPPortNumber();

        // FIXME get dynamic tcp port
        int tcpPort = 8888;
        byte tcpPortByte[] = ByteBuffer.allocate(4).putInt(tcpPort).array();
        Bytes.insertArrayIntoArray(data, 0, tcpPortByte);

        for (DataFile file : filesToAnnounce) {
            // if fits add, otherwise send and create new

            fileNameSize = file.nameSize();

            if (dataCurrentSize + fileNameSize + 1 > UdpConnection.MAXIMUM_BYTES_PAYLOAD) {
                data[4] = countFiles;
                UdpConnection.sendBroadcast(data, udpPort);

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

        data[4] = countFiles;
        UdpConnection.sendBroadcast(data, udpPort);

        return filesToAnnounce;
    }
}
