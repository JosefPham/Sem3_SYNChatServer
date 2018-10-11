/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 *
 * @author Sigurd E. Espersen
 */
public class Server implements Runnable{

    private int port;
    ExecutorService threadpool = Executors.newFixedThreadPool(10);
    InetAddress ip;
    SSLServerSocket sslServerSocket;
    
    
    public Server(InetAddress ip, int port){
        this.port = port;
        this.ip = ip;
        System.out.println(createSocket());
    }
    
    
    public boolean createSocket(){
         SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        try {
            this.sslServerSocket = (SSLServerSocket) socketFactory.createServerSocket(port, 0, ip);
           // port = sslServerSocket.getLocalPort();
           return true;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
    
    
    @Override
    public void run() {
        System.out.println("Det kører!");
    }
    
}
