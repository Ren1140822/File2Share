/**
 * Package location for domain concepts.
 */
package domain;

import util.Strings;

/**
 * Represents the configurations of the application behaviors.
 */
public class Configuration {
    
    private Integer UDPPortNumber;
    private Integer UDPTimeAnnouncement;
    private Integer refreshFileTime;
    private String sharedFolderName;
    private String downloadFolderName;
    
    
    private static final Integer DEFAULT_UDP_PORT_NUMBER = 32034;
    private static final Integer DEFAULT_UDP_TIME_ANNOUCEMENT = 30; // seconds
    private static final Integer DEFAULT_REFRESH_FILE_TIME = 45; // seconds
    private static final String DEFAULT_FOLDER_PREFIX = "/";
    private static final String DEFAULT_SHARED_FOLDER_NAME = DEFAULT_FOLDER_PREFIX + "shared";
    private static final String DEFAULT_DOWNLOAD_FOLDER_NAME = DEFAULT_FOLDER_PREFIX + "download";
    
    /**
     * Constructor by default
     */
    public Configuration(){
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
        
        this.UDPPortNumber = UDPPortNumber;
        this.UDPTimeAnnouncement = UDPTimeAnnouncement;
        this.refreshFileTime = refreshFileTime;
        this.sharedFolderName = DEFAULT_FOLDER_PREFIX + sharedFolderName;
        this.downloadFolderName = DEFAULT_FOLDER_PREFIX + downloadFolderName;
        
    }
    
    /**
     * validate all strings used
     * @param extension
     * @param sharedFolderName
     * @param downloadFolderName
     * @return true if all strings are valid, false if not
     */
    private boolean validateAllStrings(String sharedFolderName, String downloadFolderName){
        boolean aux1 = Strings.isNullOrEmptyOrWhiteSpace(sharedFolderName) && !sharedFolderName.contains(" ");
        boolean aux2 = Strings.isNullOrEmptyOrWhiteSpace(downloadFolderName) && !downloadFolderName.contains(" ");
        return aux1 && aux2;
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
        return  "udp_port_number: " + this.UDPPortNumber + 
                "\nudp_time_annoucement: " + this.UDPTimeAnnouncement + 
                "\nrefresh_file_time: " + this.refreshFileTime + 
                "\nshared_folder: " + this.sharedFolderName + 
                "\ndownload_folder: " + this.downloadFolderName;
    }
    
}
