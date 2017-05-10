/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Renato Oliveira 1140822@isep.ipp.pt
 */
public class TcpServer {

    private final int portNumber = 0;

    private ServerSocket serverSock;

    private Socket sockListener;
    /**
     * A stream used to read data from a socket.
     */
    private DataInputStream dataIn;

    public TcpServer() {
        try {
            this.serverSock = new ServerSocket(portNumber);
        } catch (IOException ex) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Reads information regarding the file requested by other user.
     *
     * @return the string containing the information
     * @throws IOException
     */
    private String readFileRequestInfo() throws IOException {
        int fileSize = 0;
        fileSize = dataIn.readInt();
        int requestSize = fileSize;
        byte[] fileData = new byte[requestSize];
        dataIn.readFully(fileData, 0, fileData.length);
        String downloadInfo = Arrays.toString(fileData);
        return downloadInfo;
    }

    public void start() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        sockListener = serverSock.accept();
                        dataIn = new DataInputStream(sockListener.getInputStream());
                         //TODO: OPEN A CONNECTION FOR EACH CLIENT

                    }
                } catch (Exception ex) {
                }
            }
        };
        thread.start();
    }
}
