/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

/**
 *
 * @author Group 9
 */
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Group 9
 */
public class Server {

    private int port;
    private boolean isStopped = false;
    private InetAddress ip;
    private ServerSocket serverSocket;

    public Server(InetAddress ip, int port) {
        this.port = port;
        this.ip = ip;
        createSocket();
        acceptClient();

    }

    private boolean createSocket() {
        try {
            serverSocket = new ServerSocket(port, 0, ip);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Accepts a client trying to connect to the server, assigns and starts a thread to the client
     */
    private void acceptClient() {
        while (!isStopped()) {
            Socket clientSocket;
            try {
                clientSocket = this.serverSocket.accept(); // Wait for connection and accept
                ClientHandler ch = new ClientHandler(clientSocket);
                ch.start();
            } catch (IOException e) {
                throw new RuntimeException("Error accepting client connection", e);
            }
        }

    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

}
