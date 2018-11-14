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
import java.util.List;
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
                sendBool(b);
            }
        }

        return l;
    }

    public boolean readStream(Object obj) {
        //   while (s.isConnected()) {

        //   try {
        //   Object obj = null;
        //   if ((obj =input.readObject())!= null) {
        // obj = input.readObject();
        System.out.println("Waiting");

        if (obj instanceof IUser) {
            System.out.println("Det er en User");
            ConUser user = new ConUser(((IProfile) obj).getFirstName(), ((IProfile) obj).getLastName(), ((IProfile) obj).getNationality(), ((IUser) obj).getProfile().getProfileText());
            user.setUserID(((IUser) obj).getUserID());
            user.setChats(((IUser) obj).getChats());
            user.setReports(((IUser) obj).getReports());
            user.setBanned(((IUser) obj).isBanned());
            sendBool(ConnectionFacade.getInstance().updateProfile(user));
            return true;

        } else if (obj instanceof IMessage) {
            ConMessage msg = new ConTextMessage(((IMessage) obj).getSenderID(), ((IMessage) obj).getContext());

            System.out.println("msg: " + msg.getContext() + " at time: " + msg.getTimestamp().toString());
            if (msg.getContext().contains("!SYN!-logout-!SYN!")) {
                System.out.println("Modtog logout");
                return false;
            } else {
                sendPublicMessage(msg);
                return true;
            }
        } else if (obj instanceof IManagement) {
            IManagement management = new ConManagement(((IManagement) obj).getAction(), ((IManagement) obj).getUserID(), ((IManagement) obj).getPw(), ((IManagement) obj).getString1());
            sendInt(ConnectionFacade.getInstance().changeInfo(management));
            return true;
        }
        else if(obj instanceof String){
            System.out.println("Modtog logout");
             return false;
        }
        //     }
        /*
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
         */
        // }
        return true;
    }

    public void run() {

        ILogin l = null;

        //  while ((l == null || l.getLoginvalue() != 2)) {
        try {
            while (!s.isClosed()) {

                /*
            if (s.isClosed()) {
                System.out.println("CLOSED!");
                break;
            }
                 */
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

    protected static void sendPublicMessage(IMessage message) {
        synchronized (clients) {
            System.out.println("Trying to send a message!");

            for (Integer i : clients.keySet()) {
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

}
