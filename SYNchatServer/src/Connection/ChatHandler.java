/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter
 */
public class ChatHandler extends Thread{

    private Socket s;
    ObjectInputStream input;
    ObjectOutputStream output;
    
  public static HashMap<String, ChatHandler> clients = new HashMap<String, ChatHandler>();    
  
  
    public ChatHandler(Socket s) {
        try {
            this.s = s;
            System.out.println("got: " + s.getInetAddress());
            output = new ObjectOutputStream (s.getOutputStream());
            input = new ObjectInputStream (s.getInputStream());
           
        } catch (IOException ex) {
            Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
  
  public void run () {
      System.out.println("Started");
    try {
      if(!clients.containsKey("bruger" + (clients.size() + 1))){
        clients.put("bruger" + (clients.size()+1), this);  
      }
      
        System.out.println("Added: " + "bruger"+clients.size());
    //  sendMessage("Welcome!");
      while (true) {
        System.out.println("Waiting");
        String msg;
          try {
              msg = input.readObject().toString();
          
        System.out.println("msg: " + msg);
        if(msg.contains(":")){
            String[] name = msg.trim().split(":");
            sendPrivateMessage(name[0].trim(), name[1]);
        }
        else{
        sendPublicMessage (msg);
        }
        } 
        catch (ClassNotFoundException ex) {
              Logger.getLogger(ChatHandler.class.getName()).log(Level.SEVERE, null, ex);
          }
        
      }
    } catch (IOException ex) {
      ex.printStackTrace ();
    } finally {
    
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
    synchronized (clients) {
        System.out.println("Trying to send a message!");
    
      for (String s : clients.keySet()) {
        ChatHandler ch = (ChatHandler) clients.get(s);
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
