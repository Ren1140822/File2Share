/**
 * Package location for domain concepts.
 */
package domain;

import util.Strings;

/**
 * Represent a data file name.
 */
public final class DataFileName {

    /**
     * The name of the file.
     */
    private String name;

    /**
     * Creates an instance of DataFileName.
     *
     * @param name name of the file
     */
    public DataFileName(String name) {
        if (!Strings.isNullOrEmptyOrWhiteSpace(name)) {
            throw new IllegalStateException();
        }

        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (this.getClass() != o.getClass()) {
            return false;
        }

        final DataFileName otherName = (DataFileName) o;
        return this.name.equals(otherName.name);
    }

    public byte length() {
        return (byte) this.name.length();
    }

    public String name() {
        return this.name;
    }
}
