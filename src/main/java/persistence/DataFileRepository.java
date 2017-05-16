/**
 * Package location for persistence concepts.
 */
package persistence;

import domain.Configuration;
import domain.DataFile;
import util.CustomFileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository to manage files.
 *
 * @author Ivo Ferro
 */
public class DataFileRepository {

    public static List<DataFile> getSharedFiles() throws IOException {

        List<DataFile> dataFiles = new ArrayList<>();

        String directory = Configuration.getSharedFolderName();
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles(new CustomFileFilter());

        for (File file : listOfFiles) {
            String fileName = file.getName();
            byte[] data = Files.readAllBytes(file.toPath());
            dataFiles.add(new DataFile(fileName, data));
        }

        return dataFiles;
    }

    public static List<DataFile> getDownloadedFiles() throws IOException {

        List<DataFile> dataFiles = new ArrayList<>();

        String directory = Configuration.getDownloadFolderName();
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            String fileName = file.getName();
            byte[] data = Files.readAllBytes(file.toPath());
            dataFiles.add(new DataFile(fileName, data));
        }

        return dataFiles;
    }
}
