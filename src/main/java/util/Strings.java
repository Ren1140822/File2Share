/**
 * Package location for util concepts.
 */
package util;

/**
 * Util class to manage strings.
 */
public final class Strings {

    /**
     * Checks if a given string is null or empty.
     *
     * @param s string to be checked.
     * @return true if it is null or empty or white space.
     */
    public static boolean isNullOrEmptyOrWhiteSpace(String s) {
        return s == null ? true : s.trim().length() <= 0;
    }
}
