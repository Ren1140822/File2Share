/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import util.Strings;

/**
 *
 * @author Pedro Fernandes
 */
public final class KnownHosts {    
    
    public static final String DEFAULT_FILENAME = "known_hosts.txt";
    
    public static Set<String> getKnownHosts() throws FileNotFoundException{
        Set<String> list = new TreeSet<>();
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
    
    public Set<String> addKnownHosts(String host) throws FileNotFoundException{
        if (!validateHost(host)){
            return KnownHosts.getKnownHosts();
        }
        Set<String> list = KnownHosts.getKnownHosts();
        list.add(host);
        saveKnownHostsFile(list);
        return list;
    }
    
    public Set<String> removeKnownHosts(String host) throws FileNotFoundException{
        if (!validateHost(host)){
            return KnownHosts.getKnownHosts();
        }
        
        Set<String> list = KnownHosts.getKnownHosts();
        
        if(!list.isEmpty() || list.contains(host)){   
            list.remove(host);
            saveKnownHostsFile(list);
        }
        return list;
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
    
    public static boolean saveKnownHostsFile(Set<String> list) throws FileNotFoundException{
        File createFile = new File(DEFAULT_FILENAME);
        try (Formatter fileFormatter = new Formatter(createFile)) {

            fileFormatter.format("%s", list.toString());
        }
        return true;
    }
}
