/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import domain.DataFile;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Renato Oliveira 1140822@isep.ipp.pt
 */
public class CommunicationController {

    private TcpConnection tcpConnection;

    public CommunicationController(String destinationIP) {
        try {
            this.tcpConnection = new TcpConnection(destinationIP);
        } catch (IOException ex) {
            Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean sendDataFileSize(DataFile dataFile) {

        try {
            File tmpFile = dataFile.getFileFromDataFile();
            tcpConnection.sendFileSize(tmpFile);

        } catch (IOException ex) {
            Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean sendDataFile(DataFile dataFile) {

        try {
            File tmpFile = dataFile.getFileFromDataFile();

            tcpConnection.sendFile(tmpFile);
        } catch (IOException ex) {
            Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean downloadDataFile(String fileName, String path) {
        final String threadFileName = fileName;
        final String threadFilePath = path;
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    int fileSize;
                    fileSize = tcpConnection.readFileSize();
                    tcpConnection.downloadFile(threadFileName, threadFilePath, fileSize);
                } catch (IOException ex) {
                    Logger.getLogger(CommunicationController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        thread.start();

        return true;
    }

}
