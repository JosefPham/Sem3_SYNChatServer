/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Starter;

import Connection.Server;
import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter
 * Test server
 */
public class SYNchatServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
         try {
            InetAddress ip = (InetAddress) InetAddress.getByName("10.126.37.220");
            int port = 8080;
            Server server = new Server(ip, port);
            Thread t = new Thread(server);
            t.start();
        } catch (IOException ex) {
            Logger.getLogger(SYNchatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
}
