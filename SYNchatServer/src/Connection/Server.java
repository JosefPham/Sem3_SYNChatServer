/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 *
 * @author Sigurd E. Espersen
 */
public class Server implements Runnable{

    private int port;
     private boolean isStopped = false;
    ExecutorService threadpool = Executors.newFixedThreadPool(10);
    InetAddress ip;
    SSLServerSocket sslServerSocket;
    
    
    public Server(InetAddress ip, int port){
        this.port = port;
        this.ip = ip;
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
        System.out.println(createSocket());
        System.out.println("Det kører!");
        while(!isStopped()) {
            SSLSocket clientSocket;
             try {
                clientSocket = (SSLSocket) this.sslServerSocket.accept(); // Wait for connection and accept
            } catch (IOException e) {
                throw new RuntimeException("Error accepting client connection", e);
            }
             handleConnection(clientSocket);
            
        }
    }
    
    
        private void handleConnection(SSLSocket client) {
            threadpool.submit(new Runnable() {
                @Override
                public void run() {
                    //TODO
                    /*
             try {       
            InputStream input  = client.getInputStream();
            OutputStream output = client.getOutputStream();

            long time = System.currentTimeMillis();
            String httpResponse = "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Type: text/html\r\n\r\nHello visitor number " + "\r\n";

            output.write(httpResponse.getBytes("UTF-8"));

            client.shutdownOutput();
            output.close();
            input.close();

            System.out.println("Request processed: " + time);

            // Simulate a connection lasting 10 seconds
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
*/
                }
            }
            );
    }
    
    
    
    
      private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            System.out.println("Closing web server");
            this.sslServerSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
    
    
    
    
}
