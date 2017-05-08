/**
 * Package location for domain concepts.
 */
package domain;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Represents a data file.
 */
public final class DataFile {

    /**
     * The data file name.
     */
    private DataFileName name;

    /**
     * The binary representation of the data file.
     */
    private byte[] bytes;

    /**
     * Creates an instance of the DataFile.
     *
     * @param name name foi the file.
     * @param bytes binary representation of the file.
     */
    public DataFile(String name, byte[] bytes) {
        if (name == null || bytes == null) {
            throw new IllegalStateException();
        }

        this.name = new DataFileName(name);
        this.bytes = bytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (this.getClass() != o.getClass()) {
            return false;
        }

        final DataFile otherFile = (DataFile) o;
        return this.name.equals(otherFile.name) && Arrays.equals(this.bytes, otherFile.bytes);
    }

    public File getFileFromDataFile() throws FileNotFoundException, IOException {
        File f = new File("data");
        FileOutputStream fileOut = new FileOutputStream(f);
        BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut);
        bufferedOut.write(this.bytes);
        bufferedOut.close();
        return f;
    }

    public byte nameSize() {
        return this.name.length();
    }

    public String name() {
        return this.name.name();
    }
}
