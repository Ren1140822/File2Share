/**
 * Package location for connection concepts.
 */
package connection;

import domain.DataFile;
import persistence.DataFileRepository;

import java.io.IOException;
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
    public void sendFilesNames() throws IOException {

        List<DataFile> filesToAnnounce = DataFileRepository.getSharedFiles();

        byte data[] = new byte[UdpConnection.MAXIMUM_BYTES_PAYLOAD];
        byte currentSize = 1, countFiles = 0, pos_index = 5, currentFileSize;

        // FIXME get dynamic tcp port
        int tcpPort = 9999;
        // TODO add tcp port to the first 4 bytes of message

        for (DataFile file : filesToAnnounce) {
            // if fits add, otherwise send and create new

            currentFileSize = file.nameSize();

            if (currentSize + currentFileSize + 1 <= UdpConnection.MAXIMUM_BYTES_PAYLOAD) {
                data[4] = countFiles;
                // TODO send packet
                // TODO reset counters
                // TODO create new packet
            } else {
                countFiles++;
                data[pos_index] = currentFileSize;
                // TODO update counters
            }

            // TODO send packet
        }
    }
}
