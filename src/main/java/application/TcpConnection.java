/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Represents a TcpConnection.
 */
public class TcpConnection {

    private final int portNumber = 0;

    /**
     * A socket used for communication.
     */
    private Socket sock;

    private Socket sock2;

    /**
     * The address files will be sent to.
     */
    private InetAddress destinationAddress;

    /**
     * A stream used to ouput data in a socket.
     */
    private DataOutputStream dataOut;

    /**
     * A stream used to read data from a socket.
     */
    private DataInputStream dataIn;

    /**
     * Constructor for tcp connection class-
     *
     * @param destinationAddress the destination ip address in string
     * @throws UnknownHostException if ip address is invalid
     * @throws IOException          if socket isn't created properly
     */
    public TcpConnection(String destinationAddress, int portNumber) throws UnknownHostException, IOException {
        this.destinationAddress = InetAddress.getByName(destinationAddress);
        ServerSocket serverSock = new ServerSocket(this.portNumber);
        sock = new Socket(this.destinationAddress, portNumber);
        sock2 = serverSock.accept();
        dataOut = new DataOutputStream(sock.getOutputStream());
        dataIn = new DataInputStream(sock2.getInputStream());

    }

    /**
     * Sends the file size of a file to the destination ip address configured
     *
     * @param file the file to send its size
     * @return true if file size sent
     * @throws FileNotFoundException if file not found
     * @throws IOException           if failed to write
     */
    public boolean sendFileSize(File file) throws FileNotFoundException, IOException {
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        dataOut.writeInt(fileBytes.length);
        return true;
    }

    /**
     * Sends a file to a waiting host.
     *
     * @param file the file to send
     * @return true if file is sent
     * @throws IOException if failed to write the file
     */
    public boolean sendFile(File file) throws IOException {
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        dataOut.write(fileBytes);
        return true;
    }

    /**
     * Downloads a file from a socket.
     *
     * @param fileName the name of the new file
     * @param path     the path of the new file
     * @param fileSize the size of the file receiving
     * @return true if file downloaded successfully
     * @throws FileNotFoundException if file not found
     * @throws IOException           if file not dowloaded from socket
     */
    public boolean downloadFile(String fileName, String path, int fileSize) throws FileNotFoundException, IOException {
        byte[] fileData = new byte[fileSize];
        dataIn.readFully(fileData, 0, fileData.length);
        //path += fileName;
        FileOutputStream fileOut = new FileOutputStream(path);
        fileOut.write(fileData);
        fileOut.close();
        return true;
    }

    /**
     * Reads the next 4 bytes of data into an integer.
     *
     * @return the integer read
     * @throws IOException
     */
    public int readFileSize() throws IOException {
        int fileSize = 0;
        fileSize = dataIn.readInt();
        return fileSize;
    }

    /**
     * Reads information regarding the file requested by other user.
     *
     * @return the string containing the information
     * @throws IOException
     */
    public String readFileRequestInfo() throws IOException {
        //TODO: Create semaphore to prevent this to run while downloadFile method is running
        int requestSize = readFileSize();
        byte[] fileData = new byte[requestSize];
        dataIn.readFully(fileData, 0, fileData.length);
        String downloadInfo = Arrays.toString(fileData);
        return downloadInfo;
    }

    /**
     * Closes the socket on this tcp connection.
     *
     * @return true if socket closed
     * @throws IOException if socket failed to close
     */
    public boolean closeConnection() throws IOException {
        sock.close();
        sock2.close();
        return true;
    }

    /**
     * Gets the current bound port number.
     *
     * @return the port number
     */
    public int getCurrentPortNumber() {
        return sock2.getLocalPort();
    }
}
