/**
 * Package location for domain concepts.
 */
package domain;

import java.util.Arrays;
import util.Strings;

/**
 * Represents the configurations of the application behaviors.
 */
public class Configuration {

    private static Integer UDPPortNumber;
    private static Integer UDPTimeAnnouncement;
    private static Integer refreshFileTime;
    private static String sharedFolderName;
    private static String downloadFolderName;
    private static boolean ignoreFolders;
    private static boolean ignoreFilesWithoutExtension;
    private static String[] ignoreFiles;
    private static String[] ignoreFilesStartingWith;
    private static String[] ignoreFilesWithExtension;

    private static final Integer DEFAULT_UDP_PORT_NUMBER = 32034;
    private static final Integer DEFAULT_UDP_TIME_ANNOUCEMENT = 30; // seconds
    private static final Integer DEFAULT_REFRESH_FILE_TIME = 45; // seconds
    private static final String DEFAULT_FOLDER_PREFIX = "";
    private static final String DEFAULT_SHARED_FOLDER_NAME = DEFAULT_FOLDER_PREFIX + "shared";
    private static final String DEFAULT_DOWNLOAD_FOLDER_NAME = DEFAULT_FOLDER_PREFIX + "download";
    private static final boolean DEFAULT_IGNORE_FOLDERS = true;
    private static final boolean DEFAULT_IGNORE_FILES_WITHOUT_EXTENSION = true;
    private static final String[] DEFAULT_IGNORE_FILES = {"sys", "etc", "bootmgr.efi"};
    private static final String[] DEFAULT_IGNORE_FILES_STARTING_WITH = {".", "_", "~"};
    private static final String[] DEFAULT_IGNORE_FILES_WITH_EXTENSION = {"store", "db"};
    
    /**
     * Constructor by default
     */
    public Configuration() {
        Configuration.UDPPortNumber = DEFAULT_UDP_PORT_NUMBER;
        Configuration.UDPTimeAnnouncement = DEFAULT_UDP_TIME_ANNOUCEMENT;
        Configuration.refreshFileTime = DEFAULT_REFRESH_FILE_TIME;
        Configuration.sharedFolderName = DEFAULT_SHARED_FOLDER_NAME;
        Configuration.downloadFolderName = DEFAULT_DOWNLOAD_FOLDER_NAME;
        Configuration.ignoreFolders = DEFAULT_IGNORE_FOLDERS;
        Configuration.ignoreFilesWithoutExtension = DEFAULT_IGNORE_FILES_WITHOUT_EXTENSION;
        Configuration.ignoreFiles = DEFAULT_IGNORE_FILES;
        Configuration.ignoreFilesStartingWith = DEFAULT_IGNORE_FILES_STARTING_WITH;
        Configuration.ignoreFilesWithExtension = DEFAULT_IGNORE_FILES_WITH_EXTENSION;
    }

    public Configuration(Integer UDPPortNumber, Integer UDPTimeAnnouncement,
                         Integer refreshFileTime, String sharedFolderName, String downloadFolderName,
                         boolean ignoreFolders, boolean ignoreFilesWithoutExtension, String[] ignoreFiles,
                         String[] ignoreFilesStartingWith, String[] ignoreFilesWithExtension) {

        if (UDPPortNumber == null || UDPTimeAnnouncement == null || refreshFileTime == null) {
            throw new IllegalStateException();
        }
        
        if (ignoreFiles == null || ignoreFilesStartingWith == null || ignoreFilesWithExtension == null) {
            throw new IllegalStateException();
        }

        if (!validateAllNumberValues(UDPPortNumber, UDPTimeAnnouncement, refreshFileTime)) {
            throw new IllegalStateException();
        }

        if (!validateAllStrings(sharedFolderName, downloadFolderName)) {
            throw new IllegalStateException();
        }

        Configuration.UDPPortNumber = UDPPortNumber;
        Configuration.UDPTimeAnnouncement = UDPTimeAnnouncement;
        Configuration.refreshFileTime = refreshFileTime;
        Configuration.sharedFolderName = DEFAULT_FOLDER_PREFIX + sharedFolderName;
        Configuration.downloadFolderName = DEFAULT_FOLDER_PREFIX + downloadFolderName;
        Configuration.ignoreFolders = ignoreFolders;
        Configuration.ignoreFilesWithoutExtension = ignoreFilesWithoutExtension;
        Configuration.ignoreFiles = ignoreFiles;
        Configuration.ignoreFilesStartingWith = ignoreFilesStartingWith;
        Configuration.ignoreFilesWithExtension = ignoreFilesWithExtension;
    }

