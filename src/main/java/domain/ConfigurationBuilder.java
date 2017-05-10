/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import util.Strings;

/**
 * a factory for configuration file
 */
public class ConfigurationBuilder {

    private Integer UDPPortNumber;
    private Integer UDPTimeAnnouncement;
    private Integer refreshFileTime;
    private String sharedFolderName;
    private String downloadFolderName;

    public ConfigurationBuilder() {
    }

    public ConfigurationBuilder withUDPPort(Integer UDPPortNumber) {
        this.UDPPortNumber = UDPPortNumber;
        if (!validateNumber(UDPPortNumber)) {
            throw new IllegalStateException("Invalid UDP Port number!");
        }
        return this;
    }

    public ConfigurationBuilder withUDPTimeAnnouncement(Integer UDPTimeAnnouncement) {
        this.UDPTimeAnnouncement = UDPTimeAnnouncement;
        if (!validateNumber(UDPTimeAnnouncement)) {
            throw new IllegalStateException("Invalid UDP time annoucement!");
        }
        return this;
    }

    public ConfigurationBuilder withRefreshFileTime(Integer refreshFileTime) {
        this.refreshFileTime = refreshFileTime;
        if (!validateNumber(refreshFileTime)) {
            throw new IllegalStateException("Invalid refresh file time!");
        }
        return this;
    }

    public ConfigurationBuilder withSharedFolderName(String sharedFolderName) {
        this.sharedFolderName = sharedFolderName;
        if ((Strings.isNullOrEmptyOrWhiteSpace(sharedFolderName)) || (sharedFolderName.contains(" "))) {
            throw new IllegalStateException("Invalid Shared Folder name!");
        }
        return this;
    }

    public ConfigurationBuilder withDownloadFolderName(String downloadFolderName) {
        this.downloadFolderName = downloadFolderName;
        if ((Strings.isNullOrEmptyOrWhiteSpace(downloadFolderName)) || (downloadFolderName.contains(" "))) {
            throw new IllegalStateException("Invalid Download Folder name!");
        }
        return this;
    }

    public Configuration buildConfiguration() {
        if (this == null) {
            return new Configuration();
        } else {
            return new Configuration(this.UDPPortNumber, this.UDPTimeAnnouncement,
                    this.refreshFileTime, this.sharedFolderName, this.downloadFolderName);
        }
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

}
