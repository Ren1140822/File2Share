/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;
import util.Strings;

/**
 *
 * @author Pedro Fernandes
 */
public final class KnownHosts {    
    
    public static final String DEFAULT_FILENAME = "known_hosts.txt";
    
    public static List<String> getKnownHosts(File file) throws FileNotFoundException{
        List<String> list = new ArrayList<>();
        try {
            try (Scanner fInput = new Scanner(new File(DEFAULT_FILENAME))) {
                while (fInput.hasNextLine()) {
                    String str = fInput.nextLine();
                    if (!str.isEmpty() || !str.startsWith("#")) {
                        list.add(str);
                    }
                }
            }
            return list;
        } catch (FileNotFoundException ex) {
            createKnownHostsFile();
            return list;
        }

    }
    
    public static List<String> getKnownHosts(){
        List<String> list = new ArrayList<>();
        return list;
    }
    
    public List<String> addKnownHosts(String host){
        if (!validateHost(host)){
            return KnownHosts.getKnownHosts();
        }
        KnownHosts.getKnownHosts().add(host);
        return KnownHosts.getKnownHosts();
    }
    
    public List<String> removeKnownHosts(String host){
        if (!validateHost(host)){
            return KnownHosts.getKnownHosts();
        }
        KnownHosts.getKnownHosts().remove(host);
        return KnownHosts.getKnownHosts();
    }
    
    private boolean validateHost(String host){
        return !Strings.isNullOrEmptyOrWhiteSpace(host);
    }
    
    private static void createKnownHostsFile() throws FileNotFoundException {

        File createFile = new File(DEFAULT_FILENAME);
        try (Formatter fileFormatter = new Formatter(createFile)) {

            fileFormatter.format("");
        }

    }
}
