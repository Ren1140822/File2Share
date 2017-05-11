/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import domain.Configuration;
import domain.ConfigurationBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * Starts the configurations of the application when system is booting.
 */
public final class StartConfiguration {

    private Configuration configuration;
    private ConfigurationBuilder builder;

    private static final String DEFAULT_FILENAME = "Config.txt";

    public StartConfiguration() throws IOException {
        this.configuration = readConfigurationFile();
    }

    /**
     * @return
     * @throws java.io.IOException
     */
    public Configuration readConfigurationFile() throws IOException {

        this.builder = new ConfigurationBuilder();
        
        String aux;

        List<String> rows = readLines(DEFAULT_FILENAME);
        if (rows == null) {
            return configuration = new Configuration();
        }

        Integer udpPort = Integer.parseInt(rows.get(0));
        Integer udpTime = Integer.parseInt(rows.get(1));
        Integer refreshTime = Integer.parseInt(rows.get(2));
        String sharedName = rows.get(3).replace("/", "");
        String downloadName = rows.get(4).replace("/", "");
        boolean ignoreFolders = Boolean.parseBoolean(rows.get(5));
        boolean ignoreFilesWithoutExtension = Boolean.parseBoolean(rows.get(6));
        
        aux = rows.get(7).replace("[", "");
        aux = aux.replace("]", "");
        String[] ignoreFiles = aux.split(", ");
        
        aux = rows.get(8).replace("[", "");
        aux = aux.replace("]", "");
        String[] ignoreFilesStartingWith = aux.split(", ");
        
        aux = rows.get(9).replace("[", "");
        aux = aux.replace("]", "");
        String[] ignoreFilesWithExtension= aux.split(", ");

        
        builder.withUDPPort(udpPort).withUDPTimeAnnouncement(udpTime)
                .withRefreshFileTime(refreshTime).withSharedFolderName(sharedName)
                .withDownloadFolderName(downloadName).withIgnoreFolders(ignoreFolders)
                .withIgnoreFilesWithoutExtension(ignoreFilesWithoutExtension)
                .withIgnoreFiles(ignoreFiles)
                .withIgnoreFilesStartingWith(ignoreFilesStartingWith)
                .withIgnoreFilesWithExtension(ignoreFilesWithExtension);
        
        return configuration = builder.buildConfiguration();

    }

    private List<String> readLines(String fileName) throws FileNotFoundException, IOException {
        try {
            File file = new File(fileName);
            List<String> rows = new ArrayList<>();
            BufferedReader read = new BufferedReader(new FileReader(file));

            String row;

            while ((row = read.readLine()) != null) {
                if (row.length() > 0) {
                    String[] temp = row.split(": ");
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
        try (Formatter fileFormatter = new Formatter(createFile)) {
            configuration = new Configuration();
            
            fileFormatter.format("%s", configuration.toString());
        }

    }

}
