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
    
     String userName = "";

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
    public void checkLogin(ILogin readLogin){
        System.out.println("Hashed Password: " + readLogin.gethPW());
       // sendPublicMessage( "Email: " + readLogin.gethMail() + " has password: " + readLogin.gethPW() );
        try {
            output.writeObject(readLogin);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     */
    public void sendLoginInfo(ILogin login) {
        try {
            output.writeObject(login);
            System.out.println("Sendte et login");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendCreateUser(Boolean b) {
        try {
            output.writeObject(b);
            System.out.println("Sendte en boolean");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    
    
    private ILogin loginLoop(Object readLogin){
        
        
        System.out.println("Started login loop");
            ILogin login = null;
            

                System.out.println("er i while");
            if (readLogin instanceof ILogin) {
                System.out.println("Det er et login");

                if (((ILogin) readLogin).getUser() == null) {
                    login = ConnectionFacade.getInstance().checkLogin((ILogin) readLogin);
                    sendLoginInfo(login);
                } else {
                    Boolean b = ConnectionFacade.getInstance().createUser((ILogin) readLogin);
                    System.out.println("Sender: " + b);
                    this.userName = ((ILogin) readLogin).getUser().getTmpName();
                    sendCreateUser(b);
                }
            }

            
            return login;
            
            
    }
    
    
    
    
    public void chatLoop(){
        
        
                    
                    
                    System.out.println("User logged in");
                    boolean b = false;
                    try {
                        /*
                        if (!clients.containsKey("bruger" + (clients.size() + 1))) {
                            clients.put("bruger" + (clients.size() + 1), this);
                        }
*/
                        clients.put(userName, this);

                        System.out.println("Added: " + userName);
                        //  sendMessage("Welcome!");
                        while (true && !b) {

                            Object o = input.readObject();

                            System.out.println("Waiting");

                            if (o instanceof IUser) {
                                System.out.println("Det er en User");
                            } else if (o instanceof String) {
                                String msg = (String) o;
                                System.out.println("msg: " + msg);
                                msg = userName + " " + msg;
                                if (msg.contains(":")) {
                                    String[] name = msg.trim().split(":");
                                    sendPrivateMessage(name[0].trim(), name[1]);
                                } else if(msg.contains("!SYN!-logout-!SYN!")){
                                    System.out.println("returning true");
                                    b = true;
                                }
                                else {
                                    sendPublicMessage(msg);
                                }

                            }
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if(!b){
                             for (String s : clients.keySet()) {
                            if (clients.get(s).equals(this)) {
                                System.out.println("removing: " + s);
                                clients.remove(s);
                            }
                        }
                        }
                       

                        try {
                            if(!b){
                              s.close();
                            }
                            
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
    }
    
    
    
    
    
    
    public void run() {
            
        
        System.out.println("Starter while");
        
        while(!s.isClosed()){
        try {
            System.out.println("læser");
            Object o = input.readObject();
            
               ILogin l = loginLoop(o); 
               
            
            
            if (l != null) {
                if (l.getLoginvalue() == 2) {
                    chatLoop();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
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
