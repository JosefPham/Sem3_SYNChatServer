package Connection;

import Acquaintance.IFriends;
import Acquaintance.ILogin;
import Acquaintance.IMessage;
import Acquaintance.IUser;
import Acquaintance.IManagement;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Group 9
 */
public class ClientHandler extends Thread {

    private volatile Map<Integer, IUser> currentPublicChatMap;
    private Socket s;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    Integer userID = -1;

    public static HashMap<Integer, ClientHandler> clients = new HashMap<Integer, ClientHandler>();

    public ClientHandler(Socket s) {
        try {
            this.s = s;

            output = new ObjectOutputStream(s.getOutputStream());
            input = new ObjectInputStream(s.getInputStream());

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendLoginInfo(ILogin login) {
        try {
            output.writeObject(login);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendBool(Boolean b) {
        try {
            output.writeObject(b);
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
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checks the login sent my the client
     * Returns a boolean if the user is being created
     * Returns an ILogin if the client is trying to login
     * @param Object
     * @return ILogin
     */
    public ILogin checkLogin(Object obj) {

        ILogin l = null;

        if (obj instanceof ILogin) {

            if (((ILogin) obj).getUser() == null) {
                l = ConnectionFacade.getInstance().checkLogin((ILogin) obj);
                ILogin sendConLog = new ConLogin(l);
                sendLoginInfo(sendConLog);
            } else {
                Boolean b = ConnectionFacade.getInstance().createUser((ILogin) obj);
                sendBool(b);
            }
        }

        return l;
    }

    /**
     * readStream receives an object from the client and manages it according to
     * the object type
     *
     * @param Object
     * @return boolean
     */
    public boolean readStream(Object obj) {


        if (obj instanceof IMessage) {
            ConMessage msg = new ConTextMessage(((IMessage) obj).getSenderID(), ((IMessage) obj).getContext());
            msg.setTimestamp(Instant.now());
            if (!msg.getContext().contains("!SYN!-logout-!SYN!")) {
                sendPublicMessage(msg);
                return true;
            }
        } else if (obj instanceof IManagement) {

            IManagement management = new ConManagement(((IManagement) obj).getAction());
            management.setPw(((IManagement) obj).getPw());
            management.setMail(((IManagement) obj).getMail());
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

    /**
     * runs the clientHandler thread, that receives input from the client
     */
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
        } finally {

            if (clients.get(userID).equals(this)) {
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

    /**
     * Removes the client from the hashmaps throughout the system.
     */
    private void kick() {
        if(currentPublicChatMap != null){
            
        
        if (currentPublicChatMap.containsKey(userID)) {
            sendPublicChatUser(currentPublicChatMap.get(userID));
            ConnectionFacade.getInstance().updatePublicChatUsers(userID); // fjernes fra public chat
            currentPublicChatMap.remove(userID);
        }
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

    /**
     * Sends a message to all the users in the public chat
     * @param IMessage
     */
    protected synchronized void sendPublicMessage(IMessage message) {
        synchronized (clients) {

            for (Integer i : currentPublicChatMap.keySet()) {
                ClientHandler ch = (ClientHandler) clients.get(i);
                try {
                    synchronized (ch.output) {
                        ch.output.writeObject(message);
                    }
                    ch.output.flush();
                } catch (IOException ex) {
                    ch.interrupt();
                }
            }
        }
    }

    /**
     * Sends the user who just entered the public chat out to all the clients in the public chat
     * @param IUser 
     */
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
