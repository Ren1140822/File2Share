/**
 * Package location for domain concepts.
 */
package domain;

/**
 * Represents the configurations of the application behaviors.
 */
public class Configuration {
    
    private String extension;
    private String filename;
    private Integer UDPPortNumber;
    private Integer UDPTimeAnnouncement;
    private Integer refreshFileTime;
    private String sharedFolderName;
    private String downloadFolderName;
    
    private static final String DEFAULT_EXTENSION = ".txt";
    private static final String DEFAULT_FILENAME = "Config";
    private static final Integer DEFAULT_UDP_PORT_NUMBER = 32034;
    private static final Integer DEFAULT_UDP_TIME_ANNOUCEMENT = 30; // seconds
    private static final Integer DEFAULT_REFRESH_FILE_TIME = 45; // seconds
    private static final String DEFAULT_FOLDER_PREFIX = "/";
    private static final String DEFAULT_SHARED_FOLDER_NAME = DEFAULT_FOLDER_PREFIX + "shared";
    private static final String DEFAULT_DOWNLOAD_FOLDER_NAME = DEFAULT_FOLDER_PREFIX + "download";
    
    /**
     * Constructor by default
     */
    private Configuration(){
        this.extension = DEFAULT_EXTENSION;
        this.filename = DEFAULT_FILENAME;
        this.UDPPortNumber = DEFAULT_UDP_PORT_NUMBER;
        this.UDPTimeAnnouncement = DEFAULT_UDP_TIME_ANNOUCEMENT;
        this.refreshFileTime = DEFAULT_REFRESH_FILE_TIME;
        this.sharedFolderName = DEFAULT_SHARED_FOLDER_NAME;
        this.downloadFolderName = DEFAULT_DOWNLOAD_FOLDER_NAME;
    }
    
    public Configuration(Integer UDPPortNumber, Integer UDPTimeAnnouncement,
            Integer refreshFileTime, String sharedFolderName, String downloadFolderName){
        
        if (UDPPortNumber == null || UDPTimeAnnouncement == null || refreshFileTime == null) {
            throw new IllegalStateException();
        }
        
        if (!validateAllNumberValues(UDPPortNumber, UDPTimeAnnouncement, refreshFileTime)){
            throw new IllegalStateException();
        }
        
        if (!validateAllStrings(sharedFolderName, downloadFolderName)){ 
            throw new IllegalStateException();
        }
        
        this.extension = DEFAULT_EXTENSION;
        this.filename = DEFAULT_FILENAME;
        this.UDPPortNumber = UDPPortNumber;
        this.UDPTimeAnnouncement = UDPTimeAnnouncement;
        this.refreshFileTime = refreshFileTime;
        this.extension = DEFAULT_FOLDER_PREFIX + extension;
        this.sharedFolderName = DEFAULT_FOLDER_PREFIX + sharedFolderName;
        this.downloadFolderName = downloadFolderName;
        
    }
    
    /**
     * validate all strings used
     * @param extension
     * @param sharedFolderName
     * @param downloadFolderName
     * @return true if all strings are valid, false if not
     */
    private boolean validateAllStrings(String sharedFolderName, String downloadFolderName){
        if (sharedFolderName == null || sharedFolderName.isEmpty()
                || sharedFolderName.contains(" ")){
            return false;
        }
        if (downloadFolderName == null || downloadFolderName.isEmpty()
                || downloadFolderName.contains(" ")){
            return false;
        }
        return true;
    }    
    
    /**
     * validate all numbers
     * @param UDPPortNumber
     * @param UDPTimeAnnouncement
     * @param refreshFileTime
     * @return true if valid numbers, false if not
     */
    private boolean validateAllNumberValues(Integer UDPPortNumber, Integer UDPTimeAnnouncement,
            Integer refreshFileTime){
        return validateNumber(UDPPortNumber) && validateNumber(UDPTimeAnnouncement) 
                && validateNumber(refreshFileTime);
    }
    
    /**
     * validate negative number insertion
     * @param value
     * @return true if non-negative, false if negative
     */
    private boolean validateNumber(Integer value){
        return !(value < 0);
    }
    
    @Override
    public String toString(){
        return "Filename: " + this.filename + this.extension + 
                "\nUDP Port Number: " + this.UDPPortNumber + 
                "\nUDP Time Annoucement: " + this.UDPTimeAnnouncement + 
                "\nRefresh File Time: " + this.refreshFileTime + 
                "\nShared folder: " + this.sharedFolderName + 
                "\nDownload folder: " + this.downloadFolderName;
    }
    
}
