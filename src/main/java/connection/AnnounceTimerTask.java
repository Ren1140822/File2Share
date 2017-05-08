package connection;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Announce timer task that announces the files names
 *
 * @author Ivo Ferro
 */
public class AnnounceTimerTask extends TimerTask {

    @Override
    public void run() {
        AnnounceService announceService = new AnnounceService();

        try {
            announceService.sendFilesNames();
            System.out.println("Announce sent.");
        } catch (IOException e) {
            System.out.println("Announce failed.");
            // It's not critical if the announce is not sent
        }
    }

}