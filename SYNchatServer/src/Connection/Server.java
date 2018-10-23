/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author Sigurd E. Espersen
 */
public class Server{

    
    private int nudeCounter;
    private int port;
    private boolean isStopped = false;
    ExecutorService threadpool = Executors.newFixedThreadPool(10);
    InetAddress ip;
    ServerSocket serverSocket;
    
    
    public Server(InetAddress ip, int port){
        this.port = port;
        this.ip = ip;
        createSocket();
        System.out.println("Server started");
        acceptClient();
        
    }
    
    
    private boolean createSocket(){
        try {
            serverSocket = new ServerSocket(port, 0, ip);
           // port = sslServerSocket.getLocalPort();
           return true;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private void acceptClient(){
        while(!isStopped()){
            Socket clientSocket;
             try {
                clientSocket = this.serverSocket.accept(); // Wait for connection and accept
                nudeCounter++;
             //    System.out.println("You recieved a nude! It is nude number: " + nudeCounter);
                 ChatHandler ch = new ChatHandler(clientSocket);
                 ch.start();
             //     handleConnection(clientSocket);
            } catch (IOException e) {
                throw new RuntimeException("Error accepting client connection", e);
            }
        }
        
        
    }
    
    
    

 
    
    
      private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            System.out.println("Closing web server");
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
    
    
    
    
}
