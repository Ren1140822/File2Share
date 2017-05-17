/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Pedro Fernandes
 */
public final class KnownHosts {    
    
    public static final String DEFAULT_FILENAME = "known_hosts.txt";
    
    /**
     * get the known hosts
     * @return KnownHosts
     * @throws FileNotFoundException 
     */
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
    
    /**
     * create known hosts file
     * @throws FileNotFoundException 
     */
    private static void createKnownHostsFile() throws FileNotFoundException {

        final File createFile = new File(DEFAULT_FILENAME);

    }
    
    /**
     * save known host file
     * @param list
     * @return 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static boolean saveKnownHostsFile(Set<String> list) throws FileNotFoundException, IOException{

        try (FileWriter fw = new FileWriter(DEFAULT_FILENAME)) {
            for (String l : list) {
                fw.write(l + "\n");
            }
        }
        return true;
    }
}
