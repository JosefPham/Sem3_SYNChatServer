/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import Acquaintance.ILogin;
import Acquaintance.IUser;
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
public class ClientHandler extends Thread {

    private Socket s;
    ObjectInputStream input;
    ObjectOutputStream output;

    public static HashMap<String, ClientHandler> clients = new HashMap<String, ClientHandler>();

    public ClientHandler(Socket s) {
        try {
            this.s = s;
            System.out.println("got: " + s.getInetAddress());
            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    public void checkLogin(ILogin login){
        System.out.println("Hashed Password: " + login.gethPW());
       // sendPublicMessage( "Email: " + login.gethMail() + " has password: " + login.gethPW() );
        try {
            output.writeObject(login);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     */
    public void sendLoginInfo(ILogin login) {
        try {
            output.writeObject(login);
            System.out.println("Skal sende her :)");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {

        try {
            ILogin l = null;

            Object login = input.readObject();

            if (login instanceof ILogin) {
                System.out.println("Det er et login");
                l = ConnectionFacade.getInstance().checkLogin((ILogin) login);

                sendLoginInfo(l);
            }

            if (l.getLoginvalue() == 2) {
            System.out.println("User logged in");
            
                

                
                try {
                    if (!clients.containsKey("bruger" + (clients.size() + 1))) {
                        clients.put("bruger" + (clients.size() + 1), this);
                    }

                    System.out.println("Added: " + "bruger" + clients.size());
                    //  sendMessage("Welcome!");
                    while (true) {
                        Object o = input.readObject();
                        
                        
                        System.out.println("Waiting");
                        
                         if (o instanceof IUser) {
                    System.out.println("Det er en User");
                }
                        
                         else if(o instanceof String){
                        String msg = (String) o;
                        System.out.println("msg: " + msg);
                        if (msg.contains(":")) {
                            String[] name = msg.trim().split(":");
                            sendPrivateMessage(name[0].trim(), name[1]);
                        } else {
                            sendPublicMessage(msg);
                        }

                    }
                  }  
                    

                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                } finally {

                    for (String s : clients.keySet()) {
                        if (clients.get(s).equals(this)) {
                            System.out.println("removing: " + s);
                            clients.remove(s);
                        }
                    }

                    try {
                        s.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                
                
                
                
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static void sendPrivateMessage(String message, String reciever) {

        for (String s : clients.keySet()) {
            if (s.equals(reciever)) {
                try {
                    ClientHandler ch = clients.get(s);
                    ch.output.writeObject(message);
                    ch.output.flush();

                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    protected static void sendPublicMessage(String message) {
        synchronized (clients) {
            System.out.println("Trying to send a message!");

            for (String s : clients.keySet()) {
                ClientHandler ch = (ClientHandler) clients.get(s);
                try {
                    synchronized (ch.output) {
                        System.out.println("About to write: " + message);
                        ch.output.writeObject(message);
                    }
                    ch.output.flush();
                } catch (IOException ex) {
                    ch.interrupt();
                }
            }
        }
    }

}
