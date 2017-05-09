/**
 * Package location for util concepts.
 */
package util;

/**
 * Util class to manage strings.
 *
 * @author Ivo Ferro
 */
public final class Bytes {

    /**
     * Insert a given bytes array into another array, starting at a given destination position.
     *
     * @param dest         destination array to be filled with the new array
     * @param destStartPos the destination position to start inserting into
     * @param source       the source array to be inserted
     */
    public static void insertArrayIntoArray(byte[] dest, int destStartPos, byte[] source) {
        for (int i = 0; i < source.length; i++) {
            dest[destStartPos] = source[i];
            destStartPos++;
        }
    }
}
