/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import application.CommunicationController;
import domain.DataFile;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 *
 * @author Renato Oliveira 1140822@isep.ipp.pt
 */
public class main {
 static CommunicationController ctrl = new CommunicationController("127.0.0.1");
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        DataFile dFile;
        File file = new File("D:/file.pdf");
       
        InputStream in = new FileInputStream(file);
        BufferedInputStream bin = new BufferedInputStream(in);
        byte[] bytes = new byte[bin.available()];
        bin.read(bytes);
        dFile = new DataFile("File", bytes);
       

      ctrl.downloadDataFile("ReadFile", "D:/newFile.jpg");
        ctrl.sendDataFileSize(dFile);
        ctrl.sendDataFile(dFile);
    }
}
