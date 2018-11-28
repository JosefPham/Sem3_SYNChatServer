/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import Acquaintance.IFriends;
import Acquaintance.ILogin;
import Acquaintance.IMessage;
import Acquaintance.IUser;
import Acquaintance.IManagement;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter
 */
public class ClientHandler extends Thread {

    private volatile Map<Integer, IUser> currentPublicChatMap;
    private Socket s;
    ObjectInputStream input;
    ObjectOutputStream output;

    Integer userID = -1;

    public static HashMap<Integer, ClientHandler> clients = new HashMap<Integer, ClientHandler>();

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

    public void sendBool(Boolean b) {
        try {
            output.writeObject(b);
            System.out.println("Sendte en boolean");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean sendFriends(IFriends friends) {

        try {
            ConFriends sendFriends = new ConFriends(friends);
            ClientHandler ch = (ClientHandler) clients.get(userID);
            try {
                synchronized (ch.output) {
                    ch.output.writeObject(sendFriends);
                }
                ch.output.flush();
            } catch (IOException ex) {
                ch.interrupt();
            }
        } catch (Exception e) {
        }
        return true;
    }

    public void sendInt(int value) {
        try {
            output.writeObject(value);
            System.out.println("Sendte en int");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMap(Map<Integer, IUser> m) {
        try {
            Map<Integer, IUser> conMap = new HashMap<>();
            for (int i : m.keySet()) {
                conMap.put(i, new ConUser(m.get(i)));
            }
            for (int i : conMap.keySet()) {

            }
            output.writeObject(conMap);

            System.out.println("Sendte en map " + userID);
            System.out.println("Map: " + m.get(userID));
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
                ILogin sendConLog = new ConLogin(l);
                sendLoginInfo(sendConLog);
            } else {
                Boolean b = ConnectionFacade.getInstance().createUser((ILogin) obj);
                System.out.println("Sender: " + b);
                sendBool(b);
            }
        }

        return l;
    }

    public boolean readStream(Object obj) {

        System.out.println("Waiting");

        if (obj instanceof IMessage) {
            ConMessage msg = new ConTextMessage(((IMessage) obj).getSenderID(), ((IMessage) obj).getContext());

            System.out.println("msg: " + msg.getContext() + " at time: " + msg.getTimestamp().toString());
            if (!msg.getContext().contains("!SYN!-logout-!SYN!")) {
                sendPublicMessage(msg);
                return true;
            }
        } else if (obj instanceof IManagement) {

            IManagement management = new ConManagement(((IManagement) obj).getAction());
            management.setPw(((IManagement) obj).getPw());
            System.out.println("Management pw " + management.getPw());
            management.setMail(((IManagement) obj).getMail());
            System.out.println("Management mail " + management.getMail());
            management.setProfile(((IManagement) obj).getProfile());
            sendBool(ConnectionFacade.getInstance().changeInfo(management, this.userID));

            return true;
        } else if (obj instanceof IFriends) {
            IFriends newConFriend = new ConFriends((IFriends) obj);
            sendBool(ConnectionFacade.getInstance().updateFriends(newConFriend, this.userID));
        } else if (obj instanceof String) {
            if (obj.toString().equals("!SYN!-logout-!SYN!")) {
                kick();
                return false;
            }

            if (obj.toString().equals("!SYN!-PublicChat-!SYN!")) {
                System.out.println("Entered public chat with " + userID);
                IUser removeUser = new ConUser(null);
                if (currentPublicChatMap != null) {
                    removeUser = currentPublicChatMap.get(userID);
                }

                currentPublicChatMap = ConnectionFacade.getInstance().updatePublicChatUsers(userID);
                if (currentPublicChatMap.containsKey(userID) && !currentPublicChatMap.isEmpty()) { // login
                    sendMap(currentPublicChatMap);
                    sendPublicChatUser(currentPublicChatMap.get(userID));
                } else { // logout
                    sendPublicChatUser(removeUser);
                }
            }
        }
        return true;
    }

    public void run() {

        ILogin l = null;

        try {
            while (!s.isClosed()) {

                Object obj;
                if ((obj = input.readObject()) != null) {

                    if (l != null) {
                        if (l.getLoginvalue() == 2) {

                            if (!(clients.containsKey(l.getUser().getUserID()))) {
                                userID = l.getUser().getUserID();
                                clients.put(l.getUser().getUserID(), this);
                            }

                            if ((obj != null) && (!(obj instanceof ILogin))) {
                                if (!readStream(obj)) {
                                    System.out.println("Logged out");
                                    l = null;
                                }
                            }
                        }
                    } else if (l == null && obj != null) {
                        l = checkLogin(obj);
                    }
                }

            }
        } catch (IOException ex) {
            System.out.println("Error reading from client! - client disconnected");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
            finally {

            if (clients.get(userID).equals(this)) {
                System.out.println("removing: " + userID);
                kick();
                clients.remove(userID);

            }

            try {
                s.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void kick() {
        if (currentPublicChatMap.containsKey(userID)) {
            sendPublicChatUser(currentPublicChatMap.get(userID));
            ConnectionFacade.getInstance().updatePublicChatUsers(userID); // fjernes fra public chat
            currentPublicChatMap.remove(userID);
        }
        ConnectionFacade.getInstance().removeOnlineUser(userID);
    }

    protected static void sendPrivateMessage(String message, Integer reciever) {

        for (Integer i : clients.keySet()) {
            if (i.equals(reciever)) {
                try {
                    ClientHandler ch = clients.get(i);
                    ch.output.writeObject(message);
                    ch.output.flush();

                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    protected synchronized void sendPublicMessage(IMessage message) {
        synchronized (clients) {
            System.out.println("Trying to send a message!");

            for (Integer i : currentPublicChatMap.keySet()) {
                ClientHandler ch = (ClientHandler) clients.get(i);
                try {
                    synchronized (ch.output) {
                        System.out.println("About to write: " + message.getContext());
                        ch.output.writeObject(message);
                    }
                    ch.output.flush();
                } catch (IOException ex) {
                    ch.interrupt();
                }
            }
        }
    }

    protected synchronized void sendPublicChatUser(IUser user) { // opdater saa den kan sende friends ogsaa - sendMap
        IUser sendUser = new ConUser(user);
        synchronized (currentPublicChatMap) {
            for (Integer i : currentPublicChatMap.keySet()) {

                if (i != userID) {

                    ClientHandler ch = (ClientHandler) clients.get(i);
                    try {
                        synchronized (ch.output) {
                            ch.output.writeObject(sendUser);
                        }
                        ch.output.flush();
                    } catch (IOException ex) {
                        ch.interrupt();
                    }
                }
            }
        }

    }

}
