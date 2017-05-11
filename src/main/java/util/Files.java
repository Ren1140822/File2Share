package util;

/**
 * Utility class to manage files.
 *
 * @author Ivo Ferro
 */
public final class Files {

    /**
     * Checks if a given file has extension.
     *
     * @param fileName the file name
     * @return true if the file has extension, false otherwise
     */
    public static boolean hasExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        return i > p;
    }

    /**
     * Gets the extension of a file.
     *
     * @param fileName the file name
     * @return the extension, or empty string in case the file has no extension
     */
    public static String getExtension(String fileName) {
        String extension = "";

        if (hasExtension(fileName)) {
            int i = fileName.lastIndexOf('.');
            extension = fileName.substring(i + 1);
        }

        return extension;
    }
}
