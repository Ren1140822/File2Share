package presentation.swing.components;

import domain.RemoteFile;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents a list model for remote files.
 *
 * @author Ivo Ferro
 */
public class RemoteFileListModel extends AbstractListModel<RemoteFile> {

    /**
     * List of the remote files
     */
    private final List<RemoteFile> remoteFiles;

    /**
     * Creates an instance of RemoteFileListModel receiving the remote files.
     *
     * @param remoteFiles remote files
     */
    public RemoteFileListModel(List<RemoteFile> remoteFiles) {
        this.remoteFiles = remoteFiles;
    }

    /**
     * Creates an instance of RemoteFileListModel receiving the remote files.
     *
     * @param remoteFiles remote files
     */
    public RemoteFileListModel(Set<RemoteFile> remoteFiles) {
        this.remoteFiles = new ArrayList<>(remoteFiles);
    }

    @Override
    public int getSize() {
        return remoteFiles.size();
    }

    @Override
    public RemoteFile getElementAt(int i) {
        return remoteFiles.get(i);
    }
}
