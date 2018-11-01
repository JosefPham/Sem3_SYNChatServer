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

    
    
    
    public ILogin checkLogin(Object obj) {

        ILogin l = null;

        if (obj instanceof ILogin) {
            System.out.println("Det er et login");

            if (((ILogin) obj).getUser() == null) {
                l = ConnectionFacade.getInstance().checkLogin((ILogin) obj);
                sendLoginInfo(l);
            } else {
                Boolean b = ConnectionFacade.getInstance().createUser((ILogin) obj);
                System.out.println("Sender: " + b);
                this.userName = ((ILogin) obj).getUser().getTmpName();
                sendCreateUser(b);
            }
        }

        return l;
    }

    public void readStream() {
        while (true) {
            try {
                Object obj = input.readObject();

                System.out.println("Waiting");

                if (obj instanceof IUser) {
                    System.out.println("Det er en User");
                } else if (obj instanceof String) {
                    String msg = (String) obj;
                    System.out.println("msg: " + msg);
                    msg = userName + " " + msg;
                    if (msg.contains(":")) {
                        String[] name = msg.trim().split(":");
                        sendPrivateMessage(name[0].trim(), name[1]);
                    } else if (msg.contains("!SYN!-logout-!SYN!")) {
                        return;
                    } else {
                        sendPublicMessage(msg);
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }

    public void run() {

        ILogin l = null;

        while (l == null || l.getLoginvalue() != 2) {

            try {
                Object obj = input.readObject();

                l = checkLogin(obj);

                if (l != null) {
                    if (l.getLoginvalue() == 2) {
                        clients.put(userName, this);
                        readStream();
                        l = null;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
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
