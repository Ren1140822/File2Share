/**
 * Package location for persistence concepts.
 */
package persistence;

import domain.DataFile;

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

        // FIXME get location from configuration file
        String directory = "shared";
        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String fileName = file.getName();
                byte[] data = Files.readAllBytes(file.toPath());
                // FIXME if the file has an extension to be ignored, ignore it
                dataFiles.add(new DataFile(fileName, data));
            }
        }

        return dataFiles;
    }
}
