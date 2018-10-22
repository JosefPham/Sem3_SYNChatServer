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
public class Server implements Runnable{

    
    private int nudeCounter;
    private int port;
    private boolean isStopped = false;
    ExecutorService threadpool = Executors.newFixedThreadPool(10);
    InetAddress ip;
    ServerSocket serverSocket;
    
    
    public Server(InetAddress ip, int port){
        this.port = port;
        this.ip = ip;
    }
    
    
    public boolean createSocket(){
        try {
            serverSocket = new ServerSocket(port, 0, ip);
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
     //   System.out.println("Det kører!");
        while(!isStopped()) {
            Socket clientSocket;
             try {
                clientSocket = this.serverSocket.accept(); // Wait for connection and accept
             
                nudeCounter++;
                 System.out.println("You recieved a nude! It is nude number: " + nudeCounter);
                  handleConnection(clientSocket);
            } catch (IOException e) {
                throw new RuntimeException("Error accepting client connection", e);
            }
            
            
        }
    }
    
    
     private void handleConnection(Socket client) {
        threadpool.submit(new Runnable() {
            @Override
            public void run() {
                //TODO
                try (Scanner scanner = new Scanner(client.getInputStream());
                    PrintWriter writerClient = new PrintWriter(client.getOutputStream(), true);) {
                    System.out.println("Sends: server ready");
                    writerClient.println("Server ready. Type your massage:");
                    while (scanner.hasNextLine()) {

                        String line = scanner.nextLine();

                        //  writerFile.println(line);
                        System.out.println("Printede : " + line);
                    }
                    writerClient.close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        client.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
    
    
    
    
}
