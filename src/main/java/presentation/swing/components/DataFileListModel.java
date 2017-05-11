package presentation.swing.components;

import domain.DataFile;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents a list model for data files.
 *
 * @author Ivo Ferro
 */
public class DataFileListModel extends AbstractListModel<DataFile> {

    /**
     * List of the remote files
     */
    private final List<DataFile> dataFiles;

    /**
     * Creates an instance of DataFileListModel receiving the data files.
     *
     * @param dataFiles data files
     */
    public DataFileListModel(List<DataFile> dataFiles) {
        this.dataFiles = dataFiles;
    }

    /**
     * Creates an instance of DataFileListModel receiving the data files.
     *
     * @param dataFiles data files
     */
    public DataFileListModel(Set<DataFile> dataFiles) {
        this.dataFiles = new ArrayList<>(dataFiles);
    }

    @Override
    public int getSize() {
        return dataFiles.size();
    }

    @Override
    public DataFile getElementAt(int i) {
        return dataFiles.get(i);
    }
}
