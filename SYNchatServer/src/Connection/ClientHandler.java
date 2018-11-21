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
import Acquaintance.IProfile;

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

    private Map<Integer, IUser> currentPublicChatMap;
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
            IFriends b = ConnectionFacade.getInstance().updateFriends(friends, userID);
            int friendID = b.getFriendlist().get(0);
            List<Integer> tmpList = new ArrayList<>();
            tmpList.add(userID);
            ConFriends sendFriends = new ConFriends(tmpList);
            ClientHandler ch = (ClientHandler) clients.get(friendID);
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

    public void sendMap(Map m) {
        try {
            output.writeObject(m);
            System.out.println("Sendte en map");
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
 
//            
//            ConUser user = new ConUser(((IProfile) obj).getFirstName(), ((IProfile) obj).getLastName(), ((IProfile) obj).getNationality(), ((IUser) obj).getProfile().getProfileText());
//            user.setUserID(((IUser) obj).getUserID());
//            user.setChats(((IUser) obj).getChats());
//            user.setReports(((IUser) obj).getReports());
//            user.setBanned(((IUser) obj).isBanned());
//            sendBool(ConnectionFacade.getInstance().updateProfile(management, userID));
            
            
            
            
            return true;
        } else if (obj instanceof String) {
            if (obj.toString().equals("!SYN!-logout-!SYN!")) {
                return false;
            }

            if (obj.toString().equals("!SYN!-PublicChat-!SYN!")) {
                System.out.println("Entered public chat with " + userID);
                Map m = ConnectionFacade.getInstance().updatePublicChatUsers(userID);
                currentPublicChatMap = m;
                if (!m.containsKey(userID)) {
                    sendMap(m);
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
                                //  l = null;
                            }
                        }
                    } else if (l == null && obj != null) {
                        l = checkLogin(obj);
                    }
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            for (Integer i : clients.keySet()) {
                if (clients.get(i).equals(this)) {
                    System.out.println("removing: " + i);
                    clients.remove(i);
                    ConnectionFacade.getInstance().removeOnlineUser(0);
                    if (currentPublicChatMap.containsKey(userID)) {
                        ConnectionFacade.getInstance().updatePublicChatUsers(userID);
                    }
                }
            }

            try {
                s.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

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

    protected void sendPublicMessage(IMessage message) {
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

    protected void sendPublicChatUser(int userID) { // opdater så den kan sende friends også - sendMap
        IUser newUser = currentPublicChatMap.get(userID);
        IProfile sendProfile = new ConProfile(newUser.getProfile());
        IUser sendUser = new ConUser(newUser);
        synchronized (currentPublicChatMap) {
            for (Integer i : currentPublicChatMap.keySet()) {
              //  if (i != userID) {

                    ClientHandler ch = (ClientHandler) clients.get(i);
                    try {
                        synchronized (ch.output) {
                            ch.output.writeObject(sendUser);
                        }
                        ch.output.flush();
                    } catch (IOException ex) {
                        ch.interrupt();
                    }
               // }
            }
        }

    }

}
