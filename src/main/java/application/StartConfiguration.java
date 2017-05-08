/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import domain.Configuration;
import domain.ConfigurationBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

/**
 *
 * Starts the configurations of the application when system is booting.
 */
public final class StartConfiguration {

    private String filename;
    private Configuration configuration;
    private ConfigurationBuilder builder;

    private static final String DEFAULT_FILENAME = "Config.txt";

    public StartConfiguration() throws IOException {
        this.filename = DEFAULT_FILENAME;

        //createConfig0urationFile(configuration);
        this.configuration = readConfigurationFile(DEFAULT_FILENAME);
        System.out.println("\n\n" + configuration.toString() + "\n\n");
    }
    
    /**
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public Configuration readConfigurationFile(String fileName) throws IOException {

        this.builder = new ConfigurationBuilder();

        List<String> rows = readLines(fileName);
        if(rows == null){
            return configuration = new Configuration();
        }
        
        Integer udpPort = Integer.parseInt(rows.get(0));
        Integer udpTime = Integer.parseInt(rows.get(1));
        Integer refreshTime = Integer.parseInt(rows.get(2));
        String sharedName = rows.get(3).replace("/", "");
        String downloadName = rows.get(4).replace("/", "");

        builder.withUDPPort(udpPort).withUDPTimeAnnouncement(udpTime)
                .withRefreshFileTime(refreshTime).withSharedFolderName(sharedName)
                .withDownloadFolderName(downloadName);

        return configuration = builder.buildConfiguration();

    }

    private List<String> readLines(String fileName) throws FileNotFoundException, IOException {
        try {
            File file = new File(fileName);
            List<String> rows = new ArrayList<String>();
            BufferedReader read = new BufferedReader(new FileReader(file));

            String row;

            while ((row = read.readLine()) != null) {
                if (row.length() > 0) {
                    String[] temp = row.split(" ");
                    rows.add(temp[1].trim());
                }
            }
            return rows;
        } catch (FileNotFoundException e) {
            createConfigurationFile();
        } catch (IOException io) {
            createConfigurationFile();
        }

        return null;
    }

    private void createConfigurationFile() throws FileNotFoundException {

            File createFile = new File(DEFAULT_FILENAME);
            Formatter fileFormatter = new Formatter(createFile);

            configuration = new Configuration();

            fileFormatter.format("%s", configuration.toString());

            fileFormatter.close();

    }

}
