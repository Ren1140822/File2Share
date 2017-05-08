/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import domain.Configuration;
import domain.ConfigurationBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;

/**
 *
 * @author Pedro Fernandes
 */
public class ChangeConfigurationController {
    
    private Configuration configuration;
    
    private static final String DEFAULT_FILENAME = "Config.txt";
    
    public ChangeConfigurationController() throws IOException{
        final StartConfiguration s = new StartConfiguration();
        this.configuration = s.readConfigurationFile();
    }
    
    public Integer currentUDPPortNumber(){
        return this.configuration.getUDPPortNumber();
    }
    
    public Integer currentUDPTimeAnnoucement(){
        return this.configuration.getUDPTimeAnnouncement();
    }
    
    public Integer currentRefreshFileTime(){
        return this.configuration.getRefreshFileTime();
    }
    
    public String currentDownloadFolderName(){
        return this.configuration.getDownloadFolderName().replace("/", "");
    }
    
    public String currentSharedFolderName(){
        return this.configuration.getSharedFolderName().replace("/", "");
    }
    
    public boolean saveConfigurations(Integer udpPort, Integer udpTime,
            Integer refreshTime, String sharedName, String downloadName) throws FileNotFoundException{
        final ConfigurationBuilder builder  = new ConfigurationBuilder();      
        
        builder.withUDPPort(udpPort).withUDPTimeAnnouncement(udpTime)
                .withRefreshFileTime(refreshTime).withSharedFolderName(sharedName)
                .withDownloadFolderName(downloadName);
        
        final Configuration changedConfiguration = builder.buildConfiguration();
        
        if (changedConfiguration == null){
            return false;
        }
        
        createConfigurationFile(changedConfiguration);
        
        return true;
    }
    
    private void createConfigurationFile(Configuration c) throws FileNotFoundException {

            File createFile = new File(DEFAULT_FILENAME);
            Formatter fileFormatter = new Formatter(createFile);

            fileFormatter.format("%s", c.toString());

            fileFormatter.close();

    }
    
}