    /**
     * validate all strings used
     *
     * @param extension
     * @param sharedFolderName
     * @param downloadFolderName
     * @return true if all strings are valid, false if not
     */
    private boolean validateAllStrings(String sharedFolderName, String downloadFolderName) {
        boolean aux1 = !Strings.isNullOrEmptyOrWhiteSpace(sharedFolderName) && !sharedFolderName.contains(" ");
        boolean aux2 = !Strings.isNullOrEmptyOrWhiteSpace(downloadFolderName) && !downloadFolderName.contains(" ");
        return aux1 && aux2;
    }

    /**
     * validate all numbers
     *
     * @param UDPPortNumber
     * @param UDPTimeAnnouncement
     * @param refreshFileTime
     * @return true if valid numbers, false if not
     */
    private boolean validateAllNumberValues(Integer UDPPortNumber, Integer UDPTimeAnnouncement,
                                            Integer refreshFileTime) {
        return validateNumber(UDPPortNumber) && validateNumber(UDPTimeAnnouncement)
                && validateNumber(refreshFileTime);
    }

    /**
     * validate negative number insertion
     *
     * @param value
     * @return true if non-negative, false if negative
     */
    private boolean validateNumber(Integer value) {
        return !(value < 0);
    }

    @Override
    public String toString() {
        return "udp_port_number: " + getUDPPortNumber() +
                "\nudp_time_announcement: " + getUDPTimeAnnouncement() +
                "\nrefresh_file_time: " + getRefreshFileTime() +
                "\nshared_folder: " + getSharedFolderName() +
                "\ndownload_folder: " + getDownloadFolderName() +
                "\nget_ignore_folders: " + getIgnoreFolders() +
                "\nignore_files_without_extension: " + getIgnoreFilesWithoutExtension() +
                "\nignore_files: " + Arrays.toString(getIgnoreFiles()) + 
                "\nignore_files_starting_with: " + Arrays.toString(getIgnoreFilesStartingWith()) +
                "\nignore_files_with_extension: " + Arrays.toString(getIgnoreFilesWithExtension());
    }

    /**
     * @return the UDPPortNumber
     */
    public static Integer getUDPPortNumber() {
        return UDPPortNumber;
    }

    /**
     * @return the UDPTimeAnnouncement
     */
    public static Integer getUDPTimeAnnouncement() {
        return UDPTimeAnnouncement;
    }

    /**
     * @return the refreshFileTime
     */
    public static Integer getRefreshFileTime() {
        return refreshFileTime;
    }

    /**
     * @return the sharedFolderName
     */
    public static String getSharedFolderName() {
        return sharedFolderName;
    }

    /**
     * @return the downloadFolderName
     */
    public static String getDownloadFolderName() {
        return downloadFolderName;
    }
    
    /**
     * @return the ignoreFolders
     */
    public static boolean getIgnoreFolders() {
        return ignoreFolders;
    }
    
    /**
     * @return the ignoreFilesWithoutExtension
     */
    public static boolean getIgnoreFilesWithoutExtension() {
        return ignoreFilesWithoutExtension;
    }
    
    /**
     * @return the files to be ignored
     */
    public static String[] getIgnoreFiles() {
        return ignoreFiles;
    }
    
    /**
     * @return the files starting with (e.g. "_") to be ignored
     */
    public static String[] getIgnoreFilesStartingWith() {
        return ignoreFilesStartingWith;
    }
    
    /**
     * @return the files starting extension (e.g. "sys") to be ignored
     */
    public static String[] getIgnoreFilesWithExtension() {
        return ignoreFilesWithExtension;
    }

}
