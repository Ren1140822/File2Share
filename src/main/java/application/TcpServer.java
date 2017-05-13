/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import connection.TcpClientThread;
import domain.Configuration;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
 

    public TcpServer() {
        try {
            this.serverSock = new ServerSocket(portNumber);
        } catch (IOException ex) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    /**
     * Reads requests from other users.
     */
    public void start() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        sockListener = serverSock.accept();
                        TcpClientThread clientThread = new TcpClientThread(sockListener);
                        clientThread.start();
                    }
                } catch (Exception ex) {
                    System.out.println("Error");
                }
            }
        };
        thread.start();
    }

  

    public int currentServerPort() {
        return serverSock.getLocalPort();
    }

  
  
}
