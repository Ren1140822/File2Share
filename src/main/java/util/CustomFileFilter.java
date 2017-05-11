package util;

import java.io.File;
import java.io.FileFilter;

/**
 * Represents a custom file filter.
 *
 * @author Ivo Ferro
 */
public class CustomFileFilter implements FileFilter {

    @Override
    public boolean accept(File file) {

        // FIXME get this properties from configuration file
        boolean ignoreFolders = true;
        boolean ignoreFilesWithoutExtension = true;
        String[] ignoreFiles = {"sys", "etc", "bootmgr.efi"};
        String[] ignoreFilesStartingWith = {".", "_", "~"};
        String[] ignoreFilesWithExtension = {"store", "db"};

        String fileName = file.getName();

        if (ignoreFolders) {
            if (!file.isFile()) {
                return false;
            }
        }

        if (ignoreFilesWithoutExtension) {
            if (!Files.hasExtension(fileName)) {
                return false;
            }
        }

        for (int i = 0; i < ignoreFiles.length; i++) {
            if (fileName.equals(ignoreFiles[i])) {
                return false;
            }
        }

        for (int i = 0; i < ignoreFilesStartingWith.length; i++) {
            if (fileName.startsWith(ignoreFilesStartingWith[i])) {
                return false;
            }
        }

        if (Files.hasExtension(fileName)) {
            String extension = Files.getExtension(fileName);
            for (int i = 0; i < ignoreFilesWithExtension.length; i++) {
                if (extension.equals(ignoreFilesWithExtension[i])) {
                    return false;
                }
            }
        }

        return true;
    }
}
