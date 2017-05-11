package connection;

import domain.DataFile;
import framework.BaseObservable;

import java.io.IOException;
import java.util.Observer;
import java.util.Set;
import java.util.TimerTask;

/**
 * Announce timer task that announces the files names
 *
 * @author Ivo Ferro
 */
public class AnnounceTimerTask extends TimerTask {

    private final BaseObservable observable;

    private final Set<DataFile> dataFiles;

    public AnnounceTimerTask(Set<DataFile> dataFiles) {
        this.dataFiles = dataFiles;
        observable = new BaseObservable();
    }

    public void addObserver(Observer observer) {
        this.observable.addObserver(observer);
    }

    @Override
    public void run() {
        AnnounceService announceService = new AnnounceService();

        try {
            this.dataFiles.clear();
            this.dataFiles.addAll(announceService.sendFilesNames());
            observable.activateChanges();
            observable.notifyObservers();
        } catch (IOException e) {
            e.printStackTrace();
            // It's not critical if the announce is not sent
        }
    }

}