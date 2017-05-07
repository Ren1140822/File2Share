/**
 * Package location for connection concepts.
 */
package connection;

import java.io.IOException;

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

        // TODO read file names

        // TODO calculate how many packets are necessary
        int numberOfPackets = 2;

        for (int i = 0; i < numberOfPackets; i++) {
            // TODO construct the packet

            // TODO send the packet
        }
    }
}
