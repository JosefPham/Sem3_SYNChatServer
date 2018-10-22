/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import Acquaintance.IUser;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter
 */
public class ChatHandler extends Thread{

    private Socket s;
    DataInputStream input;
    DataOutputStream output;
    
  public static HashMap<String, ChatHandler> clients = new HashMap<String, ChatHandler>();    
  
  
    public ChatHandler(Socket s) {
        try {
            this.s = s;
            System.out.println("got: " + s.getInetAddress());
            input = new DataInputStream (new BufferedInputStream (s.getInputStream()));
            output = new DataOutputStream (new BufferedOutputStream (s.getOutputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
  protected static Vector handlers = new Vector ();
  
  public void run () {
      System.out.println("Started");
    try {
      handlers.addElement (this); 
      if(!clients.containsKey("bruger" + handlers.size())){
        clients.put("bruger" + handlers.size(), this);  
      }
      
        System.out.println("Added: " + "bruger"+handlers.size());
    //  sendMessage("Welcome!");
      while (true) {
        System.out.println("Waiting");
        String msg = input.readUTF ();
        System.out.println("msg: " + msg);
        if(msg.contains(":")){
            String[] name = msg.trim().split(":");
            sendPrivateMessage(name[0].trim(), name[1]);
        }
        else{
        sendPublicMessage (msg);
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace ();
    } finally {
      handlers.removeElement (this);
      for (String s : clients.keySet()){
          if(clients.get(s).equals(this)){
              System.out.println("removing: " + s);
              clients.remove(s);
          }
      }
      
      try {
        s.close ();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
    
  

  
  
  
  
protected static void sendPrivateMessage(String message, String reciever){
    
    for(String s : clients.keySet()){
        if(s.equals(reciever)){
            try {
                ChatHandler ch = clients.get(s);
                ch.output.writeUTF(message);
                ch.output.flush();
                
            } catch (IOException ex) {
                Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
}  
  
  
  
protected static void sendPublicMessage (String message) {
    synchronized (handlers) {
        System.out.println("Trying to send a message!");
      Enumeration e = handlers.elements ();
      while (e.hasMoreElements ()) {
        ChatHandler ch = (ChatHandler) e.nextElement ();
        try {
          synchronized (ch.output) {
              System.out.println("About to write: " + message);
            ch.output.writeUTF(message);
          }
          ch.output.flush ();
        } catch (IOException ex) {
          ch.interrupt();
        }
      }
    }
  }  
    
    
    
}
